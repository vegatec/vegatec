package net.vegatec.media_library.command.aggregates;

import lombok.extern.slf4j.Slf4j;
import net.vegatec.media_library.command.commands.CreateMediaFileCommand;

import net.vegatec.media_library.common.events.MediaFileCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;

@Aggregate
@Slf4j
public class MediaFileAggregate {

    @AggregateIdentifier
    private Long id;

    private String path;
    private String name;
    private String sortName;
    private String artistName;
    private String artistSortName;
    private String albumName;
    private String albumSortName;

    private String albumArtistName;
    private String albumArtistSortName;

    private Integer albumReleasedOn;
    private Long playbackLength;

    private LocalDateTime createdOn;

    public MediaFileAggregate(){}

    @CommandHandler
    public MediaFileAggregate(CreateMediaFileCommand createMediaFileCommand) {
       // log.info("CreateMediaFileCommand received.");
        AggregateLifecycle.apply(new MediaFileCreatedEvent(
            createMediaFileCommand.getId()));
    }



    @EventSourcingHandler
    public void on(MediaFileCreatedEvent accountCreatedEvent) {
        //log.info("An MediaFileCreatedEvent occurred.");


       // AggregateLifecycle.apply(new MediaFileActivatedEvent(this.accountId, "ACTIVATED"));
    }
}
