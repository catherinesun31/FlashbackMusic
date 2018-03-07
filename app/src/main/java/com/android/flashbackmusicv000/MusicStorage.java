package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.ArraySet;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.Set;

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

    public ArraySet<String> createStorage(Activity a, boolean f, boolean d, boolean n, Set<String> favorites, Set<String> disliked,
                              Set<String> neutral) {

        if (f || d || n) {
            //TODO what activity here?

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

                ss.initializeSongs(currentSong, favorites, disliked, neutral);

                as.initalizeAlbum(currentSong, albumName, i);

            }

            //TODO need to decouple songstorage and albumstorage... creating album
            // with a song.

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

    public void addStorage(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference();
        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String url = dataSnapshot.getValue().toString();
                System.out.println(url);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        //String url = dataRef.toString();
        //Log.d("url", url);
        /*StorageReference storageRef = database.getReference();
        dataRef.child("URL Download").getDownloadURL().then(function(url) {
            // `url` is the download URL for 'images/stars.jpg'

            // This can be downloaded directly:
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob';
            xhr.onload = function(event) {
                var blob = xhr.response;
            };
            xhr.open('GET', url);
            xhr.send();

            // Or inserted into an <img> element:
            var img = document.getElementById('myimg');
            img.src = url;
        }).catch(function(error) {
            // Handle any errors
        });*/
    }
}
