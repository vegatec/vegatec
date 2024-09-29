package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.mediator.SpringMediator;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.RefreshPaths;
import net.vegatec.media_library.service.commands.UpdateTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class RefreshPathsHandler extends BaseTrackHandler implements CommandHandler<RefreshPaths, Void> {
//    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);

    private final ApplicationProperties applicationProperties;
    private final SpringMediator mediator;
    protected final TrackService trackService;

    public RefreshPathsHandler(ApplicationProperties applicationProperties, SpringMediator mediator, TrackService trackRepository) {
        this.applicationProperties = applicationProperties;
        this.mediator = mediator;
        this.trackService = trackRepository;
    }

    Map<File,File> imageCache = new HashMap<>();

    @Override
    public Void handle(RefreshPaths command) {
        LOG.info("Handling RefreshPaths command");
        int pageNumber = 0;

        Page<TrackDTO> page= this.trackService.findAll(PageRequest.of(pageNumber, 100));

        while (pageNumber <= page.getTotalPages()) {
            page= this.trackService.findAll(PageRequest.of(pageNumber++, 100));
            page.getContent().stream().forEach(t-> {
                LOG.info(t.toString());
                mediator.send(new UpdateTrack(t, true));
            });
        }




        return null;
    }


}
