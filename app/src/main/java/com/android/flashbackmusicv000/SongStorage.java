package com.android.flashbackmusicv000;

import android.util.Log;

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

    /*
     * Add a song to our song list
     */
    public void initializeSongs(Song currentSong, Set<String> favorites, Set<String> disliked,
                                Set<String> neutral){


            setSongFavDisNeut(currentSong, favorites, disliked, neutral);

            //ADD TO SONGS LIST
            songsList.add(currentSong);


    }

    /*
     * Update a song's favorited, disliked and neutralized.
     */
    public void setSongFavDisNeut(Song currentSong,
                                  Set<String> favorites,
                                  Set<String> disliked,
                                  Set<String> neutral){
        Log.d("Attempt", "Attempting to get favdisneut");
        boolean flag = false;
        if (favorites != null) {
            if (!favorites.isEmpty()) {
                if (favorites.contains(currentSong.getTitle()) && !flag) {
                    currentSong.favorite();
                    //adding to the string arrayList.
                    //favorites.add(currentSong.getTitle());
                    //this.favorites[favoritesNow] = currentSong.getTitle();
                    // ++favoritesNow;
                    Log.d("Added a favorite", "Added a favorite");
                    flag = true;
                }

            }
        }
        if (disliked != null) {
            if (!disliked.isEmpty()) {
                if (disliked.contains(currentSong.getTitle()) && !flag) {
                    currentSong.dislike();
                    //disliked.add(currentSong.getTitle());
                    //this.disliked[dislikedNow] = currentSong.getTitle();
                    // ++dislikedNow;
                    Log.d("Added a disliked", "Added a disliked");

                }

            }
        }
        if (neutral != null) {
            if (!neutral.isEmpty()) {
                if (neutral.contains(currentSong.getTitle()) && !flag) {
                    currentSong.neutral();
                    //neutral.add(currentSong.getTitle());
                    Log.d("Added a neutral", "Added a neutral");
                    //this.neutral[neutralNow] = currentSong.getTitle();
                    //++neutralNow;
                }
            }
        }
    }
}
