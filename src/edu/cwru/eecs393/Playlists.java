package edu.cwru.eecs393;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Need to a write a method that automatically checks to see if any files referenced by the database
 * have been modified at certain time periods.
 */

public class Playlists   {
	
		private static final String TAG = "Playlists";
		private static final String DATABASE_CREATE_PLAYLISTS = "create table playlists "
				+ "(_id integer primary key autoincrement, "
				+ "name text not null);";
		private static final String DATABASE_CREATE_ITEMS = "create table items "
				+ "(_id integer primary key autoincrement, "
				+ "songId integer, pid integer, foreign key(pid) references playlists(_id));";

		public static void onCreate(SQLiteDatabase database) {
			Log.w(TAG, "attempting to create database table...");
			database.execSQL(DATABASE_CREATE_PLAYLISTS);
			database.execSQL(DATABASE_CREATE_ITEMS);
			Log.w(TAG, "Done with that...");
		}

		public static void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(Playlists.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS playlists");
			database.execSQL("DROP TABLE IF EXISTS items");
			onCreate(database);
		}
}