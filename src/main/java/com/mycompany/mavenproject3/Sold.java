/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HAK_PHENG
 */

package com.mycompany.mavenproject3;

public class Sold  {
    private String name;
    private double price;
    private int qty;
    private int stocks;

    public Sold(String name, double price, int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getPrice() { return price; }
    public void setPrice(double Price) { this.price = price; }
    
    public int getQty() { return qty; }
    public void setqty(int stock) { this.qty = qty; }
    
    public int getStock() { return stocks; }
    public void setStock(int stock) { this.stocks = stock; }
}
