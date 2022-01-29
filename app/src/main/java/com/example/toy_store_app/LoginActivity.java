package com.example.toy_store_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.example.toy_store_app.firebase.FirebaseAT.*;
import static com.example.toy_store_app.services.FF.*;

import com.example.toy_store_app.firebase.FirebaseAT;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private Button anonymousBtn;
    private Button registerBtn;
    private CheckBox passCB;
    private CheckBox adminCB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.loginActivity_et_username);
        passwordET = (EditText) findViewById(R.id.loginActivity_et_password);
        loginBtn = (Button) findViewById(R.id.loginActivity_btn_login);
        anonymousBtn = (Button) findViewById(R.id.loginActivity_btn_anonymous);
        registerBtn = (Button) findViewById(R.id.loginActivity_btn_register);
        passCB = (CheckBox) findViewById(R.id.loginActivity_cb_password);
        adminCB = (CheckBox) findViewById(R.id.loginActivity_cb_admin);

        showPassword(passCB,passwordET); //show password on check
        loginBtn.setOnClickListener((v) -> {
            if (!isEditTextEmpty(usernameET) && ! isEditTextEmpty(passwordET)) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                FirebaseAT.getAuth().signInWithEmailAndPassword(username, password).addOnSuccessListener(authResult -> {
                    if (adminCB.isChecked()) {
                        startActivity(new Intent(LoginActivity.this,AdminDashActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this,InStoreActivity.class));
                    }
                    toast(this,"Login successfully");
                    log(LoginActivity.class,username + ": logged in successfully");
                    logToFireBase(this,username + ": logged in successfully");
                    finish();
                }).addOnFailureListener(authResult -> {
                    toast(this, "login denied");
                    log(LoginActivity.class,username + ": logg in failed");
                    logToFireBase(this,username + ": logg in failed");
                });

            } else {
                toast(this,"Please enter username and password");
            }
        }); //login with username and password
        anonymousBtn.setOnClickListener((v) -> FirebaseAT.getAuth().signInAnonymously().addOnSuccessListener(authResult -> {
            toast(this,"Login successfully");
            log(LoginActivity.class,"anonymous\tlogged in successfully");
            logToFireBase(this,"anonymous\tlogged in successfully");
        }).addOnFailureListener(authResult -> toast(this, "login failed"))); //login anonymously
        registerBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));//move to register activity
        adminCB.setOnClickListener(view -> {
            Dialog dialog = new Dialog(LoginActivity.this);
            dialog.setContentView(R.layout.dialog_login_as_admin);
            EditText dialogPasswordET = (EditText) dialog.findViewById(R.id.loginActivity_dialog_et_password);
            Button dialogSubmitBtn = (Button) dialog.findViewById(R.id.loginActivity_dialog_btn_submit);
            adminCB.setChecked(false);
            dialogSubmitBtn.setOnClickListener(v -> {
                if (dialogPasswordET.getText().toString().equals(ADMIN_PASS))
                    adminCB.setChecked(true);
                else adminCB.setChecked(false);
                dialog.dismiss();
            });
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        });//check to login as admin

    }

    @Override
    protected void onStart() {
        super.onStart();
        isLoggedIn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeListener();
    }
}