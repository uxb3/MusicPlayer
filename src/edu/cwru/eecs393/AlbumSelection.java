package edu.cwru.eecs393;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class AlbumSelection extends ExpandableListActivity {

	ArrayList<String> albums;
	ArrayList<ArrayList<Item>> songs;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		albums = MusicRetriever.getAlbums();
		Collections.sort(albums);
		songs = MusicRetriever.getGroupedSongs("album"); 
		Collections.sort(songs,new Item.ItemAlbumComparator());
		for (int x = 0; x < songs.size(); x++)
		{
			Collections.sort(songs.get(x), new Item.ItemComparator());
		}
		ExpandableListAdapter adapter = new ExpandableListAdapter(
				this, 
				albums,
				songs, 
				"album");
		
		setListAdapter(adapter);
		registerForContextMenu(getExpandableListView());
	}
	
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		PlayerState.clearNowPlaying();
		PlayerState.addNowPlaying(songs.get(groupPosition));
		PlayerState.currentSong = 0;
		try {
			if(PlayerState.mp == null)
				PlayerState.createMediaPlayer(getBaseContext());
			else
			{
				PlayerState.mp.reset();
				PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(childPosition).getURI());	
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
		startActivity(new Intent(AlbumSelection.this,Player.class));
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
		    ContextMenuInfo menuInfo) {
		if(v == getExpandableListView())
		{
			//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("Actions");
			menu.add(Menu.NONE,0,0, "Add to Queue");
		}
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
		if(item.getItemId() == 0)
		{
			boolean emptyList;
			if (PlayerState.nowPlaying.size() == 0)
			{
				emptyList = true;
			}
			else
			{
				emptyList = false;
			}
			int songId = (int) ExpandableListView.getPackedPositionChild(info.packedPosition);
			int groupId = (int) ExpandableListView.getPackedPositionGroup(info.packedPosition);
			if (songId == -1)																//add whole album if album was selected
			{
				for (int x = 0; x < songs.get(groupId).size(); x++)
				{
					PlayerState.addNowPlaying(songs.get(groupId).get(x));
				}
			}
			else 																			//add song if individual song was selected
			{
				PlayerState.addNowPlaying(songs.get(groupId).get(songId));
			}
			
			Toast toast = Toast.makeText(this, "Song added to queue.", Toast.LENGTH_SHORT);
			toast.show();
			
			if(emptyList)
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
		}
		startActivity(new Intent(AlbumSelection.this,Player.class));
		return true;
	}
}
