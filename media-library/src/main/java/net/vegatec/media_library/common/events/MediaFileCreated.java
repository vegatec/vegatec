package net.vegatec.media_library.common.events;

import java.time.LocalDateTime;


public class MediaFileCreated extends  BaseEvent<String>{

    private String title;

    private String folder;

    private String path;


    public MediaFileCreated(String id, String title, String folder, String path) {
        super(id);
        this.title = title;
        this.folder = folder;
        this.path = path;

    }


    public String getTitle() {
        return title;
    }

    public String getFolder() {
        return folder;
    }

    public String getPath() {
        return path;
    }

}
