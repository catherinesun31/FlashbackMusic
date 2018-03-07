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



public void createStorage(boolean f, boolean d, boolean n, Set<String> favorites, Set<String> disliked,
                          Set<String> neutral){


        if (f || d || n) {
            //TODO what activity here?
            ss.initializeSongs(getActivity(), favorites, disliked, neutral);
            as.initalizeAlbum(getActivity());

        }
        //TODO need to decouple songstorage and albumstorage... creating album
        // with a song.

        if ((!f && !d && !n) || (favorites.size() + neutral.size() + disliked.size() == 0)) {
            neutral = new ArraySet<>();

            for (int i = 0; i < ss.songsList.size(); ++i) {
                neutral.add(ss.songsList.get(i).getTitle());
                if (i == 0) {
                    as.allSongs = new Album("All Songs From Main Activity", ss.songsList.get(i));
                } else {
                    as.allSongs.addSong(ss.songsList.get(i));
                }
            }
            
        }
    }
}