package edu.cwru.eecs393;

import android.util.Log;

/*
 * Keeps track of the time based on the duration
 * you can change the time using the update function
 */
public class MediaTime {

	int mins = 0;
	int seconds;
	int d;
	
	/*
	 * The constructor accepts the duration in milliseconds.
	 */
	public MediaTime(int duration) {
	
		d = duration;
		duration = duration / 1000; // get in seconds
		while(duration > 60) {
			
			mins += 1;
			duration -= 60;
		}
		seconds = duration;
	}
	
	//Reduces the time by one second
	public boolean update() {
	
		if(seconds > 0) {
			
			seconds--;
		}
		else if(mins > 0) {
			
			mins--;
			seconds = 59;
		}
		else
			return false;
		return true;
	}
	
	// Put in a negative value if you want to reduce
	// need to implement this
	public boolean update(int seconds) {

		return true;
	}
	
	//Gives you the current time
	
	public String getTime() {
		
		return mins + ":" + seconds;
	}
	
	public int getDuration() {
		
		return d;
	}
}
