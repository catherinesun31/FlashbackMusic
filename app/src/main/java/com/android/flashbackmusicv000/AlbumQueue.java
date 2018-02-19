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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.flashbackmusicv000.Song;

import java.io.File;
import java.util.ArrayList;


/* AlbumQueue is an Activity that displays the list of albums
 * Previous Activity:   MainActivity
 * Next Activity:       TODO The list of songs in the album selected
 */
public class AlbumQueue extends AppCompatActivity {

    private Intent mainIntent;
    private boolean isFlashBackOn;
    private Switch switchy;

    private ArrayList<Album> albums;

    /* onCreate makes all the buttons for each album in our raw files
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_queue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set intent items
        setSentItems();


        // may need to consider an instance where the number of albums are unknown.

        Button album1 = (Button) findViewById(R.id.album1);
        Button album2 = (Button) findViewById(R.id.album2);
        Button album3 = (Button) findViewById(R.id.album3);
        Button album4 = (Button) findViewById(R.id.album4);
        Button album5 = (Button) findViewById(R.id.album5);

        //get songs to load... and


        album1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity(albums.get(0));
            }
        });
        album2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity(albums.get(1));
            }
        });
        album3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity(albums.get(2));
            }
        });
        album4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity(albums.get(3));
            }
        });
        album5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchActivity(albums.get(4));
            }
        });

        setSwitch();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /* launchActivity launches the album's song list activity */
    public void launchActivity(Album songs){

        //get all the songs from the album and add them to the intent...???


        //Toast.makeText(getApplicationContext(), "making intents", Toast.LENGTH_SHORT).show();
        Intent toSongListIntent = new Intent(this, SongListActivity.class);

        //songs are parcelable
        toSongListIntent.putExtra("songs",songs);
        //temporary
        toSongListIntent.putExtra("albumOrigin",true);

        //for loop.... loook at the class.


        //Toast.makeText(getApplicationContext(), "launching song playing activity", Toast.LENGTH_SHORT).show();
        startActivity(toSongListIntent);
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

    private void setSentItems(){

        mainIntent = this.getIntent();
        Bundle args = mainIntent.getBundleExtra("BUNDLE");
        albums = (ArrayList<Album>) args.getSerializable("ARRAYLIST");
        isFlashBackOn = mainIntent.getBooleanExtra("isOn",isFlashBackOn);

    }
    //TODO clean up code.
    private void setButtons(){



    }
    private void setSwitch(){

        mainIntent = getIntent();
        switchy = (Switch) findViewById(R.id.flashSwitch);

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
}
