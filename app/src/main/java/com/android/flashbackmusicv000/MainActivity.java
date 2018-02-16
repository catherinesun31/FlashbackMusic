/**
 * Main activity, backend activity file for activity_main.xml.This file represents the controller for the inputs
 * of the user. When they select either the song or album button, it will bring up the next activity
 * consisting of albums or songs. This activity allows the user options for what they want their app
 * to do.
 */
package com.android.flashbackmusicv000;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

SharedPreferences currentSongState;
String[] favorites;
String[] disliked;
String[] neutral;
int favoritesNow;
int dislikedNow;
int neutralNow;
ArrayList<Song> songs1;
ArrayList<Album> albums;

    /**
     * onCreate Method represents the beginning state of the main activity whenever it is started.
     * @param savedInstanceState is the previous state of this activity represented from the Bundle
     * object.
     * The current song list is loaded from the shared preference across the entire app, restoring the
     * songslist from when it was last played.                          .
     * If none of the the string sets obtained from the shared preferences have empty data,
     * then we know that there was an album. Then the songslist gets updated.
     * Anonymous listeners are then set for each of the buttons on activity_main.xml.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        favoritesNow = 0;
        dislikedNow = 0;
        neutralNow = 0;

        currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentSongState.edit();

        Set<String> fave = currentSongState.getStringSet("favorites", null);
        Set<String> dis = currentSongState.getStringSet("disliked", null);
        Set<String> neut = currentSongState.getStringSet("neutral", null);

        Song[] songs = getCurrentSongs(fave, dis, neut);
        songs1 = new ArrayList<Song>(Arrays.asList(songs));
        if (fave != null && disliked != null && neutral != null) {
            songs = getCurrentSongs(fave, dis, neut);
        }
        else {
            neutral = new String[songs.length];
            for (int i = 0; i < songs.length; ++i) {
                neutral[i] = songs[i].getTitle();
            }
        }

        //Set onClickListener for songs button
        // JANICE EDIT: 02/13, PASSING IN SONGS[] SO THAT WE CAN ACCESS IT IN THE NEXT ACTIVITY
        Button songsList = (Button) findViewById(R.id.songs);
        songsList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                launchSongs();
            }
        });


       Button albumList = (Button) findViewById(R.id.albums);

        albumList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchAlbums();
            }
        });

        /*
         * I'm thinking that here, we should make a list of all of the Song objects from songs that
         * the user has in their R.raw file, and store it in the phone's shared preferences.
         */

    }

    /**
     * To make the code less fragile we would rather get the list of strings and put them inside the
     * song object, so we can pass an array of the current songs.
     * @param favorites the set of unique titles that have been favourited.
     * @param disliked the set of unique titles that have been disliked.
     * @param neutral the set of unique titles that have been classified as neutral.
     *
     * Thefields are obtained from the directory R.raw's contents
     * An array for storing the songs to return is created.
     *                for as long as the length of the fields array,
     *                MediaMetaDataRetriever obtains the metadata(data that describes other data)source from the URI.
     *                The URI was an absolute path from the string 'path', pointing to the raw directory containing
     *                the media files. the MMDR then. A song object is created and has the Strings, extracted
     *                from the MMDR passed into it. This is added to the songs array.
     *                Once the loop is finised, the current list of songs is returned.
     *
     * @return the list of songs
     */
    public Song[] getCurrentSongs(Set<String> favorites, Set<String> disliked, Set<String> neutral) {

        Field[] fields = R.raw.class.getFields();
        Song[] songs = new Song[fields.length];
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        for (int i = 0; i < fields.length; ++i) {
            String path = "android.resource://" + getPackageName() + "/raw/" + fields[i].getName();
            final Uri uri = Uri.parse(path);

            mmr.setDataSource(getApplication(), uri);

            // Janice add in: wanted to pass in the file location as Song variable
            int songId = this.getResources().getIdentifier(fields[i].getName(), "raw", this.getPackageName());

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long mil = Long.parseLong(duration);
            int seconds = (int)Math.ceil((mil / 1000) % 60);
            int minutes = (int)Math.ceil((mil / (1000*60)) % 60);
            duration = minutes + ":" + seconds;

            //if the album does not exist within the set of albums, add a new album to it with the
            //set of songs. else simply add to a currently existing album.


            if(!checkAlbum(albumName)){

                albums.add(new Album<Song>(albumName, new Song(title, songId)));

            } else {

                Album albumToAddSong = retrieveAlbum(albumName);
                albumToAddSong.addSong(new Song(title, songId));

            }

            Log.d("Information: ", "Title: " + title + "\n" +
            "Artist: " + artist + "\n" +
            "com.android.flashbackmusicv000.Album: " + albumName + "\n" +
            "Duration: " + duration);
            Song song = new Song(title, songId);

            if (favorites != null) {
                if (favorites.contains(title)) {
                    song.favorite();
                    this.favorites[favoritesNow] = song.getTitle();
                    ++favoritesNow;
                }
            }
            if (disliked != null) {
                if (disliked.contains(title)) {
                    song.dislike();
                    this.disliked[dislikedNow] = song.getTitle();
                    ++dislikedNow;
                }
            }
            if (neutral != null) {
                if (neutral.contains(title)) {
                    song.neutral();
                    this.neutral[neutralNow] = song.getTitle();
                    ++neutralNow;
                }
            }
            songs[i] = song;
        }

        return songs;
    }

    /*
     * launchSongs:
     * @params: none
     * @return: void
     * The intents are created and the list of strings, with favourite titles, disliked titles, neutral titles
     * and songs are put inside.
     * This starts the SongsListActivity, and migrates to the list of all of the current songs
     */
    // JANICE EDIT 02/13: PASSING IN THE ARRAY OF SONGS SO WE CAN PASS THROUGH TO SONGSLIST AND SONGSPLAYING
    public void launchSongs() {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putExtra("Favorites", favorites);
        intent.putExtra("Disliked", disliked);
        intent.putExtra("Neutral", neutral);
        intent.putExtra("Song list", songs1);
        startActivity(intent);
    }

    /*
     * launchAlbums:
     */
    public void launchAlbums() {
        Intent albums  = new Intent(this, AlbumQueue.class);
        startActivity(albums);

    }

<<<<<<< HEAD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
=======
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean checkAlbum(String albumName){

        for(Album album: albums){

            if(album.getName().equals(albumName)){

                return true;

            }

        }

        return false;
    }

    private Album retrieveAlbum(String albumName){

        int index = 0;

        Album currentAlbum = null;

        ListIterator<Album> it = albums.listIterator();

        while(it.hasNext()){

            currentAlbum = it.next();
            if(currentAlbum.getName().equals(albumName)){

                return currentAlbum;

            }

        }

        return currentAlbum;

>>>>>>> James-Branch
    }

    /**
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
