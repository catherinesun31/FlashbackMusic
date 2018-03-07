package com.android.flashbackmusicv000;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.ArraySet;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;


/**
 * Created by Chelsea, Janice on 3/2/18.
 */

public class MusicStorage {


    private SongStorage ss = new SongStorage();
    private AlbumStorage as = new AlbumStorage();

    String url;

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
                url = dataSnapshot.getValue().toString();
                String downloadRoute = Environment.getExternalStorageDirectory().toString();
                downloadFile(url, url, downloadRoute+"/res/raw");
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

    }
    static void downloadFile(String download_file_path, String fileName,
                             String pathToSave) {
        int downloadedSize = 0;
        int totalSize = 0;

        try {
            URL url = new URL(download_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            File myDir;
            myDir = new File(pathToSave);
            myDir.mkdirs();

            // create a new file, to save the downloaded file

            String mFileName = fileName;
            File file = new File(myDir, mFileName);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            // runOnUiThread(new Runnable() {
            // public void run() {
            // pb.setMax(totalSize);
            // }
            // });

            // create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                // runOnUiThread(new Runnable() {
                // public void run() {
                // pb.setProgress(downloadedSize);
                // float per = ((float)downloadedSize/totalSize) * 100;
                // cur_val.setText("Downloaded " + downloadedSize + "KB / " +
                // totalSize + "KB (" + (int)per + "%)" );
                // }
                // });
            }
            // close the output stream when complete //
            fileOutput.close();
            // runOnUiThread(new Runnable() {
            // public void run() {
            // // pb.dismiss(); // if you want close it..
            // }
            // });

        } catch (final MalformedURLException e) {
            // showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            // showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            // showError("Error : Please check your internet connection " + e);
        }
    }
}
