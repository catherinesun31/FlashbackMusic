package com.android.flashbackmusicv000;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class SongListActivity extends AppCompatActivity{

    private MediaPlayer mediaPlayer;
    private boolean isFlashBackOn;
    private boolean isFromAlbum = false;
    public int index;
    DownloadManager downloadManager;

    SharedPreferences currentSongState;
    public ArraySet<String> favorites;
    public ArraySet<String> disliked;
    public ArraySet<String> neutral;
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
        Album albumSelected = null;
        if (in.getExtras() != null) {
            isFromAlbum = in.getExtras().getBoolean("albumOrigin");
            albumSelected = in.getExtras().getParcelable("songs");
            actualSongs = albumSelected.getSongs();
        }
        else {
            isFromAlbum = false;
        }


            currentSongState = getSharedPreferences("songs", MODE_PRIVATE);

            Set<String> fave = currentSongState.getStringSet("favorites", null);
            Set<String> dis = currentSongState.getStringSet("disliked", null);
            Set<String> neut = currentSongState.getStringSet("neutral", null);
            isFlashBackOn = currentSongState.getBoolean("flashback", false);

            favorites = new ArraySet<String>();
            disliked = new ArraySet<String>();
            neutral = new ArraySet<String>();

            favorites.addAll(fave);
            neutral.addAll(neut);
            disliked.addAll(dis);

            setWidgets();

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

        Switch flashback = (Switch) findViewById(R.id.flashSwitch);
        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /*
                    LinkedList<Song> songs1 = new LinkedList<Song>();
                    songs1.addAll(actualSongs);
                    FlashBackMode fbm = new FlashBackMode(songs1);
                    ArrayList<Song> newSongs = new ArrayList<Song>();
                    newSongs.addAll(fbm.createQueue());
                    launchActivity(newSongs);
                    */
                }
            }
        });

            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

            final float scale = this.getResources().getDisplayMetrics().density;
            int songId = songs.get(0).hashCode();
            int pixels = (int) (50 * scale + 0.5f);
            int textSize = (int) (10 * scale + 0.5f);
            int buttonId = songId + 1;

            //counter loop creates a new button. Attaches a 'new song' to the click listener.
        File file = Environment.getExternalStorageDirectory();
        //downloadManager.addCompletedDownload(file.getName(), file.getName(), true, "application/json", file.getAbsolutePath(),file.length(),true);
        Song song = new Song(file.getName(),file.getName().hashCode());
        actualSongs.add(song);
        songs.add(song.getTitle());
            for (index = 0; index < actualSongs.size(); index++) {
                final String fileName = actualSongs.get(index).getTitle();

                Button button = new Button(this);
                android.support.constraint.ConstraintLayout.LayoutParams params = new
                        android.support.constraint.ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, pixels);
                button.setId(songs.get(index).hashCode());
                button.setText(fileName);
                button.setBackgroundColor(Color.rgb(230, 230, 230));
                button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                button.setTextColor(Color.rgb(89, 89, 89));
                button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                button.setSingleLine(true);
                button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                button.setMarqueeRepeatLimit(1000);

                final Song newSong = actualSongs.get(index);

                System.out.println("Looped...");
                // Janice: Checking whether to play whole album or just the single song
                if (isFromAlbum) {
                    songsToPlay = actualSongs;
                    System.out.println("It is from the album");
                }

                final int songIndex = index;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!disliked.contains(fileName)) {
                            //launchActivity(newSong);
                            if (!isFromAlbum) {
                                songsToPlay = new ArrayList<Song>();
                                songsToPlay.add(actualSongs.get(songIndex));
                            }
                            launchActivity(songsToPlay);
                        }
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

    public boolean contains(String[] songs, String string) {
        for(String name: songs) {
            if (name.equals(string)) return true;
        }
        return false;
    }

    protected void buttonChange(Button button, Song song) {
        Intent intent = new Intent(this, SongListActivity.class);
        @SuppressLint("ResourceType") String songButtonName = ((Button)findViewById(button.getId() - 1)).getText().toString();
        String name = button.getText().toString();
        currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentSongState.edit();
        switch (name) {
            case "+":
                button.setText("✓");
                neutral.remove(songButtonName);
                favorites.add(songButtonName);
                song.favorite();
                break;
            case "ｘ":
                button.setText("+");
                disliked.remove(songButtonName);
                neutral.add(songButtonName);
                song.neutral();
                break;
            case "✓":
                button.setText("ｘ");
                favorites.remove(songButtonName);
                disliked.add(songButtonName);
                song.dislike();
                break;
        }

        intent.putExtra("Favorites", favorites.toArray());
        intent.putExtra("Disliked", disliked.toArray());
        intent.putExtra("Neutral", neutral.toArray());

        editor.putStringSet("favorites", favorites);
        editor.putStringSet("disliked", disliked);
        editor.putStringSet("neutral", neutral);
        editor.commit();
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
                    Toast.makeText(getApplicationContext(), "vibe mode is on", Toast.LENGTH_SHORT).show();

                } else {


                    //close event
                    isFlashBackOn = false;
                    Toast.makeText(getApplicationContext(), "vibe mode is off", Toast.LENGTH_SHORT).show();
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
