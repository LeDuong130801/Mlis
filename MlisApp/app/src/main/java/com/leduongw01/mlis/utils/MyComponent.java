package com.leduongw01.mlis.utils;

import static android.os.FileUtils.copy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyComponent {

    public static void ToastShort(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    public static String getStringRef(Context context, String name){
        SharedPreferences s = context.getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);
        return s.getString(name, "none");
    }
    public static void setStringRef(Context context, String name, String value){
        SharedPreferences s = context.getSharedPreferences(Constant.PREFERENCES_NAME, Context.MODE_PRIVATE);
        s.edit().putString(name, value).apply();
    }
}
