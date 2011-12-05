package edu.cwru.eecs393;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistsHelper extends SQLiteOpenHelper {
    
	private static final String DATABASE_NAME = "harmoniousplaylists";

	private static final int DATABASE_VERSION = 2;

	private static final String TAG = "PlaylistsHelper";

	public PlaylistsHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(TAG, "In constructor.");
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		
		Log.i(TAG, "In onCreate()");
		Playlists.onCreate(database);
		Log.i(TAG, "success");
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.i(TAG, "In onUpgrade...");
		Playlists.onUpgrade(database, oldVersion, newVersion);
		Log.i(TAG, "Success");
	}
}