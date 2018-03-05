package com.android.flashbackmusicv000;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by cailintreseder on 3/3/18.
 */

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
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        Log.d("in", "onCreate");
        com.google.android.gms.common.SignInButton signInButton =
                findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("in", "onClick");
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
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in or anonymous, and go to Main Activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
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
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        if (isAnon) Log.d("Is anonymous", "true");
        else Log.d("Is anonymous", "false");

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        /*
        if (!isAnon) {
            //Now using firebase we are signing in the user here
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(SignInActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
        else {*/
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        //}
    }

    public void signIn(boolean anon) {
        isAnon = anon;
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
