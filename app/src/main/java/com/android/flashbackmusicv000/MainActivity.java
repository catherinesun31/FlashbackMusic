/**
 * Main activity, backend activity file for activity_main.xml.This file represents the controller for the inputs
 * of the user. When they select either the song or album button, it will bring up the next activity
 * consisting of albums or songs. This activity allows the user options for what they want their app
 * to do.
 */
package com.android.flashbackmusicv000;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    SharedPreferences currentSongState;
    //SharedPreferences widgetState;
    Set<String> favorites;
    Set<String> disliked;
    Set<String> neutral;
    int favoritesNow;
    int dislikedNow;
    int neutralNow;
    private boolean isFlashBackOn;
    private Switch flashSwitch;
    String url;

    public static SharedPreferences flashBackState;
    Context mContext;
    private Switch switchy;

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
    public MusicStorage ms;
    public DownloadManager downloadManager;


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
        final SharedPreferences.Editor editor = currentSongState.edit();

        Set<String> fave = currentSongState.getStringSet("favorites", null);
        Set<String> dis = currentSongState.getStringSet("disliked", null);
        Set<String> neut = currentSongState.getStringSet("neutral", null);

        isFlashBackOn = currentSongState.getBoolean("flashback", false);
        Switch flashback = (Switch) findViewById(R.id.flashSwitch);
        flashback.setChecked(isFlashBackOn);

        favorites = new ArraySet<String>();
        neutral = new ArraySet<String>();
        disliked = new ArraySet<String>();

        //currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        //isFlashBackOn = currentSongState.getBoolean("flashback", false);
        setWidgets();

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
        neutral = ms.createStorage(MainActivity.this, f,d,n,favorites, disliked, neutral);
        //addStorage();
        // EditText for download link user provides
        final EditText url = (EditText) findViewById(R.id.urlinput);
        url.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //Check when user hits <enter>
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //get the link user provides
                    String getUrl = url.getText().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dataRef = database.getReference();
                    addStorage();
                    dataRef.child("URLDownload").setValue(getUrl);
                    return true;
                }
                return false;
            }
        });

        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LinkedList<Song> songs = new LinkedList<Song>();
                    songs.addAll(ms.getAlbumStorage().allSongs.getSongs());
                    FlashBackMode fbm = new FlashBackMode(songs);
                    ArrayList<Song> newSongs = new ArrayList<Song>();
                    //newSongs.addAll(fbm.createQueue());
                    editor.putBoolean("flashback", true);
                    editor.commit();
                    launchNowPlaying(ms.getAlbumStorage().allSongs.getSongs());
                }
                else{
                    editor.putBoolean("flashback", false);
                    editor.commit();
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
                if(ms.getAlbumStorage().allSongs == null){
                    System.out.println("HELLO");
                }
                launchSongs(ms.getAlbumStorage().allSongs);

            }
        });

        Button albumList = (Button) findViewById(R.id.albums);
        albumList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbums(ms.getAlbumStorage().getAlbums());
            }
        });

        //close event
        //isFlashBackOn = false;
        //Toast.makeText(getApplicationContext(), "vibe mode is off", Toast.LENGTH_SHORT).show();
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
        setWidgets();
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
     * launchAlbums: launches the AlbumsListActivity with all of the albums we have
     * @params: albums - the list of albums the user currently has
     * @return: void
     */
    public void launchAlbums(ArrayList<Album> albums) {
        Intent albumsIntent  = new Intent(this, AlbumQueue.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",albums);
        albumsIntent.putExtra("BUNDLE",args);
        albumsIntent.putExtra("isOn", isFlashBackOn);
        startActivity(albumsIntent);
    }

    /* @param: list of songs to check for vibe mode
     *
     * launchNowPlaying launches the SongPlayingActivity using our vibe mode settings
     */
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

    public String getLocation() {
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

    /* addStorage downloads an mp3 file from a given url
     */
    public void addStorage(){
        System.out.println("Add that storage");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("URLDownload");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                url = dataSnapshot.getValue().toString();
                System.err.println("INSIDE HERE");
                //gets the path to phone's Downloads folder
                String downloadRoute = Environment.getExternalStorageDirectory().toString();
                //change url from string to Uri
                Uri music_uri = Uri.parse(url);
                DownloadData(music_uri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            /*@Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.err.println("INSIDE HERE");
                //gets the path to phone's Downloads folder
                String downloadRoute = Environment.getExternalStorageDirectory().toString();
                //change url from string to Uri
                Uri music_uri = Uri.parse(url);
                DownloadData(music_uri);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                url = dataSnapshot.getValue().toString();
                System.err.println("INSIDE HERE2");
                //gets the path to phone's Downloads folder
                String downloadRoute = Environment.getExternalStorageDirectory().toString();
                //change url from string to Uri
                Uri music_uri = Uri.parse(url);
                DownloadData(music_uri);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}*/
        });

    }

    private boolean DownloadStatus(Cursor cursor, DownloadManager dm) {
        DownloadManager.Query query = null;
        Cursor c = null;
        query = new DownloadManager.Query();
        String statusText = "";

        if (query != null) {
            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL |
                    DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
        } else {
            return false;
        }
        c = dm.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    statusText = "STATUS_PAUSED";
                    break;
                case DownloadManager.STATUS_PENDING:
                    statusText = "STATUS_PENDING";
                    break;
                case DownloadManager.STATUS_RUNNING:
                    statusText = "STATUS_RUNNING";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    statusText = "STATUS_SUCCESSFUL";
                    break;
                case DownloadManager.STATUS_FAILED:
                    statusText = "STATUS_FAILED";
                    break;
            }
        }
        /*if (cursor != null && cursor.getColumnIndex(dm.COLUMN_STATUS) > 0 && cursor.getColumnIndex(dm.COLUMN_REASON) > 0) {


            //column for download  status
            int columnIndex = cursor.getColumnIndex(dm.COLUMN_STATUS);
            if (cursor.getInt(columnIndex) > 0) {
                int status = cursor.getInt(columnIndex);
                //column for reason code if the download failed or paused
                int columnReason = cursor.getColumnIndex(dm.COLUMN_REASON);
                int reason = cursor.getInt(columnReason);
                //get the download filename
                int filenameIndex = cursor.getColumnIndex(dm.COLUMN_LOCAL_FILENAME);
                //String status1 = dm.COLUMN_STATUS;
                //int status =
                String filename = cursor.getString(filenameIndex);

                String statusText = "";
                String reasonText = "";

                switch (status) {
                    case DownloadManager.STATUS_FAILED:
                        statusText = "STATUS_FAILED";
                        break;
                    case DownloadManager.STATUS_PAUSED:
                        statusText = "STATUS_PAUSED";
                        break;
                    case DownloadManager.STATUS_PENDING:
                        statusText = "STATUS_PENDING";
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        statusText = "STATUS_RUNNING";
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        statusText = "STATUS_SUCCESSFUL";
                        break;
                }

                if (statusText == "STATUS_SUCCESSFUL") {
                    return true;
                } else {

                    // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 3000);

                    return false;
                }
            }
        }*/
        if (statusText == "STATUS_SUCCESSFUL") {
            return true;
        }

        // Make a delay of 3 seconds so that next toast (Music Status) will not merge with this one.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);

        return false;
    }

    /* DownloadData downloads the desired mp3 from the given url. Does checks on whether it has
     * completely downloaded
     *
     * DEPENDS ON EMULATOR for the request.setDestinationInExternalPublicDir(Environ,getExternal...
     *      for Nexus 4, just replace the whole first parameter with Environment.DIRECTORY_DOWNLOADS
     *
     * @param uri - the url for the mp3 we want
     */
    public long DownloadData (Uri uri) {

        long downloadReference;

        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Downloading Song");

        //Setting description of request
        request.setDescription("Downloading Song from URL");

        //Set local destination for downloaded file to path in application's external files directory
        // This puts it into storage/emulated/0/Download

        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , "Download.mp3");
        request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() , "Download.mp3");
        //Enqueue download and save into referenceId
        System.out.println("HMMMMMMMMMMMMMMMMMMMMMMMMMM");
        downloadReference = downloadManager.enqueue(request);

        // Calling our Download Status
        DownloadManager.Query MusicDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        MusicDownloadQuery.setFilterById(downloadReference);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(MusicDownloadQuery);
        boolean status = DownloadStatus(cursor, downloadManager);

        while(!status){
            if(cursor.moveToFirst()) {
                status = DownloadStatus(cursor, downloadManager);
            }
        }
        if(status){
            ms.addNewDownload(this, Environment.getExternalStorageDirectory().toString() +
                    "/storage/emulated/0/Download/Download.mp3", favorites, disliked, neutral);
        }

        return downloadReference;
    }

    private void setWidgets() {
        switchy = (Switch) findViewById(R.id.flashSwitch);
        isFlashBackOn = currentSongState.getBoolean("flashback", false);
        switchy.setChecked(isFlashBackOn);
    }
}

