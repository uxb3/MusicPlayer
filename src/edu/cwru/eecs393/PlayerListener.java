package edu.cwru.eecs393;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class PlayerListener implements OnCompletionListener {

	public PlayerListener()
	{
		
	}
	
	public void onCompletion(MediaPlayer mp) {
		if(PlayerState.currentSong >= PlayerState.nowPlaying.size()-1 && PlayerState.repeat != 1)
		{
			mp.stop();
		}
		else if (PlayerState.currentSong >= PlayerState.nowPlaying.size()-1 && PlayerState.repeat == 1)
		{
			try {
				PlayerState.currentSong = 0;
				//PlayerState.mp.reset();
				PlayerState.mp.setDataSource(PlayerState.context, PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
				PlayerState.mp.prepare();
				PlayerState.mp.start();
				Player.updateQueueText();
				PlayerState.changeSong();
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
		else
		{			
			try {
				if (PlayerState.repeat != 2)
					PlayerState.currentSong++;
				//PlayerState.mp.reset();
				PlayerState.mp.setDataSource(PlayerState.context, PlayerState.nowPlaying.get(PlayerState.currentSong).getURI());
				PlayerState.mp.prepare();
				PlayerState.mp.start();
				Player.updateQueueText();
				PlayerState.changeSong();
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
