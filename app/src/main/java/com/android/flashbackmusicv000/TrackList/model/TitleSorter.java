package com.android.flashbackmusicv000.TrackList.model;
import java.util.Comparator;
public class TitleSorter implements Comparator<DownloadedSong>{
	//DownloadedSong
	@Override
	public int compare(DownloadedSong ds, DownloadedSong ds1){

		//depends on how we implement the 
		return ds.getTitle().compareTo(ds1.getTitle());

	}
}