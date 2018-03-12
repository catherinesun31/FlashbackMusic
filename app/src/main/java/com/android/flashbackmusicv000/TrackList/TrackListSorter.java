package com.android.flashbackmusicv000.TrackList;

import android.provider.MediaStore;
import android.widget.Button;

import com.android.flashbackmusicv000.Album;

/**
 * Created by MobileComputerWizard on 3/10/2018.
 */

//decorator pattern. There will be a generic, by album order.
    //
public abstract class TrackListSorter {


    //just send different track lists????????
    private Album ListOfAlbums[];


    public void trackListSorter(){

       this.listOfAlbums = getAllAlbums();

    }

    private static void getAllAlbums(){



    }

}
