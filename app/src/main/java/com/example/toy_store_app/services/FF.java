package com.example.toy_store_app.services;

import android.content.Context;
import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toy_store_app.firebase.FirebaseDB;

import java.util.Calendar;

public abstract class FF {
    private static final String LOG = "LOG";
    public static void toast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void log(Class c, String message) {
        Log.d(c.getSimpleName(),message + "::" + Calendar.getInstance().getTime());
    }
    public static void showPassword(CheckBox checkBox, EditText password) {
        checkBox.setOnCheckedChangeListener((checkboxView, isChecked) -> {
            if(isChecked)
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });
    }
    public static Boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setHintTextColor(Color.RED);
            return true;
        } else {
            return false;
        }
    }
    public static void logToFireBase(Context context, String message) {
        FirebaseDB.getDataReference()
                .child(LOG)
                .child(context.getClass().getSimpleName())
                .push()
                .setValue(message + ": " + Calendar.getInstance().getTime());
    }

}
