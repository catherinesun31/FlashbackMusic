package com.android.flashbackmusicv000;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

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

        com.google.android.gms.common.SignInButton signInButton =
                findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in, and go to Main Activity
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Log.i("LOGIN", account.getDisplayName() + " is already logged in");
            launchActivity();
        }
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
                Log.e(TAG, "Google sign in failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                User us = createUser(user);
                                Log.i("User", "Username: " + us.getUsername() + "\n" +
                                        "Email: " + us.getEmail());
                                Toast.makeText(SignInActivity.this, user.getDisplayName() + " Signed In", Toast.LENGTH_SHORT).show();
                                launchActivity();
                            }
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
                                Log.e("Error", "Authentication/Sign in error");
                            }
                        }
                    }
                });

    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private User createUser(FirebaseUser firebaseUser) {
        IUserBuilder builder = new UserBuilder();
        User user;
        //if (firebaseUser != null) {
            //Builder class to create a user with their information and go to main activity
            String email = firebaseUser.getEmail();
            String displayName = firebaseUser.getDisplayName();

            builder.setEmail(email);
            builder.setUsername(displayName);
            user = builder.build();
            Log.i("User info:", "Email: " + user.getEmail() + "\n" +
                    "Username: " + user.getUsername());
        /*}
        else {
            //Create a unique ID for the current anonymous user
            int ID = UUID.randomUUID().hashCode();

            //ensure the id is nonnegative
            if (ID < 0) ID = ID * (-1);
            //saveToSharedPref(ID);
            //Builder class to create user with anonymous information
            builder.setID(ID);
            user = builder.build();
            Log.i("User info", "Username: " + user.getUsername());
        }*/
        return user;
    }

    /*
    private void saveToSharedPref(int ID) {
        SharedPreferences preferences = getSharedPreferences("IDS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("anonymous_id", ID);
        editor.apply();
    }
    */
}
