package com.android.flashbackmusicv000;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.people.v1.PeopleService;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGAC = null;
    private static final String TAG2 = "Friendslist";

    FirebaseDatabase database;
    DatabaseReference dataRef;
    private FirebaseAuth mAuth;
    private String username;
    private String password;
    private GoogleSignInClient googleSignInClient;
    private boolean isAnon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database = FirebaseDatabase.getInstance();
        dataRef = database.getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
        googleSignInClient.signOut();

        com.google.android.gms.common.SignInButton signInButton =
                findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(false);
            }
        });

        Button anonymousUser = findViewById(R.id.anonymous_user);
        anonymousUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(true);
            }
        });

        mGAC = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in or anonymous, and go to Main Activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("LOGIN", "User is already logged in");
        }
        updateUI(currentUser);

        Log.d(TAG2, "onStart called");
        mGAC.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                createUser(user);
                            }
                            updateUI(user);

                            Toast.makeText(SignInActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            try {
                                mAuth.signOut();
                                googleSignInClient.signOut();
                            }
                            catch (Exception e) {
                                Log.e("Error", "Authentication error", e);
                            }
                            updateUI(null);

                        }
                    }
                });

    }

    public void signIn(boolean anon) {
        if (anon) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                createUser(user);
                                updateUI(user);
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                try {
                                    mAuth.signOut();
                                }
                                catch (Exception e) {
                                    Log.e("Error", "Anonymous user error", e);
                                }
                            }
                        }
                    });
        }
        else {
            Log.d("GOOGLE SIGN IN", "Signed in with Google");

            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //Check that the current user's information has been added to the
            //database already

            //Go to main activity
        }
        else {
            //Create an anonymous user to use as a proxy
        }
    }

    private void createUser(FirebaseUser user) {
        if (user != null) {
            //Builder class to create a user with their information and go to main activity
            String email = user.getEmail();
            String displayName = user.getDisplayName();
            Log.i("User info", "Email: " + email + "\n" +
                    "Display Name: " + displayName);
            IUserBuilder builder = new UserBuilder();
            builder.setEmail(email);
            builder.setUsername(displayName);
            User user1 = builder.build();

        }
        else {
            //Builder class to create user with anonymous information
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
    public void onResult(People.LoadPeopleResult peopleData){
        Log.d(TAG2, "onResult called - setting adapter");

        /*
        TODO get user from shared preferences to populate user's arraylist.
         */
        User user;
        ArrayList<User> arrayListContacts = new ArrayList<User>();

        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {

            PersonBuffer personBuffer = peopleData.getPersonBuffer();

            try {

                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {

                    user = new User(personBuffer.get(i).hasId() ? personBuffer.get(i).getId()
                            : null, personBuffer.get(i).hasDisplayName() ? personBuffer.get(i)
                            .getDisplayName() : null, personBuffer.get(i).hasUrl() ? personBuffer
                            .get(i).getUrl() : null, personBuffer.get(i).hasImage() ? personBuffer
                            .get(i).getImage().getUrl() : null);

                    arrayListContacts.add(user);

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
}
