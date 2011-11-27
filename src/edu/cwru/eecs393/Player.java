package edu.cwru.eecs393;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Player extends Activity implements OnClickListener {
	
	Button btnPlay, btnPause, btnStop, btnPrev, btnNext;
	TextView txtQueue;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        if(PlayerState.mp == null && PlayerState.nowPlaying.size() > 0)
        	PlayerState.createMediaPlayer(getBaseContext());
        
        txtQueue = (TextView)this.findViewById(R.id.txtQueue);
        
        btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        
        btnPause = (Button)this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        
        btnStop = (Button)this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
        
        btnPrev = (Button) this.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        
        btnNext = (Button) this.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        
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
        
        updateQueueText();
    }

    private void updateQueueText()
    {
    	String queue = "Queue:\n";
        for (int x = 0; x < PlayerState.nowPlaying.size(); x++)
        {
        	queue += (x+1) + ".  " + PlayerState.nowPlaying.get(x).title + "\n";
        }
        queue += "\nNow Playing:\n";
        if(PlayerState.currentSong != -1)
        {
        	queue += PlayerState.nowPlaying.get(PlayerState.currentSong).title;
        	queue += "\n " + PlayerState.nowPlaying.get(PlayerState.currentSong).getURI();//.getPath();
        }
        txtQueue.setText(queue);
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
			PlayerState.currentSong = -1;
			updateQueueText();
			btnPlay.setEnabled(false);
			btnPause.setEnabled(false);
			btnStop.setEnabled(false);	
		}
		else if (v.getId() == R.id.btnNext)
		{
			PlayerState.mp.pause();
			btnPlay.setEnabled(true);
			btnPause.setEnabled(false);
			btnStop.setEnabled(true);
			if(PlayerState.currentSong < PlayerState.nowPlaying.size()-1)
			{
				
				try {
					PlayerState.currentSong++;
					PlayerState.mp.reset();
					PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
					PlayerState.mp.prepare();
					PlayerState.mp.start();
					updateQueueText();
					btnPlay.setEnabled(false);
					btnPause.setEnabled(true);
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
		else if (v.getId() == R.id.btnPrev)
		{
			PlayerState.mp.pause();
			btnPlay.setEnabled(true);
			btnPause.setEnabled(false);
			btnStop.setEnabled(true);
			if(PlayerState.currentSong > 0)
			{
				
				try {
					PlayerState.currentSong--;
					PlayerState.mp.reset();
					PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
					PlayerState.mp.prepare();
					PlayerState.mp.start();
					updateQueueText();
					btnPlay.setEnabled(false);
					btnPause.setEnabled(true);
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
	}
}