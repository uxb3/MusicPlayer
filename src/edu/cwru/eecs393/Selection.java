package edu.cwru.eecs393;
import android.app.TabActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TabHost;


public class Selection extends TabActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.selection);
    	
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
    	// Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ArtistSelection.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("artists").setIndicator("Artists").setContent(intent);
        tabHost.addTab(spec);
    	
        //Do the same for the other tabs
        intent = new Intent().setClass(this, AlbumSelection.class);
        spec = tabHost.newTabSpec("albums").setIndicator("Albums").setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, SongSelection.class);
        spec = tabHost.newTabSpec("songs").setIndicator("Songs").setContent(intent);
        tabHost.addTab(spec);
        
        //if(PlayerState.mp == null)
        //	PlayerState.mp = MediaPlayer.create(this, null);
        
        tabHost.setCurrentTab(2);
    }
	
}
