package tests;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.android.flashbackmusicv000.AlbumStorage;
import com.android.flashbackmusicv000.MainActivity;
import com.android.flashbackmusicv000.SongStorage;

import org.junit.*;

/**
 * Created by Chelsea on 3/15/18.
 */

public class MusicStorageTest {

    public MainActivity currentActivity;
    @Rule
    ActivityTestRule<MainActivity> ma = new ActivityTestRule<MainActivity>(MainActivity.class);

    /*@Override
    protected Intent getActivityIntent() {
        InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra();
        return intent;
    }
*/
    /*
    Test that music storage correctly populated the arrays in MainActivity onCreate method

    Also implicitly tests that songStorage and albumStorage were correctly initialized.
     */

    @Before
    public void setup(){
        currentActivity = ma.getActivity();

    }

    @Test
    public void testCreateStorage(){
        SongStorage ss = currentActivity.ms.getSongStorage();
        AlbumStorage as = currentActivity.ms.getAlbumStorage();



    }

}
