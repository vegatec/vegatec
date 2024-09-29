package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.repository.TrackRepository;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
public abstract  class BaseTrackHandler  {
    protected static final Logger LOG = LoggerFactory.getLogger(BaseTrackHandler.class);


    /**
     * Move media file between inbox and outbox relative to application media folder
     *
     * @param sourcePath            media file to be moved
     * @param destinationPath destination subfolder under the application media folder
     * @throws IOException
     */

    protected void moveFile(Path sourcePath, Path destinationPath) throws IOException {
        if (LOG.isDebugEnabled())
            LOG.debug("Moving file {} to {} ", sourcePath, destinationPath);


        //ignore of moving to same subfolder
        if (sourcePath.equals(destinationPath))
            return;


        // check if source file exist
        if (!sourcePath.toFile().exists())
            throw new FileNotFoundException(String.format("source file {} doesn't exist", sourcePath.toString()));



        // 3. check if a same file exist on destination. If so don't move it
        if (destinationPath.toFile().exists())
            throw new FileExistsException(String.format("File {} exist on destination folder", destinationPath));


        // 4. create destination  parent folders if don't exists
        if (!destinationPath.getParent().toFile().exists())
            destinationPath.getParent().toFile().mkdirs();


        // 5. move file new destination location
        if (sourcePath.toFile().renameTo(destinationPath.toFile())) {

            LOG.info("successfully moved file {} to {}", sourcePath, destinationPath);

//            // 6. if files were moved to outbox make file read only or read/write otherwise
//            Set<PosixFilePermission> filePermissions = MediaFile.OUTBOX.equalsIgnoreCase(destinationPath) ?
//                PosixFilePermissions.fromString("r--r--r--") :
//                PosixFilePermissions.fromString("rw-rw-rw-");
//
//            Files.setPosixFilePermissions(destFilePath, filePermissions);


            // 8. copy or move any relate images
            moveArtworks(sourcePath.getParent(), destinationPath.getParent());

            // 9. remove source folders if folder structure is empty

            removeEmptyFolders(sourcePath.getParent());

        } else {
            LOG.error("failed to move  file: {} to  {}", sourcePath.toAbsolutePath(), destinationPath);
            throw new IOException("Error moving file");
        }



    }



    /**
     * Delete media file  relative to application media folder
     *
     * @param sourcePath  media file to be deleted

     * @throws IOException
     */

    protected void deleteFile(Path sourcePath) throws IOException {
        if (LOG.isDebugEnabled())
            LOG.debug("Removing file {}  ", sourcePath);


        // check if source file exist
        if (!sourcePath.toFile().exists())
            throw new FileNotFoundException(String.format("source file {} doesn't exist", sourcePath.toString()));

        // 5. remove file
        Files.delete(sourcePath);


        removeEmptyFolders(sourcePath.getParent());

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

    protected void transferTimestamps(File originalFile, File newFile) {
        try {
            String command = String.format("/usr/bin/touch \"%s\" -r \"%s\"",  newFile.getAbsolutePath(), originalFile.getAbsolutePath());
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", command);

            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();

            int exitCode = 0;

            exitCode = process.waitFor();
            if (exitCode == 0) {
                //String fileName= audioFile.getAbsolutePath();
                originalFile.delete();
                //  tempFile.renameTo(new File(fileName));
            }
            assert exitCode == 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
