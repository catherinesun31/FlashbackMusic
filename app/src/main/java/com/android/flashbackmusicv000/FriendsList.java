package com.android.flashbackmusicv000;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.android.flashbackmusicv000.User;
import com.android.flashbackmusicv000.UserAdapter;

import java.util.ArrayList;

/**
 * Created by Chelsea 3/9/18.
 *
 * ADDED TO THE SIGN IN ACTIVITY class
 */

public class FriendsList extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>{

    private GoogleApiClient mGAC = null;
    private static final String TAG2 = "Friendslist";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mGAC = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }
    @Override
    protected  void onStart(){
        super.onStart();
        Log.d(TAG2, "onStart called");
        mGAC.connect();

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG2, "onStop called");

        if(mGAC.isConnected()){
            mGAC.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint){
        Log.d(TAG2, "onConnection called");

        Plus.PeopleApi.loadVisible(mGAC, null).setResultCallback(this);
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG2, "onConnectionFailed called");
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.d(TAG2, "onConnectionSuspended called");
    }

    @Override
    public void onResult(LoadPeopleResult peopleData){
        Log.d(TAG2, "onResult called - setting adapter");

        User user;
        ArrayList<User> arrayListContacts = new ArrayList<User>();

        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {

            PersonBuffer personBuffer = peopleData.getPersonBuffer();

            try {

                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {

                  /*  user = new User(personBuffer.get(i).hasId() ? personBuffer.get(i).getId()
                            : null, personBuffer.get(i).hasDisplayName() ? personBuffer.get(i)
                            .getDisplayName() : null, personBuffer.get(i).hasUrl() ? personBuffer
                            .get(i).getUrl() : null, personBuffer.get(i).hasImage() ? personBuffer
                            .get(i).getImage().getUrl() : null);

                    arrayListContacts.add(user);*/

                }

            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG2, "Error requesting visible circles : " + peopleData.getStatus());
        }

        // Setting the adapter already loaded with all contacts retrieved from
        // the connected user account


    }

}


