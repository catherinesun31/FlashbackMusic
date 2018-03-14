package com.android.flashbackmusicv000;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SongPlayingActivity extends AppCompatActivity implements
        OnMapReadyCallback {
    private ArrayList<Song> songList;
    Context mContext;

    private Intent intent;
    private boolean isFlashBackOn;
    private Switch switchy;
    public static MediaPlayer mediaPlayer;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location locationManager;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    final String REQUESTING_LOCATION_UPDATES_KEY = "Requesting Location Updates";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private AddressResultReceiver mResultReceiver;

    private int songIndex = 0;
    private SharedPreferences currentSongState;


    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onDestory(mediaPlayer);
        Log.i("In: ", "SongPlayingActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentSongState = getSharedPreferences("songs", MODE_PRIVATE);
        isFlashBackOn = currentSongState.getBoolean("flashback", false);
        Intent i = getIntent();


        songList = i.getParcelableArrayListExtra("name_of_extra");
        final Song song = songList.get(songIndex);

        /*
        Switch flashback = (Switch) findViewById(R.id.flashSwitch);
        flashback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinkedList<Song> songs = new LinkedList<Song>();
                songs.addAll(songList);
                FlashBackMode mode = new FlashBackMode(songs);
                mode.createQueue();
            }
        });
        */
        final SharedPreferences.Editor editor = currentSongState.edit();
        Switch flashback = (Switch) findViewById(R.id.flashSwitch);
        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //LinkedList<Song> songs = new LinkedList<Song>();
                    //songs.addAll(ms.getAlbumStorage().allSongs.getSongs());
                    //FlashBackMode fbm = new FlashBackMode(songs);
                    //ArrayList<Song> newSongs = new ArrayList<Song>();
                    //newSongs.addAll(fbm.createQueue());
                    editor.putBoolean("flashback", true);
                    editor.commit();
                }
                else{
                    editor.putBoolean("flashback", false);
                    editor.commit();
                }
            }
        });

        Button queue = findViewById(R.id.queue);
        queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView songTitle = (TextView) findViewById(R.id.songtitle);
        songTitle.setText(song.getTitle());

        if( songList.get(songIndex).getSongId() > 0)
            mediaPlayer = MediaPlayer.create(this, songList.get(songIndex).getSongId());
        else {
            try {
                mediaPlayer = new MediaPlayer();
                loadDownload(mediaPlayer,  songList.get(songIndex).getFileLocation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MediaPlayer tempPlayer = mediaPlayer;
        //getLocation(savedInstanceState);

        for(++songIndex ; songIndex < songList.size(); songIndex++){
            final Song song1 = songList.get(songIndex);
            MediaPlayer nextPlayer = new MediaPlayer();
            if(songList.get(songIndex).getSongId() > 0)
                nextPlayer = MediaPlayer.create(this, songList.get(songIndex).getSongId());
            else{
                try {
                    nextPlayer.setDataSource(songList.get(songIndex).getFileLocation());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            nextPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    System.out.println("Supposed to change song info");
                    //getLocation(savedInstanceState);
                    song1.setTime(getTime());
                    song1.setDate(getDate());
                    song1.setDay(getDay());

                    TextView songTime = (TextView) findViewById(R.id.song_time);
                    String time = song1.getLastDay() + " " + song1.getLastDate() + " " + song1.getLastTime();
                    songTime.setText(time);

                    TextView songLocation = (TextView) findViewById(R.id.song_location);
                    String location = song1.getLocation();
                    songLocation.setText(location);

                    final Button statusButton = (Button) findViewById(R.id.status);
                    boolean favorited = song1.isFavorite();
                    if (favorited) {
                        //song is favorited
                        statusButton.setText("✓");
                    }
                    statusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (song1.isNeutral()) {
                                song1.favorite();
                                statusButton.setText("✓");
                            }
                            if (song1.isFavorite()) {
                                song1.dislike();
                                //skip the song here
                            }
                        }
                    });
                }

            });
            tempPlayer.setNextMediaPlayer(nextPlayer);
            tempPlayer = nextPlayer;
        }
        mediaPlayer.start();

        //getLocation(savedInstanceState);

        song.setTime(getTime());
        song.setDate(getDate());
        song.setDay(getDay());
        song.setFullDate(getFullDate());

        mLocationCallback = new LocationCallback();
        song.setLocation(getLocation());

        TextView songTime = (TextView) findViewById(R.id.song_time);
        String time = song.getLastDay() + " " + song.getLastDate() + " " + song.getLastTime();
        songTime.setText(time);

        TextView songLocation = (TextView) findViewById(R.id.song_location);
        String location = song.getLocation();
        Log.d("Location", getLocation());
        songLocation.setText(location);

        final Button statusButton = (Button) findViewById(R.id.status);
        boolean favorited = song.isFavorite();
        if (favorited) {
            //song is favorited
            statusButton.setText("✓");
        }

        statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (song.isNeutral()) {
                        song.favorite();
                        statusButton.setText("✓");
                    }
                    if (song.isFavorite()) {
                        song.dislike();
                        //skip the song here
                    }
                }
        });
        mResultReceiver = new AddressResultReceiver(new Handler());
        songLocation.setText(mAddressOutput);
    }

    @Override
    protected void onStop() {
        Log.i("In: ", "SongPlayingActivity.onStop");
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
    public void onMapReady(GoogleMap googleMap) {
        Log.i("In: ", "SongPlayingActivity.onMapReady");

        Log.d("MAP LOCATION", "OnMapReady");
        mMap = googleMap;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("In: ", "SongPlayingActivity.onSaveInstanceState");

        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }

    private void changeMap(Location location) {
        Log.i("In: ", "SongPlayingActivity.changeMap");

        Log.d("MAP LOCATION", "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    private Date getFullDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        return currentTime;
    }

    //TODO (if enough time): change this to a Time class, so that it's SRP (but also who cares)
    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        Time time = new Time(currentTime.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return dateFormat.format(currentTime);
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return dateFormat.format(currentTime);
    }

    private String getDay() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return dateFormat.format(currentTime);
    }

    //TODO (if enough time): change this to a Location class, so that it's SRP (but also who cares)
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
                            startIntentService(locationManager);
                        }
                    }
                });
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
        return address;
    }


    protected void startIntentService(Location location) {
        Log.i("In: ", "SongPlayingActivity.startIntentService");

        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationManager);
        startService(intent);
    }

    protected void onDestory(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void loadMedia(MediaPlayer mediaPlayer, int index){
        Log.i("In: ", "SongPlayingActivity.loadMedia");

        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }

        System.out.println("Loading media...");

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(songList.get(index).getSongId());
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
            //songIndex++;
            //MediaPlayer nextPlayer = new MediaPlayer();
            //loadMedia(nextPlayer);
            //mediaPlayer.setNextMediaPlayer(nextPlayer);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    /*
     * Loads a downloaded song onto the mediaPlayer and starts playing
     */
    public void loadDownload(MediaPlayer mediaPlayer, String url) throws Exception{
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private void setWidgets(){
        switchy = (Switch) findViewById(R.id.flashSwitch);
        isFlashBackOn = currentSongState.getBoolean("flashback", false);
        switchy.setChecked(isFlashBackOn);
    }

    /*
    public static String printIntent(Intent intent){
        if (intent == null) {
            return null;
        }
        return intent.toString() + " " + bundleToString(intent.getExtras());
    }
    */


    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationManager);
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
        }
    }

    public void displayAddressOutput() {
        Log.d("address", mAddressOutput);
        TextView location = findViewById(R.id.song_location);
        location.setText(mAddressOutput);
    }

}
