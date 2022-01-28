package com.example.toy_store_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.example.toy_store_app.firebase.FirebaseAT.*;
import static com.example.toy_store_app.services.FF.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private Button anonymousBtn;
    private Button registerBtn;
    private CheckBox passCB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.loginActivity_et_username);
        passwordET = (EditText) findViewById(R.id.loginActivity_et_password);
        loginBtn = (Button) findViewById(R.id.loginActivity_btn_login);
        anonymousBtn = (Button) findViewById(R.id.loginActivity_btn_anonymous);
        registerBtn = (Button) findViewById(R.id.loginActivity_btn_register);
        passCB = (CheckBox) findViewById(R.id.loginActivity_sb_password);

        showPassword(passCB,passwordET); //show password on check
        loginBtn.setOnClickListener((v) -> {
            if (!isEditTextEmpty(usernameET) && ! isEditTextEmpty(passwordET)) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();


            } else {
                toast(this,"Please enter username and password");

            }
        }); //login with username and password
        anonymousBtn.setOnClickListener((v) -> {

        }); //login anonymously
        registerBtn.setOnClickListener((v) -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });//move to register activity


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