package edu.cwru.eecs393;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// Note you need to use BindService in order to do progress bar and update the activty look it up in Acitvty developer section
public class Player extends Activity implements OnClickListener {
	
	Button btnPlay, btnPause, btnPrev, btnNext;
	TextView txtQueue;
	String queue = "";
	MediaTime time;
	//Button btnStop;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        if(PlayerState.mp == null && PlayerState.nowPlaying.size() > 0)
        	PlayerState.createMediaPlayer(getBaseContext());
        
        btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        
        btnPause = (Button)this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        
        //btnStop = (Button)this.findViewById(R.id.btnStop);
        //btnStop.setOnClickListener(this);
        
        btnPrev = (Button) this.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        
        btnNext = (Button) this.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        
        txtQueue = (TextView)this.findViewById(R.id.txtQueue);
        
        if(PlayerState.mp.isPlaying())
        {
        	btnPlay.setEnabled(false);
        	btnPause.setEnabled(true);
        	//btnStop.setEnabled(true);
        	
        }
        else if(PlayerState.nowPlaying.size() > 0)
        {
        	btnPlay.setEnabled(true);
        	btnPause.setEnabled(false);
        	//btnStop.setEnabled(false);
        }
        else
        {
        	btnPlay.setEnabled(false);
        	btnPause.setEnabled(false);
        	//btnStop.setEnabled(false);
        }
        updateQueueText();
    }

    private void updateQueueText()
    {
    	time = new MediaTime(PlayerState.mp.getDuration());
    	String queue = "Queue:\n";
    	int x;
    	if(PlayerState.currentSong > 0)
    		x = PlayerState.currentSong-1;
    	else
    		x = 0;
        for (x = x; x < PlayerState.nowPlaying.size() && x < (PlayerState.currentSong + 10); x++)
        {
        	if(x == PlayerState.currentSong)
        		queue += ">>";
        	queue += (x+1) + ".  " + PlayerState.nowPlaying.get(x).title + "\n";
        }
        queue += "\nNow Playing\n";
        if(PlayerState.currentSong != -1)
        {
        	queue += " " +PlayerState.nowPlaying.get(PlayerState.currentSong).title;
        	queue += "\n " + PlayerState.nowPlaying.get(PlayerState.currentSong).artist;
        	queue += "\n " + PlayerState.nowPlaying.get(PlayerState.currentSong).album;
        }
        
        String currTime =  "\n" + time.getTime();
        txtQueue.setText(queue + currTime);
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
				//btnStop.setEnabled(true);
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
				//btnStop.setEnabled(true);
			}
		}
		/*
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
		*/
		else if (v.getId() == R.id.btnNext)
		{
			PlayerState.mp.pause();
			btnPlay.setEnabled(true);
			btnPause.setEnabled(false);
			//btnStop.setEnabled(true);
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
			//btnStop.setEnabled(true);
			/*
			 * if we are after the 5 second mark we want to go to the beginning of this song
			 * if we are within 5 seconds of this song then we go to the previous song
			 */
			if(PlayerState.mp.getCurrentPosition() > 5000) {
				
				PlayerState.mp.seekTo(0);
				PlayerState.mp.start();
			}
			else if(PlayerState.currentSong > 0)
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