package edu.cwru.eecs393;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PlaylistsAdapter {

	private PlaylistsHelper helper;
	private SQLiteDatabase db;
	private Context context;
	
	//PLAYLISTS DATABASE FIELDS
	private static final String PLAYLISTS = "playlists";
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	
	//ITEMS DATABASE FIELDS
	private static final String ITEMS = "items";
	
	public PlaylistsAdapter(Context context) {
		this.context = context;
	}

	public PlaylistsAdapter open() throws SQLException {
		helper = new PlaylistsHelper(context);
		db = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		helper.close();
	}
	
	public boolean createPlaylist(String name) {
		
		return (db.insert(PLAYLISTS, null, playlistContent(name)) > 0);
	}
	
	public Cursor getPlaylists() throws SQLException {
		
		return db.query(PLAYLISTS, new String [] {KEY_NAME}, null, null, null, null, null);
	}
	
	private ContentValues playlistContent(String name) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		return values;
	}
}
