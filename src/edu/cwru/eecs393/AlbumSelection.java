package edu.cwru.eecs393;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class AlbumSelection extends ExpandableListActivity {

	ArrayList<String> albums;
	ArrayList<ArrayList<MusicRetriever.Item>> songs;
	
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
		Playlists.clearNowPlaying();
		Playlists.addNowPlaying(songs.get(groupPosition).get(childPosition));
		Playlists.currentSong = 0;
		//make call to start music player activity
		startActivity(new Intent(AlbumSelection.this,Player.class));
		return true;
	}
}
