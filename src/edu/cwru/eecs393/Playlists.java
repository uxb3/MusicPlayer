package edu.cwru.eecs393;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

public class Playlists extends SQLiteOpenHelper /*implements AsyncRestRequestListener<Methods, JSONObject>*/ {
	private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "Playlists";
/*    private static final String PLAYLISTS_TABLE =
                "CREATE TABLE " + table_name + " (_id TEXT PRIMARY KEY, songPath TEXT);";*/
    private static SQLiteDatabase database = null;
    private static Context context;
    
    private SQLiteStatement insertStmt;
    
    Playlists(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	database = db;
    }
    
    public void onCreate(SQLiteDatabase db, String playlist) {
    	
    	database = db;
    	database.execSQL("CREATE TABLE " + playlist + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
    			" artist TEXT);");
    }
    
    public void onCreate(SQLiteDatabase db, String playlist, String id, String uri, String title, String album, String artist) {
    	
    	database = db;
    	database.execSQL("CREATE TABLE " + playlist + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, title TEXT, album TEXT," +
    			" artist TEXT);");
    	
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
	
	public boolean createPlaylist(String playlist) {
		
		try {
			
			database.execSQL("CREATE TABLE " + playlist + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, uri TEXT, name TEXT, album TEXT," +
     		" artist TEXT);");
			return true;
		}
		catch (Exception e) {
			
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
	
	public void addSong(String playlist, String uri, String title, String album, String artist) {
		
		ContentValues c = new ContentValues();
		c.put("uri", uri);
		c.put("title", title);
		c.put("album", album);
		c.put("artist", artist);
		database.insert(playlist, null, c);
	}
	
	public void deleteSong(String playlist, long id) {
		
		database.delete(playlist, "_id = " + id, null);
	}
	
	public void deletePlaylist(String playlist) {
		
		database.execSQL("DROP TABLE "+ playlist + ";");
	}
}