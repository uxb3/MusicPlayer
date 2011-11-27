package edu.cwru.eecs393;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

public class Playlists  /*implements AsyncRestRequestListener<Methods, JSONObject>*/ {
	private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "Playlists";
    private static SQLiteDatabase database = null;
    
    //private SQLiteStatement insertStmt;
    private final Context context;	 
    private PlaylistsHelper helper;
    
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
	
	public boolean createPlaylist(String playlist) {
		
		try {
			
			database.execSQL("CREATE TABLE " + playlist + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
     		" artist TEXT);");
			return true;
		}
		catch (SQLException e) {
			
			return false;
		}
		
	}
	
	/*
	 * Creates a Playlist with a initial song added to it.
	 */
	public boolean createPlaylistWSong(String playlist, String uri, String title, String album, String artist ) {
		
		try {
			database.execSQL("CREATE TABLE " + playlist + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
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
		database.insert(playlist, null, c);
		return true;
	}
	
	public boolean addSong(String playlist, String uri, String title, String album, String artist) {
		
		ContentValues c = new ContentValues();
		c.put("uri", uri);
		c.put("title", title);
		c.put("album", album);
		c.put("artist", artist);
		return (database.insert(playlist, null, c) > 0);
	}
	
	public boolean deleteSong(String playlist, long id) {
		
		return (database.delete(playlist, "_id = " + id, null) > 0);
	}
	
	public void deletePlaylist(String playlist) {
		
		database.execSQL("DROP TABLE "+ playlist + ";");
	}
	
	public Cursor getPlaylists() {
		
		return database.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' ORDER BY name;", null); 
	}
	
	public Cursor getSongs(String playlist) {
		
		return database.rawQuery("SELECT * FROM " + playlist + ";", null);
	}
}