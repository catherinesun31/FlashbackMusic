package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.ArraySet;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Chelsea, Janice on 3/2/18.
 */

public class MusicStorage {


    private SongStorage ss = new SongStorage();
    private AlbumStorage as = new AlbumStorage();


    public void initializeMusic(Activity activity) {

        ss.initializeSongs(activity);
        as.initalizeAlbum();

    }

    public SongStorage getSongStorage(){
        return ss;
    }

    public AlbumStorage getAlbumStorage(){
        return as;
    }

    /*
    public Song[] getCurrentSongs(MainActivity a, Set<String> favorites, Set<String> disliked, Set<String> neutral) {

            /* The following conditional statements add to the ArrayLists of strings.
            * will instead add the strings to the songs... and pass them as albums.


            //TODO what is this supposed to be doing lol

            boolean flag = false;
            if (favorites != null) {
                if (!favorites.isEmpty()) {
                    if (favorites.contains(title) && !flag) {
                        currentSong.favorite();
                        //adding to the string arrayList.
                        favorites.add(currentSong.getTitle());
                        //this.favorites[favoritesNow] = currentSong.getTitle();
                        ++favoritesNow;
                        flag = true;
                    }

                }
            }
            if (disliked != null) {
                if (!disliked.isEmpty()) {
                    if (disliked.contains(title) && !flag) {
                        currentSong.dislike();
                        disliked.add(currentSong.getTitle());
                        //this.disliked[dislikedNow] = currentSong.getTitle();
                        ++dislikedNow;
                    }

                }
            }
            if (neutral != null) {
                if (!neutral.isEmpty()) {
                    if (neutral.contains(title) && !flag) {
                        currentSong.neutral();
                        neutral.add(currentSong.getTitle());
                        //this.neutral[neutralNow] = currentSong.getTitle();
                        ++neutralNow;
                    }
                }
            }

            //if the album does not exist within the set of albums, add a new album to it with the
            //set of songs. else simply add to a currently existing album.

            if(!checkAlbum(albumName)){

                albums.add(new Album(albumName, currentSong));

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
            songs[i] = currentSong;


        return songs;
    }
    */


    public void createStorage(boolean f, boolean d, boolean n, Set<String> favorites, Set<String> disliked,
                              Set<String> neutral){

        if (f || d || n) {
            //do something
        }
        if ((!f && !d && !n) || (favorites.size() + neutral.size() + disliked.size() == 0)) {
            neutral = new ArraySet<>();

            for (int i = 0; i < ss.songsList.size(); ++i) {
                neutral.add(ss.songsList.get(i).getTitle());
                if(i == 0) {
                    as.allSongs = new Album("All Songs From Main Activity",ss.songsList.get(i));
                }
                else {
                    as.allSongs.addSong(ss.songsList.get(i));
                }
            }
        }
    }
}