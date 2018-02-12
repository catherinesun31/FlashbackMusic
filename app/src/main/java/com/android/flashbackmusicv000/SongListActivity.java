package com.android.flashbackmusicv000;

import android.content.Intent;
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
import java.lang.reflect.Field;

public class SongListActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private static int[] MEDIA_RES_IDS;
    int totalSongs = getNumberOfSongs();

    //MEDIA_RES_IDS = new int[totalSongs];



    // com.android.flashbackmusicv000.Song Instances
    //Song song1 = new Song(int R.raw.a01_everything_i_love);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int totalSongs = getNumberOfSongs();
        MEDIA_RES_IDS = new int[totalSongs];

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

        song1B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song2B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song3B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song4B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song5B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song6B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song7B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song8B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song9B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });
        song10B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity();
            }
        });


        final Button favorited1 = (Button) findViewById(R.id.button1);
        final Button favorited2 = (Button) findViewById(R.id.button2);
        final Button favorited3 = (Button) findViewById(R.id.button3);
        final Button favorited4 = (Button) findViewById(R.id.button4);
        final Button favorited5 = (Button) findViewById(R.id.button5);
        final Button favorited6 = (Button) findViewById(R.id.button6);
        final Button favorited7 = (Button) findViewById(R.id.button7);
        final Button favorited8 = (Button) findViewById(R.id.button8);
        final Button favorited9 = (Button) findViewById(R.id.button9);
        final Button favorited10 = (Button) findViewById(R.id.button10);

        favorited1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited1);
            }
        });
        favorited2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited2);
            }
        });
        favorited3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited3);
            }
        });
        favorited4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited4);
            }
        });
        favorited5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited5);
            }
        });
        favorited6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited6);
            }
        });
        favorited7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited7);
            }
        });
        favorited8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited8);
            }
        });
        favorited9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited9);
            }
        });
        favorited10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonChange(favorited10);
            }
        });}

    public void buttonChange(Button button) {
        button.setText("✓");
    }





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
