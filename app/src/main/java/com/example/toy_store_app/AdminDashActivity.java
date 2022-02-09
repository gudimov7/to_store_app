package com.example.toy_store_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;;
import android.os.Bundle;
import android.widget.Button;
import static com.example.toy_store_app.services.FF.*;

/**
 * Admin Activity show admin options
 * @author Vyacheslav Gudimov
 */
public class AdminDashActivity extends AppCompatActivity {
    private Button viewItemsBtn;
    private Button viewOrdersBtn;

    /**
     * first function to start as activity starts
     * @param savedInstanceState if has memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);
        getSupportActionBar().hide();

        //initiate all activity views
        viewItemsBtn = (Button) findViewById(R.id.adminDashActivity_btn_storeItemList);
        viewOrdersBtn = (Button) findViewById(R.id.adminDashActivity_btn_purchases);

        //create all needed permissions array
        final int PERMISSION_ALL = 1;
        final String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CAMERA
        };

        //request all permissions if needed
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        /**
         * StoreItem list button click listener -> start AdminItemList activity
         */
        viewItemsBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminItemsListActivity.class)));

        /**
         * Orders list button click listener -> start AdminPurchaseDash activity
         */
        viewOrdersBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminPurchaseDashActivity.class)));
    }

}