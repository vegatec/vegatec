package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.commands.MoveTrack;
import net.vegatec.media_library.service.commands.UnpublishTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MoveTrackHandler  implements CommandHandler<MoveTrack, TrackDTO> {
    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);
    private final TrackRepository   repository;
    private final TrackMapper trackMapper;
    private final ApplicationProperties applicationProperties;

    public MoveTrackHandler(TrackRepository repository, TrackMapper trackMapper, ApplicationProperties applicationProperties) {
        this.repository = repository;
        this.trackMapper = trackMapper;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public TrackDTO handle(MoveTrack command) {
        LOG.info("Unpublish track with id {}", command.getTrackId());

        Optional<Track> track= repository.findById(command.getTrackId());

        if   (track.isPresent()) {

            Track value = track.get();

            String originalSubfolder = value.getSubfolder();

            Path sourcePath= Path.of(applicationProperties.getMediaFolder(), value.getFilePath());

            value.subfolder(command.getSubfoler());

            Path destinationPath= Path.of(applicationProperties.getMediaFolder(), value.getFilePath());

            try {
                moveFile(sourcePath, destinationPath);
                value= repository.save(value);;

            } catch (IOException e) {
                value.setSubfolder(originalSubfolder);
            }


            return trackMapper.toDto(value);
        }

        return null;
    }

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
