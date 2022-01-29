package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.toy_store_app.firebase.FirebaseAT;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.Address;
import com.example.toy_store_app.services.User;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText phoneET;
    private EditText streetET;
    private EditText cityET;
    private EditText countryET;
    private Button clearBtn;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameET = (EditText) findViewById(R.id.registrationActivity_et_name);
        emailET = (EditText) findViewById(R.id.registrationActivity_et_email);
        passwordET = (EditText) findViewById(R.id.registrationActivity_et_password);
        phoneET = (EditText) findViewById(R.id.registrationActivity_et_phone);
        streetET = (EditText) findViewById(R.id.registrationActivity_et_street);
        cityET = (EditText) findViewById(R.id.registrationActivity_et_city);
        countryET = (EditText) findViewById(R.id.registrationActivity_et_country);
        clearBtn = (Button) findViewById(R.id.registrationActivity_btn_clear);
        submitBtn = (Button) findViewById(R.id.registrationActivity_btn_submit);

        clearBtn.setOnClickListener(v -> {
            nameET.setText("");
            emailET.setText("");
            passwordET.setText("");
            phoneET.setText("");
            streetET.setText("");
            cityET.setText("");
            countryET.setText("");
        });
        submitBtn.setOnClickListener(v -> {
            if (
                    !isEditTextEmpty(nameET) &&
                    !isEditTextEmpty(emailET) &&
                    !isEditTextEmpty(passwordET) &&
                    !isEditTextEmpty(phoneET) &&
                    !isEditTextEmpty(streetET) &&
                    !isEditTextEmpty(cityET) &&
                    !isEditTextEmpty(countryET)
            ) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String phone = phoneET.getText().toString();
                String street = streetET.getText().toString();
                String city = cityET.getText().toString();
                String country = countryET.getText().toString();

                FirebaseAT.getAuth().createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    FirebaseUser fUser = FirebaseAT.getAuth().getCurrentUser();
                    User user = new User(fUser.getUid(), name, phone, new Address(street, city, country));
                    FirebaseDB.getDataReference().child(FirebaseDB.USERS_CHILD).child(fUser.getUid()).setValue(user);
                    toast(this,"User created successfully");
                    log(LoginActivity.class,FirebaseAT.getAuth().getUid() + ": registered in successfully");
                    logToFireBase(this,FirebaseAT.getAuth().getUid() + ": registered in successfully");
                    finish();
                }).addOnFailureListener(authResult -> {
                    toast(this,"User create failed");
                });

            } else {
                toast(this, "please fill all fields");
            }
        });
    }
}