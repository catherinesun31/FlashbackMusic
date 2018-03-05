/**
 * Main activity, backend activity file for activity_main.xml.This file represents the controller for the inputs
 * of the user. When they select either the song or album button, it will bring up the next activity
 * consisting of albums or songs. This activity allows the user options for what they want their app
 * to do.
 */
package com.android.flashbackmusicv000;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View.OnKeyListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    SharedPreferences currentSongState;
    //SharedPreferences widgetState;
    Set<String> favorites;
    Set<String> disliked;
    Set<String> neutral;
    int favoritesNow;
    int dislikedNow;
    int neutralNow;
    public static MediaPlayer mediaPlayer;
    private boolean isFlashBackOn;
    private Switch flashSwitch;
    public static SharedPreferences flashBackState;


    ArrayList<Song> songs1;

    private Album allSongs;

    //albums need to be passed...
    //ArrayList<Album> albums;
    Context mContext;

//albums need to be passed...


    private FusedLocationProviderClient mFusedLocationClient;
    private Location locationManager;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    final String REQUESTING_LOCATION_UPDATES_KEY = "Requesting Location Updates";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private SongPlayingActivity.AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    private ArrayList<Album> albums;
    private MusicStorage ms;

    FirebaseDatabase database;
    DatabaseReference dataRef;


    /**
     * onCreate Method represents the beginning state of the main activity whenever it is started.
     *
     * @param savedInstanceState is the previous state of this activity represented from the Bundle
     *                           object.
     *                           The current song list is loaded from the shared preference across the entire app, restoring the
     *                           songslist from when it was last played.                          .
     *                           If none of the the string sets obtained from the shared preferences have empty data,
     *                           then we know that there was an album. Then the songslist gets updated.
     *                           Anonymous listeners are then set for each of the buttons on activity_main.xml.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("In: ", "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create a shared preference for flashback service state.

        favoritesNow = 0;
        dislikedNow = 0;
        neutralNow = 0;

        currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        SharedPreferences.Editor editor = currentSongState.edit();

        Set<String> fave = currentSongState.getStringSet("favorites", null);
        Set<String> dis = currentSongState.getStringSet("disliked", null);
        Set<String> neut = currentSongState.getStringSet("neutral", null);
        isFlashBackOn = currentSongState.getBoolean("flashback", false);


        favorites = new ArraySet<String>();
        neutral = new ArraySet<String>();
        disliked = new ArraySet<String>();

        final EditText url = (EditText) findViewById(R.id.urlinput);
        url.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String getUrl = url.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dataRef = database.getReference();
                    dataRef.child("URL Download").setValue(getUrl);
                    return true;
                }
                return false;
            }
        });

        Song[] songs = {};

        boolean f = false;
        if (fave != null) {
            f = true;
        }
        boolean d = false;
        if (dis != null) {
            d = true;
        }
        boolean n = false;
        if (neut != null) {
            n = true;
        }

        ms = new MusicStorage();

        //ms. getCurrentSongs();
        ms.createStorage(f,d,n,favorites, disliked, neutral);


        songs1 = ms.getSongStorage().songsList;

        Switch flashback = (Switch) findViewById(R.id.flashSwitch);
        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LinkedList<Song> songs = new LinkedList<Song>();
                    songs.addAll(allSongs.getSongs());
                    FlashBackMode fbm = new FlashBackMode(songs);
                    ArrayList<Song> newSongs = new ArrayList<Song>();
                    //newSongs.addAll(fbm.createQueue());
                    launchNowPlaying(allSongs.getSongs());
                }
            }
        });


        //Add list of favorited/disliked/neutral songs to shared preferences
        editor.putStringSet("favorites", favorites);
        editor.putStringSet("disliked", disliked);
        editor.putStringSet("neutral", neutral);
        editor.commit();

        //Set onClickListener for songs button
        Button songsList = (Button) findViewById(R.id.songs);
        songsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSongs(allSongs);
            }
        });

        Button albumList = (Button) findViewById(R.id.albums);
        albumList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbums();
            }
        });

        setSwitch();

        //close event
        isFlashBackOn = false;
        Toast.makeText(getApplicationContext(), "flashback mode is off", Toast.LENGTH_SHORT).show();
        //

        /*
         * I'm thinking that here, we should make a list of all of the Song objects from songs that
         * the user has in their R.raw file, and store it in the phone's shared preferences.
         */

        //Create the IntentService to automatically update the user's location every minute or so
        //mResultReceiver = new SongPlayingActivity.AddressResultReceiver(new Handler());
        mLocationCallback = new LocationCallback();
        mContext = this;
    }


    @Override
    protected void onStart() {
        Log.i("In: ", "MainActivity.onStart");
        super.onStart();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        Log.i("In: ", "MainActivity.onStop");
        super.onStop();
        try {

        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("In: ", "MainActivity.onSaveInstanceState");

        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("In: ", "MainActivity.onMapReady");

        Log.d("MAP LOCATION", "OnMapReady");
        mMap = googleMap;

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("In: ", "MainActivity.onLocationChanged");

        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*
     * launchSongs:
     * @params: none
     * @return: void
     * The intents are created and the list of strings, with favourite titles, disliked titles, neutral titles
     * and songs are put inside.
     * This starts the SongsListActivity, and migrates to the list of all of the current songs
     */
    // JANICE EDIT 02/13: PASSING IN THE ARRAY OF SONGS SO WE CAN PASS THROUGH TO SONGSLIST AND SONGSPLAYING

    public void launchSongs(Album allSongs) {

        //strings to be sent in an activity towards the SongListActivity

        Intent toSongListIntent = new Intent(this, SongListActivity.class);


        //All songs.....
        toSongListIntent.putExtra("songs",allSongs);
        toSongListIntent.putExtra("isFromAlbum", false);
        //temporary


        toSongListIntent.putExtra("isOn", isFlashBackOn);
        //temporary, whilst passing strings.
        // need to change this...
        startActivity(toSongListIntent);
    }

    /*
     * launchAlbums:
     */
    public void launchAlbums() {

        Intent albumsIntent  = new Intent(this, AlbumQueue.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",albums);
        albumsIntent.putExtra("BUNDLE",args);
        albumsIntent.putExtra("isOn", isFlashBackOn);
        startActivity(albumsIntent);

    }

    public void launchNowPlaying(ArrayList<Song> songs) {
        Intent intent = new Intent(this, SongPlayingActivity.class);

        intent.putExtra("name_of_extra", songs);
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





    /**
     *
     * @param item
     * @return boolean
     */
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


    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput(String mAddressOutput, String mAreaOutput, String mCityOutput, String mStateOutput) {
        Log.i("In: ", "SongPlayingActivity.displayAddressOutput");

        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAddressOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                Log.d("Area", mAddressOutput);
            //mLocationText.setText(mAreaOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startIntentService(Location location) {
        Log.i("In: ", "SongPlayingActivity.startIntentService");

        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationManager);
        startService(intent);
    }

    private String getLocation() {
        Log.i("In: ", "SongPlayingActivity.getLocation");

        String address = "";

        //Ask for location permissions
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }

        //Get the user's location
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //logic to handle location
                        if (location != null) {
                            locationManager = location;

                            Log.d("Current Location", "Longitude: " + locationManager.getLongitude() + "\n"
                                    + "Latitude: " + locationManager.getLatitude());

                            //Get the location as an address

                            //Log.d("Address: ", mAddressOutput);
                        }
                    }
                });
        return address;

    }

    private void setSwitch(){

        flashSwitch = (Switch) findViewById(R.id.flashSwitch);
        flashBackState = getApplicationContext().getSharedPreferences("isOn", MODE_PRIVATE);

        /*
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });*/

        /*
         * I'm thinking that here, we should make a list of all of the Song objects from songs that
         * the user has in their R.raw file, and store it in the phone's shared preferences.
         */

    }


    @Override
    public void onRestart(){

        super.onRestart();

        isFlashBackOn = MainActivity.flashBackState.getBoolean("isOn", isFlashBackOn);

        flashSwitch.setChecked(isFlashBackOn);


    }
}

