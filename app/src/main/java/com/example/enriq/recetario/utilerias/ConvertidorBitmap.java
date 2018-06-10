package com.example.enriq.recetario.utilerias;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by enriq on 10/06/2018.
 */

public class ConvertidorBitmap implements Serializable {
    private int[] pixels;
    private int width, height;

    public ConvertidorBitmap(Bitmap bitmap){
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
    }

    public Bitmap getBitmap(){
        return Bitmap.createBitmap(pixels,width,height,Bitmap.Config.ARGB_8888);
    }
}
