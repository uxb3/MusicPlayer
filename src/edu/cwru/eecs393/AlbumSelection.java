package edu.cwru.eecs393;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class AlbumSelection extends ExpandableListActivity {

	ArrayList<String> albums;
	ArrayList<ArrayList<Item>> songs;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		albums = MusicRetriever.getAlbums();
		songs = MusicRetriever.getGroupedSongs("album"); 
		ExpandableListAdapter adapter = new ExpandableListAdapter(
				this, 
				albums,
				songs, 
				"album");
		
		setListAdapter(adapter);
	}
	
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		PlayerState.clearNowPlaying();
		PlayerState.addNowPlaying(songs.get(groupPosition).get(childPosition));
		PlayerState.currentSong = 0;
		try {
			if(PlayerState.mp == null)
				PlayerState.createMediaPlayer(getBaseContext());
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
		//make call to start music player activity
		startActivity(new Intent(AlbumSelection.this,Player.class));
		return true;
	}
}
