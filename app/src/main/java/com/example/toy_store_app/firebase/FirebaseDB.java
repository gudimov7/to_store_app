package com.example.toy_store_app.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FireBase RealTime DataBase private class
 * @author Vyacheslav Gudimov
 */
public class FirebaseDB {
    public static final String USERS_CHILD = "Users";
    public static final String TOYS_CHILD = "store_items";
    public static final String CART_CHILD = "order";
    public static final String ORDER_CHILD = "completed_orders";
    public static final String FIREBASE_LOG_CHILD = "LOG";
    private static final String FIREBASE_ROOT_URL = "https://toystoreapp-edbfc-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static FirebaseDatabase database;

    /**
     * private empty constructor
     */
    private FirebaseDB() { }

    /**
     * get Firebase realtime database
     * @return Firebase realtime database
     */
    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance(FIREBASE_ROOT_URL);
        }
        return database;
    }

    /**
     * get FireBase root reference
     * @return
     */
    public static DatabaseReference getDataReference() {
        return getDatabase().getReference("");
    }

}