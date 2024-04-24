package net.vegatec.media_library.command.aggregates;


import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import net.vegatec.media_library.command.commands.CreateMediaFile;

import net.vegatec.media_library.common.events.MediaFileCreated;

import net.vegatec.media_library.config.ApplicationProperties;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;



@Aggregate
public class MediaFileAggregate {
    Logger logger = LoggerFactory.getLogger(MediaFileAggregate.class);

    private static final String MISSING_TITLE = "missing title";
    private static final String MISSING_ARTIST = "missing artist ";
    private static final String MISSING_ALBUM = "missing title";
    private static final String MISSING_ALBUM_ARTIST = "missing album artist";
    private static final String MISSING_GENRE = "missing genre";

    public static final String DOWNLOADED = "downloaded";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";
    public static final String TRASH = "trash";


    private  ApplicationProperties applicationProperties;

    @AggregateIdentifier
    private Long id;

    private String path;

    private String subfolder;



    private String name;

    private String artistName;

    private String albumTitle;

    private String albumArtistName;

    private Integer albumReleasedYear;

    private String genreName;

    private Integer trackNumber;

    private Integer playbackLength;

    private Instant createdOn;

    private Integer bitRate;

    public Logger getLogger() {
        return logger;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getAlbumArtistName() {
        return albumArtistName;
    }

    public Integer getAlbumReleasedYear() {
        return albumReleasedYear;
    }

    public String getGenreName() {
        return genreName;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public Integer getPlaybackLength() {
        return playbackLength;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public MediaFileAggregate()  {}

    @CommandHandler
    public  MediaFileAggregate(CreateMediaFile command, ApplicationProperties applicationProperties) {

        logger.info("CreateMediaFileCommand received.");
        this.applicationProperties = applicationProperties;
        MediaFileCreated event= buildMediaFileCreatedEvent(command.getId(), command.getFile());
//        MediaFileCreated mediaFileCreated= new MediaFileCreated(
//            command.getId(),
//            command.getTitle(),
//            command.getSubfolder(),
//            command.getPath(),
//            command.getArtistName(),
//            command.getAlbumTitle(),
//            command.getAlbumArtistName(),
//            command.getAlbumReleasedYear(),
//            command.getGenreName(),
//            command.getPlaybackLength(),
//            command.getTrackNumber(),
//            command.getCreatedOn()
//        );

        AggregateLifecycle.apply(event);
    }



    @EventSourcingHandler
    public void on(MediaFileCreated event) {
        logger.info("A MediaFileCreatedEvent occurred.");
        this.id= event.getId();
        this.subfolder = event.getSubfolder();
        this.path= event.getPath();
        this.name = event.getName();
        this.artistName= event.getArtistName();
        this.albumTitle= event.getAlbumTitle();
        this.albumArtistName= event.getAlbumArtistName();
        this.albumReleasedYear= event.getAlbumReleasedYear();
        this.genreName= event.getGenreName();
        this.trackNumber= event.getTrackNumber();
        this.playbackLength= event.getPlaybackLength();
        this.createdOn= event.getCreatedOn();


       // AggregateLifecycle.apply(new MediaFileActivatedEvent(this.accountId, "ACTIVATED"));
    }




    protected MediaFileCreated buildMediaFileCreatedEvent(Long id, File file) {
        if(logger.isDebugEnabled())
            logger.debug("buildCreateMediaFileCommand("+ file+ ")");
        try {

            Mp3File mp3File = new Mp3File(file);
            String folder = file.getParentFile().getPath();

            if (mp3File.hasId3v2Tag()) {
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
                    releasedYear = Integer.parseInt(id3v2Tag.getYear());
                } catch (Exception e){

                }

                try {
                    trackNumber = Integer.parseInt(id3v2Tag.getTrack().split("/")[0]);
                } catch (Exception e){


                }

                Instant createdOn= Instant.ofEpochMilli(file.lastModified());

                MediaFileCreated event = new MediaFileCreated(
                    id,
                    MediaFileAggregate.DOWNLOADED,
                    file.getPath(),
                    title,
                    artist,
                    album,
                    albumArtist,
                    releasedYear,
                    genre,
                    (int)mp3File.getLengthInSeconds() ,
                    trackNumber,
                    createdOn
                );

//
//                Path newPath = Paths.get(applicationProperties.getMediaFolder(), event.getPath());
//                File newFile = newPath.toFile();
//
//                if (!newFile.getParentFile().exists())
//                    newFile.getParentFile().mkdirs();
//
//                //save to new file base on media file properties. i.e title, artist, album
//                mp3File.save(newFile.getAbsolutePath());
//
//                // extract cover image
//
//                byte[] albumImageData = id3v2Tag.getAlbumImage();
//                if (albumImageData != null) {
//                    String mimeType = id3v2Tag.getAlbumImageMimeType();
//                    // Write image to file - can determine appropriate file extension from the mime type
//                    String extractedImageFileName = "album-artwork";
//                    RandomAccessFile extractedImageFile = new RandomAccessFile(extractedImageFileName, "rw");
//                    extractedImageFile.write(albumImageData);
//                    extractedImageFile.close();
//
//                    File folder = newFile.getParentFile();
//                    File artwork = new File(folder, "artwork.jpg");//.replaceFirst(SOURCE, TARGET);
//                    File thumbnail = new File(folder, "thumbnail.jpg");//.replaceFirst(SOURCE, TARGET);
//
//
//                    if (artwork.exists())
//                        System.out.format("Image file: {0} already exist ", artwork);
//                    else {
//                        ImageUtils.scale(extractedImageFileName, 256, 256, artwork.getAbsolutePath());
//                        System.out.format("Image file: {0} don't  exist will create a scaled version ", artwork);
//
//                    }
//
//                    if (thumbnail.exists())
//                        System.out.format("Image file: {0} already exist ", thumbnail);
//                    else {
//                        ImageUtils.scale(extractedImageFileName, 128, 128, thumbnail.getAbsolutePath());
//                        System.out.format("Image file: {0} don't  exist will create a scaled version ", thumbnail);
//
//                    }
//
//                }
//
//
//                // delete original file
//                file.delete();

                return event;

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

        return null;
    }


    /**
     * Move media file between inbox and outbox relative to application media folder
     *

     * @param destinationSubfolder destination subfolder under the application media folder
     * @throws IOException
     */

    protected void moveMediaFile(String destinationSubfolder) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("Moving out media file to %s ", destinationSubfolder);


        //ignore of moving to same subfolder
        if (getSubfolder().equalsIgnoreCase(destinationSubfolder))
            return;

        // 1. remember original media file location
        Path sourceFilePath = Paths.get(applicationProperties.getMediaFolder(),  getPath()).toAbsolutePath();

        // check if source file exist
        if (!sourceFilePath.toFile().exists())
            throw new FileNotFoundException(sourceFilePath.toString());

        //change subfolder
        subfolder= destinationSubfolder;

        // 2. determine new location. No only the subfolder might change
        Path destFilePath = Paths.get(applicationProperties.getMediaFolder(), getPath()).toAbsolutePath();

        //source and destination the same ignore. no necessary because already check above
        if (destFilePath == sourceFilePath)
            return;

        // 3. check if a same file exist on destination. If so don't move it
        if (destFilePath.toFile().exists())
            throw new FileAlreadyExistsException(String.format("File %s exist on destination folder", destFilePath));


        // 4. create destination  parent folders if don't exists
        if (!destFilePath.getParent().toFile().exists())
            destFilePath.getParent().toFile().mkdirs();


        // 5. move file new destination location
        if (sourceFilePath.toFile().renameTo(destFilePath.toFile())) {

            logger.info("successfully moved file %s to inbox", destFilePath);

            // 6. if files were moved to outbox make file read only or read/write otherwise
            Set<PosixFilePermission> filePermissions = OUTBOX.equalsIgnoreCase(destinationSubfolder) ?
                PosixFilePermissions.fromString("r--r--r--") :
                PosixFilePermissions.fromString("rw-rw-rw-");

            Files.setPosixFilePermissions(destFilePath, filePermissions);

            // 7. Verify that file exists on destination and update to which subfolder resides.
            if (destFilePath.toFile().exists())
                subfolder= destinationSubfolder;


            // 8. copy or move any relate images
            moveArtworks(sourceFilePath.getParent(), destFilePath.getParent());

            // 9. remove source folders if folder structure is empty

            removeEmptyFolders(sourceFilePath.getParent());

        } else {
            logger.error("failed to move  file: %s to destination subfolder: %s", sourceFilePath.toAbsolutePath(), destinationSubfolder);
            throw new IOException("Error moving file");
        }



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
