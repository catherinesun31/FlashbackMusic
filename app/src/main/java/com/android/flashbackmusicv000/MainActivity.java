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
import java.util.LinkedHashSet;
import java.util.List;
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
ArrayList<Song> songs;

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

    public Song[] getCurrentSongs(Set<String> favorites, Set<String> disliked, Set<String> neutral) {
        Field[] fields = R.raw.class.getFields();
        Song[] songs = new Song[fields.length];
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        for (int i = 0; i < fields.length; ++i) {
            String path = "android.resource://" + getPackageName() + "/raw/" + fields[i].getName();
            final Uri uri = Uri.parse(path);

            mmr.setDataSource(getApplication(), uri);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long mil = Long.parseLong(duration);
            int seconds = (int)Math.ceil((mil / 1000) % 60);
            int minutes = (int)Math.ceil((mil / (1000*60)) % 60);
            duration = minutes + ":" + seconds;

            Log.d("Information: ", "Title: " + title + "\n" +
            "Artist: " + artist + "\n" +
            "Album: " + albumName + "\n" +
            "Duration: " + duration);
            Song song = new Song(title);

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
     *
     * This starts the SongsListActivity, and migrates to the list of all of the current songs
     */
    // JANICE EDIT 02/13: PASSING IN THE ARRAY OF SONGS SO WE CAN PASS THROUGH TO SONGSLIST AND SONGSPLAYING
    public void launchSongs() {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putExtra("Favorites", favorites);
        intent.putExtra("Disliked", disliked);
        intent.putExtra("Neutral", neutral);
        intent.putExtra("Song list", songs);
        startActivity(intent);
    }

    /*
     * launchAlbums:
     */
    public void launchAlbums() {
        Intent albums  = new Intent(this, AlbumQueue.class);
        startActivity(albums);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
