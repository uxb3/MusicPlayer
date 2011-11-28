package edu.cwru.eecs393;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/*
 * Note a song in this database is defined by 5 elements: _id, uri, title, album, and artist.
 */
public class Playlists  /*implements AsyncRestRequestListener<Methods, JSONObject>*/ {
	private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "Playlists";
    private static SQLiteDatabase database = null;
    
    //private SQLiteStatement insertStmt;
    private final Context context;	 
    private PlaylistsHelper helper;
    
    /*
     * Helps initialize and create, and update the database.
     */
    private static class PlaylistsHelper extends SQLiteOpenHelper {
    
    	PlaylistsHelper(Context context) {
            
    		super(context, DB_NAME, null, DATABASE_VERSION);
        }	
    	@Override
        public void onCreate(SQLiteDatabase db) {
        	database = db;
        }
    	
    	@Override
    	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    		// TODO Auto-generated method stub
    		
    	}
    	
    	public void loadData() 
    	{
    		if (database == null)
    			database = this.getWritableDatabase();	
    	}
    }
    
    public Playlists(Context c) {
    	
    	this.context = c;
    }
    
    public Playlists open() throws SQLException {
    	
    	helper = new PlaylistsHelper(context);
    	database = helper.getWritableDatabase();
    	return this;
    }
    
    public void close() {
    	
    	helper.close();
    }
	
	/**
	 * Creates a given playlist.
	 * @param playlistName
	 * @return whether or not the playlist was created.
	 */
	public boolean createPlaylist(String playlistName) {
		
		try {
			
			database.execSQL("CREATE TABLE " + playlistName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
     		" artist TEXT);");
			return true;
		}
		catch (SQLException e) {
			
			return false;
		}
		
	}
	
	/**
	 * Creates a given playlist and adds to it a given song.
	 * @param playlistName
	 * @param uri
	 * @param title
	 * @param album
	 * @param artist
	 * @return whether or not creating the playlist and adding the song to it was successful.
	 */
	public boolean createPlaylistWSong(String playlistName, String uri, String title, String album, String artist ) {
		
		try {
			database.execSQL("CREATE TABLE " + playlistName + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
    			" artist TEXT);");
		}
		catch (Exception e) {
			
			return false;
		}
		ContentValues c = new ContentValues();
		c.put("uri", uri);
		c.put("title", title);
		c.put("album", album);
		c.put("artist", artist);
		return (database.insert(playlistName, null, c) > 0);
	}
	
	/**
	 * Adds a given song to a given playlist.
	 * @param playlistName
	 * @param uri
	 * @param title
	 * @param album
	 * @param artist
	 * @return whether or not adding the song was successful.
	 */
	public boolean addSong(String playlistName, String uri, String title, String album, String artist) {
		
		ContentValues c = new ContentValues();
		c.put("uri", uri);
		c.put("title", title);
		c.put("album", album);
		c.put("artist", artist);
		return (database.insert(playlistName, null, c) > 0);
	}
	
	/**
	 * Deletes a given song from a given playlist.
	 * @param playlistName
	 * @param id
	 * @return whether the deletion was successful.
	 */
	public boolean deleteSong(String playlistName, long id) {
		
		return (database.delete(playlistName, "_id = " + id, null) > 0);
	}
	
	/**
	 * Deletes a given playlist.
	 * @param playlistName
	 */
	public void deletePlaylist(String playlistName) {
		
		database.execSQL("DROP TABLE "+ playlistName + ";");
	}
	
	/**
	 * Gives you all of the playlists currently in the database.
	 * @return Cursor
	 */
	public Cursor getPlaylists() {
		
		return database.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' ORDER BY name;", null); 
	}
	
	/**
	 *Gives you all of the songs in a given playlist.
	 * @param playlistName
	 * @return Cursor
	 */
	public Cursor getSongs(String playlistName) {
		
		return database.rawQuery("SELECT * FROM " + playlistName + ";", null);
	}
}