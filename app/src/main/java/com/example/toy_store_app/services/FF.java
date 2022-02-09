package com.example.toy_store_app.services;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.toy_store_app.firebase.FirebaseAT;
import com.example.toy_store_app.firebase.FirebaseDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Helper class
 * static functions for multiple use
 * @author Vyacheslav Ggudimov
 */
public abstract class FF {
    public static final String ADMIN_PASS = "123456";
    private static final String LOG = "LOG";

    /**
     * prompt a message on device
     * @param context for using in activity
     * @param message String message to display
     */
    public static void toast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * log message
     * @param c class name to use as a tag
     * @param message String message to be written
     */
    public static void log(Class c, String message) {
        Log.d(c.getSimpleName(),message + "::" + calendarDate());
    }

    /**
     * show hide password with edit text type password on check box click
     * @param checkBox CheckBox to be clicked
     * @param password Edit text with password input
     */
    public static void showPassword(CheckBox checkBox, EditText password) {
        checkBox.setOnCheckedChangeListener((checkboxView, isChecked) -> {
            if(isChecked)
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });
    }

    /**
     * check if EditText is empty
     * @param editText EditText to check
     * @return return true if EditText is empty
     */
    public static Boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setHintTextColor(Color.RED);
            return true;
        } else {
            return false;
        }
    }

    /**
     * write a message to Firebase data base to log child
     * @param context show which activity sent message
     * @param message String message to be written in log
     */
    public static void logToFireBase(Context context, String message) {
        FirebaseDB.getDataReference()
                .child(LOG)
                .child(context.getClass().getSimpleName())
                .push()
                .setValue(message + ": " + calendarDate());
    }

    /**
     * function to prompt user with needed permission
     * @param context activity that uses permission
     * @param permissions String array with permissions needed
     * @return return true if permissions granted
     */
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

    /**
     * create a file from bitmap image
     * @param context Activity using this function
     * @param bitmap Bitmap image to be converted
     * @param fileNameToSave String fileName to be saved
     * @return File from image
     */
    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) {
        // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

        //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }
    }

    /**
     * Firebase User children to be updated
     * @param context Activity using this function
     * @param key String Key name to be update
     * @param value Object key new value
     */
    public static void updateUserChildren(Context context, String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        FirebaseDB
                .getDataReference()
                .child(FirebaseDB.USERS_CHILD)
                .child(FirebaseAT.getAuth().getUid())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        log(context.getClass(), FirebaseAT.getAuth().getUid() + ": " + key + ": update key successful");
                        logToFireBase(context,FirebaseAT.getAuth().getUid() + ": " + key + ": update key successful");
                    } else {
                        log(context.getClass(), FirebaseAT.getAuth().getUid() + ": " + key + ": update key failed");
                        logToFireBase(context,FirebaseAT.getAuth().getUid() + ": " + key + ": update key failed");
                    }
                });
    }

    /**
     * Firebase Purchases class children to be updated
     * @param context Activity using this function
     * @param key String Key name to be update
     * @param value Object key new value
     */
    public static void updatePurchasesChildren(Context context, String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        FirebaseDB
                .getDataReference()
                .child(FirebaseDB.ORDER_CHILD)
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        log(context.getClass(), FirebaseAT.getAuth().getUid() + ": " + key + ": update key successful");
                        logToFireBase(context,FirebaseAT.getAuth().getUid() + ": " + key + ": update key successful");
                    } else {
                        log(context.getClass(), FirebaseAT.getAuth().getUid() + ": " + key + ": update key failed");
                        logToFireBase(context,FirebaseAT.getAuth().getUid() + ": " + key + ": update key failed");
                    }
                });
    }

    /**
     * function to send email message to email address
     * @param context activity using this function
     * @param addresses String array wit email addresses to receive message
     * @param text  String message to be sent
     */
    public static void composeEmail(Context context, String[] addresses, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, " Purchase");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        intent.setData(Uri.parse("mailto:"));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else Toast.makeText(context, "No suitable app for this action", Toast.LENGTH_SHORT).show();
    }

    /**
     * get current date stamp
     * @return String value of date stamp HH:mm_dd/MM/YYYY
     */
    public static String calendarDate() {
        Calendar cal = Calendar.getInstance();
        return String.format(
                "%02d:%02d_%02d/%02d/%04d",
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) +1,
                cal.get(Calendar.YEAR)
        );
    }
}
