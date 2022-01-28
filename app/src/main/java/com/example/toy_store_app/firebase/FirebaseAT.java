package com.example.toy_store_app.firebase;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAT {
    public static final String USERS_CHILD = "Users";
    private static FirebaseAuth auth;
    private static FirebaseAuth.AuthStateListener authListener;
    private static FirebaseUser firebaseUser;
    private FirebaseAT(){}

    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        getAuthListener();
        return auth;
    }
    public static void isLoggedIn() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.reload();
        }
    }
    public static FirebaseAuth.AuthStateListener getAuthListener() {
        return authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("TOY_STORE_APP","onAuthStateChanged: signed_in" + user.getUid());
            }
            else {
                Log.d("TOY_STORE_APP","onAuthStateChanged: signed_out");
            }
        };
    }
    public static void removeListener() {
        if (authListener!= null)
            auth.removeAuthStateListener(authListener);
    }


}