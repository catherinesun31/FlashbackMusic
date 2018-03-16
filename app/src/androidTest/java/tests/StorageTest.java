package tests;

import android.util.ArraySet;

import com.android.flashbackmusicv000.AlbumStorage;
import com.android.flashbackmusicv000.Song;
import com.android.flashbackmusicv000.SongStorage;

import org.junit.Test;

import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * Created by janice on 3/15/18.
 */

public class StorageTest {


    /* This tests if initialize our songs - either from downloads or from our already downloaded
     * list - in the correct dislike/favorite/neutral
     */
    @Test
    public void testSongStorage(){
        SongStorage ss = new SongStorage();
        Set<String> fakeFavorites = new ArraySet<String>();
        Set<String> fakeDisliked = new ArraySet<String>();
        Set<String> fakeNeutral = new ArraySet<String>();
        Song aSong1 = new Song("Title1", 1234);

        // If song is in favorited in preferences
        fakeFavorites.add("Title1");
        ss.initializeSong(aSong1, fakeFavorites, fakeDisliked, fakeNeutral);
        assertEquals(aSong1.isDislike(), false);
        assertEquals(aSong1.isFavorite(), true);
        assertEquals(aSong1.isNeutral(), false);

        // If song is in disliked in preferences
        fakeFavorites.clear();
        fakeDisliked.add("Title1");
        ss.initializeSong(aSong1, fakeFavorites, fakeDisliked, fakeNeutral);
        assertEquals(aSong1.isDislike(), true);
        assertEquals(aSong1.isFavorite(), false);
        assertEquals(aSong1.isNeutral(), false);

        // If song is neutral in preferences
        fakeDisliked.clear();
        fakeNeutral.add("Title1");
        ss.initializeSong(aSong1, fakeFavorites, fakeDisliked, fakeNeutral);
        assertEquals(aSong1.isDislike(), false);
        assertEquals(aSong1.isFavorite(), false);
        assertEquals(aSong1.isNeutral(), true);
    }

    @Test
    public void testAlbumStorage(){
        AlbumStorage as = new AlbumStorage();
        Song mulanSong1 = new Song("Reflection", 1234);
        Song mulanSong2 = new Song("I'll Make a Man Out of You", 1234);
        Song lalalandSong = new Song("City of Stars", 1234);

        // Adding in a new song of a new album
        as.initializeAlbum(mulanSong1, "Mulan");
        assertEquals(as.getAlbums().size(), 1);
        assertEquals(as.allSongs.getSongs().indexOf(0), mulanSong1);

        // Adding in a new song of a same album
        as.initializeAlbum(mulanSong2, "Mulan");
        assertEquals(as.getAlbums().size(), 1);     // size of albums shouldn't change
        assertEquals(as.allSongs.getSongs().indexOf(1), mulanSong2);

        // Adding in a new song of a different album
        as.initializeAlbum(lalalandSong, "La La Land");
        assertEquals(as.getAlbums().size(), 2);     // should increase album list
        assertEquals(as.allSongs.getSongs().indexOf(0), mulanSong1);
    }


}
