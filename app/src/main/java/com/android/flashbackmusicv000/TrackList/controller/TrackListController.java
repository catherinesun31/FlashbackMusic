package com.android.flashbackmusicv000.TrackList.controller;

//The View is a fragment
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongPlayingActivity;
import com.android.flashbackmusicv000.TrackList.model.AlbumSorter;
import com.android.flashbackmusicv000.TrackList.model.ArtistSorter;
import com.android.flashbackmusicv000.TrackList.model.DownloadedSong;
import com.android.flashbackmusicv000.TrackList.model.FavouriteSorter;
import com.android.flashbackmusicv000.TrackList.model.TitleSorter;
import com.android.flashbackmusicv000.TrackList.model.TrackListSorter;


//querying and updating the model, model to handle database seperately
//send data from input to the model,

/**
 * Created by MobileComputerWizard on 3/10/2018.
 */

public class TrackListController extends Fragment {
//MVC design pattern
    private View view;
    //Model model;
    private Button titleButton;
    private Button artistButton;
    private Button albumButton;
    private Button favouriteButton;
    private TrackListSorter trackListSorter;
    private TitleSorter titleSorter;
    private ArtistSorter artistSorter;
    private AlbumSorter albumSorter;
    private FavouriteSorter favouriteSorter;
    //temp test data
    private DownloadedSong[] testSongList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_song_playing, container, false);

        setModel();
        setView();

        return view;
    }

    private void setView(){

        titleButton = (Button) view.findViewById(R.id.titleButton);
        artistButton = (Button) view.findViewById(R.id.artistButton);
        albumButton = (Button) view.findViewById(R.id.albumButton);
        favouriteButton = (Button) view.findViewById(R.id.favoriteButton);
        setButtonListener(new SortButtonListener());

    }

    private void setButtonListener(SortButtonListener sortListener){

        titleButton.setOnClickListener(sortListener);
        artistButton.setOnClickListener(sortListener);
        albumButton.setOnClickListener(sortListener);
        favouriteButton.setOnClickListener(sortListener);

    }

    private void setModel(){

        setData();
        setTrackListers();

    }

    public void setData(){

        //Only temp testing data.
        DownloadedSong song1 = new DownloadedSong(new String []{"kljasdfs","y5redsa","asdf","0"});
        DownloadedSong song2 = new DownloadedSong(new String []{"kpadfapfs","oqredsa","asdf","1"});
        DownloadedSong song3 = new DownloadedSong(new String []{"apadfapfs","yqredsa","zdse","2"});
        DownloadedSong song4 = new DownloadedSong(new String []{"noName","anon","noAlbum","0"});

        testSongList = new DownloadedSong[]{song1,song2,song3,song4};

    }

    private void setTrackListers(){
        //Getting the data first....
        trackListSorter = new TrackListSorter(testSongList);

        titleSorter = new TitleSorter();
        artistSorter = new ArtistSorter();
        albumSorter = new AlbumSorter();
        favouriteSorter = new FavouriteSorter();

    }

    private class SortButtonListener implements View.OnClickListener {

        public void onClick(View v){

            switch(v.getId()){
                //Decorator pattern could be used..... All doing the same thing.
                case R.id.titleButton: trackListSorter.setSort(titleSorter);break;
                case R.id.artistButton:trackListSorter.setSort(artistSorter);break;
                case R.id.albumButton:trackListSorter.setSort(albumSorter);break;
                case R.id.favoriteButton:trackListSorter.setSort(favouriteSorter);break;

            }

            trackListSorter.sortTracks();

        }

    }
}
