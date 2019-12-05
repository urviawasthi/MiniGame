package com.example.minigame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;


import androidx.appcompat.app.AlertDialog;

public class GameView extends SurfaceView {

    /** Main game loop. */
    private MainThread gameThread ;

    /** Indicates whether game is over or not. */
    private boolean isGameOver;

    /** Current context for this game view. */
    private Context gameContext;

    //Need these to draw
    private Paint paint;
    private SurfaceHolder surfaceHolder;

    //Constructor
    public GameView(Context context, int x, int y) {
        super(context);
        gameThread = new MainThread(this);
        gameContext = context;
        //right now, the game is not over and the user is playing the game
        isGameOver = false; //$$$ would this cause problemos?
        //initialize drawing stuff
        surfaceHolder = getHolder();
        paint = new Paint();
        //Implement SurfaceHolder.Callback and only render on the Surface
        //when you receive the surfaceCreated(SurfaceHolder holder) callback. For example:
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop render thread here
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //start render thread here
                //When the surface is created, call your game thread variable's function that
                //controls the running and set it equal to true
                //draw();
                gameThread.setRunning(true);
                gameThread.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });
    }


    protected void onDraw(Canvas canvas) {
        if (surfaceHolder.getSurface().isValid()) {
            System.out.println("it is valid and I'm trying to draw");
            canvas.drawColor(Color.RED);
            surfaceHolder.unlockCanvasAndPost(canvas);
        } else {
            System.out.println("not valid");
        }
    }
    }
