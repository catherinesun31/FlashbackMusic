package com.android.flashbackmusicv000;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class VibeMode {
    ArrayList<Song> vibeQueue;
    ArrayList<Song> unsorted;
    DatabaseReference ref;
    ArrayList<HashMap<String, Object>> songs = new ArrayList<>();
    Location currentLocation;


    public VibeMode(Location location) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        currentLocation = location;
        ref = database.getReference("Locations");
        unsorted = new ArrayList<Song>();
        getInstance(new FirebaseCallback() {
            @Override
            public void onCallback(HashMap<String, Object> hm) {
                songs.add(hm);

            }
        });
        //vibeQueue = new ArrayList<Song>();
        //createQueue();
    }

    private void getInstance(FirebaseCallback callback) {
        final FirebaseCallback cb = callback;
        //TODO: get all usernames from Firebase, add them to a list
        ref.addChildEventListener(new ChildEventListener() {
            //queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    String location = (String)dataSnapshot.getValue();
                    String[] coordinates = location.split(",");
                    int latitude = Integer.parseInt(coordinates[0]);
                    int longitude = Integer.parseInt(coordinates[1]);
                    Location loc = new Location("");
                    loc.setLatitude(latitude);
                    loc.setLongitude(longitude);
                    HashMap<String, Object> hashMap = new HashMap<>();

                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        hashMap.put(child.getKey(), child.getValue());
                    }
                    hashMap.put("Location", loc);

                    /*ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    String username = (String)dataSnapshot.getKey();
                    String val = (String)dataSnapshot.getValue();
                    ArrayList<String> pair = new ArrayList<>(2);
                    pair.add(0, username);
                    pair.add(1, val);*/
                    cb.onCallback(hashMap);
                    //dataList.add(pair);
                }
                else {
                    Log.e("Error", "No children");
                    //list will still be null
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "Cancelled");
            }
        });

    }


    public void createQueue() {
        int total = 0;
        for (HashMap<String, Object> hm: songs) {
            Location location = (Location)hm.get("Location");
            float[] result = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    currentLocation.getLatitude(), currentLocation.getLongitude(),result);
            boolean isClose = result[0] < 1000;
            if (isClose) {
                total++;
            }
        }
        /*vibeQueue.add(unsorted.get(0));
        for (int i = 1; i < unsorted.size(); ++i) {
            for (int j = 0; j < vibeQueue.size(); ++j) {
                Song compare = vibeQueue.get(j);
                Song current = unsorted.get(i);
                int compareTotal = getScore(compare);
                int currentTotal = getScore(current);
                if (compareTotal == currentTotal) {
                    if (compare.isFavorite() && !current.isFavorite()) {
                        vibeQueue.add(j+1, current);
                    }
                    else if (!compare.isFavorite() && current.isFavorite()) {
                        vibeQueue.add(j, current);
                    }
                    else if (compare.isFavorite() && current.isFavorite()) {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            vibeQueue.add(j+1, current);
                        }
                        else {
                            vibeQueue.add(j, current);
                        }
                    }
                    else {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            vibeQueue.add(j+1, current);
                        }
                        else {
                            vibeQueue.add(j, current);
                        }
                    }
                }
                else {
                    if (compareTotal > currentTotal) {
                        vibeQueue.add(j+1, current);
                    }
                    else {
                        vibeQueue.add(j, current);
                    }
                }
            }
        }
        //Make sure that no song that wasn't played at a previous time/day/place, or is disliked,
        //will be played in flashback mode
        for (Song song: vibeQueue) {
            if (getScore(song) == 0) vibeQueue.remove(song);
            if (song.isDislike()) vibeQueue.remove(song);
        }
        return vibeQueue;*/
    }

    public void updateLocation(Location location) {

    }

    public int getScore(Song song) {
        int total = 0;

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        if (dateFormat.format(currentTime).equals(song.getLastDay())) ++total;

        int time = 0;
        dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String hour = dateFormat.format(currentTime).substring(0, 2);
        String lastTime = "";
        if (Integer.parseInt(hour) >= 5 && Integer.parseInt(hour) < 11) {
            lastTime = "Morning";
        }
        else if (Integer.parseInt(hour) >= 11 && Integer.parseInt(hour) < 17) {
            lastTime = "Afternoon";
        }
        else {
            lastTime = "Night";
        }
        if (lastTime.equals(song.getLastTimeOfDay())) ++total;

        //if the location is within 1000 feet

        return total;
    }
}
