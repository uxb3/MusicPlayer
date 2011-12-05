package edu.cwru.eecs393;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaPlayer;


public class PlayerState {
	static MusicRetriever music;
	static List<Item> nowPlaying;
	static int currentSong = -1;
	static MediaPlayer mp;
	static boolean play;
	static Context context;
	static PlayerListener listener;
	static int repeat = 0;
	
	
	static void prepare(ContentResolver cr)
	{
		nowPlaying = new ArrayList<Item>();
		music = new MusicRetriever(cr);
		music.prepare();
		play = false;
		listener = new PlayerListener();
	}
	
	static void addNowPlaying(List<Item> songs)
	{
		for(int cnt = 0; cnt < songs.size(); cnt++)
			nowPlaying.add(songs.get(cnt));
	}
	
	static void addNowPlaying(Item song) {
		
		nowPlaying.add(song);
	}
	
	static void createMediaPlayer(Context cont)
	{
		context = cont;
		if (PlayerState.mp == null)
		{
			PlayerState.mp = MediaPlayer.create(cont, PlayerState.nowPlaying.get(currentSong).getURI());
			mp.setOnCompletionListener(listener);
		}
	}
	
	static void clearNowPlaying()
	{
		nowPlaying.clear();
		currentSong = -1;
	}
	
	static void shuffle()
	{
		List<Item> shuffled = new ArrayList<Item>();
		shuffled.add(nowPlaying.get(currentSong));
		nowPlaying.remove(currentSong);
		
		while(nowPlaying.size() > 0)
		{
			int random = (int) (Math.random()*nowPlaying.size());
			shuffled.add(nowPlaying.get(random));
			nowPlaying.remove(random);
		}
		currentSong = 0;
		nowPlaying = shuffled;
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
