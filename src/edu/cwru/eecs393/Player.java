package edu.cwru.eecs393;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// Note you need to use BindService in order to do progress bar and update the activty look it up in Acitvty developer section
public class Player extends Activity implements OnClickListener, OnSeekBarChangeListener {
	
	static Button btnPlay;
	Button btnPause;
	Button btnPrev;
	Button btnNext;
	Button btnShuffle;
	Button btnRepeat;
	Button btnJump;
	static ImageView imgArt;
	static TextView txtQueue, txtStart, txtEnd;
	String queue = "";
	static MediaTime time;
	static SeekBar seek;
	static boolean playing;
	//Button btnStop;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        setContentView(R.layout.player);
        if(PlayerState.mp == null && PlayerState.nowPlaying.size() > 0)
        	PlayerState.createMediaPlayer(getBaseContext());
        
        seek = (SeekBar)this.findViewById(R.id.seekBar);
        seek.setOnSeekBarChangeListener(this);
        
        btnPlay = (Button)this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        
        //btnPause = (Button)this.findViewById(R.id.btnPause);
        //btnPause.setOnClickListener(this);
        
        //btnStop = (Button)this.findViewById(R.id.btnStop);
        //btnStop.setOnClickListener(this);
        
        btnPrev = (Button) this.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(this);
        
        btnNext = (Button) this.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        
        btnShuffle = (Button)this.findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(this);
        
        btnRepeat = (Button)this.findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener(this);
        
        btnJump = (Button)this.findViewById(R.id.btnJump);
        btnJump.setOnClickListener(this);
        
        imgArt = (ImageView) this.findViewById(R.id.imgArt);
        
        txtQueue = (TextView)this.findViewById(R.id.txtQueue);
        txtStart = (TextView)this.findViewById(R.id.txtTimeStart);
        txtEnd = (TextView)this.findViewById(R.id.txtTimeEnd);
        
