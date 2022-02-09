package com.example.toy_store_app.firebase;


import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * FireBase authentication private class
 * @author Vyacheslav Gudimov
 */
public class FirebaseAT {
    private static FirebaseAuth auth;
    private static FirebaseAuth.AuthStateListener authListener;
    private static FirebaseUser firebaseUser;

    /**
     * empty private constructor
     */
    private FirebaseAT(){}

    /**
     * get firebase user authentication
     * @return Firebase user authentication
     */
    public static FirebaseAuth getAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        getAuthListener();
        return auth;
    }

    /**
     * check if user is logged in
     * log in if false
     */
    public static void isLoggedIn() {
        firebaseUser = getAuth().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.reload();
        }
    }

    /**
     * initiate FireBase authentication Listener
     * @return FirebaseAuth.AuthStateListener
     */
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

    /**
     * stop FirebaseAuth.AuthStateListener
     */
    public static void removeListener() {
        if (authListener!= null)
            auth.removeAuthStateListener(authListener);
    }


}