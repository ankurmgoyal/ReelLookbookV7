package com.example.ankurmgoyal.reellookbookv7;

/**
 * Created by ankurmgoyal on 4/4/2017.
 */

public class InventoryUnit {

    String styleNumber;
    String color;
    String size;
    String type;
    int quantity;
    double currentPrice;
    double originalPrice;

    public InventoryUnit(String color, double currentPrice, double originalPrice, int quantity, String size, String styleNumber, String type) {
        this.color = color;
        this.currentPrice = currentPrice;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.size = size;
        this.styleNumber = styleNumber;
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
