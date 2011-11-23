package edu.cwru.eecs393;

import java.util.ArrayList;
import java.util.Comparator;

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
    
    public static class ItemArtistComparator implements Comparator<ArrayList<Item>> {
		public int compare(ArrayList<Item> item1, ArrayList<Item> item2) {
			return item1.get(0).getArtist().compareTo(item2.get(0).getArtist());
		}
    }
    
    public static class ItemAlbumComparator implements Comparator<ArrayList<Item>> {
		public int compare(ArrayList<Item> item1, ArrayList<Item> item2) {
			return item1.get(0).getAlbum().compareTo(item2.get(0).getAlbum());
		}
    }
    
    public static class ItemComparator implements Comparator<Item> {
		public int compare(Item item1, Item item2) {
			return item1.getTitle().compareTo(item2.getTitle());
		}
    }
}


