package edu.cwru.eecs393;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Player extends Activity {
	
	MediaPlayer mp;
	Uri someuri;
	
	 // Create an anonymous implementation of OnClickListener
    private OnClickListener PlayListener = new OnClickListener() {
        public void onClick(View v) 
        {
            mp.start();
        }
    };
    private OnClickListener PauseListener = new OnClickListener() {
        public void onClick(View v) 
        {
          mp.pause();
        }
    };
    private OnClickListener StopListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	mp.stop();
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(getBaseContext(), someuri);
        /*try {
            mp.setDataSource(soundUrl);
            mp.prepare();
        }
        catch (IOException e) {}
        catch (IllegalArgumentException e) {}
        catch (IllegalStateException e) {}*/
        Button button2 = (Button)this.findViewById(R.id.button1);
        //Register the onClick listener with the implementation above
        button2.setOnClickListener(PauseListener);
        
        Button button1 = (Button)this.findViewById(R.id.button2);
        //Register the onClick listener with the implementation above
        button1.setOnClickListener(PlayListener);
        
        Button button3 = (Button)this.findViewById(R.id.button3);
        //Register the onClick listener with the implementation above
        button3.setOnClickListener(StopListener);
    }
    


    
}