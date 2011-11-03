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
        
        Button button2 = (Button)this.findViewById(R.id.btnPlay);
        button2.setOnClickListener(this);
        
        Button button1 = (Button)this.findViewById(R.id.btnPause);
        button1.setOnClickListener(this);
        
        Button button3 = (Button)this.findViewById(R.id.btnStop);
        button3.setOnClickListener(this);
    }

	public void onClick(View v) 
	{
		if (v == findViewById(R.id.btnPlay))
		{
			mp.start();
		}
		else if (v == findViewById(R.id.btnPause))
		{
			mp.pause();
		}
		else if (v == findViewById(R.id.btnStop))
		{
			mp.stop();
		}
	}
    


    
}