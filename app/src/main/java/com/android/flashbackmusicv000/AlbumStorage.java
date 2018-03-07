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

    public void initalizeAlbum( Song currentSong, String albumName, int i){

        if(!checkAlbum(albumName)){

            albumsList.add(new Album(albumName, currentSong));

        }
        else {
            Album albumToAddSong = retrieveAlbum(albumName);
            // changed below from creating new song
            albumToAddSong.addSong(currentSong);
        }
        if(i == 0) {
            this.allSongs = new Album("All Songs From Main Activity",currentSong);
        }
        else {
            this.allSongs.addSong(currentSong);
        }

    }



    public boolean checkAlbum(String albumName){

        for(Album album: albumsList){
            if(album.getName().equals(albumName)){
                return true;
            }
        }
        return false;
    }

    public Album retrieveAlbum(String albumName){
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

    public ArrayList<Album> getAlbums(){
        return albumsList;
    }
}
