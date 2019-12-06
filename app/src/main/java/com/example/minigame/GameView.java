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

    /** Geoff's face in the middle of the screen. */
    private Geoff happyGeoff;

    //Need these to draw
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private CharacterSprite characterSprite;
    private int enemiesKilled = 0;
    private ArrayList<CharacterSprite> wave1 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave2 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave3 = new ArrayList<>();
    private Canvas sameCanvas;
    private boolean start = false;
    Bitmap myBackground;
    private int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    Bitmap pauseButton;


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
                characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.studenttemp));
                gameThread.setRunning(true);
                //assuming that start calls the run() function in main thread
                gameThread.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });
    }

    @Override
    public void draw(Canvas canvas1) {
        super.draw(canvas1);
        sameCanvas = canvas1;
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
            characterSprite.draw(canvas1);
            for (int i = 0; i < wave1.size(); i++) {
                wave1.get(i).draw(canvas1);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                float touched_x = motionEvent.getX();
                float touched_y = motionEvent.getY();
                System.out.println("x: " + touched_x + ", y: " + touched_y);
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
        //have 3 arrays with three waves with initialized characters with randomized positions and randomized quadrants
        //if else statements - if all the characters in the previous wave are gone, start new wave
        //IF first wave -> loop through them and call move on them
        if (enemiesKilled > 8) {
            //third infinite wave
        } else if (enemiesKilled > 3) {
            //second wave
        } else {
            //first create three enemies
            if (!start) {
                //create the enemies
                //System.out.println("CREATE ENEMIESSS");
                for (int i = 0; i < 30; i+= 10) {
                    wave1.add(new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.studenttemp))); //create 3 different ones and add them
                    start = true;
                }
            } else {
                //move the already created enemies
                for (int i = 0; i < 3; i++) {
                    wave1.get(i).move();
                }
            }
        }
    }
}
