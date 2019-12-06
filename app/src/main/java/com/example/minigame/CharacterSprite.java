package com.example.minigame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class CharacterSprite {
    private Bitmap image;
    private boolean isCrashing;
    private double xPosition;
    private double yPosition;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int quadrant;
    private int edge;
    private int characterWidth = 150;
    private int characterHeight = 200;
    private double xDisplacement;
    private double yDisplacement;
    private double slope;
    private int speed;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        image = Bitmap.createScaledBitmap(
                image, characterWidth, characterHeight, false);
        generatePosition();
        System.out.println("quadrant is " + quadrant + " and edge is " + edge);
        if (edge == 1 && quadrant == 1) {
            xPosition = generateXPosition();
            yPosition = 0;
        }
        if (edge == 1 && quadrant == 2) {
            xPosition = generateXPosition();
            yPosition = 0;
        }
        if (edge == 1 && quadrant == 3) {
            xPosition = generateXPosition();
            yPosition = screenHeight;
        }
        if (edge == 1 && quadrant == 4) {
            xPosition = generateXPosition();
            yPosition = screenHeight;
        }
        if (edge == 2 && quadrant == 1) {
            yPosition = generateYPosition();
            xPosition = screenWidth;
        }
        if (edge == 2 && quadrant == 2) {
            yPosition = generateYPosition();
            xPosition = 0;
        }
        if (edge == 2 && quadrant == 3) {
            yPosition = generateYPosition();
            xPosition = 0;
        }
        if (edge == 2 && quadrant == 4) {
            yPosition = generateYPosition();
            xPosition = screenWidth;
        }
        speed = 1;
        //initial x and y velocites, without scaling
        xDisplacement = Math.abs(xPosition - (screenWidth / 2));
        yDisplacement = Math.abs((screenHeight / 2) - yPosition);

        //scaling the velocities
        slope = yDisplacement / xDisplacement * speed;

        isCrashing = false;
    }

    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        canvas.drawBitmap(image, (int) xPosition, (int) yPosition, null);
    }

    public void move() {
        if (isCrashing) {
            //gameOVER
        } else {
            //update its location
            if (quadrant == 1) {
                xPosition = xPosition - speed;
                yPosition = yPosition + slope;
            }
            if (quadrant == 2) {
                xPosition = xPosition + speed;
                yPosition = yPosition + slope;
            }
            if (quadrant == 3) {
                xPosition = xPosition + speed;
                yPosition = yPosition - slope;
            }
            if (quadrant == 4) {
                xPosition = xPosition - speed;
                yPosition = yPosition - slope;
            }
            //depending on which wave it is, change the SPEED
            //depending on where it's coming from, DIFF X AND Y
            //redraw
        }
    }

    private int generateXPosition() {
        Random rand = new Random();
        int high = 0;
        int low = 0;
        if (quadrant == 1 || quadrant == 4) {
            high = screenWidth;
            low = screenWidth / 2;
            return rand.nextInt(high-low) + low;
        }
        else {
            high = screenWidth / 2;
            low = 0;
            return rand.nextInt(high-low) + low;
        }
    }

    private int generateYPosition() {
        Random rand = new Random();
        int high = 0;
        int low = 0;
        if (quadrant == 3 || quadrant == 4) {
            high = screenHeight;
            low = screenHeight / 2;
            return rand.nextInt(high-low) + low;
        }
        else {
            high = screenHeight / 2;
            low = 0;
            return rand.nextInt(high-low) + low;
        }
    }

    private void generatePosition() {
        Random rand = new Random();
        quadrant = rand.nextInt(4) + 1;
        edge = rand.nextInt(2) + 1;
    }
}
