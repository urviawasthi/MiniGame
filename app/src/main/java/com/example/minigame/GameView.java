package com.example.minigame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;


import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class GameView extends SurfaceView {

    /** Main game loop. */
    private MainThread gameThread ;

    /** Indicates whether game is over or not. */
    private boolean isGameOver;

    /** Current context for this game view. */
    private Context gameContext;

    /** Geoff player in the middle of the screen. */
    private Geoff happyGeoff;

    /** Keeping track of enemy count and kill count to update game difficulty */
    private int enemiesKilled = 0;
    private int enemyCount = 0;

    //Need these to draw
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private ArrayList<CharacterSprite> wave1 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave2 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave3 = new ArrayList<>();
    private int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int height = Resources.getSystem().getDisplayMetrics().heightPixels;

    Bitmap myBackground;
    Bitmap pauseButton;
    Bitmap studentImage;


    //Constructor
    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        gameThread = new MainThread(this, surfaceHolder);
        setFocusable(true);
        gameContext = context;
        //right now, the game is not over and the user is playing the game
        isGameOver = false;

        //initialize drawing stuff
        paint = new Paint();
        myBackground = BitmapFactory.decodeResource(getResources(),R.drawable.gameactivitybackground);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pleasepause);
        pauseButton = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        happyGeoff = new Geoff(context);
        studentImage = BitmapFactory.decodeResource(getResources(),R.drawable.studenttemp);

        //BUTTON LOCATION -> x is from (width - 230) to (width - 30)
        //                -> y is from (0) to (30)
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
            //System.out.println("it is valid and I'm trying to draw");
            Rect src = new Rect();
            src.set(0, 0, width, height);
            Rect dest = new Rect();
            dest.set(0, 0, 1000, 600);
            canvas1.drawBitmap(myBackground, dest, src, null);
            canvas1.drawBitmap(pauseButton, width - 230, 30, null);
            canvas1.drawBitmap(
                    happyGeoff.getBitmap(),
                    happyGeoff.getX(),
                    happyGeoff.getY(),
                    paint);
            //we need to draw the sprites to the canvas
            //wave1 is the list of enemies
            //each time, we loop through the array list of enemies
            //and draw each enemy to the canvas
            for (int i = 0; i < wave1.size(); i++) {
                wave1.get(i).move();
                wave1.get(i).draw(canvas1);
            }
        }
    }
    public void pause() {
        //when the game is paused
        //setting the variable to false
        //running = false;
        try {
            //stopping the thread
            gameThread.setRunning(false);
            gameThread.join();
        } catch (InterruptedException e) {
        }
        //also show a dialog that tells the user the game is paused, and asks them to resume
        AlertDialog.Builder builder = new AlertDialog.Builder(gameContext);
        builder.setMessage("Game Paused!");
        // Add the buttons
        builder.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
            //user clicked resume
            public void onClick(DialogInterface dialog, int id) {
                resume();
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            // user clicked quit
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(gameContext, MainActivity.class);
                gameContext.startActivity(intent);
            }
        });

        builder.create().show();
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        System.out.println("im in the resume function");
        gameThread.setRunning(true);
        try {
            gameThread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                float touched_x = motionEvent.getX();
                float touched_y = motionEvent.getY();
                if (touched_x > width - 230 && touched_x < width - 30 && touched_y > 30 && touched_y < 230) {
                    System.out.println("CLICKED PAUSE");
                    pause();
                }
        }
      return true;
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

        //we'll have a counter with the amount of enemies
        //every time you create an enemy, you increase the counter
        //first we add one enemy to the arraylist and once that enemy is killed, then you create another enemy
        if (enemyCount == 0) {
            //no enemies have been created yet
            CharacterSprite enemy = new CharacterSprite(studentImage, enemyCount + 1);
            wave1.add(enemy);
            System.out.println("added enemy");
            enemyCount++;
        }
        //keep on increasing the amount of enemies you kill and speed of enemies
        //will be based on how many enemies have been killed
        if (enemiesKilled > 0) {
            //player killed one enemy, move on to unlimited loop
            for (int i = 0; i < enemiesKilled; i++) {
                CharacterSprite enemy = new CharacterSprite(studentImage, enemyCount + 1);
                wave1.add(enemy);
                enemyCount++;
            }
        }
    }
}
