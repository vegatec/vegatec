package net.vegatec.media_library.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.mpatric.mp3agic.*;
import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Artist;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.domain.TrackType;
import net.vegatec.media_library.domain.Track_;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackSearchRepository;
import net.vegatec.media_library.service.criteria.TrackCriteria;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing {@link net.vegatec.media_library.domain.Track}.
 */
@Service
@Transactional
public class TrackService {

    private final Logger logger = LoggerFactory.getLogger(TrackService.class);


    private static final String MISSING_TITLE = "missing title";
    private static final String MISSING_ARTIST = "missing artist ";
    private static final String MISSING_ALBUM = "missing title";
    private static final String MISSING_ALBUM_ARTIST = "missing album artist";
    private static final String MISSING_GENRE = "missing genre";

    public static final String DOWNLOADED = "downloaded";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";
    public static final String TRASH = "trash";

    private final TrackRepository trackRepository;

    private final TrackTypeRepository trackTypeRepository;

    private final TrackMapper trackMapper;

    private final TrackSearchRepository trackSearchRepository;

    private final TrackQueryService trackQueryService;

    private final ApplicationProperties applicationProperties;

    public TrackService(TrackRepository trackRepository, TrackTypeRepository trackTypeRepository, TrackMapper trackMapper, TrackSearchRepository trackSearchRepository, TrackQueryService trackQueryService, ApplicationProperties applicationProperties) {
        this.trackRepository = trackRepository;
        this.trackTypeRepository = trackTypeRepository;
        this.trackMapper = trackMapper;
        this.trackSearchRepository = trackSearchRepository;
        this.trackQueryService = trackQueryService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackDTO save(TrackDTO trackDTO) {
        logger.debug("Request to save Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        TrackDTO result = trackMapper.toDto(track);
        trackSearchRepository.index(track);
        return result;
    }

    /**
     * Update a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackDTO update(TrackDTO trackDTO) {
        logger.debug("Request to update Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        TrackDTO result = trackMapper.toDto(track);
        trackSearchRepository.index(track);
        return result;
    }

    /**
     * Partially update a track.
     *
     * @param trackDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrackDTO> partialUpdate(TrackDTO trackDTO) {
        logger.debug("Request to partially update Track : {}", trackDTO);

        return trackRepository
            .findById(trackDTO.getId())
            .map(existingTrack -> {
                trackMapper.partialUpdate(existingTrack, trackDTO);

                return existingTrack;
            })
            .map(trackRepository::save)
            .map(savedTrack -> {
                trackSearchRepository.index(savedTrack);
                return savedTrack;
            })
            .map(trackMapper::toDto);
    }

    /**
     * Get all the tracks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> findAll(Pageable pageable) {
        logger.debug("Request to get all Tracks");
        return trackRepository.findAll(pageable).map(trackMapper::toDto);
    }

    /**
     * Get all the tracks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrackDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trackRepository.findAllWithEagerRelationships(pageable).map(trackMapper::toDto);
    }

    /**
     * Get one track by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrackDTO> findOne(Long id) {
        logger.debug("Request to get Track : {}", id);
        return trackRepository.findOneWithEagerRelationships(id).map(trackMapper::toDto);
    }

    /**
     * Delete the track by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        logger.debug("Request to delete Track : {}", id);
        trackRepository.deleteById(id);
        trackSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the track corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> search(String query, Pageable pageable) {
        logger.debug("Request to search for a page of Tracks for query {}", query);
        return trackSearchRepository.search(query, pageable).map(trackMapper::toDto);
    }


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
                    .subfolder(INBOX)
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
                file.delete();



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

//    @Transactional(readOnly = true)
    public void fixFilePath(String folder) {

        TrackCriteria criteria= new TrackCriteria();
        StringFilter subfolder= new StringFilter();
        subfolder.setEquals("outbox");
        criteria.setSubfolder(subfolder);
//        Page<TrackDTO> page= trackQueryService.findByCriteria(criteria, PageRequest.of(0, 100));
//        List<Track> allTracks= trackMapper.toEntity(page.getContent());

        Track example= new Track().subfolder("inbox");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("filePath");
        //  .withIncludeNullValues();
                //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);

        int pageNumber= 0, totalPages= 0;

        do  {
            Page<Track> page= trackRepository.findAll(Example.of(example, matcher), PageRequest.of(pageNumber, 100).withSort(Sort.by("album.artist.name").and(Sort.by("album.name"))));

            for (Track track : page.getContent()) {
                logger.info("track: {} - {} - {} ", track.getAlbum().getArtist(), track.getAlbum(), track.getName());
                try {
                    Track updatedTrack= moveMediaFile(track, track.getSubfolder());
                    trackRepository.save(updatedTrack);
                } catch (IOException e) {
                    logger.error("unable to move media file", e.getMessage());
                }
            }

            totalPages= page.getTotalPages();
            pageNumber++;

        } while (pageNumber < totalPages);

    }

    /**
     * Move media file between inbox and outbox relative to application media folder
     *

     * @param destinationSubfolder destination subfolder under the application media folder
     * @throws IOException
     */

    protected Track moveMediaFile(Track track, String destinationSubfolder) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("Moving out media file to %s ", destinationSubfolder);


        //ignore of moving to same subfolder
//        if (track.getSubfolder().equalsIgnoreCase(destinationSubfolder))
//            return;

        // 1. remember original media file location
        Path sourceFilePath = Paths.get(applicationProperties.getMediaFolder(),  track.getFilePath()).toAbsolutePath();

        // check if source file exist
        if (!sourceFilePath.toFile().exists())
            throw new FileNotFoundException(sourceFilePath.toString());

        //change subfolder
      //  track.subfolder(destinationSubfolder);

        if (track.getFilePath() != track.getUpdatedPath())
            track.setFilePath(track.getUpdatedPath());

        // 2. determine new location. No only the subfolder might change
        Path destFilePath = Paths.get(applicationProperties.getMediaFolder(), destinationSubfolder, track.getUpdatedPath()).toAbsolutePath();

        //source and destination the same ignore. no necessary because already check above
//        if (destFilePath == sourceFilePath)
//            return track;

        // 3. check if a same file exist on destination. If so don't move it
        if (destFilePath.toFile().exists())
            throw new FileAlreadyExistsException(String.format("File %s exist on destination folder", destFilePath));


        // 4. create destination  parent folders if don't exists
        if (!destFilePath.getParent().toFile().exists())
            destFilePath.getParent().toFile().mkdirs();


        // 5. move file new destination location
        if (sourceFilePath.toFile().renameTo(destFilePath.toFile())) {

            logger.info("successfully moved file %s to destination", destFilePath);

            // 6. if files were moved to outbox make file read only or read/write otherwise
            Set<PosixFilePermission> filePermissions = OUTBOX.equalsIgnoreCase(destinationSubfolder) ?
                PosixFilePermissions.fromString("r--r--r--") :
                PosixFilePermissions.fromString("rw-rw-rw-");

            Files.setPosixFilePermissions(destFilePath, filePermissions);

            // 7. Verify that file exists on destination and update to which subfolder resides.
            if (destFilePath.toFile().exists())
                track.subfolder(destinationSubfolder);


            // 8. copy or move any relate images
            moveArtworks(sourceFilePath.getParent(), destFilePath.getParent());

            // 9. remove source folders if folder structure is empty

            removeEmptyFolders(sourceFilePath.getParent());

        } else {
            logger.error("failed to move  file: %s to destination subfolder: %s", sourceFilePath.toAbsolutePath(), destinationSubfolder);
            throw new IOException("Error moving file");
        }

        return track;


    }


    protected void moveArtworks(Path sourceAlbumPath, Path destAlbumPath) {
        // if is a check in file also check out the artwork



        // list all images

        final PathMatcher filter = FileSystems.getDefault().getPathMatcher("glob:**/*.jpg");
        try (final Stream<Path> stream = Files.list(sourceAlbumPath)) {
            System.out.println("images found");

            stream.filter(filter::matches)
                .forEach(i -> {
                    System.out.println(i.toString());
                    System.out.println(i.toFile().getName());
                    try {
                        if (hasMediaFiles(sourceAlbumPath))
                            Files.copy(i, Paths.get(destAlbumPath.toString(), i.toFile().getName()), StandardCopyOption.REPLACE_EXISTING);
                        else
                            Files.move(i, Paths.get(destAlbumPath.toString(), i.toFile().getName()), StandardCopyOption.REPLACE_EXISTING);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
        } catch (IOException ex) {
            System.err.println(ex.getMessage());

        }


    }

    private boolean hasMediaFiles(Path path) {

        final PathMatcher filter = FileSystems.getDefault().getPathMatcher("glob:**/*.mp3");

        try (final Stream<Path> stream = Files.list(path)) {
            System.out.println("audio files found");

            return stream.filter(filter::matches).count() > 0;

        } catch (IOException ex) {
            System.err.println(ex.getMessage());

        }
        return false;
    }

    private static boolean isEmptyFolder(final Path folder) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(folder)) {
            return !dirStream.iterator().hasNext();
        }
    }

    private void removeEmptyFolders(Path path) {
        try {

            if (isEmptyFolder(path)) {
                Path sourceArtistPath = path.getParent();
                path.toFile().delete();
                if (isEmptyFolder(sourceArtistPath))
                    sourceArtistPath.toFile().delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
