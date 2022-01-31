package com.example.toy_store_app.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.toy_store_app.firebase.FirebaseDB;

import java.util.Calendar;

public abstract class FF {
    public static final String ADMIN_PASS = "123456";
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
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
