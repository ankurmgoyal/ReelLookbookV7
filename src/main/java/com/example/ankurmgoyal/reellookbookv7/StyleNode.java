package com.example.ankurmgoyal.reellookbookv7;

/**
 * Created by ankurmgoyal on 3/30/2017.
 */

public class StyleNode {
    private String styleNumber;
    private String color;

    public StyleNode(String styleNumber, String color){
        this.styleNumber = styleNumber;
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getColor() {
        return color;
    }

    public String getStyleNumber() {
        return styleNumber;
    }
}
