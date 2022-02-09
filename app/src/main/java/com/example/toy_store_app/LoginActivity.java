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

/**
 * Activity show Login view
 * @author Vyacheslav Gudimov
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private Button anonymousBtn;
    private Button registerBtn;
    private CheckBox passCB;
    private CheckBox adminCB;


    /**
     * first function to start as activity starts
     * @param savedInstanceState if has memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initiate all views
        usernameET = (EditText) findViewById(R.id.loginActivity_et_username);
        passwordET = (EditText) findViewById(R.id.loginActivity_et_password);
        loginBtn = (Button) findViewById(R.id.loginActivity_btn_login);
        anonymousBtn = (Button) findViewById(R.id.loginActivity_btn_anonymous);
        registerBtn = (Button) findViewById(R.id.loginActivity_btn_register);
        passCB = (CheckBox) findViewById(R.id.loginActivity_cb_password);
        adminCB = (CheckBox) findViewById(R.id.loginActivity_cb_admin);

        //show password on check
        showPassword(passCB,passwordET);

        /**
         * login button click listener -> start InStore activity -> authenticate with Firebase
         *  ! fields cannot be empty
         */
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
                    log(LoginActivity.class,username + ": login failed");
                    logToFireBase(this,username + ": login failed");
                });

            } else {
                toast(this,"Please enter username and password");
            }
        });

        /**
         * anonymous button click listener  -> anonymous auth with Firebase
         */
        anonymousBtn.setOnClickListener((v) -> FirebaseAT.getAuth().signInAnonymously().addOnSuccessListener(authResult -> {
            startActivity(new Intent(LoginActivity.this, InStoreActivity.class));
            toast(this,"Login successfully");
            log(LoginActivity.class,"anonymous login successfully");
            logToFireBase(this,"anonymous login successfully");
        }).addOnFailureListener(authResult -> toast(this, "login failed"))); //login anonymously

        /**
         * register button click listener  -> start Register activity
         */
        registerBtn.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));//move to register activity

        //if checked login button click refer to admin dash activity instead InStore activity
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

    /**
     * function to run on start of activity
     * check if user authenticated
     * initiate auth listener
     */
    @Override
    protected void onStart() {
        super.onStart();
        isLoggedIn();
    }

    /**
     * function to move from activity
     * remove auth listener
     */
    @Override
    protected void onStop() {
        super.onStop();
        removeListener();
    }
}