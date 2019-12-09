package com.example.minigame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Geoff {
    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //screen sizes
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    //rectangle for detecting collision
    private Rect detectCollision;

    //context
    private Context context;

    //constructor
    public Geoff(Context setContext) {
        //setting context
        context = setContext;
        //Getting bitmap from drawable resource and scale it
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.happygeoff);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, 200, 275, false);

        //setting x and y positions of geoff to be in the center of the screen
        x = (screenWidth / 2) - (bitmap.getWidth() / 2);
        y = (screenHeight / 2) - (bitmap.getHeight() / 2);

        //rectangle for detecting collision
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    /*
     * These are getters you can generate it automatically
     * right click on editor -> generate -> getters
     * */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap() {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poutygeoff1);
        bitmap = Bitmap.createScaledBitmap(
                bitmap, 200, 275, false);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //returning the detect collision object for geoff
    public Rect getDetectCollision() {
        return detectCollision;
    }
}
