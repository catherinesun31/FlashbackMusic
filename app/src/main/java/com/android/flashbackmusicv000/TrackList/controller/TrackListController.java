package com.android.flashbackmusicv000.TrackList.controller;

//The View is a fragment
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongPlayingActivity;
import com.android.flashbackmusicv000.TrackList.model.AlbumSorter;
import com.android.flashbackmusicv000.TrackList.model.ArtistSorter;
import com.android.flashbackmusicv000.TrackList.model.DownloadedSong;
import com.android.flashbackmusicv000.TrackList.model.FavouriteSorter;
import com.android.flashbackmusicv000.TrackList.model.TitleSorter;
import com.android.flashbackmusicv000.TrackList.model.TrackListSorter;
import com.android.flashbackmusicv000.utility.Globals;

import java.util.ArrayList;

import static com.android.flashbackmusicv000.utility.Globals.isOn;


//querying and updating the model, model to handle database seperately
//send data from input to the model,

/**
 * Created by MobileComputerWizard on 3/10/2018.
 */

public class TrackListController extends Fragment {
//MVC design pattern
    //views....
    private Context context;
    private View view;
    private ConstraintLayout constraintLayout;

    private ArrayList<Button> trackButtons;
    private Button titleButton;
    private Button artistButton;
    private Button albumButton;
    private Button favouriteButton;

    private Switch switchy;
    //model
    private TrackListSorter trackListSorter;
    private TitleSorter titleSorter;
    private ArtistSorter artistSorter;
    private AlbumSorter albumSorter;
    private FavouriteSorter favouriteSorter;
    //temp test data
    private ArrayList<DownloadedSong> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View is our layout
        view = inflater.inflate(R.layout.content_song_playing, container, false);
        //Context is the parentof the Activity/Fragment

        context = this.getContext();

        //1. set model
        setModel();
        //2. set view
        setView();

        return view;

    }


    private void setModel(){

        setData();
        setTrackListers();

    }

    public void setData(){
        //For initialising by default
        //Should ideally get data from object within the model.
        //Only temp testing data.
        DownloadedSong song1 = new DownloadedSong(new String []{"kljasdfs","y5redsa","asdf","0"});
        DownloadedSong song2 = new DownloadedSong(new String []{"kpadfapfs","oqredsa","asdf","1"});
        DownloadedSong song3 = new DownloadedSong(new String []{"apadfapfs","yqredsa","zdse","2"});
        DownloadedSong song4 = new DownloadedSong(new String []{"noName","anon","noAlbum","0"});

        songList = new ArrayList<DownloadedSong>();

        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        songList.add(song4);

    }

    private void setTrackListers(){
        //Getting the data first....
        trackListSorter = new TrackListSorter(songList);
        titleSorter = new TitleSorter();
        artistSorter = new ArtistSorter();
        albumSorter = new AlbumSorter();
        favouriteSorter = new FavouriteSorter();

    }
    //model ended

    //view begins
    private void setView(){

        setWidgets();
        setListeners();

    }

    private void setWidgets(){

        setSortButtons();
        setTrackButtons();
        setSwitch();

    }

    private void setSortButtons(){

        titleButton = (Button) view.findViewById(R.id.titleButton);
        artistButton = (Button) view.findViewById(R.id.artistButton);
        albumButton = (Button) view.findViewById(R.id.albumButton);
        favouriteButton = (Button) view.findViewById(R.id.favoriteButton);

    }

    private void setTrackButtons(){

        //trackButtons = new ArrayList<Button>();
        Iterator trackIterator = songList.iterator();
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);

        //Track Button needs way to display the data....
        TrackButton currentButton = null;
        DownloadedSong currentSong = null;
        while(trackIterator.hasNext()){

            //turn each track into a button.
            currentSong = (DownloadedSong) trackIterator.next();
            currentButton = new TrackButton(Color.GREEN);

            currentButton.setText(""+currentSong);
            currentButton.setOnClickListener(currentButton);

            //
            //trackButtons.add(button);
            //assuming it is top down
            constraintLayout.addView(currentButton);

        }

    }


    //Look again at Switchy and make it global.
    //Externalise it. The anonymous listener is an inner class.
    //Also only ever using one switch, not multiple.... if switch reimplemented, externalise the listener.
    //If I can globalise then I do not need to use shared preferences across different activities, as
    //state is globally accessible. The abstraction is that this is only one button.
    //private void setSwitch(){
    private void setSwitch() {

        Globals.currentContext = context;
        switchy = (Switch) view.findViewById(R.id.flashSwitch);
        switchy.setChecked(Globals.isOn);
        switchy.setOnCheckedChangeListener(new Globals.SwitchListener());

    }

    private void setListeners(){

        setTopButtonListeners(new SortButtonListener());

    }

    private void setTopButtonListeners(SortButtonListener sortListener){

        titleButton.setOnClickListener(sortListener);
        artistButton.setOnClickListener(sortListener);
        albumButton.setOnClickListener(sortListener);
        favouriteButton.setOnClickListener(sortListener);

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
            //1. sort
            trackListSorter.sortTracks();
            //2. get the sorted tracks
            trackListSorter.getSongs();
            //3. update
            updateDisplay();

        }

    }

    private void updateDisplay(){

        //We already have a view....
        //1. turn track into a button
        songList.clear();
        songList = trackListSorter.getSongs();
        //2. add to view.
        setTrackButtons();
    }

    /**
     * Specialised button that has a particular colour and listener for this layout.
     */
    private class TrackButton extends AppCompatButton implements View.OnClickListener{

        private int highlightColour;
        private ColorDrawable startButtonColour;
        private int defaultColour;
        public TrackButton(int highlightColour){

            super(context);
            super.setText("Button");
            this.setBackgroundColor(Color.RED);
            this.startButtonColour = (ColorDrawable) this.getBackground();
            defaultColour = startButtonColour.getColor();
        //defaultColour = this.getColor();
        //buttonColor.setColor(defaultColour);

            this.highlightColour = highlightColour;

        }

        public void onClick(View v) {

            Button button = (Button)v;
            //Drawable drColor = button.getBackground();
            ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
            int currentColour = buttonColor.getColor();
            //highlight and check
            if (currentColour == defaultColour) {

                button.setBackgroundColor(highlightColour);

            } else {

                button.setBackgroundColor(defaultColour);

            }
        }
    }

}