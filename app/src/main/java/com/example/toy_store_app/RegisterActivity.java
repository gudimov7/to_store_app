package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.toy_store_app.firebase.FirebaseAT;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.Address;
import com.example.toy_store_app.services.Order;
import com.example.toy_store_app.services.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private TextView headerTV;

    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText phoneET;
    private EditText streetET;
    private EditText cityET;
    private EditText countryET;

    private TextView nameTV;
    private TextView emailTV;
    private TextView passwordTV;
    private TextView phoneTV;
    private TextView streetTV;
    private TextView cityTV;
    private TextView countryTV;

    private Button clearBtn;
    private Button submitBtn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        Intent onStartIntent = getIntent();
        boolean returnedUser = onStartIntent.getBooleanExtra("returnedUser", false);

        headerTV = (TextView) findViewById(R.id.registrationActivity_tv_header);

        nameET = (EditText) findViewById(R.id.registrationActivity_et_name);
        emailET = (EditText) findViewById(R.id.registrationActivity_et_email);
        passwordET = (EditText) findViewById(R.id.registrationActivity_et_password);
        phoneET = (EditText) findViewById(R.id.registrationActivity_et_phone);
        streetET = (EditText) findViewById(R.id.registrationActivity_et_street);
        cityET = (EditText) findViewById(R.id.registrationActivity_et_city);
        countryET = (EditText) findViewById(R.id.registrationActivity_et_country);

        nameTV = (TextView) findViewById(R.id.registrationActivity_tv_name);
        emailTV = (TextView) findViewById(R.id.registrationActivity_tv_email);
        passwordTV = (TextView) findViewById(R.id.registrationActivity_tv_password);
        phoneTV = (TextView) findViewById(R.id.registrationActivity_tv_phone);
        streetTV = (TextView) findViewById(R.id.registrationActivity_tv_street);
        cityTV = (TextView) findViewById(R.id.registrationActivity_tv_city);
        countryTV = (TextView) findViewById(R.id.registrationActivity_tv_country);

        clearBtn = (Button) findViewById(R.id.registrationActivity_btn_clear);
        submitBtn = (Button) findViewById(R.id.registrationActivity_btn_submit);

        user = new User();

        if (returnedUser) {
            viewCurrentUserActivity();
        } else {
            createNewUserActivity();
        }


    }

    private void viewCurrentUserActivity() {
        //change header text
        headerTV.setText("USER");

        //set EditTexts invisible
        nameET.setVisibility(View.GONE);
        emailET.setVisibility(View.GONE);
        passwordET.setVisibility(View.GONE);
        phoneET.setVisibility(View.GONE);
        streetET.setVisibility(View.GONE);
        cityET.setVisibility(View.GONE);
        countryET.setVisibility(View.GONE);

        //set TextViews visible
        nameTV.setVisibility(View.VISIBLE);
        emailTV.setVisibility(View.VISIBLE);
        passwordTV.setVisibility(View.VISIBLE);
        phoneTV.setVisibility(View.VISIBLE);
        streetTV.setVisibility(View.VISIBLE);
        cityTV.setVisibility(View.VISIBLE);
        countryTV.setVisibility(View.VISIBLE);

        //change buttons texts
        clearBtn.setText("Edit");
        submitBtn.setText("Close");

        FirebaseDB
                .getDataReference()
                .child(FirebaseDB.USERS_CHILD)
                .child(FirebaseAT.getAuth().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user.setId(FirebaseAT.getAuth().getUid());
                        user.setName(snapshot.child(User.USER_NAME).getValue(String.class));
                        user.setPhone(snapshot.child(User.USER_PHONE).getValue(String.class));
                        user.setAdmin(snapshot.child(User.USER_IS_ADMIN).getValue(Boolean.class));
                        user.setAddress(snapshot.child(User.USER_ADDRESS).getValue(Address.class));
                        user.setOrder(snapshot.child(User.USER_ORDER).getValue(Order.class));

                        nameTV.setText(user.getName());
                        emailTV.setText(FirebaseAT.getAuth().getCurrentUser().getEmail());
                        passwordTV.setText(String.format("%1$c %1$c %1$c %1$c %1$c %1$c",'\u2022'));
                        phoneTV.setText(user.getPhone());
                        streetTV.setText(user.getAddress().getStreet());
                        cityTV.setText(user.getAddress().getCity());
                        countryTV.setText(user.getAddress().getCountry());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        //buttons on click actions
        clearBtn.setOnClickListener(v -> editReturnedUser());
        submitBtn.setOnClickListener(v -> finish());
    }

    private void createNewUserActivity() {
        //set TextViews invisible
        nameTV.setVisibility(View.GONE);
        emailTV.setVisibility(View.GONE);
        passwordTV.setVisibility(View.GONE);
        phoneTV.setVisibility(View.GONE);
        streetTV.setVisibility(View.GONE);
        cityTV.setVisibility(View.GONE);
        countryTV.setVisibility(View.GONE);

        //set EditTexts visible
        nameET.setVisibility(View.VISIBLE);
        emailET.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        phoneET.setVisibility(View.VISIBLE);
        streetET.setVisibility(View.VISIBLE);
        cityET.setVisibility(View.VISIBLE);
        countryET.setVisibility(View.VISIBLE);

        //change buttons texts
        clearBtn.setText("Clear");
        submitBtn.setText("Submit");

        //buttons on click actions
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
                    user = new User(fUser.getUid(), name, phone, new Address(street, city, country));
                    FirebaseDB.getDataReference().child(FirebaseDB.USERS_CHILD).child(fUser.getUid()).setValue(user);
                    toast(this,"User created successfully");
                    log(RegisterActivity.class,FirebaseAT.getAuth().getUid() + ": registered in successfully");
                    logToFireBase(this,FirebaseAT.getAuth().getUid() + ": registered in successfully");
                    finish();
                }).addOnFailureListener(authResult -> {
                    toast(this,"User create failed");
                    log(RegisterActivity.class,email + ": registered failed");
                    logToFireBase(this,email + ": registered failed");
                });

            } else {
                toast(this, "please fill all fields");
            }
        });
    }

    private void editReturnedUser() {
        //set TextViews invisible
        nameTV.setVisibility(View.GONE);
        emailTV.setVisibility(View.GONE);
        passwordTV.setVisibility(View.GONE);
        phoneTV.setVisibility(View.GONE);
        streetTV.setVisibility(View.GONE);
        cityTV.setVisibility(View.GONE);
        countryTV.setVisibility(View.GONE);

        //set EditTexts visible
        nameET.setVisibility(View.VISIBLE);
        emailET.setVisibility(View.VISIBLE);
        passwordET.setVisibility(View.VISIBLE);
        phoneET.setVisibility(View.VISIBLE);
        streetET.setVisibility(View.VISIBLE);
        cityET.setVisibility(View.VISIBLE);
        countryET.setVisibility(View.VISIBLE);

        //set EditTexts hints
        nameET.setText(user.getName());
        emailET.setText(FirebaseAT.getAuth().getCurrentUser().getEmail());
        passwordET.setText(String.format("%1$c %1$c %1$c %1$c %1$c %1$c",'\u2022'));
        phoneET.setText(user.getPhone());
        streetET.setText(user.getAddress().getStreet());
        cityET.setText(user.getAddress().getCity());
        countryET.setText(user.getAddress().getCountry());

        //change buttons texts
        clearBtn.setText("Clear");
        submitBtn.setText("Submit");

        //buttons on click actions
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

                if (
                        !street.equals(user.getAddress().getStreet()) &&
                        !city.equals(user.getAddress().getCity()) &&
                        !country.equals(user.getAddress().getCountry())
                ) {
                    Address updatedAddress = new Address(street, city, country);
                    updateUserChildren(this, User.USER_ADDRESS, updatedAddress);
                }
                if (!name.equals(user.getName())) {
                    updateUserChildren(this, User.USER_NAME, name);
                }
                if (!phone.equals(user.getPhone())) {
                    updateUserChildren(this,User.USER_PHONE, phone);
                }
                if (!email.equals(FirebaseAT.getAuth().getCurrentUser().getEmail())) {
                    FirebaseAT
                            .getAuth()
                            .getCurrentUser()
                            .updateEmail(email)
                            .addOnCompleteListener(onChangeCredentialCompleteListener);
                }
                if (!password.contains("\u2022")) {
                    FirebaseAT
                            .getAuth()
                            .getCurrentUser()
                            .updatePassword(password)
                            .addOnCompleteListener(onChangeCredentialCompleteListener);
                }
                viewCurrentUserActivity();
            } else {
                toast(this, "please fill all fields");
            }
        });

    }

    private OnCompleteListener onChangeCredentialCompleteListener = (task) -> {
        if (task.isSuccessful()) {
            toast(this,"update credential successful");
            log(RegisterActivity.class, FirebaseAT.getAuth().getUid() + ": update credential successful");
            logToFireBase(this,FirebaseAT.getAuth().getUid() + ": update credential successful");
        } else {
            toast(this,"update credential failed");
            log(RegisterActivity.class, FirebaseAT.getAuth().getUid() + ": update credential failed");
            logToFireBase(this, FirebaseAT.getAuth().getUid() + ": update credential failed");
        }
    };
}