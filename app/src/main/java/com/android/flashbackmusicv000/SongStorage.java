package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

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


    public void initializeSongs(Activity a, Set<String> fave, Set<String> dis, Set<String> neut){

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


            Song currentSong = new Song(title, songId);

            getSongFavDisNeut(currentSong, fave, dis, neut);

            //ADD TO SONGS LIST
            songsList.add(currentSong);
        }


    }

    /*
    Update a song's favorited, disliked and neutralized songs.
     */
    public void getSongFavDisNeut(Song currentSong,
                                  Set<String> favorites,
                                  Set<String> disliked,
                                  Set<String> neutral){
        boolean flag = false;
        if (favorites != null) {
            if (!favorites.isEmpty()) {
                if (favorites.contains(currentSong.getTitle()) && !flag) {
                    currentSong.favorite();
                    //adding to the string arrayList.
                    favorites.add(currentSong.getTitle());
                    //this.favorites[favoritesNow] = currentSong.getTitle();
                   // ++favoritesNow;
                    flag = true;
                }

            }
        }
        if (disliked != null) {
            if (!disliked.isEmpty()) {
                if (disliked.contains(currentSong.getTitle()) && !flag) {
                    currentSong.dislike();
                    disliked.add(currentSong.getTitle());
                    //this.disliked[dislikedNow] = currentSong.getTitle();
                   // ++dislikedNow;
                }

            }
        }
        if (neutral != null) {
            if (!neutral.isEmpty()) {
                if (neutral.contains(currentSong.getTitle()) && !flag) {
                    currentSong.neutral();
                    neutral.add(currentSong.getTitle());
                    //this.neutral[neutralNow] = currentSong.getTitle();
                    //++neutralNow;
                }
            }
        }
    }
}
