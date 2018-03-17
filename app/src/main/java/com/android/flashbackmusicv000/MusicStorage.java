package com.android.flashbackmusicv000;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.ArraySet;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;



/**
 * Created by Chelsea, Janice on 3/2/18.
 */

public class MusicStorage {


    private SongStorage ss = new SongStorage();
    private AlbumStorage as = new AlbumStorage();

    public SongStorage getSongStorage() {
        return ss;
    }

    public AlbumStorage getAlbumStorage() {
        return as;
    }

    /* This method gets all the songs we've stored so far in our phone and initializes them as songs
     * and albums.
     */
    public ArraySet<String> createStorage(Activity a, boolean f, boolean d, boolean n, Set<String> favorites, Set<String> disliked,
                              Set<String> neutral) {

        // If we have any songs at all
        if (f || d || n) {

            // Loops through all of our raw fields
            Field[] fields = R.raw.class.getFields();
            // Song[] songs = new Song[fields.length];
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            for (int i = 0; i < fields.length; ++i) {

                String path = "android.resource://" + a.getPackageName() + "/raw/" + fields[i].getName();
                final Uri uri = Uri.parse(path);

                mmr.setDataSource(a.getApplication(), uri);

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

                ss.initializeSong(currentSong, favorites, disliked, neutral);

                as.initializeAlbum(currentSong, albumName);

            }

            if ((!f && !d && !n) || (favorites.size() + neutral.size() + disliked.size() == 0)) {
                neutral = new ArraySet<>();
                Log.d("Is it empty", "It is empty");
                Log.d("Neutral size", "It is empty");


                for (int i = 0; i < ss.songsList.size(); ++i) {
                    Log.d("Running songsList", "Running for loop " + i);

                    neutral.add(ss.songsList.get(i).getTitle());
                    if (i == 0) {
                        as.allSongs = new Album("All Songs From Main Activity", ss.songsList.get(i));
                    } else {
                        as.allSongs.addSong(ss.songsList.get(i));
                    }
                }
            }
        }
        return (ArraySet<String>) neutral;
    }

    public void addNewDownload(Activity a, String path, Set<String> favorites, Set<String> disliked,
                               Set<String> neutral){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

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

        // Get shared preferences to add song to the string list of neutrals
        SharedPreferences currentSongState = a.getSharedPreferences("songs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = currentSongState.edit();

        Song currentSong = new Song(title, path);
        neutral.add(title);
        ss.initializeSong(currentSong, favorites, disliked, neutral);
        as.initializeAlbum(currentSong, albumName);
        editor.putStringSet("neutral", neutral);
    }

}
