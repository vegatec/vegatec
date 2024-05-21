package net.vegatec.media_library.service;

import com.mpatric.mp3agic.*;
import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.domain.TrackType;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackSearchRepository;
import net.vegatec.media_library.service.criteria.TrackCriteria;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.events.FileCreated;
import net.vegatec.media_library.service.mapper.TrackMapper;
import net.vegatec.media_library.util.DebounceExecutor;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link Track}.
 */
@Service
@Transactional
public class TrackImporterService {

    private final Logger logger = LoggerFactory.getLogger(TrackImporterService.class);


    private static final String MISSING_TITLE = "missing title";
    private static final String MISSING_ARTIST = "missing artist ";
    private static final String MISSING_ALBUM = "missing title";
    private static final String MISSING_ALBUM_ARTIST = "missing album artist";
    private static final String MISSING_GENRE = "missing genre";

    private final TrackRepository trackRepository;
    private final TrackTypeRepository trackTypeRepository;
    private final ApplicationProperties applicationProperties;
    private volatile  boolean fileImportPaused = true;

    private DebounceExecutor debouncer = new DebounceExecutor();


    public TrackImporterService(TrackRepository trackRepository, TrackTypeRepository trackTypeRepository, ApplicationProperties applicationProperties) {
        this.trackRepository = trackRepository;
        this.trackTypeRepository = trackTypeRepository;
        this.applicationProperties = applicationProperties;
    }

