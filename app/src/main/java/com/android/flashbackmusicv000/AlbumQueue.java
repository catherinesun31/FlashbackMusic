package com.android.flashbackmusicv000;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlbumQueue extends AppCompatActivity {

    private Button[] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_queue);

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        //

        buttons = new Button[9];


    }

    private void setButtons(){

        for(int i = 0; i<buttons.length;++i){

            //buttons[i] = (Button) findViewById(R.id.album+"i");
            String buttonID = "button" + (i+1);

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i].setOnClickListener(new AlbumButtonListener());


        }

    }
    private class AlbumButtonListener implements View.OnClickListener{

            public void onClick(View v){

                Toast.makeText(AlbumQueue.this, "v.getId", Toast.LENGTH_SHORT).show();


                switch(v.getId()){

                    //if v.getId, then goto the now playing page. Then break

                    case R.id.album1: ;break;
                    case R.id.album2: ;break;
                    case R.id.album3: ;break;
                    case R.id.album4: ;break;
                    case R.id.album5: ;break;
                    case R.id.album6: ;break;
                    case R.id.album7: ;break;
                    case R.id.album8: ;break;
                    default:  Toast.makeText(AlbumQueue.this, "Defualt", Toast.LENGTH_SHORT).show();


                }


            }


    }

    public void startActivity(){

        Intent myIntent = new Intent(this, SongPlayingActivity.class);

    }


}
