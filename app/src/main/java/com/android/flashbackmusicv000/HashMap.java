package com.android.flashbackmusicv000;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HashMap {
    private ArrayList<ArrayList<String>> list;
    private DatabaseReference ref;
    private static final String TAG = "HASH ERROR";

    HashMap() {
        //Check that the hash map has not already been instantiated on Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ArrayList<ArrayList<String>> databaseInstance = getInstance();
        if (databaseInstance != null) {
            list = databaseInstance;
        }
        //If it has not, create a new hash map
        else {
            String val = "0";
            ArrayList<String> fruits = createFruits();
            list = new ArrayList<ArrayList<String>>();
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
            ref.child("anonymous_users").child(username).setValue(val);
        }
    }

    private ArrayList<ArrayList<String>> getInstance() {
        //ArrayList<ArrayList<String>> dataList;
        //TODO: get all usernames from Firebase, add them to a list
        Query queryRef = ref.orderByChild("anonymous_users");
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    ArrayList<ArrayList<String>> dataList = new ArrayList<>();
                    if (dataSnapshot.getChildrenCount() != 0) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String username = child.toString();
                            String val = (String) child.getValue();
                            ArrayList<String> pair = new ArrayList<>();
                            pair.set(0, username);
                            pair.set(1, val);
                            dataList.add(pair);
                        }
                        list = dataList;
                    }
                    else {
                        Log.e(TAG, "No children");
                        //list will still be null
                    }
                }            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Cancelled");
            }
        });
        return null;
        //return dataList;
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
