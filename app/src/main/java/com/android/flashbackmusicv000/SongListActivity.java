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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.flashbackmusicv000.Song;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class SongListActivity extends AppCompatActivity{

    private MediaPlayer mediaPlayer;
    private boolean isFlashBackOn;
    private boolean isFromAlbum = false;
    public int index;

    SharedPreferences currentSongState;
    public ArrayList<String> favorites;
    public ArrayList<String> disliked;
    public ArrayList<String> neutral;
    public ArrayList<String> songs;
    ArrayList<Song> actualSongs;
    ArrayList<Song> songsToPlay = new ArrayList<Song>();    // Janice add in
    private Switch switchy;
    private Intent in;

    // com.android.flashbackmusicv000.Song Instances

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        in = getIntent();

        /*a modification James Rich*/
        // just mocking up to get it working
        if(in.getBooleanExtra("albumOrigin",true)) {
            isFromAlbum = true;
            Album albumSelected = in.getExtras().getParcelable("songs");
            actualSongs = albumSelected.getSongs();


            currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
            SharedPreferences.Editor editor = currentSongState.edit();

            favorites = (ArrayList<String>)currentSongState.getStringSet("favorites", null);
            disliked = (ArrayList<String>)currentSongState.getStringSet("disliked", null);
            neutral = (ArrayList<String>)currentSongState.getStringSet("neutral", null);

            setWidgets();


            //getExtras.... passing strings????
        /*
        Bundle b = in.getExtras();
        if (b != null) {
            String[] f = (String[])b.get("Favorites");
            String[] d = (String[])b.get("Disliked");
            String[] n = (String[])b.get("Neutral");
            ArrayList<Song> tempSongs = getIntent().getParcelableArrayListExtra("Song list");
            System.out.println(tempSongs.size());

            if (f != null) { favorites = new ArrayList<>(Arrays.asList(f)); }
            else { favorites = new ArrayList<>(); }

            if (d != null) disliked = new ArrayList<>(Arrays.asList(d));
            else { disliked = new ArrayList<>(); }

            if (n != null) neutral = new ArrayList<>(Arrays.asList(n));
            else { neutral = new ArrayList<>(); }

            if (tempSongs != null) actualSongs = tempSongs;
            else { actualSongs = new ArrayList<>(); }
        }

        */
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
            //do this to show songs in alphabetical order
            Collections.sort(songs);

            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

            final float scale = this.getResources().getDisplayMetrics().density;
            int songId = actualSongs.get(0).getSongId();//neutral.get(0).hashCode();
            int pixels = (int) (50 * scale + 0.5f);
            int textSize = (int) (15 * scale + 0.5f);
            int buttonId = songId + 1;

            //counter loop creates a new button. Attaches a 'new song' to the click listener.

            for (index = 0; index < actualSongs.size(); index++) {
                String fileName = actualSongs.get(index).getTitle();

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

                final Song newSong = actualSongs.get(index);

                // Janice: Checking whether to play whole album or just the single song
                if (isFromAlbum) {
                    songsToPlay = actualSongs;
                } else {
                    songsToPlay.add(newSong);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //launchActivity(newSong);
                        launchActivity(songsToPlay);
                    }
                });

                constraintLayout.addView(button, params);

                final Button add = new Button(this);
                android.support.constraint.ConstraintLayout.LayoutParams smallParams = new
                        android.support.constraint.ConstraintLayout.LayoutParams(
                        pixels, pixels);
                add.setId((int) button.getId() + 1);

                if (actualSongs.contains(fileName)) add.setText("+");
                else if (actualSongs.contains(fileName)) add.setText("x");
                else add.setText("✓");

                add.setBackgroundColor(Color.rgb(230, 230, 230));
                add.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                constraintLayout.addView(add, smallParams);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonChange(add, newSong);
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


                if (index == 0) {
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
    }

    public boolean contains(String[] songs, String string) {
        for(String name: songs) {
            if (name.equals(string)) return true;
        }
        return false;
    }

    protected void buttonChange(Button button, Song song) {
        Intent intent = new Intent(this, SongListActivity.class);
        String name = button.getText().toString();
        switch (name) {
            case "+":
                button.setText("✓");
                neutral.remove(name);
                favorites.add(name);
                song.favorite();
                return;
            case "ｘ":
                button.setText("+");
                disliked.remove(name);
                neutral.add(name);
                song.neutral();
                return;
            case "✓":
                button.setText("ｘ");
                favorites.remove(name);
                disliked.add(name);
                song.dislike();
                return;
        }

        intent.putExtra("Favorites", favorites.toArray());
        intent.putExtra("Disliked", disliked.toArray());
        intent.putExtra("Neutral", neutral.toArray());
    }

    public void launchActivity(ArrayList<Song> songList /*Song song*/) {
        //new intent ... should have the
        Intent intent = new Intent(this, SongPlayingActivity.class);

        intent.putExtra("name_of_extra", songList);
        //intent.putExtra("name_of_extra", song);
        intent.putExtra("isOn", isFlashBackOn);
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

    // Sets the flashback mode switch across activities
    private void setWidgets(){

        switchy = (Switch) findViewById(R.id.flashSwitch);
        isFlashBackOn = in.getBooleanExtra("isOn",isFlashBackOn);
        switchy.setChecked(isFlashBackOn);

        switchy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked) {

                    //run event;
                    isFlashBackOn = true;
                    Toast.makeText(getApplicationContext(), "flashback mode is on", Toast.LENGTH_SHORT).show();

                } else {


                    //close event
                    isFlashBackOn = false;
                    Toast.makeText(getApplicationContext(), "flashback mode is off", Toast.LENGTH_SHORT).show();
                    //
                }
            }
        });
    }

    @Override
    public void onBackPressed(){

        //super.onBackPressed();
        //sharedPreferences switch state.

        SharedPreferences.Editor editor = MainActivity.flashBackState.edit();
        editor.putBoolean("isOn", isFlashBackOn);

        editor.apply();
        finish();

    }

    public void onRestart(){

        super.onRestart();

        isFlashBackOn = MainActivity.flashBackState.getBoolean("isOn", isFlashBackOn);

        switchy.setChecked(isFlashBackOn);


    }

}
