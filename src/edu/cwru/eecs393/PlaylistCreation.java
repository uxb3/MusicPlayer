package edu.cwru.eecs393;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlaylistCreation extends Activity implements OnClickListener, OnKeyListener {

	private static final String TAG = "PlaylistCreation";
	private PlaylistsAdapter playlists;
	private EditText playlistName;
	private Long mRowId;
	private Button submit;
	
	@Override
	public void onCreate(Bundle icicle) {
	
		super.onCreate(icicle);
		Log.i(TAG, "we are in");
		playlists = new PlaylistsAdapter(this);
		playlists.open();
		Log.i(TAG, "Opened the db..");
		setContentView(R.layout.create_playlist);
		Log.i(TAG, "layout setup just fine...");
		playlistName = (EditText) findViewById(R.id.editText1);
		playlistName.setOnKeyListener(this);
		submit = (Button) findViewById(R.id.button1);
		submit.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		
		String result = playlistName.getText().toString();
		if(result.length() > 0) {
			playlists.createPlaylist(playlistName.getText().toString());
			Log.i(TAG, "Successfully created playlist.");
			finish();
			startActivity(new Intent(PlaylistCreation.this, PlaylistsSelection.class));
		}
		else {
			
			Toast toast = Toast.makeText(this, "Enter a valid playlist name please.", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
	
	public void onBackPressed() {
	   
		startActivity(new Intent(PlaylistCreation.this, PlaylistsSelection.class));
	}
	
	// Need to implement this later
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		/*
		if(keyCode == KeyEvent.KEYCODE_ENTER) {
			
			onClick(v);
			return true;
		}*/
		
		return false;
			
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playlists != null) {
			playlists.close();
		}
	}
}
