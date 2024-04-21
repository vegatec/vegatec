package net.vegatec.media_library.command.aggregates;


import net.vegatec.media_library.command.commands.CreateMediaFile;

import net.vegatec.media_library.common.events.MediaFileCreated;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;

@Aggregate
public class MediaFileAggregate {
    Logger logger = LoggerFactory.getLogger(MediaFileAggregate.class);

    public static final String DOWNLOADED = "downloaded";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";
    public static final String TRASH = "trash";


   // private final ApplicationProperties applicationProperties;

    @AggregateIdentifier
    private Long id;

    private String path;

    private String subfolder;

    private String title;

    private String artistName;

    private String albumTitle;

    private String albumArtistName;

    private Integer albumReleasedYear;

    private String genreName;

    private Integer trackNumber;

    private Long playbackLength;

    private LocalDateTime createdOn;


    public MediaFileAggregate()  {}

    @CommandHandler
    public  MediaFileAggregate(CreateMediaFile command) {
        logger.info("CreateMediaFileCommand received.");
        //this.applicationProperties = applicationProperties;
        MediaFileCreated mediaFileCreated= new MediaFileCreated(
            command.getId(),
            command.getTitle(),
            command.getSubfolder(),
            command.getPath(),
            command.getArtistName(),
            command.getAlbumTitle(),
            command.getAlbumArtistName(),
            command.getAlbumReleasedYear(),
            command.getGenreName(),
            command.getPlaybackLength(),
            command.getTrackNumber(),
            command.getCreatedOn()
        );

        AggregateLifecycle.apply(mediaFileCreated);
    }



    @EventSourcingHandler
    public void on(MediaFileCreated event) {
        logger.info("A MediaFileCreatedEvent occurred.");
        this.id= event.getId();
        this.title = event.getTitle();
        this.subfolder = event.getSubfolder();
        this.path= event.getPath();

       // AggregateLifecycle.apply(new MediaFileActivatedEvent(this.accountId, "ACTIVATED"));
    }


}
