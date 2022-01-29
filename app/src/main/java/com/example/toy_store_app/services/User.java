package com.example.toy_store_app.services;


import java.util.ArrayList;

public class User {
    private String id, name, phone;
    private Address address;
    private ArrayList <StoreItem> cart;
    private boolean isAdmin;

    public User() {
        cart = new ArrayList<StoreItem>();
    }

    public User(String id, String name, String phone, Address address) {
        this();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.address = address;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<StoreItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<StoreItem> cart) {
        this.cart = cart;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return this.name + "\t:\n" + this.address;
    }
}
