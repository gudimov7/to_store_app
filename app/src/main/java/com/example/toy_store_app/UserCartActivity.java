package com.example.toy_store_app;

import static com.example.toy_store_app.services.FF.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Activity show User obj cart
 * @author Vyacheslav Gudimov
 */
public class UserCartActivity extends AppCompatActivity {

    private TextView headerTV;
    private ListView orderList;
    private TextView totalPriceTV;
    private Button backToShoppingBtn;
    private Button purchaseBtn;

    private User user;

    /**
     * first function to start as activity starts
     * @param savedInstanceState if has memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        //initiate activity views
        headerTV = (TextView) findViewById(R.id.userCartActivity_tv_cartHeader);
        orderList = (ListView) findViewById(R.id.userCartActivity_lv_orderList);
        totalPriceTV = (TextView) findViewById(R.id.userCartActivity_tv_totalPrice);
        backToShoppingBtn = (Button) findViewById(R.id.userCartActivity_btn_continueShopping);
        purchaseBtn = (Button) findViewById(R.id.userCartActivity_btn_purchase);

        //initiate User obj
        user = new User();
        //authenticate User
        getUser();

        //on back button click function -> finish activity
        backToShoppingBtn.setOnClickListener(v -> finish());

        /**
         * on purchase button click listener
         * 1. check anonymous User clicked -> send to Register activity new User
         * 2. if cart is  not empty ->  * create new OrderCompleted obj
         *                              * send Store owner email message with order
         */
        purchaseBtn.setOnClickListener((v) -> {
            //if anonymous user logged in
            if( FirebaseAT.getAuth().getCurrentUser().getEmail() == null)
                handleAnonymous(UserCartActivity.this);

            //if cart not empty
            if(!user.getOrder().getCart().isEmpty()) {
                //update owner list
                OrderCompleted completedOrder = new OrderCompleted(
                        user.getOrder().getCart(),
                        user,
                        calendarDate()
                );
                FirebaseDB
                        .getDataReference()
                        .child(FirebaseDB.ORDER_CHILD)
                        .child(user.getId())
                        .push()
                        .setValue(completedOrder);

                toast(this,"Order submitted");


                //send email to buyer
                String mailText = String.format("This buyer \'%s\'\n" +
                        "has completed his online purchase\n\n", user.getName());
                for(StoreItem item: completedOrder.getCart())
                    mailText = mailText.concat(item.toString() + "\n");
                mailText = mailText.concat(String.format("Total price %.2f $\n", completedOrder.getTotalPrice()));
                mailText = mailText.concat("\nCostumer:\n" + user.toString());
                composeEmail(this,new String[]{"gudimov7@gmail.com"}, mailText);

                //clear users complete order
                user.getOrder().getCart().clear();
                updateUserChildren(this, User.USER_ORDER, user.getOrder());

                log(UserCartActivity.class,FirebaseAT.getAuth().getUid() + " : Order completed");
                logToFireBase(this,FirebaseAT.getAuth().getUid() + " : Order completed");

                finish();
            }
        });

        /**
         * set on Order ListView click listener
         * show user Full StoreItem description
         * Allow to remove item from cart
         */
        orderList.setOnItemClickListener(((parent, view, position, id) -> {
            //create new dialog with item description
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_store_item_full_view);

            //initiate dialog views
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

            //set dialog description from StoreItem in position clicked
            StoreItem item = user.getOrder().getCart().get(position);
            itemHeaderName.setText(item.getItemName());
            Picasso.get().load(item.getPic()).resize(70,70).into(itemImage);
            itemName.setText("Name:\t" + item.getItemName());
            itemAge.setText("Age:\t" + item.getDescription().getAge());
            itemColor.setText("Color:\t" + item.getDescription().getColor());
            itemMaterial.setText("Material:\t" + item.getDescription().getMaterial());
            itemMade.setText("Made in:\t" + item.getDescription().getMade());
            itemPrice.setText(String.format("Price:\t%.2f",item.getPrice()));

            //change text for remove button to 'REMOVE'
            removeBtn.setText("REMOVE");

            //on back button clicked -> dismiss dialog
            backBtn.setOnClickListener(v -> dialog.dismiss());

            //on remove button clicked -> remove StoreItem obj from User obj cart -> dismiss dialog
            removeBtn.setOnClickListener(v -> {
                user.getOrder().getCart().remove(position);
                refreshCart();
                dialog.dismiss();
            });

            dialog.show();
            //show dialog view full width wrap content height
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }));

    }

    /**
     * refresh StoreItem adapter
     * and update total price view
     */
    private void refreshCart() {
        StoreItemListViewAdapter SIA = new StoreItemListViewAdapter(UserCartActivity.this,R.layout.layout_store_item_list_row, user.getOrder().getCart());
        totalPriceTV.setText(String.format("Total price:\t %.2f$",user.getOrder().getTotalPrice()));
        orderList.setAdapter(SIA);
    }

    /**
     * authenticate user with Firebase and create User obj from Firebase data
     */
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


}