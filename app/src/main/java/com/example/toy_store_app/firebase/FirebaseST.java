package com.example.toy_store_app.firebase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseST {
    public static final String FIREBASE_STORAGE = "gs://toystoreapp-edbfc.appspot.com/";


    private static FirebaseStorage storage;
    private FirebaseST() {}
    public static StorageReference getStorageRef() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance(FIREBASE_STORAGE);
        }
        return storage.getReference();
    }
}