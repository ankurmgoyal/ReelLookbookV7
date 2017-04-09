package com.example.ankurmgoyal.reellookbookv7;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankurmgoyal on 4/3/2017.
 */

public class SizeMap {

    static HashMap<String,String[]> map;
    static String[] regularSingle = {"XS","S","M","L","XL"};
    static String[] regularLong = {"SMALL","MED","LARGE"};
    static String[] hyphenated = {"S-M","M-L"};
    static String[] numerical = {"24","25","26","27","28","29","30","31","32","33"};
    static String[] noSize= {"N/A"};
    static String[] oneSize = {"1 SIZE"};
    static String[] smallnumerical = {"0","1","3","5","7","9","11","13","15"};

    static{
        map = new HashMap<String,String[]>();
        map.put("Regular Single",regularSingle);
        map.put("Regular Long",regularLong);
        map.put("Hyphenated",hyphenated);
        map.put("Numerical",numerical);
        map.put("No Size",noSize);
        map.put("One Size", oneSize);
        map.put("Small Numerical", smallnumerical);

    }

    public static String[] getSizes(String key){
        return map.get(key);
    }

}
