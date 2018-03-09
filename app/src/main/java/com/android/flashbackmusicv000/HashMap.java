package com.android.flashbackmusicv000;

import java.util.ArrayList;

public class HashMap {
    private ArrayList<ArrayList<String>> list;

    HashMap() {
        String val = "0";
        ArrayList<String> fruits = createFruits();
        list = new ArrayList<ArrayList<String>>();
        for (String fruit: fruits) {
            ArrayList<String> fruitPair = new ArrayList<String>();
            fruitPair.add(fruit);
            fruitPair.add(val);
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
            hashed.set(val, "1");
            return hashed.get(0);
        }
        else {
            ++val;
            while (!hashValue.equals("0")) {
                hashValue = list.get(val).get(1);
            }
            hashed = list.get(val);
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
