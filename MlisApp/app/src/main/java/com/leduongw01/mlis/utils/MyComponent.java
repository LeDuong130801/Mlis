package com.leduongw01.mlis.utils;

import android.content.Context;
import android.widget.Toast;

public class MyComponent {
    public static void ToastShort(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
