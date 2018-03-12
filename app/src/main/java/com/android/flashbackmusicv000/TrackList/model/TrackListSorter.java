package com.android.flashbackmusicv000.TrackList.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Arrays;


public class TrackListSorter{
	
	private Comparator<DownloadedSong> sortStrategy;
	private DownloadedSong[] dsList;

	public TrackListSorter(DownloadedSong[] dsList){

		this.dsList = dsList;

	}

	public void setSort(Comparator<DownloadedSong> sortStrategy){

		this.sortStrategy = sortStrategy;

	}

	public void sortTracks(){

		if(this.sortStrategy != null) {

			Arrays.sort(dsList, sortStrategy);

		}
	}

	public DownloadedSong[] getSongs(){

		return dsList;

	}

	@Override
	public String toString(){

		String listOfSongs = "";

		for(DownloadedSong ds : dsList){

			listOfSongs += "[" + ds + "]";

		}

		return listOfSongs;

	}

}