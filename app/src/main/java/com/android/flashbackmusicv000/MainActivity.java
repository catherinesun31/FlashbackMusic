package com.android.flashbackmusicv000;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set onClickListener for songs button
        Button songsList = (Button) findViewById(R.id.songs);
        songsList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                launchSongs();
            }
        });


       Button albumList = (Button) findViewById(R.id.albums);

        albumList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchAlbums();
            }
        });
    }

    /*
     * launchSongs:
     * @params: none
     * @return: void
     *
     * This starts the SongsListActivity, and migrates to the list of all of the current songs
     */
    public void launchSongs() {
        Intent intent = new Intent(this, SongListActivity.class);
        startActivity(intent);
    }

    /*
     * launchAlbums:
     */
    public void launchAlbums() {
        Intent albums  = new Intent(this, AlbumQueue.class);
        startActivity(albums);

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
