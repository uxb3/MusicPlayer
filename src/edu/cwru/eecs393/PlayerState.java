package edu.cwru.eecs393;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.media.MediaPlayer;


public class PlayerState {
	static MusicRetriever music;
	static List<MusicRetriever.Item> nowPlaying;
	static int currentSong = -1;
	static MediaPlayer mp;
	static boolean play;
	
	
	static void prepare(ContentResolver cr)
	{
		nowPlaying = new ArrayList<MusicRetriever.Item>();
		music = new MusicRetriever(cr);
		music.prepare();
		play = false;
	}
	
	static void addNowPlaying(MusicRetriever.Item song)
	{
		nowPlaying.add(song);
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
