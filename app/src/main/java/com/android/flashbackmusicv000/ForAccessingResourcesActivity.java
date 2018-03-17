package com.android.flashbackmusicv000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.flashbackmusicv000.friend.Friend;
import com.android.flashbackmusicv000.friend.NameGenerator;
import com.android.flashbackmusicv000.friend.NonFriend;

import java.io.File;
import java.io.InputStream;

//A temporary and not particularly relevant Activity for testing objects independently so they have access to the resources
// The Android are forcing developers to store in somewhat extroadinary ways.
public class ForAccessingResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_accessing_resources);

        forFriendClassTesting();
    }


    private void forFriendClassTesting(){
        /*
        String fileName = "R.raw.namesdb";
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        NameGenerator ng = NameGenerator.getInstance(file);
        System.out.println(ng);

        Friend friend = new Friend("Abdi");

        NonFriend fiend = new NonFriend(ng);

        System.out.println("Hashcode of Friend is " + friend.hashCode() + " and their name is " + friend);
        System.out.println("Hashcode of Friend is " + fiend.hashCode() + " and their name is " + fiend);

        System.out.println("Are the equal? :" + (friend.hashCode() == fiend.hashCode()));
        System.out.println("Are the equal to themselves? Friend:" + (friend.hashCode() == friend.hashCode()) + " NonFriend :" + (fiend.hashCode() == fiend.hashCode()));
        */
    }
}
