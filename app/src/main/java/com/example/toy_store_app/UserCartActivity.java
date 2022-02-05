package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.log;
import static com.example.toy_store_app.services.FF.logToFireBase;
import static com.example.toy_store_app.services.FF.toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toy_store_app.adapters.StoreItemListViewAdapter;
import com.example.toy_store_app.firebase.FirebaseAT;
import com.example.toy_store_app.firebase.FirebaseDB;
import com.example.toy_store_app.services.OrderCompleted;
import com.example.toy_store_app.services.StoreItem;
import com.example.toy_store_app.services.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UserCartActivity extends AppCompatActivity {

    private TextView headerTV;
    private ListView orderList;
    private TextView totalPriceTV;
    private Button backToShoppingBtn;
    private Button purchaseBtn;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        headerTV = (TextView) findViewById(R.id.userCartActivity_tv_cartHeader);
        orderList = (ListView) findViewById(R.id.userCartActivity_lv_orderList);
        totalPriceTV = (TextView) findViewById(R.id.userCartActivity_tv_totalPrice);
        backToShoppingBtn = (Button) findViewById(R.id.userCartActivity_btn_continueShopping);
        purchaseBtn = (Button) findViewById(R.id.userCartActivity_btn_purchase);

        user = new User();
        getUser();

        backToShoppingBtn.setOnClickListener(v -> finish());
        purchaseBtn.setOnClickListener((v) -> {

            if( FirebaseAT.getAuth().getUid() == null) {
                Intent registerNewIntent = new Intent(this,RegisterActivity.class);
                registerNewIntent.putExtra("returnedUser", true);
                startActivity(registerNewIntent);
            }

            if(!user.getOrder().getCart().isEmpty()) {
                //update owner list
                OrderCompleted completedOrder = new OrderCompleted(
                        user.getOrder().getCart(),
                        user,
                        Calendar.getInstance().getTime().toString()
                );
                FirebaseDB
                        .getDataReference()
                        .child(FirebaseDB.ORDER_CHILD)
                        .child(user.getId())
                        .push()
                        .setValue(completedOrder);
                for (int i = 0; i < user.getOrder().getCart().size(); i ++) {
                    FirebaseDB
                            .getDataReference()
                            .child(FirebaseDB.USERS_CHILD)
                            .child(user.getId())
                            .child(User.USER_ORDER)
                            .child(User.USER_CART)
                            .child(String.valueOf(i))
                            .removeValue();
                }


                //send email to buyer
                String mailText = String.format("This buyer \'%s\'\n" +
                        "has completed his online purchase\n\n", user.getName());
                for(StoreItem item: user.getOrder().getCart())
                    mailText = mailText.concat( item.toString() + "\n");
                mailText = mailText.concat(String.format("Total price %.2f $\n", user.getOrder().getTotalPrice()));
                mailText = mailText.concat("\nCostumer:\n" + user.toString());
                composeEmail(new String[]{"gudimov7@gmail.com"}, mailText);

                finish();
            }

        });

        orderList.setOnItemClickListener(((parent, view, position, id) -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_store_item_full_view);

            TextView itemHeaderName = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemNameHeader);
            ImageView itemImage = dialog.findViewById(R.id.fullStoreIte_dialog_iv_itemPic);
            TextView itemName = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemName);
            TextView itemAge = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemAge);
            TextView itemColor = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemColor);
            TextView itemMaterial = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMaterial);
            TextView itemMade = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemMade);
            TextView itemPrice = dialog.findViewById(R.id.fullStoreItem_dialog_tv_itemPrice);
            Button backBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_back);
            Button removeBtn = dialog.findViewById(R.id.fullStoreItem_dialog_btn_buy);

            StoreItem item = user.getOrder().getCart().get(position);
            itemHeaderName.setText(item.getItemName());
            Picasso.get().load(item.getPic()).resize(70,70).into(itemImage);
            itemName.setText("Name:\t" + item.getItemName());
            itemAge.setText("Age:\t" + item.getDescription().getAge());
            itemColor.setText("Color:\t" + item.getDescription().getColor());
            itemMaterial.setText("Material:\t" + item.getDescription().getMaterial());
            itemMade.setText("Made in:\t" + item.getDescription().getMade());
            itemPrice.setText(String.format("Price:\t%.2f",item.getPrice()));

            removeBtn.setText("REMOVE");
            backBtn.setOnClickListener(v -> dialog.dismiss());
            removeBtn.setOnClickListener(v -> {
                user.getOrder().getCart().remove(position);
                refreshCart();
                dialog.dismiss();
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }));

    }

    private void refreshCart() {
        StoreItemListViewAdapter SIA = new StoreItemListViewAdapter(UserCartActivity.this,R.layout.layout_store_item_list_row, user.getOrder().getCart());
        totalPriceTV.setText(String.format("Total price:\t %.2f$",user.getOrder().getTotalPrice()));
        orderList.setAdapter(SIA);
    }
    private void getUser() {
        FirebaseDB.getDataReference().child(FirebaseDB.USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if(data.getKey().equals(FirebaseAT.getAuth().getUid())) {
                        user = data.getValue(User.class);
                        refreshCart();
                    }
                }
                log(UserCartActivity.class,"fetched user successfully");
                logToFireBase(UserCartActivity.this,"fetched user successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(UserCartActivity.class,"fetched user canceled");
                logToFireBase(UserCartActivity.this,"fetched user canceled");
            }
        });
    }

    private void composeEmail(String[] addresses, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, " Purchase");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        intent.setData(Uri.parse("mailto:"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else Toast.makeText(this, "No suitable app for this action", Toast.LENGTH_SHORT).show();
    }
}