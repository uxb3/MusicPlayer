package edu.cwru.eecs393;

public class PlaylistInfo {

	private String name;
	private long id;
	public PlaylistInfo(long i, String n) {
		
		id = i;
		name = n;
		
	}
	
	public String getName() {
		
		return name;
	}
	
	public long getId() {
		
		return id;
	}
}
