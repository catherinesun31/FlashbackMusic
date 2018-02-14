package com.android.flashbackmusicv000;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.android.flashbackmusicv000.Song;

import java.io.File;

public class SongListActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private static int[] MEDIA_RES_IDS;
    int totalSongs = getNumberOfSongs();

    //MEDIA_RES_IDS = new int[totalSongs];



    // com.android.flashbackmusicv000.Song Instances
    // Song song1 = new Song(int R.raw.a01_everything_i_love);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //int totalSongs = getNumberOfSongs();
        //MEDIA_RES_IDS = new int[totalSongs];

        Button song1B = (Button) findViewById(R.id.song1);
        Button song2B = (Button) findViewById(R.id.song2);
        Button song3B = (Button) findViewById(R.id.song3);
        Button song4B = (Button) findViewById(R.id.song4);
        Button song5B = (Button) findViewById(R.id.song5);
        Button song6B = (Button) findViewById(R.id.song6);
        Button song7B = (Button) findViewById(R.id.song7);
        Button song8B = (Button) findViewById(R.id.song8);
        Button song9B = (Button) findViewById(R.id.song9);
        Button song10B = (Button) findViewById(R.id.song10);

        song1B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song2B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song3B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song4B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song5B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song6B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song7B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song8B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song9B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });
        song10B.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity();
            }
        });

        /*
        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(MEDIA_RES_ID);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        java.io.File file = new java.io.File("/Users/cailintreseder/AndroidStudioProjects/FlashbackMusic/app/src/main/res/raw");
        File[] files = file.listFiles();
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        Button songButton;

        for (int i = 0; i < totalSongs; ++i) {
            String fileName = files[i].getName().replace(".mp3", "");
            Button button = new Button(this);
            ScrollView.LayoutParams params = new ScrollView.LayoutParams(
                    ScrollView.LayoutParams.MATCH_PARENT,
                    50,
                    Gravity.CENTER_VERTICAL
            );
            button.setId(button.toString().hashCode());
            button.setText(fileName);
            button.setBackgroundColor(Color.rgb(230, 230, 230));
            button.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            button.setTextColor(Color.rgb(89,89,89));
            button.setTextSize(20f);
            button.setSingleLine(true);
            button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            button.setMarqueeRepeatLimit(1000);
            scrollView.addView(button, params);
            songButton = (Button) findViewById(button.getId());
            songButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //set button listener
                }
            });

            Button add = new Button(this);
            ScrollView.LayoutParams smallParams = new ScrollView.LayoutParams(
                    50,
                    50
            );
            add.setId(add.toString().hashCode());
            add.setText("@string/favorited");
            add.setBackgroundColor(Color.rgb(230, 230, 230));
            add.setTextSize(20f);
            scrollView.addView(add, smallParams);
            add = (Button) findViewById(add.getId());
            add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //set button listener
                }
            });
        }
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void launchActivity(){
        Intent intent = new Intent(this, SongPlayingActivity.class);
        startActivity(intent);
    }

    private int getNumberOfSongs() {
        java.io.File file = new java.io.File("/Users/cailintreseder/AndroidStudioProjects/FlashbackMusic/app/src/main/res/raw");
        System.out.println(file.length());
        return (int)file.length();
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
