package edu.cwru.eecs393;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Player extends Activity implements OnClickListener {
	
	Button btnPlay, btnPause, btnStop;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        if(PlayerState.mp == null)
        	PlayerState.mp = MediaPlayer.create(getBaseContext(), null);
        /*try {
            mp.setDataSource(soundUrl);
            mp.prepare();
        }
        catch (IOException e) {}
        catch (IllegalArgumentException e) {}
        catch (IllegalStateException e) {}*/
        
        btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        
        btnPause = (Button)this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        
        btnStop = (Button)this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        
        if(PlayerState.mp.isPlaying())
        {
        	btnPlay.setEnabled(false);
        	btnPause.setEnabled(true);
        	btnStop.setEnabled(true);
        }
        else if(PlayerState.nowPlaying.size() > 0)
        {
        	btnPlay.setEnabled(true);
        	btnPause.setEnabled(false);
        	btnStop.setEnabled(false);
        }
        else
        {
        	btnPlay.setEnabled(false);
        	btnPause.setEnabled(false);
        	btnStop.setEnabled(false);
        }
    }

	public void onClick(View v) 
	{
		if (v.getId() == R.id.btnPlay)
		{
			if(PlayerState.nowPlaying.size()>0)
			{
				PlayerState.play = true;
				btnPause.setEnabled(true);
				btnPlay.setEnabled(false);
				btnStop.setEnabled(true);
				PlayerState.mp.start();
			}
		}
		else if (v.getId() == R.id.btnPause)
		{
			if(PlayerState.mp.isPlaying())
			{
				PlayerState.mp.pause();
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
				btnStop.setEnabled(true);
			}
		}
		else if (v.getId() == R.id.btnStop)
		{
			PlayerState.play = false;
			PlayerState.mp.stop();
			PlayerState.nowPlaying.clear();
			btnPlay.setEnabled(false);
			btnPause.setEnabled(false);
			btnStop.setEnabled(false);	
		}
	}
}