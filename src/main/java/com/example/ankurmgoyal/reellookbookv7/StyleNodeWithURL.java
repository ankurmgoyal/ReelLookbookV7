package com.example.ankurmgoyal.reellookbookv7;

/**
 * Created by ankurmgoyal on 4/3/2017.
 */

public class StyleNodeWithURL {

    private String styleNumber;
    private String color;
    private String url;

    public StyleNodeWithURL(String styleNumber, String color, String url){
        this.styleNumber = styleNumber;
        this.color = color;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
