package com.android.flashbackmusicv000;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.flashbackmusicv000.Song;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class SongListActivity extends AppCompatActivity{

    private MediaPlayer mediaPlayer;

    SharedPreferences currentSongState;
    ArrayList<String> favorites;
    ArrayList<String> disliked;
    ArrayList<String> neutral;
    ArrayList<String> songs;

    // com.android.flashbackmusicv000.Song Instances
    //Song song1 = new Song(int R.raw.a01_everything_i_love);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        //SharedPreferences.Editor editor = currentSongState.edit();

        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            String[] f = (String[])b.get("Favorites");
            String[] d = (String[])b.get("Disliked");
            String[] n = (String[])b.get("Neutral");

            if (f != null) { favorites = new ArrayList<>(Arrays.asList(f)); }
            else { favorites = new ArrayList<>(); }

            if (d != null) disliked = new ArrayList<>(Arrays.asList(d));
            else { disliked = new ArrayList<>(); }

            if (n != null) neutral = new ArrayList<>(Arrays.asList(n));
            else { neutral = new ArrayList<>(); }

        }

        songs = new ArrayList<String>();
        if (favorites != null) {
            songs.addAll(favorites);
        }
        if (disliked != null) {
            songs.addAll(disliked);

        }
        if (neutral != null) {
            songs.addAll(neutral);
        }
        Collections.sort(songs);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Field[] fields = R.raw.class.getFields();
        final float scale = this.getResources().getDisplayMetrics().density;
        int songId = neutral.get(0).hashCode();
        int pixels = (int) (50 * scale + 0.5f);
        int textSize = (int) (15 * scale + 0.5f);
        int buttonId = songId + 1;

        for (int i = 0; i < fields.length; ++i) {
            String fileName = songs.get(i);

            Button button = new Button(this);
            android.support.constraint.ConstraintLayout.LayoutParams params = new
                    android.support.constraint.ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, pixels);
            button.setId(fileName.hashCode());
            button.setText(fileName);
            button.setBackgroundColor(Color.rgb(230, 230, 230));
            button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            button.setTextColor(Color.rgb(89, 89, 89));
            button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            button.setSingleLine(true);
            button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            button.setMarqueeRepeatLimit(1000);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchActivity();
                }
            });
            constraintLayout.addView(button, params);

            final Button add = new Button(this);
            android.support.constraint.ConstraintLayout.LayoutParams smallParams = new
                    android.support.constraint.ConstraintLayout.LayoutParams(
                    pixels, pixels);
            add.setId((int) button.getId() + 1);

            if (neutral.contains(fileName)) add.setText("+");
            else if (disliked.contains(fileName)) add.setText("x");
            else add.setText("✓");

            add.setBackgroundColor(Color.rgb(230, 230, 230));
            add.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            constraintLayout.addView(add, smallParams);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonChange(add);
                }
            });

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
            } else {
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
    }

    public boolean contains(String[] songs, String string) {
        for(String name: songs) {
            if (name.equals(string)) return true;
        }
        return false;
    }

    protected void buttonChange(Button button) {
        Intent intent = new Intent(this, SongListActivity.class);
        String name = button.getText().toString();
        switch (name) {
            case "+":
                button.setText("✓");
                neutral.remove(name);
                favorites.add(name);
                return;
            case "ｘ":
                button.setText("+");
                disliked.remove(name);
                neutral.add(name);
                return;
            case "✓":
                button.setText("ｘ");
                favorites.remove(name);
                disliked.add(name);
                return;
        }

        intent.putExtra("Favorites", favorites.toArray());
        intent.putExtra("Disliked", disliked.toArray());
        intent.putExtra("Neutral", neutral.toArray());
    }

    public void launchActivity() {
        Intent intent = new Intent(this, SongPlayingActivity.class);
        startActivity(intent);
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
