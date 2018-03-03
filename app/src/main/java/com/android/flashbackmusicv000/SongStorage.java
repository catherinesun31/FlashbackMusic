package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Chelsea and Janice on 3/2/18.
 */

public class SongStorage {

    public ArrayList<Song> songsList;


    public SongStorage(){
        if(songsList == null){
            songsList = new ArrayList<Song>();
        }

    }


    public void initializeSongs(Activity a){

        //TODO assuming we download the files into the raw file list.
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

            Song currentSong = new Song(title, songId);

        }

    }
}
