package edu.cwru.eecs393;

import android.content.ContentUris;
import android.net.Uri;

public class Item {
    long id;
    String artist;
    String title;
    String album;
    long duration;

    public Item(long id, String artist, String title, String album, long duration) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public Uri getURI() {
        return ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }
}
