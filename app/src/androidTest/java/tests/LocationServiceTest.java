package tests;

import android.location.LocationManager;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;


import com.android.flashbackmusicv000.MainActivity;

import org.junit.*;


import static junit.framework.Assert.assertEquals;

//@RunWith(AndroidJUnit4.class)

/**
 * Created by Chelsea on 3/14/18.
 *
 * TESTS MainActivity.getLocation()
 * Utilizes tests.MockLocation to test current location works correctly.
 */

public class LocationServiceTest {
    public MockLocation mock;

    @Rule
    public ActivityTestRule<MainActivity> mA = new ActivityTestRule<MainActivity>(MainActivity.class);

   @Before
    public void setup(){
        MainActivity mainActivity = mA.getActivity();
        //set to mockLocation somehow?
       mock = new MockLocation(LocationManager.NETWORK_PROVIDER, mA.getActivity());

    }

    //use the location and test that the app uses the user's location?
    @Test
    public void testUserLocationBlacks(){

        //Mock location is Black's beach.
        mock.pushLocation(32.887261, -117.252967);
        //set location in main activity
        Log.d("testUserLocation", mA.getActivity().getLocation());
       assertEquals(mA.getActivity().getLocation(), "");


    }

}
