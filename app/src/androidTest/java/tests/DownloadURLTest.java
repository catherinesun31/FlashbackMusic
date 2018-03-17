package tests;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import com.android.flashbackmusicv000.MainActivity;
import com.android.flashbackmusicv000.Song;
import com.android.flashbackmusicv000.SongStorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Chelsea on 3/16/18.
 *
 * addStorage(String url), DownloadData(Uri uri)
 *
 *  Tests scenario for user entering URL to download
 */

public class DownloadURLTest {

    //check DownloadData works --> passes to addStorage --> from onCreate method


    public MainActivity currentActivity;
    public String blackhole = "https://www.dropbox.com/s/fg7gc1tmdl1jre8/transmission-002-the-blackhole.mp3?dl=1";
    public String stars = "https://www.dropbox.com/s/ilvs4t50l2rxxzz/spiraling-stars.mp3?dl=1";

    public Intent downloadIntent;
    public DownloadManager dm;
    public SongStorage ss;
    public Song blackHoleSong, starSong;
    @Rule
    ActivityTestRule<MainActivity> ma = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    ServiceTestRule mServiceRule = new ServiceTestRule();


    @Before
    public void setup(){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(blackhole);

        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        downloadIntent= new Intent(InstrumentationRegistry.getTargetContext(),
                DownloadManager.class);
        currentActivity = ma.getActivity();
        dm = currentActivity.downloadManager;
         ss = currentActivity.ms.getSongStorage();
         blackHoleSong = new Song(title, blackhole);


         mmr.setDataSource(stars);
         title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
         starSong = new Song(title, stars);
    }


    /*
    Tests DownloadData correctly returns the reference long value given a URL
     */
    @Test
    public void testDownloadData(){

        //return values returned from DownloadData
        long blackholeReturn, starReturn;

        blackholeReturn = ma.getActivity().DownloadData(Uri.parse(blackhole));
        starReturn = ma.getActivity().DownloadData(Uri.parse(stars));

        Log.d("Download reference for blackhole", String.valueOf(blackholeReturn));
        Log.d("Download reference for stars", String.valueOf(starReturn));

        //check that song storage now has reference to newly downloaded songs
        assertEquals(ss.songsList.contains(starSong), true);
        assertEquals(ss.songsList.contains(blackHoleSong), true);

    }


}
