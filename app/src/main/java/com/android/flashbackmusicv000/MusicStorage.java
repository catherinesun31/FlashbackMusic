package com.android.flashbackmusicv000;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.ArraySet;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.File;

import java.lang.reflect.Field;
import java.io.File;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

            String path = Environment.getExternalStorageDirectory().toString() + "/storage/emulated/0/Download/";
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();

                    mmr.setDataSource(path + fileName);
                    fileName = fileName.replace(".mp3", "");

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


                    Song currentSong = new Song(title, path + fileName);

                    ss.initializeSong(currentSong, favorites, disliked, neutral);

                    as.initializeAlbum(currentSong, albumName);
                    System.out.println("SIZE OF SONGLIST" + ss.songsList.size());
                }
            } else {
                System.err.println("Access denied");
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

    public boolean unpackZip(String path, String zipname,Set<String> favorites, Set<String> disliked,
                             Set<String> neutral, Activity a)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }
                System.err.println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        "/"+filename);
                addNewDownload(a, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                        "/"+filename, favorites, disliked, neutral);
                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
