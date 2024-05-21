package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;

import java.io.File;

public class UploadMediaFile implements Command<Void> {
    private File file;

    public UploadMediaFile(File imageFile) {
        this.file = imageFile;
    }
    public File getFile() {
        return file;
    }

}
