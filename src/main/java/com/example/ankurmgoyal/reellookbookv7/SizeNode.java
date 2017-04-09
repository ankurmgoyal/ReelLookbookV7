package com.example.ankurmgoyal.reellookbookv7;

/**
 * Created by ankurmgoyal on 4/3/2017.
 */

public class SizeNode {
    private String size;
    private int quantity;

    public SizeNode(int quantity, String size) {
        this.quantity = quantity;
        this.size = size;
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
}
