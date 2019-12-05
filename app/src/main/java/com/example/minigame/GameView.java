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

import static com.example.minigame.MainThread.canvas;

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

    //Initializing Geoff, the legend himself
    private Geoff geoffHappy;

    //Constructor
    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        gameThread = new MainThread(this, surfaceHolder);
        setFocusable(true);

        gameContext = context;

        //right now, the game is not over and the user is playing the game
        isGameOver = false; //$$$ would this cause problemos?

        //initialize drawing stuff
        paint = new Paint();

        //initializing geoff
        geoffHappy = new Geoff(context);

        //Implement SurfaceHolder.Callback and only render on the Surface
        //when you receive the surfaceCreated(SurfaceHolder holder) callback. For example:
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop render thread here
                boolean retry = true;
                while (retry) {
                    try {
                        gameThread.setRunning(false);
                        gameThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    retry = false;
                }
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

    @Override
    public void draw(Canvas canvas1) {
        super.draw(canvas1);
        if (canvas1 != null) {
            //background color for canvas
            canvas1.drawColor(Color.GREEN);

            //drawing geoff to the canvas
            canvas.drawBitmap(
                    geoffHappy.getBitmap(),
                    geoffHappy.getX(),
                    geoffHappy.getY(),
                    paint);
        }
    }

    public void update() {
        //if game is over, then display respective dialog
        if (isGameOver) {
            AlertDialog.Builder builder = new AlertDialog.Builder(gameContext);
            builder.setMessage("Game Over!");
            //PUT API WITH ADVICE SLIPS
            // Add the buttons
            builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked play again
                    Intent intent = new Intent(gameContext, GameActivity.class);
                    gameContext.startActivity(intent);
                }
            });
            builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // user clicked main menu
                    Intent intent = new Intent(gameContext, MainActivity.class);
                    gameContext.startActivity(intent);
                }
            });
            builder.create().show();

            //also need to add game to high scores here?
            //will implement the above later!
            //move positions of students
            //MAIN: if geoff is hit, game over is false, playing is false
            //if new position clashes with geoff
            //EXTRA: lives and pic changes
        }
    }
    }
