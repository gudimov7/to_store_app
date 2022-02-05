package com.example.toy_store_app.services;

public class User {
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_PHONE = "phone";
    public static final String USER_ADDRESS = "address";
    public static final String USER_IS_ADMIN = "admin";
    public static final String USER_ORDER = "order";
    public static final String USER_CART = "cart";

    private String id, name, phone;
    private Address address;
    private boolean isAdmin;
    private Order order;

    public User() {
        order = new Order();
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return this.name + "\t:\n" + this.address +"\nPhone:\t" + this.phone;
    }
}