    @EventListener
    public void handle(FileCreated event) {
        fileImportPaused = true;
        //debounce to start import when all files are copied
        debouncer.debounce(3000, () -> {
            try {
                fileImportPaused= false;
                importFiles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void importFiles() throws IOException {

        Path downloadsFolder =  Path.of(applicationProperties.getMediaFolder(), Track.DOWNLOADED);

        logger.debug("Started scanning all media files on {}", downloadsFolder);

        Files.walkFileTree(downloadsFolder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile (Path path, BasicFileAttributes attrs)    throws IOException
            {
                File file = path.toFile();
                if ( !fileImportPaused && attrs.isRegularFile() && file.getName().endsWith(".mp3") ) {
                    importFile(file);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }



    @Transactional
    public void importFile( File file) {
        if(logger.isDebugEnabled())
            logger.debug("buildCreateMediaFileCommand("+ file+ ")");
        try {

            TrackType type= trackTypeRepository.getReferenceById(1L);
            Mp3File mp3File = new Mp3File(file);
            //String folder = file.getParentFile().getPath();

            if (mp3File.hasId3v2Tag() ) {
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();



                int releasedYear=0, trackNumber =0;
                String title, artist, album, albumArtist, genre;

                try {
                    title = id3v2Tag.getTitle();

                    if (title == null) title = file.getName();
                } catch (Exception e){
                    title= MISSING_TITLE;

                }

                try {
                    artist = id3v2Tag.getArtist();
                    if (artist == null) artist = MISSING_ARTIST;
                } catch (Exception e){
                    artist= MISSING_ARTIST;

                }

                try {
                    album = id3v2Tag.getAlbum();
                    if (album == null) album = MISSING_ALBUM;
                } catch (Exception e){
                    album= MISSING_ALBUM;


                }
                try {
                    albumArtist = id3v2Tag.getAlbumArtist();
                    if (albumArtist == null)
                        albumArtist = MISSING_ALBUM_ARTIST;
                } catch (Exception e){
                    albumArtist= MISSING_ALBUM_ARTIST;

                }
                try {
                    genre = id3v2Tag.getGenreDescription();
                    if (genre == null)
                        genre = MISSING_GENRE;

                } catch (Exception e){
                    genre= MISSING_GENRE;

                }


                try {
                    String year = id3v2Tag.getYear() == null? ((ID3v24Tag)id3v2Tag).getRecordingTime(): id3v2Tag.getYear();

                    releasedYear = Integer.parseInt(year);

                } catch (Exception e){

                }

                try {
                     trackNumber = Integer.parseInt(id3v2Tag.getTrack().split("/")[0]);
                } catch (Exception e){


                }

                Instant createdOn= Instant.ofEpochMilli(file.lastModified());

                Track track = new Track()
                        .id(0L)
                    .type(type)
                    .subfolder(Track.INBOX)
                    .filePath(file.getPath())
                    .name(title)
                    .artist(artist)
                    .album(album, albumArtist, releasedYear)
                    .genre(genre)
                    .playbackLength((int)mp3File.getLengthInSeconds())
                    .bitRate(mp3File.getBitrate())
                    .trackNumber(trackNumber)
                    .createdOn(createdOn);


                Path newPath = Paths.get(applicationProperties.getMediaFolder(), track.getFilePath());
                File newFile = newPath.toFile();

                if (!newFile.getParentFile().exists())
                    newFile.getParentFile().mkdirs();

                //save to new file base on media file properties. i.e title, artist, album
                mp3File.save(newFile.getAbsolutePath());



                // extract cover image

                byte[] albumImageData = id3v2Tag.getAlbumImage();
                if (albumImageData != null) {
                    String mimeType = id3v2Tag.getAlbumImageMimeType();
                    // Write image to file - can determine appropriate file extension from the mime type
                    String extractedImageFileName = "album-artwork";
                    RandomAccessFile extractedImageFile = new RandomAccessFile(extractedImageFileName, "rw");
                    extractedImageFile.write(albumImageData);
                    extractedImageFile.close();

                    File folder = newFile.getParentFile();
                    File artwork = new File(folder, "artwork.jpg");//.replaceFirst(SOURCE, TARGET);
                    File thumbnail = new File(folder, "thumbnail.jpg");//.replaceFirst(SOURCE, TARGET);

                    try {

                        if (artwork.exists())
                            System.out.format("Image file: {0} already exist ", artwork);
                        else {
                            ImageUtils.scale(extractedImageFileName, 256, 256, artwork.getAbsolutePath());
                            System.out.format("Image file: {0} don't  exist will create a scaled version ", artwork);

                        }

                        if (thumbnail.exists())
                            System.out.format("Image file: {0} already exist ", thumbnail);
                        else {
                            ImageUtils.scale(extractedImageFileName, 128, 128, thumbnail.getAbsolutePath());
                            System.out.format("Image file: {0} don't  exist will create a scaled version ", thumbnail);

                        }
                    }catch (Exception ex) {
                        System.out.format("Unable to extract artwork from id3 tag of file {0} ", file.getName());
                    }

                }

                // delete original file
                File parent = file.getParentFile();

//                do {
//                    try {
//                        Thread.sleep(25);
//                    } catch (InterruptedException e) {
//
//                    }
//
//                } while (file.length() != newFile.length());


                if (file.delete()) {

                    if (!parent.getName().equals(Track.DOWNLOADED) &&   parent.list().length == 0)
                        parent.delete();
                }


                this.trackRepository.save(track);



            }

        }catch (UnsupportedTagException e){
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }


//    public void fixFilePath(String folder) {
//
//        TrackCriteria criteria= new TrackCriteria();
//        StringFilter subfolder= new StringFilter();
//        subfolder.setEquals("outbox");
//        criteria.setSubfolder(subfolder);
////        Page<TrackDTO> page= trackQueryService.findByCriteria(criteria, PageRequest.of(0, 100));
////        List<Track> allTracks= trackMapper.toEntity(page.getContent());
//
//        Track example= new Track().subfolder("outbox");
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnoreNullValues()
//                .withIgnorePaths("filePath");
//        //  .withIncludeNullValues();
//                //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
//
//        int pageNumber= 0, totalPages= 0;
//
//        do  {
//            Page<Track> page= trackRepository.findAll(Example.of(example, matcher), PageRequest.of(pageNumber, 100).withSort(Sort.by("album.artist.name").and(Sort.by("album.name"))));
//
//            for (Track track : page.getContent()) {
//                logger.info("track: {} - {} - {} ", track.getAlbum().getArtist(), track.getAlbum(), track.getName());
//                try {
//                    Track updatedTrack= moveMediaFile(track, track.getSubfolder());
//                    trackRepository.save(updatedTrack);
//                } catch (IOException e) {
//                    logger.error("unable to move media file", e.getMessage());
//                }
//            }
//
//            totalPages= page.getTotalPages();
//            pageNumber++;
//
//        } while (pageNumber < totalPages);
//
//    }

//    @Async
//    public void execute(Track track) throws Exception {
//
//        logger.debug("Updating  media file  {}", track);
//
//        // 1. find original file
//        Optional<Track> originalTrack = trackRepository.findById(track.getId());
//        if (originalTrack.isEmpty())
//            throw new Exception("Not media file was found!");
//
//        logger.info("-->>>>>>>>>> original file path  {}", originalTrack.get().getFilePath());
//
//        moveMediaFile(originalTrack.get(), Track.INBOX);
//
//        Path currentFilePath = Paths.get(applicationProperties.getMediaFolder(), originalTrack.get().getFilePath()).toAbsolutePath();
//
//        //move to temp to update tags
//
//        logger.info("-->>>>>>>>>>before creating temp file  {}", currentFilePath.getParent().toFile());
//        File tempFile = File.createTempFile("media-library", ".mp3", currentFilePath.getParent().toFile());
//        logger.info("-->>>>>>>>>>temp file  {}",tempFile.toString());
//
//        if (currentFilePath.toFile().renameTo(tempFile)) {
//
//
//            // 3. update  id3 tags using original media file
//            Mp3File mp3File = new Mp3File(tempFile);
//
//            ID3v2 id3v2Tag = new ID3v23Tag();
//            id3v2Tag.setTitle(track.getName());
//            id3v2Tag.setArtist(track.getArtist().getName());
//            id3v2Tag.setAlbum(track.getAlbum().getName());
//            id3v2Tag.setAlbumArtist(track.getAlbum().getArtist().getName());
//            id3v2Tag.setYear(track.getAlbum().getReleasedYear().toString());
//
//            try {
//                id3v2Tag.setGenreDescription(track.getGenre().getName());
//            } catch (Exception ex) {
//
//            }
//            mp3File.setId3v2Tag(id3v2Tag);
//
//            // 4. now update original media file with new information
//            originalTrack.get()
//                    .subfolder(Track.INBOX)
//                    .name(track.getName())
//                    .artist(track.getArtist().getName())
//                    .album( track.getAlbum().getName(), track.getAlbum().getArtist().getName(), track.getAlbum().getReleasedYear())
//                    .genre(track.getGenre().getName());
//
//            File destFile = Paths.get(applicationProperties.getMediaFolder(), originalTrack.get().getFilePath()).toAbsolutePath().toFile();
//
//            // 5. create parent folders if don't exists
//            if (!destFile.getParentFile().exists())
//                destFile.getParentFile().mkdirs();
//
//
//            mp3File.save(destFile.getAbsolutePath());
//
//            tempFile.delete();
//
//
//
//            // 6. persist changes
//            trackRepository.save(originalTrack.get());
//
//        }
//    }








}
