package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.commands.EmptyTrash;
import net.vegatec.media_library.service.commands.MoveTrack;
import net.vegatec.media_library.service.criteria.TrackCriteria;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import tech.jhipster.service.filter.StringFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Component
public class EmptyTrashHandler extends BaseTrackHandler implements CommandHandler<EmptyTrash, Void> {
//    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);
    private final TrackRepository   repository;
    private final TrackMapper trackMapper;
    private final ApplicationProperties applicationProperties;

    public EmptyTrashHandler(TrackRepository repository, ApplicationProperties applicationProperties, TrackRepository repository1, TrackMapper trackMapper, ApplicationProperties applicationProperties1) {
        this.repository = repository;
        this.trackMapper = trackMapper;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Void handle(EmptyTrash command) {
        LOG.info("Handling Empty Trash");

        TrackCriteria criteria= new TrackCriteria();
        StringFilter subfolder= new StringFilter();
        subfolder.setEquals(Track.TRASH);
        criteria.setSubfolder(subfolder);

        Track example= new Track().subfolder(Track.TRASH);
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withIgnorePaths("filePath");
        //  .withIncludeNullValues();
        //.withStringMatcher(ExampleMatcher.StringMatcher.EXACT);



        List<Track> tracks= repository.findAll(Example.of(example, matcher));

        for (Track track : tracks) {
            LOG.info("track: {} - {} - {} ", track.getAlbum().getArtist(), track.getAlbum(), track.getName());
            try {
                repository.delete(track);
                deleteFile(Paths.get(applicationProperties.getMediaFolder(),track.getFilePath()));

            } catch (IOException e) {
                LOG.error("unable to empty trash because {}", e.getMessage());
            }
        }



        return null;
    }




}
