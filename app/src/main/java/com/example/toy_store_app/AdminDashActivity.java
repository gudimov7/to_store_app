package com.example.toy_store_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AdminDashActivity extends AppCompatActivity {

    private Button viewItemsBtn;
    private Button viewOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        viewItemsBtn = (Button) findViewById(R.id.adminDashActivity_btn_storeItemList);
        viewOrdersBtn = (Button) findViewById(R.id.adminDashActivity_btn_purchases);

        viewItemsBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminItemsListActivity.class)));
        viewOrdersBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminPurchaseDashActivity.class)));
    }
}