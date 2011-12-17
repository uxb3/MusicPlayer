package edu.cwru.eecs393;
import java.sql.SQLException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlaylistsSelection extends ListActivity {

	private PlaylistsAdapter playlists;
	private static final String TAG = "PlaylistsSelection";
	private String [] pnames = null;
	private long [] pids = null;

	//PLAYLISTS DATABASE FIELDS
	private static final String PLAYLISTS = "playlists";
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";

	//ITEMS DATABASE FIELDS
	private static final String ITEMS = "items";
	private static final String KEY_PID = "pid";
	private static final String Key_SONGID = "songId";

	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		Log.i(TAG, "About to start opening database.");
		playlists = new PlaylistsAdapter(this);
		Log.i(TAG, "Attempted to set up database connection.");
		playlists.open();
		Log.i(TAG, "Successfully opened the database...");
		Cursor cursor = playlists.getPlaylists();
		pnames = new String [cursor.getCount()+1];
		pids = new long[cursor.getCount()+1];
		pnames[0] = "Create new playlist...";
		pids[0] = 0;
		if(cursor != null) {

			int cnt = 1;
			int index = cursor.getColumnIndex(KEY_NAME);
			int index2 = cursor.getColumnIndex(KEY_ID);
			while(cursor.moveToNext()) {

				pnames[cnt] = cursor.getString(index);
				pids[cnt] = cursor.getLong(index2);
				cnt++;
			}
			Log.i(TAG, "Generated " + cursor.getCount() + " playlists.");
			cursor.close();
		}
		else
			Log.i(TAG, "No playlists were found.");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, pnames);
		
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	public void onBackPressed() {
	   
		if(PlaylistState.addSongMode)
			PlaylistState.addSongMode = false;
		startActivity(new Intent(PlaylistsSelection.this, Selection.class));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		if(position == 0) {
			
			finish();
			startActivity(new Intent(PlaylistsSelection.this, PlaylistCreation.class));
		}
		/* this means that we don't want to view the playlist, but add the nowplaying song to it */
		else if(PlaylistState.addSongMode) {
			
			PlaylistState.addSongMode = false;
			Item currentSong = PlaylistState.plusSong;
			if(playlists.addSong(pids[position], currentSong.getId())) {
				
				Toast toast = Toast.makeText(this, currentSong.title + " was added to playlist.", Toast.LENGTH_SHORT);
				toast.show();
			}
			else {
				
				Toast toast = Toast.makeText(this, currentSong.title + " is already in playlist.", Toast.LENGTH_SHORT);
				toast.show();
			}
			finish();
			
		}
		else {
			//Check to make sure the playlist has songs
			Cursor cur = playlists.getSongs(pids[position]);
			if (cur.getCount() == 0)
			{
				Toast toast = Toast.makeText(this, "Playlist is empty.", 3);
				toast.show();
			}
			else
			{
				PlaylistState.pid = pids[position];
				startActivity(new Intent(PlaylistsSelection.this, ViewPlaylist.class));
			}
		}
		
		Log.i(TAG, "Click");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
		    ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if(v == getListView() && info.position != 0)
		{
			menu.setHeaderTitle("Actions");
			menu.add(Menu.NONE, 0, 0, "Delete playlist");
			menu.add(Menu.NONE, 1, 1, "Add songs");
		}
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int playlistIndex = info.position;
		if(item.getItemId() == 0)
		{
			if(playlists.deletePlaylist(pids[playlistIndex]))
				Log.i(TAG, "Deleted the playlist.");
			else
				Log.i(TAG, "Failed to delete playlist.");
			playlists.close();
			finish();
			startActivity(new Intent(PlaylistsSelection.this, PlaylistsSelection.class));
			
		}
		else if(item.getItemId() == 1) {
			
			playlists.close();
			PlaylistState.pid = pids[playlistIndex];
			finish();
			startActivity(new Intent(PlaylistsSelection.this, PlaylistAdder.class));
		}
		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playlists != null) {
			playlists.close();
		}
	}
}