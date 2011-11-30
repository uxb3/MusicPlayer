package edu.cwru.eecs393;
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlaylistsSelection extends ListActivity {
	
	private PlaylistsAdapter playlists;
	private static final String TAG = "PlaylistsSelection";

		//PLAYLISTS DATABASE FIELDS
		private static final String PLAYLISTS = "playlists";
		private static final String KEY_ID = "_id";
		private static final String KEY_NAME = "name";
		
		//ITEMS DATABASE FIELDS
		private static final String ITEMS = "items";
		
		public void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);
		playlists = new PlaylistsAdapter(this);
		playlists.open();
		Cursor cursor = playlists.getPlaylists();
		ArrayList <String> pnames = new ArrayList<String>();
		int index = cursor.getColumnIndex(KEY_NAME);
		Log.i(TAG, "index = " + index);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		
		if(cursor != null) {
			
			while(cursor.moveToNext()) {
			
				adapter.add(cursor.getString(index));
			}
		}
		else {
			
			Log.i(TAG, "No playlists were found.");
		}
		
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		/*String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();*/
		Log.i(TAG, "Click");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playlists != null) {
			playlists.close();
		}
	}
}