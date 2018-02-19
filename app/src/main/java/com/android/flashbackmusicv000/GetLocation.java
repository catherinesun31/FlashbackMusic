package com.android.flashbackmusicv000;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class GetLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient client;
    private Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private Location locationManager;
    private LocationRequest locationRequest;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //client = LocationServices.getFusedLocationProviderClient(this);
    }

    public GetLocation(){

    }

    public GetLocation(Context context, Bundle savedInstanceState) {
        this.context = context;
        googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission((Activity)this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity)this.context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1","ins");
        }
        else if(mMap != null) {
            Log.d("test2", "outs");
            mMap.setMyLocationEnabled(true);

        }

        locationManager = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        /*
        client.getLastLocation().addOnSuccessListener((Activity)this.context, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //logic to handle location
                if (location != null) {
                    locationManager = location;
                }
                else { locationManager = null; }
            }
        });

        client.getLastLocation().addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Failure message", e.toString());
            }
        });
        */
        return this.locationManager;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
