package com.android.flashbackmusicv000.TrackList.model;

import com.android.flashbackmusicv000.Song;

import java.util.Comparator;

public class DownloadedSong extends Song {
	
	private String title;
	private String artist;
	private String album;
	private String status;

	public DownloadedSong(String[] info){

    //just sending empty data for super class.... wll ammend appropriately, later.
    super("",0);
    
		this.title = info[0];
    this.artist = info[1];
    this.album = info[2];
    this.status = info[3];

	}

  
  public String getTitle(){

    return this.title;

  }


  public String getArtist(){

    return this.artist;

  }


  public String getAlbum(){

    return this.album;

  }

  public int getFavouriteStatus(){

    return Integer.parseInt(status);

  }

  public String toString(){

  		return this.title +", "+this.artist+", "+this.album+", "+this.status;

  	}

}