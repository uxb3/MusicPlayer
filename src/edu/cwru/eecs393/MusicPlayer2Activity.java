package edu.cwru.eecs393;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MusicPlayer2Activity extends Activity {
	protected static final long SPLASHTIME = 2500;
	protected boolean active = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // removes title bar on app, making image full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        
        Thread prepareThread = new Thread() {
        	@Override
        	public void run() {
        		PlayerState.prepare(getContentResolver());
        	}
        };
        prepareThread.start();
        
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(active && (waited < SPLASHTIME)) {
                        sleep(100);
                        if(active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    startActivity(new Intent(MusicPlayer2Activity.this,Selection.class));
                    stop();
                }
            }
        };
        if(PlayerState.nowPlaying == null)
        	splashTread.start();
        else
        	startActivity(new Intent(MusicPlayer2Activity.this,Selection.class));
    }
}