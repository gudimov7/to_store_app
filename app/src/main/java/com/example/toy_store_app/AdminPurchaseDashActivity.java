package com.example.toy_store_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AdminPurchaseDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_purchase_dash);
        getSupportActionBar().hide();
    }
}