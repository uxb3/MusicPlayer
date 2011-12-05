package edu.cwru.eecs393;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SongSelection extends ListActivity {

	private static final String TAG = "ListActivity";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.);
		Cursor mCursor = PlayerState.music.getSongsCursor();
		
		// this method is deprecated, so this piece of code needs to be reimplemented
		startManagingCursor(mCursor);						//this will requery the MediaStore every time this activity is reloaded
		
		ListAdapter adapter = new SimpleCursorAdapter(
				this, 
				R.layout.list_item,
				mCursor,
				new String[] {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST}, 
				new int[] {R.id.textTitle, R.id.textArtist});
		
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		PlayerState.clearNowPlaying();
		PlayerState.addNowPlaying(MusicRetriever.mItems);
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
		
		//make call to start music player activity
		startActivity(new Intent(SongSelection.this,Player.class));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
		    ContextMenuInfo menuInfo) {
		if(v == getListView())
		{
			menu.setHeaderTitle("Actions");
			menu.add(Menu.NONE,0,0, "Add to Queue");
			menu.add(Menu.NONE,1,1, "Add to Playlist");
		}
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		if(item.getItemId() == 0)
		{
			int songId = info.position;
			PlayerState.addNowPlaying(MusicRetriever.mItems.get(songId));
			
			Toast toast = Toast.makeText(this, "Song added to queue.", Toast.LENGTH_SHORT);
			toast.show();
			
			if(PlayerState.nowPlaying.size() == 1)
			{
				PlayerState.currentSong = 0;
				try {
					if (PlayerState.mp == null)
						PlayerState.mp = MediaPlayer.create(getBaseContext(), PlayerState.nowPlaying.get(0).getURI());
					else
					{
						PlayerState.mp.reset();
						PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(0).getURI());	
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
			}
			startActivity(new Intent(SongSelection.this,Player.class));
		}
		else if(item.getItemId() == 1) {
			
			startActivity(new Intent(SongSelection.this, PlaylistsSelection.class));
		}
		return true;
	}
}
