package edu.cwru.eecs393;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Retrieves and organizes media to play. Before being used, you must call {@link #prepare()},
 * which will retrieve all of the music on the user's device (by performing a query on a content
 * resolver). After that, it's ready to retrieve a random song, with its title and URI, upon
 * request.
 */
public class MusicRetriever {
    final static String TAG = "MusicRetriever";

    static ContentResolver mContentResolver;

    // the items (songs) we have queried
    static List<Item> mItems = new ArrayList<Item>();

    Random mRandom = new Random();

    public MusicRetriever(ContentResolver cr) {
        mContentResolver = cr;
    }

    public static Bitmap getAlbumArt(Item song) throws FileNotFoundException
    {
    	Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    	Uri uri = ContentUris.withAppendedId(sArtworkUri, song.albumid);
    	InputStream in = mContentResolver.openInputStream(uri);
    	Bitmap artwork = BitmapFactory.decodeStream(in);
    	return artwork;
    }
    
    /**
     * Loads music data. This method may take long, so be sure to call it asynchronously without
     * blocking the main thread.
     */
    public void prepare() {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i(TAG, "Querying media...");
        Log.i(TAG, "URI: " + uri.toString());

        // Perform a query on the content resolver. The URI we're passing specifies that we
        // want to query for all audio media on external storage (e.g. SD card)
        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, MediaStore.Audio.Media.TITLE);
        Log.i(TAG, "Query finished. " + (cur == null ? "Returned NULL." : "Returned a cursor."));

        if (cur == null) {
            // Query failed...
            Log.e(TAG, "Failed to retrieve music: cursor is null :-(");
            return;
        }
        if (!cur.moveToFirst()) {
            // Nothing to query. There is no music on the device. How boring.
            Log.e(TAG, "Failed to move cursor to first row (no query results).");
            return;
        }

        Log.i(TAG, "Listing...");

        // retrieve the indices of the columns where the ID, title, etc. of the song are
        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
        int albumidColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

        Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
        Log.i(TAG, "ID column index: " + String.valueOf(titleColumn));

        // add each song to mItems
        do {
            Log.i(TAG, "ID: " + cur.getString(idColumn) + " Title: " + cur.getString(titleColumn));
            mItems.add(new Item(
                    cur.getLong(idColumn),
                    cur.getString(artistColumn),
                    cur.getString(titleColumn),
                    cur.getString(albumColumn),
                    cur.getLong(durationColumn),
                    cur.getLong(albumidColumn)));
        } while (cur.moveToNext());

        Log.i(TAG, "Done querying media. MusicRetriever is ready.");
    }
    
    public static Item getItem(long id)
    {
    	for (int x = 0; x < mItems.size(); x++)
    	{
    		if (mItems.get(x).id == id)
    			return mItems.get(x);
    	}
    	return null;
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    /** Returns a random Item. If there are no items available, returns null. */
    public Item getRandomItem() {
        if (mItems.size() <= 0) return null;
        return mItems.get(mRandom.nextInt(mItems.size()));
    }
    
    public Cursor getSongsCursor()  throws SQLException
    {
    	Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Perform a query on the content resolver. The URI we're passing specifies that we
        // want to query for all audio media on external storage (e.g. SD card)
        return mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, MediaStore.Audio.Media.TITLE);
    }

    public static ArrayList<ArrayList<Item>> getGroupedSongs(String group)
    {
    	boolean groupArtist;
    	ArrayList<ArrayList<Item>> songs = new ArrayList<ArrayList<Item>>();
    	ArrayList<String> groups = new ArrayList<String>();
    	
    	if(group.toLowerCase().equals("artist"))
    		groupArtist = true;
    	else
    		groupArtist = false;
    	
    	for (int x = 0;x < mItems.size(); x++)
    	{
    		boolean found = false;
    		int y = 0;
    		while(y < groups.size())
    		{
    			if(groupArtist)
    			{
    				if(groups.get(y).equals(mItems.get(x).getArtist()))
					{
    						found = true;
    						break;
					}
    			}
    			else
    			{
    				if(groups.get(y).equals(mItems.get(x).getAlbum()))
					{
    						found = true;
    						break;
					}
    			}
    			y++;
    		}
    		
    		if(found)
    		{
    			songs.get(y).add(mItems.get(x));
    		}
    		else
    		{
    			if(groupArtist)
    			{
    				ArrayList<Item> list = new ArrayList<Item>();
    				groups.add(mItems.get(x).getArtist());
    				list.add(mItems.get(x));
    				songs.add(list);
    			}
    			else
    			{
    				ArrayList<Item> list = new ArrayList<Item>();
    				groups.add(mItems.get(x).getAlbum());
    				list.add(mItems.get(x));
    				songs.add(list);
    			}
    		}
    	}
    	return songs;
    }
    
    public static ArrayList<String> getArtists()
    {
    	ArrayList<String> artists = new ArrayList<String>();
    	
    	for(int x = 0; x < mItems.size(); x++)
    	{
    		boolean found = false;
    		int y = 0;
    		while(y < artists.size())
    		{
				if(((String)artists.get(y)).equals(((Item)mItems.get(x)).getArtist()))
				{
						found = true;
						break;
				}
    			y++;
    		}
    		
    		if(!found)
    		{
    			artists.add(mItems.get(x).getArtist());
    		}
    	}
    	return artists;
    }
    
    public static ArrayList<String> getAlbums()
    {
    	ArrayList<String> albums = new ArrayList<String>();
    	
    	for(int x = 0; x < mItems.size(); x++)
    	{
    		boolean found = false;
    		int y = 0;
    		while(y < albums.size())
    		{
				if(((String)albums.get(y)).equals(((Item)mItems.get(x)).getAlbum()))
				{
						found = true;
						break;
				}
    			y++;
    		}
    		
    		if(!found)
    		{
    			albums.add(mItems.get(x).getAlbum());
    		}
    	}
    	return albums;
    }
}