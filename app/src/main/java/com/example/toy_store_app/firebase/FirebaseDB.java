package com.example.toy_store_app.firebase;

import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FirebaseDB {
    private static FirebaseDatabase database;

    private FirebaseDB() { }

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance("https://toystoreapp-edbfc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        }
        return database;
    }
    public static DatabaseReference getDataReference() {
        return getDatabase().getReference("");
    }

}