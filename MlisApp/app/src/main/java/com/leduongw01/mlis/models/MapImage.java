package com.leduongw01.mlis.models;

import android.graphics.Bitmap;

public class MapImage {
    public String id;
    public Bitmap bitmap;
    public boolean hasRes;
    public MapImage(){}
    public MapImage(String id, Bitmap bitmap){
        this.id = id;
        this.bitmap = bitmap;
        if (bitmap == null)
        hasRes = false;
        else hasRes = true;
    }
}
