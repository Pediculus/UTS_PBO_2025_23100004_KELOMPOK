/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author HAK_PHENG
 */
public class Transaction {
    private String customerName;
    private String itemName;
    private double price;
    private double totalPrice;
    private int qty;
    private int stocks;
    
    public Transaction(String customerName, String itemName, double price, int qty, double totalPrice) {
        this.customerName = customerName;
        this.itemName = itemName;
        this.price = price;
        this.totalPrice = totalPrice;
        this.qty = qty;

    }
    
    public String getCustomerName() { return customerName; }
    public void setName(String customerName) { this.customerName = customerName; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public double getPrice() { return price; }
    public void setPrice(double Price) { this.price = price; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public int getQty() { return qty; }
    public void setqty(int stock) { this.qty = qty; }
    
    public int getStock() { return stocks; }
    public void setStock(int stock) { this.stocks = stock; }
}
