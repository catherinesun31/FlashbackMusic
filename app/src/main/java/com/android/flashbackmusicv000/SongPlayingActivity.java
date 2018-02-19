package com.android.flashbackmusicv000;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import android.content.ServiceConnection;
import android.content.Context;
import android.content.ComponentName;
import android.os.IBinder;

public class SongPlayingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MediaPlayer mediaPlayer;

    private Intent intent;
    private boolean isFlashBackOn;
    private Switch switchy;

    private FusedLocationProviderClient mFusedLocationClient;
    private AddressResultReceiver mResultReceiver;
    private Location locationManager;


    private boolean mIsBound = false;
    private MusicBackgroundService mServ;
    private Intent toMusicServiceIntent;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //do nothing
    }


    //Karla add in
    private ServiceConnection SCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //mServ = ((MusicBackgroundService.ServiceBinder)iBinder).getService();
            //MusicService.ServiceBinder binder = (MusicService.ServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServ = null;
        }
    };
    void doBindService(){
        bindService(new Intent(this, MusicBackgroundService.class), SCon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    void doUnbindService(){
        if (mIsBound) {

            unbindService(SCon);
            mIsBound = false;
        }
    }
    //end Karla add in


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playing);

        Intent i = getIntent();
        setWidgets();
        //bug could be here.... something to do with the intents....


        //song being passed a parcelable, through the intent... but no parcelable was sent...???

        final Song song = (Song) i.getParcelableExtra("name_of_extra");

        //temporary, will restore
        //startMusicService(song);
        sendIntentToMusicService();


        TextView songTitle = (TextView) findViewById(R.id.songtitle);
        songTitle.setText(song.getTitle());

        final int nameInt = song.getSongId();
        loadMedia(nameInt);
        //THE MEDIAPLAYER BUTTON
        mediaPlayer.start();
        getLocation(savedInstanceState);
        song.setTime(getTime());
        song.setDate(getDate());
        song.setDay(getDay());

        TextView songTime = (TextView) findViewById(R.id.song_time);
        String time = song.getLastDay() + " " + song.getLastDate() + " " + song.getLastTime();
        songTime.setText(time);

        TextView songLocation = (TextView) findViewById(R.id.song_location);
        String location = song.getLocation();
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
    }

    private void sendIntentToMusicService(){

        Intent toMusicService = new Intent(this,MusicBackgroundService.class);
        intent.putExtra("message", "toMusicService");
        startService(intent);

    }

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
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

    private void getLocation(Bundle savedInstanceState) {

        //Get the user's location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1", "ins");
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //logic to handle location
                if (location != null) {
                    locationManager = location;
                    Log.d("Current Location", "Longitude: " + locationManager.getLongitude() + "\n"
                            + "Latitude: " + locationManager.getLatitude());
                    //Add the location to the song's last-played location
                    String cityName=null;
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.US);
                    List<Address> addresses;
                    String StreetName = "";
                    double longitude = locationManager.getLongitude();
                    double latitude = locationManager.getLatitude();
                    try {
                        addresses = gcd.getFromLocation(latitude, longitude, 1);
                        if (addresses.size() > 0) StreetName = addresses.get(0).getThoroughfare();
                        String s = longitude + "\n" + latitude +
                                "\n\nMy Currrent Street is: " + StreetName;
                        Log.d("Location", s);
                    }
                    catch (Exception e) {
                        Log.d("Exception", "There was an error with the Geocoder");
                    }
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

    /**
     * sets the flashback switch
     */
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

    /**
     * method starting a background service to keep music playing on a seperqte thread
     * @param song
     */
    private void startMusicService(Song song){

        toMusicServiceIntent = new Intent();
        toMusicServiceIntent.setClass(this, MusicBackgroundService.class);
        toMusicServiceIntent.putExtra("song", song);
        startService(toMusicServiceIntent);

    }

    /*
    public static String printIntent(Intent intent){
        if (intent == null) {
            return null;
        }
        return intent.toString() + " " + bundleToString(intent.getExtras());
    }
    */

    /**
     * Tells activity to save the flash back switch state and finish of when popping from the activity stack.
     */
    @Override
    public void onBackPressed(){

        //super.onBackPressed();
        //sharedPreferences switch state.

        SharedPreferences.Editor editor = MainActivity.flashBackState.edit();
        editor.putBoolean("isOn", isFlashBackOn);

        editor.apply();
        finish();

    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, locationManager);
        startService(intent);
    }


    /*protected void startMusicBackgroundService(){
        Intent music = new Intent();
        music.setClass(this, MusicBackgroundSerivce.class);
        startService(music);
    }*/


    class AddressResultReceiver extends ResultReceiver {
        String mAddressOutput;
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {

            }

        }
    }


}
