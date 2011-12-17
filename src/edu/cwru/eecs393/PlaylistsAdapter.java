package edu.cwru.eecs393;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * This adapter can be used by activities to access the Playlists database.
 * The database consists of two tables:
 * Playlists (long _id AUTOINCREMENT, String name) & Items (long _id, foreign key (pid) references Playlists(_id), long mediaId)
 */
/*
 * The sdk has a tool that allows you to run sql commands etc and browse the database
 */
public class PlaylistsAdapter {

	private static final String TAG = "PlaylistsAdapter";
	private PlaylistsHelper helper;
	private SQLiteDatabase db;
	private Context context;
	
	//PLAYLISTS DATABASE FIELDS
	private static final String PLAYLISTS = "playlists";
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	
	//ITEMS DATABASE FIELDS
	private static final String ITEMS = "items";
	private static final String KEY_PID = "pid";
	private static final String KEY_SONGID = "songId";
	
	public PlaylistsAdapter(Context context) {
		this.context = context;
	}

	public PlaylistsAdapter open() throws SQLException {
		
		Log.i(TAG, "In open()");
		helper = new PlaylistsHelper(context);
		Log.i(TAG, "Successfully created PlaylistsHelper.");
		db = helper.getWritableDatabase();
		Log.i(TAG, "Success");
		return this;
	}

	public void close() {
		helper.close();
	}
	
	/*
	 * Playlist table methods
	 */
	public boolean createPlaylist(String name) {
		
		return (db.insert(PLAYLISTS, null, playlistContent(name)) > 0);
	}
	
	public boolean deletePlaylist(long id) {
		
		db.delete(ITEMS, id + "=" + KEY_PID, null);
		return (db.delete(PLAYLISTS, id + "=" + KEY_ID, null) > 0);
	}
	
	public boolean deleteAllPlaylists() {
		
		return (db.delete(PLAYLISTS, null, null) > 0);
	}
	
	public Cursor getPlaylistNames() throws SQLException {
		
		return db.query(PLAYLISTS, new String [] {KEY_NAME}, null, null, null, null, KEY_NAME);
	}
	
	public Cursor getPlaylists() throws SQLException {
		
		return db.query(PLAYLISTS, null, null, null, null, null, null);
	}
	
	private ContentValues playlistContent(String name) {
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		return values;
	}
	
	
	/*
	 * Item table methods
	 */
	
	public boolean addSong(long pid, long songid) {
		
		/*
		 * Get rid of this if statement when you implement exists array in PlaylistAdder
		 * pid + "=" + KEY_PID + " & " + songid + "=" + KEY_SONGID
		 */
		Log.i(TAG, "checking to see if the particular playlist contains this song");
		Cursor p = db.query(ITEMS, null, KEY_PID + "=" + pid, null, null, null, null);
		int index = p.getColumnIndex(KEY_SONGID);
		while(p.moveToNext()) {
			
			if(p.getLong(index) == songid)
				return false;
		}
		return (db.insert(ITEMS, null, songContent(pid, songid)) > 0);
	}
	
	public boolean deleteSong(long pid, long songid) {
		
		return (db.delete(ITEMS, pid + "=" + KEY_PID + " AND "  + songid + "=" + KEY_SONGID, null) > 0 );
	}
	
	public Cursor getSongs(long pid) {
		
		return db.query(ITEMS, new String [] {KEY_SONGID}, pid + "=" + KEY_PID, null, null, null, null);
	}
	
	private ContentValues songContent(long pid, long songid) {
		
		ContentValues values = new ContentValues();
		values.put(KEY_PID, pid);
		values.put(KEY_SONGID, songid);
		return values;
	}
}