        if(PlayerState.mp.isPlaying())
        {
        	playing = true;
        	btnPlay.setBackgroundResource(R.drawable.pause);
        	try {
				imgArt.setImageBitmap(MusicRetriever.getAlbumArt(PlayerState.nowPlaying.get(PlayerState.currentSong)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				imgArt.setImageResource(R.drawable.icon);
			}
        	PlayerState.changeSong();
        	//btnPlay.setEnabled(false);
        	//btnPause.setEnabled(true);
        	//btnStop.setEnabled(true);
        	
        }
        else if(PlayerState.nowPlaying.size() > 0)
        {
        	playing = false;
        	btnPlay.setBackgroundResource(R.drawable.play);
        	try {
				imgArt.setImageBitmap(MusicRetriever.getAlbumArt(PlayerState.nowPlaying.get(PlayerState.currentSong)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				imgArt.setImageResource(R.drawable.icon);
			}
        	//btnPlay.setEnabled(true);
        	//btnPause.setEnabled(false);
        	//btnStop.setEnabled(false);
        }
        else
        {
        	playing = true;
        	btnPlay.setBackgroundResource(R.drawable.pause);
        	PlayerState.changeSong();
        	//btnPlay.setEnabled(false);
        	//btnPause.setEnabled(false);
        	//btnStop.setEnabled(false);
        }
        
        if (PlayerState.repeat == 0)
        {
        	btnRepeat.setBackgroundResource(R.drawable.repeat);
        }
        else if (PlayerState.repeat == 1)
        {
        	btnRepeat.setBackgroundResource(R.drawable.repeat_all);
        }
        else if (PlayerState.repeat == 2)
        {
        	btnRepeat.setBackgroundResource(R.drawable.repeat_one);
        }
        updateQueueText();
    }

    public static void updateQueueText()
    {
    	time = new MediaTime(PlayerState.mp.getDuration());
    	String queue = "Queue:\n";
    	int x;
    	if(PlayerState.currentSong > 0)
    		x = PlayerState.currentSong-1;
    	else
    		x = 0;
        for (x = x; x < PlayerState.nowPlaying.size() && x < (PlayerState.currentSong + 2); x++)
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
        
        try {
			imgArt.setImageBitmap(MusicRetriever.getAlbumArt(PlayerState.nowPlaying.get(PlayerState.currentSong)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			imgArt.setImageResource(R.drawable.icon);
		}
    }
	public void onClick(View v) 
	{
		if (v.getId() == R.id.btnRepeat)
		{
			switch (PlayerState.repeat)
			{
				case 0:
					PlayerState.repeat = 1;
					btnRepeat.setBackgroundResource(R.drawable.repeat_all);
					break;
				case 1:
					PlayerState.repeat = 2;
					btnRepeat.setBackgroundResource(R.drawable.repeat_one);
					break;
				case 2:
					PlayerState.repeat = 0;
					btnRepeat.setBackgroundResource(R.drawable.repeat);
					break;
				default:
					break;
			}
		}
		else if (v.getId() == R.id.btnShuffle)
		{
			if (PlayerState.nowPlaying.size() > 1)
			{
				PlayerState.shuffle();
				updateQueueText();
			}
		}
		else if (v.getId() == R.id.btnJump)
		{
			if (PlayerState.nowPlaying.size() > 0)
			{
				try {
					int random = (int) (Math.random()*PlayerState.nowPlaying.size());
					PlayerState.currentSong = random;
					PlayerState.mp.reset();
					PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
					PlayerState.mp.prepare();
					PlayerState.mp.start();
					updateQueueText();
		        	playing = true;
		        	btnPlay.setBackgroundResource(R.drawable.pause);
		        	PlayerState.changeSong();
					//btnPlay.setEnabled(false);
					//btnPause.setEnabled(true);
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
		else if (v.getId() == R.id.btnPlay && playing == false)
		{
			if(PlayerState.nowPlaying.size()>0)
			{
				PlayerState.play = true;
	        	playing = true;
	        	btnPlay.setBackgroundResource(R.drawable.pause);
	        	PlayerState.changeSong();
				//btnPause.setEnabled(true);
				//btnPlay.setEnabled(false);
				//btnStop.setEnabled(true);
				PlayerState.mp.start();
			}
		}
		else if (v.getId() == R.id.btnPlay && playing == true)
		{
			if(PlayerState.mp.isPlaying())
			{
				PlayerState.mp.pause();
	        	playing = false;
	        	btnPlay.setBackgroundResource(R.drawable.play);
				//btnPlay.setEnabled(true);
				//btnPause.setEnabled(false);
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
        	playing = false;
        	btnPlay.setBackgroundResource(R.drawable.play);
			//btnPlay.setEnabled(true);
			//btnPause.setEnabled(false);
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
		        	playing = true;
		        	btnPlay.setBackgroundResource(R.drawable.pause);
		        	PlayerState.changeSong();
					//btnPlay.setEnabled(false);
					//btnPause.setEnabled(true);
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
			else if(PlayerState.currentSong == PlayerState.nowPlaying.size()-1 && PlayerState.repeat == 1)
			{
				try {
					PlayerState.currentSong = 0;
					PlayerState.mp.reset();
					PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
					PlayerState.mp.prepare();
					PlayerState.mp.start();
					updateQueueText();
		        	playing = true;
		        	btnPlay.setBackgroundResource(R.drawable.pause);
		        	PlayerState.changeSong();
					//btnPlay.setEnabled(false);
					//btnPause.setEnabled(true);
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
        	playing = false;
        	btnPlay.setBackgroundResource(R.drawable.play);
			//btnPlay.setEnabled(true);
			//btnPause.setEnabled(false);
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
					PlayerState.changeSong();
		        	playing = true;
		        	btnPlay.setBackgroundResource(R.drawable.pause);
					//btnPlay.setEnabled(false);
					//btnPause.setEnabled(true);
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
			else if (PlayerState.currentSong == 0 && PlayerState.repeat == 1)
			{
				try {
					PlayerState.currentSong = PlayerState.nowPlaying.size()-1;
					PlayerState.mp.reset();
					PlayerState.mp.setDataSource(getBaseContext(), PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
					PlayerState.mp.prepare();
					PlayerState.mp.start();
					updateQueueText();
					PlayerState.changeSong();
		        	playing = true;
		        	btnPlay.setBackgroundResource(R.drawable.pause);
					//btnPlay.setEnabled(false);
					//btnPause.setEnabled(true);
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

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
            PlayerState.mp.seekTo(progress);
            seek.setProgress(progress);
        }
		else if(progress == 0)
			return;
		MediaTime current = new MediaTime(PlayerState.mp.getCurrentPosition());
		MediaTime remaining = new MediaTime(PlayerState.mp.getDuration()-PlayerState.mp.getCurrentPosition());
		txtStart.setText(current.getTime());
		txtEnd.setText(remaining.getTime());
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}