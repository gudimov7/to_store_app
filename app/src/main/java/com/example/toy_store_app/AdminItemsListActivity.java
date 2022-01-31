package com.example.toy_store_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.EventLogTags;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.toy_store_app.adapters.StoreItemAdminDashAdapter;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.firebase.FirebaseST;
import com.example.toy_store_app.services.ItemDescription;
import com.example.toy_store_app.services.StoreItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.toy_store_app.services.FF.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminItemsListActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ListView itemsLV;
    private Button addBtn;
    private ArrayList<StoreItem> storeItems;
    private Dialog dialog;
    private ImageButton itemPicIV;
    private Bitmap bmpImg;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_items_list);
        storeItems = new ArrayList<>();

        itemsLV = (ListView) findViewById(R.id.adminItemListActivity_lv_storeItemList);
        addBtn = (Button) findViewById(R.id.adminItemListActivity_btn_add);

        addBtn.setOnClickListener(v -> {
            dialog = new Dialog(AdminItemsListActivity.this);
            dialog.setContentView(R.layout.dialog_store_item_add_new);
            itemPicIV = (ImageButton) dialog.findViewById(R.id.adminStoreItem_dialog_iv_itemPic);
            EditText itemNameET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemName);
            EditText itemAgeET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemAge);
            EditText itemColorET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemColor);
            EditText itemMaterialET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemMaterial);
            EditText itemMadeET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemMade);
            EditText itemPriceET = (EditText) dialog.findViewById(R.id.adminStoreItem_dialog_et_itemPrice);
            Button addBtn = (Button) dialog.findViewById(R.id.adminStoreItem_dialog_btn_add);
            Button clearBtn = (Button) dialog.findViewById(R.id.adminStoreItem_dialog_btn_clear);

            itemPicIV.setOnClickListener(iv -> {
                if (!isEditTextEmpty(itemNameET)) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");

                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent,camIntent});

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            });
            clearBtn.setOnClickListener(cv -> {
                itemNameET.setText("");
                itemAgeET.setText("");
                itemColorET.setText("");
                itemMaterialET.setText("");
                itemMadeET.setText("");
                itemPriceET.setText("");
            });
            addBtn.setOnClickListener(av -> {
                if (
                        bmpImg != null &&
                        !isEditTextEmpty(itemNameET) &&
                        !isEditTextEmpty(itemAgeET) &&
                        !isEditTextEmpty(itemColorET) &&
                        !isEditTextEmpty(itemMaterialET) &&
                        !isEditTextEmpty(itemMadeET) &&
                        !isEditTextEmpty(itemPriceET)

                ) {
                    String itemName = itemNameET.getText().toString();
                    String itemAge = itemAgeET.getText().toString();
                    String itemColor = itemColorET.getText().toString();
                    String itemMaterial = itemMaterialET.getText().toString();
                    String itemMade = itemMadeET.getText().toString();
                    float itemPrice = Float.parseFloat(itemPriceET.getText().toString());

                    File file = bitmapToFile(this,bmpImg,itemName +".jpeg");
                    Uri fileURI = Uri.fromFile(file);

                    FirebaseST.
                            getStorageRef().
                            child(FirebaseST.
                            TOYS_FOLDER).
                            child(itemName).
                            putFile(fileURI).
                            addOnFailureListener(e -> {
                                toast(this,"Upload file to storage failed");
                                log(AdminItemsListActivity.class,"Upload file to storage failed" + e.getMessage());
                                logToFireBase(this,"Upload file to storage failed" + e.getMessage());
                            }).addOnSuccessListener(taskSnapshot -> {
                                toast(this,"Upload file to storage successful");
                                log(AdminItemsListActivity.class,"Upload file to storage successful");
                                logToFireBase(this,"Upload file to storage successful");
                     }).continueWithTask(task -> {
                         if (!task.isSuccessful()) {
                             throw task.getException();
                         }
                         StorageReference ref = FirebaseST.getStorageRef().child(FirebaseST.TOYS_FOLDER).child(itemName);
                         return ref.getDownloadUrl();
                     }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imgPath = downloadUri.getPath();
                            StoreItem item = new StoreItem(
                                    itemName,
                                    new ItemDescription(
                                            itemAge,
                                            itemColor,
                                            itemMaterial,
                                            itemMade
                                    ),
                                    itemPrice,
                                    imgPath
                            );
                            storeItems.add(item);
                            FirebaseDB.getDataReference().child(FirebaseDB.TOYS_CHILD).child(itemName).setValue(item);
                            log(AdminItemsListActivity.class,"add item successfully");
                            logToFireBase(this,"add item successfully");
                            toast(this,"add item successfully");
                            refreshLV();
                        } else {
                            toast(AdminItemsListActivity.this,"something went wrong");
                        }
                    });

                    dialog.dismiss();
                }
            });


            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        });
        refreshLV();

    }


    void refreshLV() {
        FirebaseDB.getDataReference().child(FirebaseDB.TOYS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    StoreItem item = new StoreItem();
                    for (DataSnapshot items: data.getChildren()) {
                        item.setDescription(data.child(StoreItem.ITEM_DESCRIPTION).getValue(ItemDescription.class));
                        item.setItemName(data.child(StoreItem.ITEM_NAME).getValue(String.class));
                        item.setPic(data.child(StoreItem.ITEM_PIC).getValue(String.class));
                        item.setPrice(data.child(StoreItem.ITEM_PRICE).getValue(Float.class));
                    }
                    storeItems.add(item);

                }
                StoreItemAdminDashAdapter SIADA = new StoreItemAdminDashAdapter(AdminItemsListActivity.this,R.layout.layout_store_item_admin_dash,storeItems);
                itemsLV.setAdapter(SIADA);
                toast(AdminItemsListActivity.this,"fetched item list successfully");
                log(AdminItemsListActivity.class,"fetched item list successfully");
                logToFireBase(AdminItemsListActivity.this,"fetched item list successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toast(AdminItemsListActivity.this,"refresh list got wrong");
                log(AdminItemsListActivity.class,"refresh list got wrong");
                logToFireBase(AdminItemsListActivity.this,"refresh list got wrong");
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Uri imgURI = null;
            if (data.hasExtra("data")) {
                bmpImg = (Bitmap) data.getExtras().get("data");
                imgPath = MediaStore.Images.Media.insertImage(getContentResolver(), bmpImg , Calendar.getInstance().getTime().toString()," ");
            } else {
                imgURI = data.getData();
                imgPath = imgURI.getPath();
                try {
                    bmpImg = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            itemPicIV.setImageBitmap(bmpImg);


        }
    }


}