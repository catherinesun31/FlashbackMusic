package com.android.flashbackmusicv000.TrackList.model;
import java.util.Comparator;
public class AlbumSorter implements Comparator<DownloadedSong>{
	
	@Override
	public int compare(DownloadedSong ds, DownloadedSong ds1){
		//depends on how we implement the 
		return ds.getAlbum().compareTo(ds1.getAlbum());

	}

}