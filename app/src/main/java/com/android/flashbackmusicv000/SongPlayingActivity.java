package com.android.flashbackmusicv000;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SongPlayingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MediaPlayer mediaPlayer;

    private Intent intent;
    private boolean isFlashBackOn;
    private Switch switchy;

    private FusedLocationProviderClient mFusedLocationClient;


    private Location locationManager;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent i = getIntent();
        setWidgets();
        //bug could be here.... something to do with the intents....



        //song being passed a parcelable, through the intent... but no parcelable was sent...???

        Song song = (Song) i.getParcelableExtra("name_of_extra");

        TextView songTitle = (TextView) findViewById(R.id.songtitle);
        songTitle.setText(song.getTitle());

        final int nameInt = song.getSongId();
        loadMedia(nameInt);
        mediaPlayer.start();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1", "ins");
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //logic to handle location
                    if (location != null) {
                        locationManager = location;
                        Log.d("Current Location", "Longitude: " + locationManager.getLongitude() + "\n"
                                + "Latitude: " + locationManager.getLatitude());
                    }
                }
            });
        }
    }

    public void loadMedia(int resourceId){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(resourceId);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }

    private void setWidgets(){

        intent = getIntent();
        switchy = (Switch) findViewById(R.id.flashSwitch);
        isFlashBackOn = intent.getBooleanExtra("isOn",isFlashBackOn);

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

    /*
    public static String printIntent(Intent intent){


        if (intent == null) {

            return null;

        }

        return intent.toString() + " " + bundleToString(intent.getExtras());


    }
    */

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.release();
    }

}
