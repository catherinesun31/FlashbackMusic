package com.android.flashbackmusicv000;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.*;


import static android.content.Context.LOCATION_SERVICE;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Chelsea on 3/14/18.
 *
 * Utilizes MockLocation to test current location works correctly.
 */

public class LocationServiceTest {
    public MockLocation mock;

    @Rule
    public ActivityTestRule<MainActivity> mA = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup(){
      //  mock = new MockLocation(LocationManager.NETWORK_PROVIDER, mA.getActivity());

    }

    //use the location and test that the app uses the user's location?
    @Test
    public void testUserLocationBlacks(){

        //Mock location is Black's beach.
      //  mock.pushLocation(32.887261, -117.252967);
        //set location in main activity
       // Log.d("testUserLocation", mA.getActivity().getLocation());
       // assertEquals(mA.getActivity().getLocation(), "");


    }

}
