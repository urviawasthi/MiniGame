package com.example.minigame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Geoff {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //screen sizes
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    //constructor
    public Geoff(Context context) {
        //Getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.happygeoff);

        bitmap = Bitmap.createScaledBitmap(
                bitmap, 200, 275, false);

        x = (screenWidth / 2) - (bitmap.getWidth() / 2);
        y = (screenHeight / 2) - (bitmap.getHeight() / 2);
    }

    //Method to update avatar of character
    public void update(Context context){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poutygeoff);
    }

    /*
     * These are getters you can generate it automatically
     * right click on editor -> generate -> getters
     * */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
