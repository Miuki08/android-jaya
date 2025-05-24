package com.example.jumat_berkah;

public class Order {
    private int id;
    private int user_id;
    private int flower_id;
    private String flowerName;
    private int quantity;
    private String order_date;
    private String totalPrice;

    public int getId() { return id; }
    public int getUser_id() { return user_id; }
    public int getFlower_id() { return flower_id; }
    public String getFlowerName() { return flowerName; }
    public int getQuantity() { return quantity; }
    public String getOrder_date() { return order_date; }
    public String getTotalPrice() { return totalPrice; }
}
