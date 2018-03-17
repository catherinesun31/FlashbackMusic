package tests;

import com.android.flashbackmusicv000.TrackList.model.AlbumSorter;
import com.android.flashbackmusicv000.TrackList.model.ArtistSorter;
import com.android.flashbackmusicv000.TrackList.model.DownloadedSong;
import com.android.flashbackmusicv000.TrackList.model.FavouriteSorter;
import com.android.flashbackmusicv000.TrackList.model.TitleSorter;
import com.android.flashbackmusicv000.TrackList.model.TrackListSorter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;


/**
 * Created by MobileComputerWizard on 3/16/2018.
 */

public class TrackListSortJUNitTest {
    // not an activity
    private TrackListSorter trackListSorter;
    private ArrayList<DownloadedSong> songList;
    private TitleSorter titleSorter;
    private ArtistSorter artistSorter;
    private AlbumSorter albumSorter;
    private FavouriteSorter favouriteSorter;

    private DownloadedSong song1;
    private DownloadedSong song2;
    private DownloadedSong song3;
    private DownloadedSong song4;

    @Before
    public void setup(){
        System.out.println("Setup...");

        song1 = new DownloadedSong(new String []{"kljasdfs","y5redsa","asdf","0"});
        song2 = new DownloadedSong(new String []{"kpadfapfs","oqredsa","asdf","1"});
        song3 = new DownloadedSong(new String []{"apadfapfs","yqredsa","zdse","2"});
        song4 = new DownloadedSong(new String []{"noName","anon","noAlbum","0"});

        songList = new ArrayList<DownloadedSong>();

        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        songList.add(song4);

        trackListSorter = new TrackListSorter(songList);
        titleSorter = new TitleSorter();
        artistSorter = new ArtistSorter();
        albumSorter = new AlbumSorter();
        favouriteSorter = new FavouriteSorter();

    }

    @Test
    public void runtTests(){

        song1 = new DownloadedSong(new String []{"kljasdfs","y5redsa","asdf","0"});
        song2 = new DownloadedSong(new String []{"kpadfapfs","oqredsa","asdf","1"});
        song3 = new DownloadedSong(new String []{"apadfapfs","yqredsa","zdse","2"});
        song4 = new DownloadedSong(new String []{"noName","anon","noAlbum","0"});

        ArrayList testAL = new ArrayList<DownloadedSong>();
        testAL.add(song1);
        testAL.add(song2);
        testAL.add(song3);
        testAL.add(song4);
        ArrayList<DownloadedSong> compare = trackListSorter.getSongs();

        for(int i = 0; i< compare.size(); ++i){

            String test1= ""+testAL.get(i);
            String test2 = ""+compare.get(i);
            assertTrue(test1.equals(test2));

        }
        testAL.clear();
        compare.clear();

        trackListSorter.setSort(titleSorter);

        testAL.add(song3);
        testAL.add(song1);
        testAL.add(song2);
        testAL.add(song4);

        compare = trackListSorter.getSongs();

        for(int i = 0; i< compare.size(); ++i){

            String test1= ""+testAL.get(i);
            String test2 = ""+compare.get(i);
            assertTrue(test1.equals(test2));

        }
        testAL.clear();
        compare.clear();

        trackListSorter.setSort(artistSorter);

        testAL.add(song4);
        testAL.add(song2);
        testAL.add(song1);
        testAL.add(song3);

        compare = trackListSorter.getSongs();

        for(int i = 0; i< compare.size(); ++i){

            String test1= ""+testAL.get(i);
            String test2 = ""+compare.get(i);
            assertTrue(test1.equals(test2));

        }

        testAL.clear();
        compare.clear();

        trackListSorter.setSort(albumSorter);

        testAL.add(song2);
        testAL.add(song1);
        testAL.add(song4);
        testAL.add(song3);

        compare = trackListSorter.getSongs();

        for(int i = 0; i< compare.size(); ++i){

            String test1= ""+testAL.get(i);
            String test2 = ""+compare.get(i);
            assertTrue(test1.equals(test2));

        }

        testAL.clear();
        compare.clear();


        trackListSorter.setSort(favouriteSorter);

        testAL.add(song1);
        testAL.add(song4);
        testAL.add(song2);
        testAL.add(song3);

        compare = trackListSorter.getSongs();

        for(int i = 0; i< compare.size(); ++i){

            String test1= ""+testAL.get(i);
            String test2 = ""+compare.get(i);
            assertTrue(test1.equals(test2));

        }

        testAL.clear();
        compare.clear();

    }

}
