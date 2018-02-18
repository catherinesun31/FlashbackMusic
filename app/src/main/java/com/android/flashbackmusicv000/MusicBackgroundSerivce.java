/*
    Chelsea and Karla add in to get music to play in the background
 */

package com.android.flashbackmusicv000;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.widget.Toast;

import java.lang.reflect.Field;


public class MusicBackgroundSerivce extends Service  implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;

    //private Song song;
    private static int songId;







    SharedPreferences currentSongState;







    // TODO Fix setSongId and in onCreate
    //public int setSongId(Song song){
    //    songId = song.getSongId();
    //return songId;
    //}





    public MusicBackgroundSerivce() { }

    public class ServiceBinder extends Binder {
        public MusicBackgroundSerivce getService()
        {
            return MusicBackgroundSerivce.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0){return mBinder;}

    @Override
    public void onCreate (){
        super.onCreate();

        //currentSongState = getSharedPreferences("songs", MODE_PRIVATE);


        //Intent i = new Intent(this, Song.class);
        //Intent i = getIntent();
        //Song song = (Song) i.getParcelableExtra("name_of_extra");
        //songId = song.getSongId();
        //System.out.println("This is the songId " + songId);


        Field[] fields = R.raw.class.getFields();
        //Song[] songs = new Song[fields.length];
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        for (int i = 0; i < fields.length; ++i) {
            String path = "android.resource://" + getPackageName() + "/raw/" + fields[i].getName();
            final Uri uri = Uri.parse(path);

            //mmr.setDataSource(getApplication(), uri);

            //String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            int songId = this.getResources().getIdentifier(fields[i].getName(), "raw", this.getPackageName());
            //System.out.println("This is the songId " + songId);

            mmr.setDataSource(getApplication(), uri);

            //Song song = new Song(title, songId);

            //songs[i] = song;
            //song = songs[i];
            //System.out.println("This is the song " + song);
            //System.out.println("This is the song at i  " + songs[i]);
            //songId = song.getSongId();
            //System.out.println("This is the songId " + songId);


            mPlayer = MediaPlayer.create(this, songId);
            mPlayer.setOnErrorListener(this);
        }




        //TODO can't have this be hardcoded
        //mPlayer = MediaPlayer.create(this, songId);
        //mPlayer.setOnErrorListener(this);

        if(mPlayer!= null)
        {
            mPlayer.setLooping(true);
            mPlayer.setVolume(150,150);
        }


        mPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra){

                onError(mPlayer, what, extra);
                return true;
            }
        });

        //TODO if a new song is clicked to play, pause the current song and play that one
        //if(){

        //}
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic()
    {
        if(mPlayer.isPlaying())
        {
            mPlayer.pause();
            length=mPlayer.getCurrentPosition();
            Toast.makeText(this, "Music is Paused", Toast.LENGTH_LONG).show();
        }
    }

    public void resumeMusic()
    {
        if(mPlayer.isPlaying()==false)
        {
            mPlayer.seekTo(length);
            Toast.makeText(this, "Music is started", Toast.LENGTH_LONG).show();
            mPlayer.start();
        }
    }

    public void stopMusic()
    {
        mPlayer.stop();
        Toast.makeText(this, "Music is stoped", Toast.LENGTH_LONG).show();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroy ()
    {
        super.onDestroy();
        if(mPlayer != null)
        {
            try{
                mPlayer.stop();
                mPlayer.release();
            }finally {
                mPlayer = null;
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        if(mPlayer != null)
        {
            try{
                mPlayer.stop();
                mPlayer.release();
            }finally {
                mPlayer = null;
            }
        }
        return false;
    }




}
