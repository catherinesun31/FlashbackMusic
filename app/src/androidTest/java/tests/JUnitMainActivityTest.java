package tests;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import com.android.flashbackmusicv000.AlbumQueue;
import com.android.flashbackmusicv000.MainActivity;
import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongListActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Chelsea on 2/18/18.
 */

public class JUnitMainActivityTest {

    Instrumentation.ActivityMonitor am =
            getInstrumentation().addMonitor(AlbumQueue.class.getName(), null, false);
    Instrumentation.ActivityMonitor sm =
            getInstrumentation().addMonitor(SongListActivity.class.getName(), null, false);

    @Rule
    public ActivityTestRule<MainActivity> rule  = new ActivityTestRule<MainActivity>(MainActivity.class);

    /*
    Check if the correct activites are launched upon button click of songs and albums
    in the main activity.
     */
    @Test
    public void testLaunchAlbumSongActivities(){

        MainActivity activity = rule.getActivity();
        final Button albumButton = (Button) activity.findViewById(R.id.albums);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                albumButton.performClick();
            }
        });
        AlbumQueue aq = (AlbumQueue) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(aq);
        aq.finish();

        final Button songButton = (Button) activity.findViewById(R.id.songs);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                songButton.performClick();
            }
        });
        SongListActivity sq = (SongListActivity) getInstrumentation().waitForMonitorWithTimeout(sm, 5000);
        assertNotNull(sq);
        sq.finish();

    }

}
