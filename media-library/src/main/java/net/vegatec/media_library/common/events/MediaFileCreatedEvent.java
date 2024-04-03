package net.vegatec.media_library.common.events;

public class MediaFileCreatedEvent extends  BaseEvent<Long>{
    public MediaFileCreatedEvent(Long id) {
        super(id);
    }
}
