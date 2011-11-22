package edu.cwru.eecs393;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<String> groups;
	private ArrayList<ArrayList<Item>> songs;
	private boolean groupArtist;
	
	public ExpandableListAdapter(Context cont, ArrayList<String> group, ArrayList<ArrayList<Item>> song, String groupBy)
	{
		context = cont;
		groups = group;
		songs = song;
		if (groupBy.equals("artist") || groupBy.equals("Artist"))
		{
			groupArtist = true;
		}
		else
		{
			groupArtist = false;
		}
	}
	
	@Override
	public boolean areAllItemsEnabled()
	{
		return true;
	}
	
	public Object getChild(int groupPos, int songPos) {
		return songs.get(groupPos).get(songPos);
	}

	public long getChildId(int groupPos, int songPos) {
		return songPos;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Item song = (Item)getChild(groupPosition, childPosition);
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
		}
		TextView tvTitle = (TextView) convertView.findViewById(R.id.textTitle);
		TextView tvArtist = (TextView) convertView.findViewById(R.id.textArtist);
		
		tvTitle.setText("         " + song.getTitle());
		if(groupArtist)
		{
			tvArtist.setText("                  " + song.getAlbum());
		}
		else
		{
			tvArtist.setText("");
		}
		
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return songs.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String group = (String) getGroup(groupPosition);
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
		}
		TextView tvGroup = (TextView) convertView.findViewById(R.id.textTitle);
		TextView tvArtist = (TextView) convertView.findViewById(R.id.textArtist);
		
		tvGroup.setText("       " + group);
		if(groupArtist)
		{
			tvArtist.setText("");
		}
		else
		{
			tvArtist.setText("");
		}
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
