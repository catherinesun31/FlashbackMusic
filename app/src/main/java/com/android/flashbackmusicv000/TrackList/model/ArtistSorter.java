package com.android.flashbackmusicv000.TrackList.model;
import java.util.Comparator;
public class  ArtistSorter implements Comparator<DownloadedSong>{

	@Override
	public int compare(DownloadedSong ds, DownloadedSong ds1){

		//depends on how we implement the 
		return ds.getArtist().compareTo(ds1.getArtist());

	}

}