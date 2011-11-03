package edu.cwru.eecs393;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Player extends Activity implements OnClickListener {
	
	MediaPlayer mp;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        if(Playlists.currentSong >= 0)
        	mp = MediaPlayer.create(getBaseContext(), Playlists.nowPlaying.get(Playlists.currentSong).getURI());
        /*try {
            mp.setDataSource(soundUrl);
            mp.prepare();
        }
        catch (IOException e) {}
        catch (IllegalArgumentException e) {}
        catch (IllegalStateException e) {}*/
        
        Button btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        
        Button btnPause = (Button)this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        
        Button btnStop = (Button)this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
    }

	public void onClick(View v) 
	{
		if (v.getId() == R.id.btnPlay)
		{
			mp.start();
		}
		else if (v.getId() == R.id.btnPause)
		{
			mp.pause();
		}
		else if (v.getId() == R.id.btnStop)
		{
			mp.stop();
		}
	}
    


    
}