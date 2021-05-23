package com.meaty.seller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.meaty.seller.R;
import com.meaty.seller.model.firebase.SellerProfile;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore database;
    private Intent intentHomeScreen;
    private Intent intentSellerProfileScreen;
    private static final Integer RC_SIGN_IN = 1;
    private static final String GOOGLE_SIGN_IN_TAG = "GOOGLE_SIGN_IN";
    private static final String USER_SHARED_PREFERENCES = "userPreferences";
    private static final String SIGN_IN_TAG = "SIGN_IN";
    private static final String UID_SHARED_PREFERENCES = "uid";
    private static final String PHONE_SHARED_PREFERENCES = "phone";
    private static final String SELLER_COLLECTION_NAME = "seller";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
        googleSignInButtonClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        SellerProfile seller = getUserSharedPreferences();
        if (mFireBaseAuth.getCurrentUser() != null && seller.getPhoneNumber() != null) {
            Log.i(SIGN_IN_TAG, "The user is already signed in. Opening new activity");
            goToActivity(intentHomeScreen);
        } else {
            Log.i(SIGN_IN_TAG, "The user is not signed in");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(GOOGLE_SIGN_IN_TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(GOOGLE_SIGN_IN_TAG, "Google sign in failed", e);
            }
        }
    }

    private void setup() {
        configureGoogleSignIn();
        // Creating instance of FirebaseDb
        database = FirebaseFirestore.getInstance();
        mFireBaseAuth = FirebaseAuth.getInstance();
        intentHomeScreen = new Intent(this, HomeScreenActivity.class);
        intentSellerProfileScreen = new Intent(this, SellerProfileActivity.class);
    }

    private void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void googleSignInButtonClick() {
        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE_SIGN_IN_TAG, "signInWithCredential:success");
                            goToSellerProfileOrHomeScreenActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(GOOGLE_SIGN_IN_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToSellerProfileOrHomeScreenActivity() {
        final FirebaseUser user = mFireBaseAuth.getCurrentUser();
        final String uid = user.getUid();
        database.collection(SELLER_COLLECTION_NAME)
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SellerProfile sellerResponse = documentSnapshot.toObject(SellerProfile.class);
                        if (sellerResponse == null) {
                            intentSellerProfileScreen.putExtra("userUID", uid);
                            goToActivity(intentSellerProfileScreen);
                        } else {
                            if (sellerResponse.getPhoneNumber() != null) {
                                saveUserPreferences(sellerResponse);
                                goToActivity(intentHomeScreen);
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication Problem. Please contact us",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToActivity(Intent intent) {
        LoginActivity.this.startActivity(intent);
        finish();
    }

    private void saveUserPreferences(SellerProfile seller) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(UID_SHARED_PREFERENCES, seller.getSellerId());
        editor.apply();
    }

    private SellerProfile getUserSharedPreferences() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences(USER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SellerProfile seller = new SellerProfile();
        seller.setSellerId(userSharedPreference.getString(UID_SHARED_PREFERENCES, null));
        seller.setPhoneNumber(userSharedPreference.getString(PHONE_SHARED_PREFERENCES, null));
        return seller;
    }
}
