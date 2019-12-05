package com.example.minigame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class CharacterSprite {
    private Bitmap image;
    private boolean isCrashing;
    private int xVelocity;
    private int yVelocity;
    private int xPosition;
    private int yPosition;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public CharacterSprite(Bitmap bmp, int x, int y) {
        image = bmp;
        xPosition = x;
        yPosition = y;
        isCrashing = false;
    }

    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        //System.out.println("im drawing student");
        canvas.drawBitmap(image, xPosition, yPosition, null);
    }

    public void move(int wave) {
        if (isCrashing) {
            //gameOVER
        } else {
            //update its location
            //depending on which wave it is, change the SPEED
            //depending on where it's coming from, DIFF X AND Y
            //redraw
            if (wave == 1) {
                //System.out.println("MOOOOVEEEE");
                xPosition += 5;
                yPosition += 5;
                //draw(canvas);
            }
        }
    }
}
