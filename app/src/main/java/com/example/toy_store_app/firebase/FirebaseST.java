package com.example.toy_store_app.firebase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * FireBase Storage private class
 * @author Vyacheslav Gudimov
 */
public class FirebaseST {
    public static final String FIREBASE_STORAGE = "gs://toystoreapp-edbfc.appspot.com/";
    public static final String TOYS_FOLDER = "store_items";
    private static FirebaseStorage storage;

    /**
     * private empty constructor
     */
    private FirebaseST() {}

    /**
     * get Firebase Storage reference
     * @return Firebase Storage reference
     */
    public static StorageReference getStorageRef() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance(FIREBASE_STORAGE);
        }
        return storage.getReference();
    }
}