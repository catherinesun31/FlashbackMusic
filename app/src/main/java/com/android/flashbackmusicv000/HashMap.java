package com.android.flashbackmusicv000;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HashMap {
    private ArrayList<ArrayList<String>> list;
    private DatabaseReference ref;
    private static final String TAG = "HASH ERROR";
    private static final String ANON = "anonymous_users";

    HashMap() {
        //Check that the hash map has not already been instantiated on Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference(ANON);
        getInstance(new FirebaseCallback() {
            @Override
            public void onCallback(String value) {
                Log.d(TAG, value);
            }
        });
        //ref.child("Anonymous").setValue(true);

        //If it has not, create a new hash map
        if (list == null) {
            String val = "0";
            ArrayList<String> fruits = createFruits();
            list = new ArrayList<>();
            for (String fruit : fruits) {
                ArrayList<String> fruitPair = new ArrayList<String>();
                fruitPair.add(0, fruit);
                fruitPair.add(1, val);
                list.add(fruitPair);
            }
            update();
        }
    }

    public ArrayList<ArrayList<String>> getList() {
        return list;
    }

    //TODO: Test Hash functionality
    public String hash(int userVal) {
        int val = userVal%list.size();
        //hash to that index, if it has not already been used
        ArrayList<String> hashed = list.get(val);
        String hashValue = hashed.get(1);
        if (hashValue.equals("0")) {
            hashed.set(1, "1");
            update();
            return hashed.get(0);
        }
        //if it has been used, go through all succeeding indices until one has
        //not been used. If they have all been used, print an error.
        else {
            int count = 0;
            while (hashValue.equals("1")) {
                ++val;
                //return to the beginning if reached the end of the list
                if (val == list.size()) {
                    val = 0;
                    ++count;
                    if (count > 5) {
                        Log.e(TAG, "Iterated over the list more than 5 times." +
                                "Check for error.");
                        return "Default Fruit";
                    }
                }
                hashed = list.get(val);
                hashValue = hashed.get(1);

            }
            //set the value as "used"
            hashed.set(1, "1");
            update();
            return hashed.get(0);
        }
    }

    //Updates the Firebase Database with new information for the usernames
    //TODO: check that update works on Firebase
    private void update() {
        for (ArrayList<String> pair: list) {
            String username = pair.get(0);
            String val = pair.get(1);
            ref.child(username).setValue(val);
        }
    }

    private void getInstance(FirebaseCallback callback) {
        final FirebaseCallback cb = callback;
        //TODO: get all usernames from Firebase, add them to a list
        ref.addValueEventListener(new ValueEventListener() {
        //queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(ANON)) {
                        for (DataSnapshot ds: child.getChildren()) {
                            String username = ds.toString();
                            String val = (String) ds.getValue();
                            ArrayList<String> pair = new ArrayList<>();
                            pair.set(0, username);
                            cb.onCallback("USERNAME: " + username + " " + val);
                        }
                    }
                }
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals(ANON)) {
                                for (DataSnapshot ds: child.getChildren()) {
                                    String username = ds.toString();
                                    String val = (String) ds.getValue();
                                    ArrayList<String> pair = new ArrayList<>();
                                    pair.set(0, username);
                                    pair.set(1, val);
                                    dataList.add(pair);
                                }
                            }
                        }
                        list = dataList;
                    }
                    else {
                        Log.e(TAG, "No children");
                        //list will still be null
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Cancelled");
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals(ANON)) {
                        for (DataSnapshot ds: child.getChildren()) {
                            String username = ds.toString();
                            String val = (String) ds.getValue();
                            ArrayList<String> pair = new ArrayList<>();
                            pair.set(0, username);
                            cb.onCallback("USERNAME: " + username + " " + val);
                        }
                    }
                }
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals(ANON)) {
                                for (DataSnapshot ds : child.getChildren()) {
                                    String username = ds.toString();
                                    String val = (String) ds.getValue();
                                    ArrayList<String> pair = new ArrayList<>();
                                    pair.set(0, username);
                                    pair.set(1, val);
                                    dataList.add(pair);
                                }
                            }
                        }
                        list = dataList;
                    } else {
                        Log.e(TAG, "No children");
                        //list will still be null
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addChildEventListener(new ChildEventListener() {
        //queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if (child.getKey().equals(ANON)) {
                                String username = child.toString();
                                String val = (String) child.getValue();
                                ArrayList<String> pair = new ArrayList<>();
                                pair.set(0, username);
                                pair.set(1, val);
                                cb.onCallback("USERNAME: " + username + " " + val);
                                dataList.add(pair);
                            }
                        }
                        list = dataList;
                    }
                    else {
                        Log.e(TAG, "No children");
                        //list will still be null
                    }
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
                Log.e(TAG, "Cancelled");
            }
        });

    }

    private ArrayList<String> createFruits() {
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Granny Smith");
        fruits.add("Pineapple");
        fruits.add("Kiwi");
        fruits.add("Strawberry");
        fruits.add("Grapefruit");
        fruits.add("Mango");
        fruits.add("Fig");
        fruits.add("Orange");
        fruits.add("Passion Fruit");
        fruits.add("Guava");
        fruits.add("Blackberry");
        fruits.add("Blueberry");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Pear");
        fruits.add("Coconut");
        fruits.add("Cantaloupe");
        fruits.add("Tangerine");
        fruits.add("Raspberry");
        fruits.add("Watermelon");
        fruits.add("Red Delicious");
        fruits.add("Persimmon");
        fruits.add("Tomato");
        return fruits;
    }
}
