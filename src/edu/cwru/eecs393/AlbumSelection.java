package edu.cwru.eecs393;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class AlbumSelection extends Activity {

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ScrollView scroll = new ScrollView(this);
		TableLayout table = new TableLayout(this);
		loadTable(table);
		scroll.addView(table);
		setContentView(scroll);
	}
	
	private void loadTable(TableLayout table)
	{
		
	}
}
