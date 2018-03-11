package com.android.flashbackmusicv000.TrackList;

//The View is a fragment
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.flashbackmusicv000.R;
import com.android.flashbackmusicv000.SongPlayingActivity;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_song_playing, container, false);
        setView();
        setModel();
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



    }

    private class SortButtonListener implements View.OnClickListener {

        public void onClick(View v){

            switch(v.getId()){

                //Decorator pattern could be used..... All doing the same thing.
                case R.id.titleButton:  ;break;
                case R.id.artistButton:  ;break;
                case R.id.albumButton:  ;break;
                case R.id.favoriteButton:  ;break;

            }

        }

    }
}
