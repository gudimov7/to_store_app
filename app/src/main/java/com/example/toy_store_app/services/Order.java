package com.example.toy_store_app.services;

import java.util.ArrayList;

public class Order {
    private ArrayList <StoreItem> cart;
    private float totalPrice;

    public Order(ArrayList<StoreItem> cart) {
        this();
        this.cart = cart;
        setTotalPrice();
    }

    public Order() {
        cart = new ArrayList<>();
    }

    public ArrayList<StoreItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<StoreItem> cart) {
        this.cart = cart;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    private boolean setTotalPrice(){
        if (cart != null) {
            for (StoreItem item: cart)
                totalPrice += item.getPrice();
            return true;
        }
        return false;
    }

    public void addItemToCart(StoreItem item) {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        cart.add(item);
    }

    public boolean removeItemFromCart(StoreItem item) {
        if (cart != null && cart.contains(item)) {
            cart.remove(item);
            return true;
        }
        return false;
    }
}