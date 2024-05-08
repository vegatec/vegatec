package net.vegatec.media_library.service.events;

import org.springframework.context.ApplicationEvent;

import java.io.File;

public class FileCreated extends ApplicationEvent {
    public File getFile() {
        return file;
    }

    private final File file;

    public FileCreated(Object source, File file) {
        super(source);
        this.file = file;
    }
}
