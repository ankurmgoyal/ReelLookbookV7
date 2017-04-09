package com.example.ankurmgoyal.reellookbookv7;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by ankurmgoyal on 2/2/2017.
 */

public class Tag {

    private boolean availibity;
    private int left;
    private int top;
    private double price;
    int[] sizeRibbon;
    private String styleNumber;
    private String color;

    public Tag(boolean availibity, int left, int top, double price){
        this.availibity = availibity;
        this.left = left;
        this.top = top;
        this.price = price;
        this.styleNumber = styleNumber;
        this.color = color;

        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:mysql://107.180.43.9:3306/dor", "xxxx", "xxx");
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT SUM( Quantity ) FROM  `main` WHERE (`StyleNumber` =  'JA1700')");
                    while(rs.next()){
                        //System.out.println(rs.getString(1));
                    }
                    conn.close();
                }
                catch(Exception e){
                }
                return null;
            }
        }.execute();
    }

    public String getStyleNumber() {
        return styleNumber;
    }

    public void setStyleNumber(String styleNumber) {
        this.styleNumber = styleNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getAvailibity() {
        return availibity;
    }

    public int getLeft() {
        return left;
    }

    public double getPrice() {
        return price;
    }

    public int getTop() {
        return top;
    }
}
