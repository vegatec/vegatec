package net.vegatec.media_library.command.commands;


public class CreateMediaFile  extends BaseCommand<String> {

    private String title;

    private String folder;

    private String path;

    public String getTitle() {
        return title;
    }

    public String getFolder() {
        return folder;
    }

    public String getPath() {
        return path;
    }



    public CreateMediaFile(String id, String title, String folder, String path  ) {
            super(id);
            this.title = title;
            this.folder = folder;
            this.path = path;

    }


}
