import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongListActivity;
import com.android.flashbackmusicv000.SongPlayingActivity;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Chelsea on 2/18/18.
 * Test SongListActivity
 */

public class JUnitSongListActivityTest {
    public ActivityTestRule<SongListActivity> rule = new ActivityTestRule<SongListActivity>(com.android.flashbackmusicv000.SongListActivity.class);



    @Before
    public void setup(){

    }


    /*
    Check buttons correctly add songs to favorites/disliked/netural
     */
    @Test
    public void testButtonChange(){
        SongListActivity activity = rule.getActivity();

        String song_name = activity.songs.get(0);

        final int song_id = song_name.hashCode();
        String button_type = "+";

        //to get the button with the + - check
        Button button = (Button) activity.findViewById(song_id+1);

        button.performClick();

        String new_value = button.getText().toString();
        assertEquals("✓", new_value);

        // check song_name added to favorites list
        // song is favorited
        assertEquals(true, activity.favorites.contains(song_name));
        assertEquals(false, activity.disliked.contains(song_name));


        button.performClick();
        new_value = button.getText().toString();
        assertEquals("x", new_value);

        //check song not in favorites and now in dislikes.
        //song is disliked
        assertEquals(true, activity.disliked.contains(song_name));
        assertEquals(false, activity.favorites.contains(song_name));


        button.performClick();
        new_value = button.getText().toString();
        assertEquals("+", new_value);

        //check song not in disliked and in neutral
        assertEquals(true, activity.neutral.contains(song_name));


    }

    /*
    Check that the Song button takes to correct screen for that song to play
     */
    @Test
    public void testLaunchActivity(){

        SongListActivity activity = rule.getActivity();

        Instrumentation.ActivityMonitor sm =
                getInstrumentation().addMonitor(SongPlayingActivity.class.getName(), null, false);

        final Button songButton = (Button) activity.findViewById(R.id.songs);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                songButton.performClick();
            }
        });
        SongPlayingActivity sq = (SongPlayingActivity) getInstrumentation().waitForMonitorWithTimeout(sm, 5000);
        assertNotNull(sq);
        sq.finish();

    }




}
