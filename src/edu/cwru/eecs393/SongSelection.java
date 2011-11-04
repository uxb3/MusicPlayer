package edu.cwru.eecs393;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SongSelection extends ListActivity {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.);
		Cursor mCursor = PlayerState.music.getSongsCursor();
		startManagingCursor(mCursor);						//this will requery the MediaStore every time this activity is reloaded
		
		ListAdapter adapter = new SimpleCursorAdapter(
				this, 
				R.layout.list_item,
				mCursor,
				new String[] {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST}, 
				new int[] {R.id.textTitle, R.id.textArtist});
		
		setListAdapter(adapter);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		PlayerState.clearNowPlaying();
		PlayerState.addNowPlaying(PlayerState.music.mItems.get(position));
		PlayerState.currentSong = 0;
		//make call to start music player activity
		startActivity(new Intent(SongSelection.this,Player.class));
	}
}
