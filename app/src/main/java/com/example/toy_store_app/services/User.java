package com.example.toy_store_app.services;

/**
 * User class
 * @author Vyacheslav Gudimov
 */
public class User {
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_PHONE = "phone";
    public static final String USER_ADDRESS = "address";
    public static final String USER_IS_ADMIN = "admin";
    public static final String USER_ORDER = "order";
    public static final String USER_CART = "cart";

    private String id, name,email, phone;
    private Address address;
    private Order order;

    /**
     * empty constructor
     * initiate User cart ArrayList
     */
    public User() {
        order = new Order();
    }

    /**
     * full constructor
     * starts with empty constructor
     * @param id User ID as String
     * @param name User name as String
     * @param email User email as String
     * @param phone User phone as String
     * @param address User address as Address obj
     */
    public User(String id, String name,String email, String phone, Address address) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;

    }

    /**
     * return private id value
     * @return String id value
     */
    public String getId() {
        return id;
    }

    /**
     * set private id value
     * @param id String to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * return private name value
     * @return String name value
     */
    public String getName() {
        return name;
    }

    /**
     * set private name value
     * @param name String to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * return private phone value
     * @return String phone value
     */
    public String getPhone() {
        return phone;
    }

    /**
     * set private phone value
     * @param phone String to be set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * return private address value
     * @return Address address value
     */
    public Address getAddress() {
        return address;
    }

    /**
     * set private address value
     * @param address Address obj to be set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * return private order value
     * @return Order order value
     */
    public Order getOrder() {
        return order;
    }

    /**
     * set private order value
     * @param order Order obj to be set
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * return private email value
     * @return String email value
     */
    public String getEmail() {
        return email;
    }

    /**
     * set private email value
     * @param email String to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * override Object to string function for User class
     * @return User all params multiple lines
     */
    @Override
    public String toString() {
        return this.name + "\t:\n" + this.address +"\nPhone:\t" + this.phone;
    }
}
