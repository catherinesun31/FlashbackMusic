package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
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

    public void initalizeAlbum(Activity a){
        Field[] fields = R.raw.class.getFields();
        Song[] songs = new Song[fields.length];
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        for (int i = 0; i < fields.length; ++i) {

            String path = "android.resource://" + a.getPackageName() + "/raw/" + fields[i].getName();
            final Uri uri = Uri.parse(path);

            mmr.setDataSource(a.getApplication(), uri);

            // Janice add in: wanted to pass in the file location as Song variable
            int songId = a.getResources().getIdentifier(fields[i].getName(), "raw", a.getPackageName());

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            long mil = Long.parseLong(duration);
            int seconds = (int) Math.ceil((mil / 1000) % 60);
            int minutes = (int) Math.ceil((mil / (1000 * 60)) % 60);
            duration = minutes + ":" + seconds;

            Log.d("Information: ", "Title: " + title + "\n" +
                    "Artist: " + artist + "\n" +
                    "com.android.flashbackmusicv000.Album: " + albumName + "\n" +
                    "Duration: " + duration);
            if(!checkAlbum(albumName)){

                albumsList.add(new Album(albumName, currentSong));

            }
            else {
                Album albumToAddSong = retrieveAlbum(albumName);
                albumToAddSong.addSong(new Song(title, songId));
            }
            if(i == 0) {
                this.allSongs = new Album("All Songs From Main Activity",currentSong);
            }
            else {
                this.allSongs.addSong(currentSong);
            }

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
}
