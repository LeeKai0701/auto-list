package com.kevin.autolist;

public class Utils {

    public static boolean isStrEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean isStrNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

}
