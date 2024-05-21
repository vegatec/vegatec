package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;

import java.io.File;

public class UploadFile implements Command<Void> {
    private File file;
    private String folder;

    public UploadFile(File imageFile, String folder) {
        this.file = imageFile;
        this.folder = folder;
    }

    public File getFile() {
        return file;
    }
    public String getFolder() {
        return folder;
    }
}
