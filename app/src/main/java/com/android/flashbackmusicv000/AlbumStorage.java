package com.android.flashbackmusicv000;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by Chelsea and Janice on 3/2/18.
 */

public class AlbumStorage {

    private ArrayList<Album> albumsList;
    public Album allSongs;

    public AlbumStorage(){
        if(albumsList == null){
            albumsList = new ArrayList<Album>();
        }
    }

    public void initalizeAlbum(){



    }

    private boolean checkAlbum(String albumName){

        for(Album album: albumsList){
            if(album.getName().equals(albumName)){
                return true;
            }
        }
        return false;
    }

    private Album retrieveAlbum(String albumName){
        int index = 0;
        Album currentAlbum = null;
        ListIterator<Album> it = albumsList.listIterator();
        while(it.hasNext()){

            currentAlbum = it.next();
            if(currentAlbum.getName().equals(albumName)){
                return currentAlbum;
            }
        }
        return currentAlbum;
    }
}
