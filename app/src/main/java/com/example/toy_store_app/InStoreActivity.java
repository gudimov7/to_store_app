package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.log;
import static com.example.toy_store_app.services.FF.logToFireBase;
import static com.example.toy_store_app.services.FF.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.toy_store_app.adapters.StoreItemListViewAdapter;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.ItemDescription;
import com.example.toy_store_app.services.StoreItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class InStoreActivity extends AppCompatActivity {

    private ListView itemsList;
    private ArrayList<StoreItem> inStoreItems;
    private Spinner sortSpinner;
    private ImageButton userInfoBtn;
    private ImageButton cartBtn;
    private NotificationBadge cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store);

        itemsList = (ListView) findViewById(R.id.inStoreActivity_lv_itemsList);
        sortSpinner = (Spinner) findViewById(R.id.inStoreActivity_sp_sortSpinner);
        userInfoBtn = (ImageButton) findViewById(R.id.inStoreActivity_ib_userInfoBtn);
        cartBtn = (ImageButton) findViewById(R.id.inStoreActivity_ib_cartBtn);
        cartBadge = (NotificationBadge) findViewById(R.id.inStoreActivity_nb_cartBadge);
        inStoreItems = new ArrayList<>();

        refreshList();
        itemsList.setOnItemClickListener(((parent, view, position, id) -> {
            Dialog dialog = new Dialog(InStoreActivity.this);
            dialog.setContentView(R.layout.dialog_store_item_full_view);

            TextView itemNameHeader = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemNameHeader);
            TextView itemName = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemName);
            TextView itemAge = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemAge);
            TextView itemColor = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemColor);
            TextView itemMaterial = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMaterial);
            TextView itemMade = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMade);
            TextView itemPrice = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemPrice);
            ImageView itemPic = dialog.findViewById(R.id.fullStoreIte_dialog_iv_itemPic);
            Button backBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_back);
            Button buyBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_buy);

            itemNameHeader.setText(inStoreItems.get(position).getItemName());
            itemName.setText("Name:\t" + inStoreItems.get(position).getItemName());
            itemAge.setText("Age:\t" + inStoreItems.get(position).getDescription().getAge() + "+");
            itemColor.setText("Color:\t" + inStoreItems.get(position).getDescription().getColor());
            itemMaterial.setText("Material:\t" + inStoreItems.get(position).getDescription().getMaterial());
            itemMade.setText("Made in:\t" + inStoreItems.get(position).getDescription().getMade());
            itemPrice.setText(String.format("Price:\t%.2f$", inStoreItems.get(position).getPrice()));
            Picasso.get().load(inStoreItems.get(position).getPic()).resize(70,70).into(itemPic);

            backBtn.setOnClickListener(v -> dialog.dismiss());
            buyBtn.setOnClickListener(v-> {
                //TODO: add item to cart
                dialog.dismiss();
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }));

        cartBtn.setOnClickListener(v -> startActivity(new Intent(InStoreActivity.this, UserCartActivity.class)));
        userInfoBtn.setOnClickListener(v -> startActivity(new Intent(InStoreActivity.this, RegisterActivity.class)));

        sortSpinner.setPrompt("Sort by");
        sortBy();
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortSpinner.getChildAt(0).setClickable(false);
                switch (position) {
                    case 1:
                        Collections.sort(inStoreItems, (o1, o2) ->{
                            return o1.getItemName().compareTo(o2.getItemName());
                        });
                        break;
                    case 2:
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return o2.getItemName().compareTo(o1.getItemName());
                        });
                        break;
                    case 3:
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return (int)( o1.getPrice() - o2.getPrice());
                        });
                        break;
                    case 4:
                        Collections.sort(inStoreItems, (o1, o2) -> {
                            return (int)( o2.getPrice() - o1.getPrice());
                        });
                        break;
                }
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void refreshList() {
        inStoreItems.clear();
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
                    inStoreItems.add(item);
                }
                StoreItemListViewAdapter SIA = new StoreItemListViewAdapter(InStoreActivity.this,R.layout.layout_store_item_list_row, inStoreItems);
                itemsList.setAdapter(SIA);
                log(InStoreActivity.class,"fetched item list successfully");
                logToFireBase(InStoreActivity.this,"fetched item list successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(InStoreActivity.class,"refresh list canceled");
                logToFireBase(InStoreActivity.this,"refresh canceled");
            }
        });


    }
    private void updateBadge() {
        cartBadge.setNumber(1);
    }
    private void sortBy() {
        ArrayAdapter<CharSequence> sortByAdp = ArrayAdapter.createFromResource(this, R.array.sort_by, R.layout.textview_spinner_single_row);
        sortSpinner.setAdapter(sortByAdp);
    }
}