package net.vegatec.media_library.command.services;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import net.vegatec.media_library.command.aggregates.MediaFileAggregate;
import net.vegatec.media_library.command.commands.CreateMediaFile;
import net.vegatec.media_library.common.events.MediaFileCreated;
import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.util.ImageUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;


@Service
public class MediaFileImporter implements Runnable {

    private final  Logger logger = LoggerFactory.getLogger(MediaFileImporter.class);

    private static final String MISSING_TITLE = "missing title";
    private static final String MISSING_ARTIST = "missing artist ";
    private static final String MISSING_ALBUM = "missing title";
    private static final String MISSING_ALBUM_ARTIST = "missing album artist";
    private static final String MISSING_GENRE = "missing genre";



    private WatchService watchService;
    private final CommandGateway commandGateway;
    private final ApplicationProperties applicationProperties;

    private Thread thread;


    public MediaFileImporter(CommandGateway commandGateway, ApplicationProperties applicationProperties)   {
        this.commandGateway = commandGateway;
        this.applicationProperties = applicationProperties;

        this.thread = new Thread(this);
        this.thread.start();

    }



    @Override
    public void run() {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            Path foler = Paths.get(applicationProperties.getMediaFolder(), MediaFileAggregate.DOWNLOADED);

            foler.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    System.out.println(
                        "Event kind:" + event.kind()
                            + ". File affected: " + filename.toFile() + ".");

                    Path path= Paths.get(applicationProperties.getMediaFolder(), MediaFileAggregate.DOWNLOADED, filename.toString());

                    CreateMediaFile command= buildCreateMediaFileCommand(java.lang.System.currentTimeMillis(), path.toFile());

                    if (command != null)
                        commandGateway.send(command);
                }
                key.reset();
            }
        } catch (Exception ex) {
            System.err.println("something bad happened");
        }
    }



    protected CreateMediaFile buildCreateMediaFileCommand(Long id, File file) {
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

                LocalDateTime createdOn= Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime();

                CreateMediaFile command = new CreateMediaFile(
                    id,
                    MediaFileAggregate.DOWNLOADED,
                    file.getPath(),
                    title,
                    artist,
                    album,
                    albumArtist,
                    releasedYear,
                    genre,
                    mp3File.getLengthInSeconds(),
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

                return command;

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



}
