package edu.cwru.eecs393;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ViewPlaylist extends ListActivity {

	private static final String TAG = "ViewPlaylist";
	private PlaylistsAdapter playlists;
	private List<Item> list = new ArrayList<Item>();
	private int pos = 0;
	
	public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		playlists = new PlaylistsAdapter(this);
		playlists.open();
		
		Log.i(TAG, "database is open...");
		Cursor songs = playlists.getSongs(PlaylistState.pid);
		startManagingCursor(songs);
		Log.i(TAG, "query done");
		String [] names = new String[songs.getCount()];
		Log.i(TAG, "" +songs.getCount());
		int cnt = 0;
		int index = songs.getColumnIndex(PlaylistState.KEY_SONGID);
		while(songs.moveToNext()) {
			
			list.add(MusicRetriever.getItem(songs.getLong(index)));
			names[cnt] = list.get(cnt).getTitle();
			cnt++;
		}
		songs.close();
		Log.i(TAG, "generated the lists");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		for(cnt = 0; cnt < names.length; cnt++) {
			adapter.add(names[cnt]);
		}
		Log.i(TAG, "made the adapter");
		setListAdapter(adapter);
		registerForContextMenu(getListView());
		Log.i(TAG, "set the adapter");
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		
		Log.i(TAG, "Attemping to play the song..");
		PlayerState.clearNowPlaying();
		PlayerState.addNowPlaying(list);
		Log.i(TAG, "The position is: " + position + PlayerState.nowPlaying.size());
		PlayerState.currentSong = position;
		try {
			if (PlayerState.mp == null)
				PlayerState.createMediaPlayer(getBaseContext());
			else
			{
				PlayerState.mp.reset();
				PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(position).getURI());
				PlayerState.mp.prepare();
			}

			PlayerState.play = true;
			PlayerState.mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startActivity(new Intent(ViewPlaylist.this, Player.class));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
		    ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if(v == getListView())
		{
			pos = info.position;
			menu.setHeaderTitle("Actions");
			menu.add(Menu.NONE, 0, 0, "Delete song");
		}
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int songIndex = info.position;
		if(item.getItemId() == 0)
		{
			long id = list.get(pos).getId();
			if(playlists.deleteSong(PlaylistState.pid, id)) {
				Log.i(TAG, "success in deleting song");
				Toast toast = Toast.makeText(this, "Song deleted.", Toast.LENGTH_SHORT);
				toast.show();
				finish();
				startActivity(new Intent(ViewPlaylist.this, ViewPlaylist.class));		
			}
			else {
				
				Log.i(TAG, "The song id was: " + id);
				Toast toast = Toast.makeText(this, "Song was not deleted.", Toast.LENGTH_SHORT);
				toast.show();
			}
				
		}
		return true;
	}
	
	public void onBackPressed() {
		   
		finish();
		startActivity(new Intent(ViewPlaylist.this, PlaylistsSelection.class));
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playlists != null) {
			playlists.close();
		}
	}
}