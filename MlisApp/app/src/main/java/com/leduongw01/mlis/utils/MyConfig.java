package com.leduongw01.mlis.utils;

import java.util.List;

public class MyConfig {
    public static Integer WaitTime = 3600;
    public static String serverAddress = "http://192.168.1.35:8080/";
    public static String server(){
        String[] o =  serverAddress.split("/");
        return o[2];
    }
    public static String port = "8080";
    public static long DAYOFNEW = 7;
}
