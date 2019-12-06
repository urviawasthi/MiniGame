package com.example.minigame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

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
    private Rect detectCollision;

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        image = Bitmap.createScaledBitmap(
                image, characterWidth, characterHeight, false);
        generatePosition();
        speed = 5;
        //initial x and y velocites, without scaling
        xDisplacement = Math.abs(xPosition - (screenWidth / 2));
        yDisplacement = Math.abs((screenHeight / 2) - yPosition);

        //scaling the velocities
        slope = yDisplacement / xDisplacement * speed;

        // detecting collision
        isCrashing = false;

        //the collision object will be a rectangle
        detectCollision = new Rect((int) xPosition, (int) yPosition, image.getWidth(), image.getHeight());

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

            //detecting collision between enemy and geoff
            detectCollision.left = (int) xPosition;
            detectCollision.top = (int) yPosition;
            detectCollision.right = (int) xPosition + image.getWidth();
            detectCollision.bottom = (int) yPosition + image.getHeight();

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

    //generates the quadrant and which edge the enemy will approach from
    //based on those values, generates a position accordingly
    private void generatePosition() {
        Random rand = new Random();
        quadrant = rand.nextInt(4) + 1;
        edge = rand.nextInt(2) + 1;

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
    }

    //setting the xPosition so that we can move the bitmap away as soon as collision is detected
    //or so that we can move it away when user taps on it
    private void setXPosition(double setXPosition) {
        xPosition = setXPosition;
    }

    //returns the detect collision rectangle so that we can detect collision in different quadrants
    private Rect getDetectCollision() {
        return detectCollision;
    }
}
