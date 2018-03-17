package com.android.flashbackmusicv000.TrackList.model;
import java.util.Comparator;
public class FavouriteSorter implements Comparator<DownloadedSong>{
	
	//sort by favourites....

	@Override
	public int compare(DownloadedSong ds, DownloadedSong ds1){

		//depends on how we implement the 

		return ds.getFavouriteStatus() - ds1.getFavouriteStatus();

	}

}