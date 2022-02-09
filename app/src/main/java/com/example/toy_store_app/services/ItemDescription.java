package com.example.toy_store_app.services;

/**
 * StoreItem obj description class
 * @author Vyacheslav Gudimov
 */
public class ItemDescription {
    private String age, color, material, made;

    /**
     * empty constructor
     */
    public ItemDescription() {
    }

    /**
     * full constructor
     * @param age String for toy age restriction
     * @param color String for toy color
     * @param material String for toy material
     * @param made String for toy made country
     */
    public ItemDescription(String age, String color, String material, String made) {
        this.age = age;
        this.color = color;
        this.material = material;
        this.made = made;
    }

    /**
     * return private age value
     * @return String age value
     */
    public String getAge() {
        return age;
    }

    /**
     * set private age value
     * @param age String to be set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * return private color value
     * @return String color value
     */
    public String getColor() {
        return color;
    }

    /**
     * set private color value
     * @param color String to be set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * return private Material value
     * @return String Material value
     */
    public String getMaterial() {
        return material;
    }

    /**
     * set private material value
     * @param material String to be set
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * return private made value
     * @return String made value
     */
    public String getMade() {
        return made;
    }

    /**
     * set private made value
     * @param made String to be set
     */
    public void setMade(String made) {
        this.made = made;
    }

    /**
     * override Object to string function for ItemDescription class
     * @return ItemDescription all params multiple lines
     */
    @Override
    public String toString() {
        return "age:\t" + age + "\n" +
            "color:\t" + color + "\n" +
            "material:\t" + material + "\n" +
            "made:\t" + made + "\n";
    }
}
