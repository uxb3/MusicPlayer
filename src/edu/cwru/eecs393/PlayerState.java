package edu.cwru.eecs393;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaPlayer;


public class PlayerState{
	static MusicRetriever music;
	static List<MusicRetriever.Item> nowPlaying;
	static int currentSong = -1;
	static MediaPlayer mp;
	static boolean play;
	static Context context;
	static PlayerListener listener;
	
	static void prepare(ContentResolver cr)
	{
		nowPlaying = new ArrayList<MusicRetriever.Item>();
		music = new MusicRetriever(cr);
		music.prepare();
		play = false;
		listener = new PlayerListener();
	}
	
	static void addNowPlaying(MusicRetriever.Item song)
	{
		nowPlaying.add(song);
	}
	
	static void createMediaPlayer(Context cont)
	{
		context = cont;
		if (PlayerState.mp == null)
		{
			PlayerState.mp = MediaPlayer.create(cont, PlayerState.nowPlaying.get(0).getURI());
			mp.setOnCompletionListener(listener);
		}
	}
	
	static void clearNowPlaying()
	{
		nowPlaying.clear();
		currentSong = -1;
	}
	
	static void removeNowPlaying(int index)
	{
		nowPlaying.remove(index);
		if(currentSong > index)
		{
			currentSong--;
		}
	}
}
