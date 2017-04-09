package com.example.ankurmgoyal.reellookbookv7;

/**
 * Created by ankurmgoyal on 4/4/2017.
 */

public class StyleUnit {

    String styleNumber;
    String color;
    String url;
    String type;
    String type2;
    String type3;

    public StyleUnit(String color, String styleNumber, String type, String type2, String type3, String url) {
        this.color = color;
        this.styleNumber = styleNumber;
        this.type2 = type2;
        this.type3 = type3;
        this.type = type;
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
