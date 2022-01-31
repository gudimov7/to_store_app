package com.example.toy_store_app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;;
import android.os.Bundle;
import android.widget.Button;
import static com.example.toy_store_app.services.FF.*;
public class AdminDashActivity extends AppCompatActivity {


    private Button viewItemsBtn;
    private Button viewOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        viewItemsBtn = (Button) findViewById(R.id.adminDashActivity_btn_storeItemList);
        viewOrdersBtn = (Button) findViewById(R.id.adminDashActivity_btn_purchases);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        viewItemsBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminItemsListActivity.class)));
        viewOrdersBtn.setOnClickListener(v -> startActivity(new Intent(AdminDashActivity.this,AdminPurchaseDashActivity.class)));
    }

}