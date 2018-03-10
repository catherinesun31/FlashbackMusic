package com.android.flashbackmusicv000;

import android.util.Log;

import java.util.ArrayList;

public class HashMap {
    private ArrayList<ArrayList<String>> list;

    public HashMap() {
        String val = "0";
        ArrayList<String> fruits = createFruits();
        list = new ArrayList<ArrayList<String>>();
        for (String fruit: fruits) {
            ArrayList<String> fruitPair = new ArrayList<String>();
            fruitPair.add(0, fruit);
            fruitPair.add(1, val);
            list.add(fruitPair);
        }
    }

    public ArrayList<ArrayList<String>> getList() {
        return list;
    }

    public String hash(int userVal) {
        int val = userVal%list.size();
        //hash to that index, if it has not already been used
        ArrayList<String> hashed = list.get(val);
        String hashValue = hashed.get(1);
        if (hashValue.equals("0")) {
            hashed.set(1, "1");
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
                        Log.e("HASH ERROR", "Iterated over the list more than 5 times." +
                                "Check for error.");
                        return "Default Fruit";
                    }
                }
                hashed = list.get(val);
                hashValue = hashed.get(1);

            }
            //set the value as "used"
            hashed.set(1, "1");
            return hashed.get(0);
        }
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
