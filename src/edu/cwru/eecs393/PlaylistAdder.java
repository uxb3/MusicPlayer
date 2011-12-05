package edu.cwru.eecs393;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import java.util.ArrayList;

public class PlaylistAdder extends Activity implements OnItemClickListener, OnClickListener {

	private static final String TAG = "PlaylistAdder";
	private PlaylistsAdapter playlists;
	private long [] songIds;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		playlists = new PlaylistsAdapter(this);
		playlists.open();
		setContentView(R.layout.add_songs);
		ListView songs = (ListView) findViewById(R.id.list);
		Button submit = (Button) findViewById(R.id.testbutton);
		submit.setOnClickListener(this);
		Cursor cursor = PlayerState.music.getSongsCursor();
		//Cursor psongs = playlists.getSongs(PlaylistState.pid);
		//ArrayList<Long> sps = new ArrayList<Long>();
		//int index = psongs.getColumnIndex(PlaylistState.KEY_SONGID);
		//while(cursor.moveToNext())
			//sps.add(cursor.getLong(index));
		String [] names = null;
		String [] artists = null;
		boolean [] exists = null;
		if(cursor != null) {

			names = new String [cursor.getCount()];
			artists = new String [cursor.getCount()];
			songIds = new long [cursor.getCount()];
			exists = new  boolean [cursor.getCount()];

			int in = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
			int ia = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
			int is = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
			int cnt = 0;
			while(cursor.moveToNext()) {

				names[cnt] = cursor.getString(in);
				artists[cnt] = cursor.getString(ia);
				songIds[cnt] = cursor.getLong(is);
				// Add Code for checking if song exists
				/*for(int i = 0; i < sps.size(); i++) {
					
					if(songIds[cnt] == sps.get(i)) {
						
						exists[cnt] = true;
						sps.remove(i);
					}
				}*/
				cnt++;
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		Log.i(TAG, "Attempting to add songs");
		if(names != null) {
			for(int cnt = 0; cnt < names.length; cnt++) {

				Log.i(TAG, names[cnt]);
				adapter.add(names[cnt]);
				/*if(exists[cnt] == true) {
					
					adapter.getView(cnt, null, null).setBackgroundColor(Color.GRAY);
					adapter.getItemViewType(cnt);
				}*/
				//add code to change color
			}
		}
		Log.i(TAG, "Songs added..");
		songs.setAdapter(adapter);
		songs.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> l, View v, int position, long id) {

		long songid = songIds[position];
		if(playlists.addSong(PlaylistState.pid, songid)) {
			Toast toast = Toast.makeText(this, "Song added", Toast.LENGTH_SHORT);
			toast.show();
		}
		else {
			Toast toast = Toast.makeText(this, "Song is already in the playlist.", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playlists != null) {
			playlists.close();
		}
	}

	public void onClick(View v) {
		
		playlists.close();
		finish();
		startActivity(new Intent(PlaylistAdder.this, PlaylistsSelection.class));
	}
}