package tests;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.android.flashbackmusicv000.AlbumStorage;
import com.android.flashbackmusicv000.MainActivity;
import com.android.flashbackmusicv000.SongStorage;

import org.junit.*;

/**
 * Created by Chelsea on 3/15/18.
 *
 * Class tests MusicStorage, AlbumStorage and SongStorage classes,
 *  Album and Song classes,
 * as well as part of onCreate() in MainActivity implicitly.
 */

public class MusicStorageTest {

    public MainActivity currentActivity;
    @Rule
    ActivityTestRule<MainActivity> ma = new ActivityTestRule<MainActivity>(MainActivity.class);

    /*
    Test that music storage correctly populated the arrays in MainActivity onCreate method

     */

    @Before
    public void setup(){
        currentActivity = ma.getActivity();

    }

    @Test
    public void testCreateStorage(){
        SongStorage ss = currentActivity.ms.getSongStorage();
        AlbumStorage as = currentActivity.ms.getAlbumStorage();

        //iterate through ss and as, log the values of the song and albums in the lists
        //Testing was correctly populated and that the song and album classes were correctly populated

        for(int i = 0; i < 10; i++){
            Log.d("Songtitle: ", ss.songsList.get(i).getTitle());
            Log.d("Song full date", ss.songsList.get(i).getFullDate().toString());
        }

        for(int i = 0; i< 10; i++){
            Log.d("Album name", as.getAlbums().get(i).getName());

        }
    }
}
