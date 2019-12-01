package com.example.minigame;

import android.content.Context;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    /** This is the variable that indicates whether the game is being played or not. */
    private boolean playing;

    /** This is the main game loop. */
    private Thread gameThread = null;

    //Constructor
    public GameView(Context context) {
        super(context);
    }

    /**
     * Running a loop while the user is playing.
     */
    @Override
    public void run() {
        while (playing) {
            //to update the frame
            update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }

    /**
     * Updating the frame, and location of our characters.
     */
    private void update() {

    }

    /**
     * Drawing the characters to the canvas.
     */
    private void draw() {

    }

    /**
     * Controlling how many frames are drawn per second.
     */
    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the game is paused.
     */
    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Resuming the game.
     */
    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
