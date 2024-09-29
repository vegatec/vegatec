package net.vegatec.media_library.service.commands.handlers;

import com.mpatric.mp3agic.*;
import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.service.commands.EmbedImages;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Component
public class EmbedImagesHandler extends BaseTrackHandler implements CommandHandler<EmbedImages, Void> {
//    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);

    private final ApplicationProperties applicationProperties;

    public EmbedImagesHandler( ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    Map<File,File> imageCache = new HashMap<>();

    @Override
    public Void handle(EmbedImages command) {
        LOG.info("Handling Empty Trash");


        try {
            traverse(Track.INBOX);
            traverse(Track.OUTBOX);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    private void traverse(String folder) throws IOException {

        Path path =  Path.of(applicationProperties.getMediaFolder(), folder);

        LOG.info("Started scanning all media files on {}", folder);

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile (Path path, BasicFileAttributes attrs)    throws IOException
            {
                File file = path.toFile();
                if (  attrs.isRegularFile() && file.getName().endsWith(".mp3") ) {
                    File imageFile = findImage(file.getParentFile());
                    if (imageFile != null) {
                       embedImage(file, imageFile);
                    }
                }

                return FileVisitResult.CONTINUE;
            }


        });
    }

    private File findImage(File folder) {
        File cachedImage= imageCache.get(folder);
        if (cachedImage != null)
            return cachedImage;
        else {
            File[] imageFiles = folder.listFiles((arg0, name) -> name.endsWith(".jpg"));
            Arrays.sort(imageFiles, (Comparator<File>) (file0, file1) -> (int) (file1.length() - file0.length()));
            if  (imageFiles.length > 0) {
                imageCache.put(folder, imageFiles[0]);
                return imageFiles[0];
            }
        }
        return null;
    }

    private void embedImage( File audioFile, File selectedImageFile) {
        LOG.info(String.format("image: %s - audio: %s", selectedImageFile.getName(), audioFile.getName()));
        if (audioFile != null && audioFile.length() > 0 && selectedImageFile != null) {


            try {
                Mp3File mp3File = new Mp3File(audioFile);


                if (mp3File.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                    if ( id3v2Tag != null && (id3v2Tag.getAlbumImage() == null || id3v2Tag.getAlbumImage().length == 0) ) {
                        RandomAccessFile file = new RandomAccessFile(selectedImageFile, "rw");
                        byte[] data = new byte[(int) file.length()];
                        file.readFully(data, 0, data.length);

                        id3v2Tag.setAlbumImage(data, "jpg");

                        File tempFile = new File(String.format("%s/wc-%s", audioFile.getParent(), audioFile.getName()));

                        mp3File.save(tempFile.getAbsolutePath());

                        transferTimestamps(audioFile, tempFile);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }  catch (Exception e) {
                LOG.error(String.format("audio file: {}, image file {}, error: {}", audioFile.getAbsolutePath(), selectedImageFile.getName(), e.getMessage()));
            }

        }

    }







}
