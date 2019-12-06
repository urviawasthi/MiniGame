package com.example.minigame;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.SurfaceHolder;



import androidx.appcompat.app.AlertDialog;

public class MainThread extends Thread {
    private GameView gameView;
    private boolean running = false;
    private SurfaceHolder holder;
    public static Canvas canvas;


    public MainThread(GameView gameView, SurfaceHolder surfaceHolder) {
        super();
        this.gameView = gameView;
        this.holder = surfaceHolder;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;
            try {
                //lock the canvas to make changes to it before unlocking it
                canvas = holder.lockCanvas();
                synchronized(holder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            } catch (Exception e) {
                //we are so fucking dumb it's unreal
                System.out.println(e.getMessage());
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread.start();
    }
    */

    /* public void pause() {
        //when the game is paused
        //setting the variable to false
        running = false;
        try {
            //stopping the thread
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
                // resume();
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
    } */

}
