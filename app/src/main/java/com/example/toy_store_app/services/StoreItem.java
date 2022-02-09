package com.example.toy_store_app.services;

/**
 * StoreItem class
 * @author Vyacheslav Gudimov
 */
public class StoreItem {
    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_DESCRIPTION_AGE = "age";
    public static final String ITEM_DESCRIPTION_COLOR = "color";
    public static final String ITEM_DESCRIPTION_MADE = "made";
    public static final String ITEM_DESCRIPTION_MATERIAL = "material";
    public static final String ITEM_PIC = "pic";
    public static final String ITEM_PRICE = "price";
    private String itemName, pic;
    private ItemDescription description;
    private float price;

    /**
     * empty constructor
     */
    public StoreItem() {
    }

    /**
     * full constructor
     * @param itemName String for item name
     * @param description ItemDescription describe item
     * @param price float StoreItem price
     * @param pic String url for StoreItem img
     */
    public StoreItem(String itemName, ItemDescription description, float price, String pic) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.pic = pic;
    }

    /**
     * return private pic value
     * @return String pic url value
     */
    public String getPic() {
        return pic;
    }

    /**
     * set private pic value
     * @param pic String url to be set
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * return private itemName value
     * @return String itemName value
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * set private itemName value
     * @param itemName String to be set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * return private description value
     * @return ItemDescription description value
     */
    public ItemDescription getDescription() {
        return description;
    }

    /**
     * set private description value
     * @param description ItemDescription to be set
     */
    public void setDescription(ItemDescription description) {
        this.description = description;
    }

    /**
     * return private price value
     * @return float price value
     */
    public float getPrice() {
        return price;
    }

    /**
     * set private price value
     * @param price float to be set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * override Object to string function for ItemDescription class
     * @return StoreItem all params multiple lines
     */
    @Override
    public String toString() {
        return "StoreItem{\n" +
                "\tname:\t" + itemName + "\n" +
                "\tdescription:\t" + description + "\n" +
                "\tprice:\t" + price + "$\n" +
                "}";
    }
}
