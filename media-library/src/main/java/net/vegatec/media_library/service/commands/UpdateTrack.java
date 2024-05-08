package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;

public class UpdateTrack implements Command<Void> {



    private String title;
    private String artist;
    private String album;
    private String genre;
    private int releaseYear;
    private String albumArtist;
    public UpdateTrack(String title, String artist, String album, String genre, int releaseYear, String albumArtist) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.albumArtist = albumArtist;

    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }
}
