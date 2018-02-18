import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;

import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongListActivity;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Chelsea on 2/18/18.
 * Test SongListActivity
 */

public class JUnitSongListActivityTest {
    public ActivityTestRule<SongListActivity> rule = new ActivityTestRule<SongListActivity>(com.android.flashbackmusicv000.SongListActivity.class);



    @Before
    public void setup(){

    }

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

    @Test
    public void testLaunchActivity(){


    }




}
