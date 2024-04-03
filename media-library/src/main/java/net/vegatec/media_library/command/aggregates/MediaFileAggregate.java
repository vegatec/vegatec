package net.vegatec.media_library.command.aggregates;

import lombok.extern.slf4j.Slf4j;
import net.vegatec.media_library.command.commands.CreateMediaFileCommand;

import net.vegatec.media_library.common.events.MediaFileCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class MediaFileAggregate {

    @AggregateIdentifier
    private Long id;

    private String path;

    private String name;


    public MediaFileAggregate(){}

    @CommandHandler
    public MediaFileAggregate(CreateMediaFileCommand createMediaFileCommand) {
      //  log.info("CreateAccountCommand received.");
        AggregateLifecycle.apply(new MediaFileCreatedEvent(
            createMediaFileCommand.getId()));
    }
}
