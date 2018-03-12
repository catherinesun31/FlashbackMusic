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

/**
 * Created by Chelsea 3/9/18.
 */

public class FriendsList extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>{

    private GoogleApiClient mGAC = null;
    private static final String TAG = "Friendslist"

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.SignInActivity);
        mGAC = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }
    @Override
    protected  void onStart(){
        super.onStart();
        Log.d(TAG, "onStart called");
        mGAC.connect();

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");

        if(mGAC.isConnected()){
            mGAC.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint){
        Log.d(TAG, "onConnection called");

        Plus.PeopleApi.loadVisible(mGAC, null).setResultCallback(this);
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed called");
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.d(TAG, "onConnectionSuspended called");
    }

}
