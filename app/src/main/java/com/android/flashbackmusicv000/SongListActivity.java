package com.android.flashbackmusicv000;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

public class SongListActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private static int[] MEDIA_RES_IDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int totalSongs = getNumberOfSongs();
        //Log.d("Total Songs", totalSongs + "");
        MEDIA_RES_IDS = new int[totalSongs];
        /*
        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(MEDIA_RES_ID);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        */

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        //Button songButton;
        Field[] fields = R.raw.class.getFields();
        final float scale = this.getResources().getDisplayMetrics().density;
        int songId = fields[0].getName().replace(".mp3", "").hashCode();
        int pixels = (int) (50 * scale + 0.5f);
        int textSize = (int) (15 * scale + 0.5f);
        int buttonId = songId + 1;

        for (int i = 0; i < fields.length; ++i) {
            String fileName = fields[i].getName().replace(".mp3", "");

            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + fields[i].getName());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, mediaPath);

            Button button = new Button(this);
            android.support.constraint.ConstraintLayout.LayoutParams params = new
                    android.support.constraint.ConstraintLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, pixels);
            button.setId(fileName.toString().hashCode());
            button.setText(fileName);
            button.setBackgroundColor(Color.rgb(230, 230, 230));
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setTextColor(Color.rgb(89,89,89));
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            button.setSingleLine(true);
            button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            button.setMarqueeRepeatLimit(1000);
            constraintLayout.addView(button, params);

            /*button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //set button listener
                }
            });*/

            Button add = new Button(this);
            android.support.constraint.ConstraintLayout.LayoutParams smallParams = new
                    android.support.constraint.ConstraintLayout.LayoutParams(
                    pixels, pixels);
            add.setId((int)button.getId() + 1);
            add.setText("✓");
            add.setBackgroundColor(Color.rgb(230, 230, 230));
            add.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            constraintLayout.addView(add, smallParams);
            //add = (Button) findViewById(add.getId());
            /*add.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //set button listener
                }
            });*/

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(
                    button.getId(), ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constraintSet.connect(
                    add.getId(), ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constraintSet.connect(
                    button.getId(), ConstraintSet.END,
                    add.getId(), ConstraintSet.START, pixels);


            if (i == 0) {
                constraintSet.connect(
                        button.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                constraintSet.connect(
                        add.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            }

            else {
                constraintSet.connect(
                        button.getId(), ConstraintSet.TOP,
                        songId, ConstraintSet.BOTTOM, 0);
                constraintSet.connect(
                        add.getId(), ConstraintSet.TOP,
                        buttonId, ConstraintSet.BOTTOM, 0);
            }
            constraintSet.constrainDefaultHeight(button.getId(), pixels);
            constraintSet.constrainDefaultHeight(add.getId(), pixels);
            constraintSet.applyTo(constraintLayout);

            songId = button.getId();
            buttonId = songId + 1;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
