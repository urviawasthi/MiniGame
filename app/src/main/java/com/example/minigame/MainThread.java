package com.example.minigame;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;

import androidx.appcompat.app.AlertDialog;

public class MainThread extends Thread {
    private GameView gameView;
    private boolean running = false;

    public MainThread(GameView gameView) {
        this.gameView = gameView;
    }

    public void setRunning(boolean run) {
        running = run;
    }
    public void run() {
        while (running) {

            //get the canvas
            //send the canvas to onDraw
            Canvas c = gameView.getHolder().lockCanvas();
            gameView.onDraw(c);

            //to update the frame
            //update students and geoff
            update();

            //to draw the frame
            //draw them at the different position
            //System.out.println("Im drawing?");
            //We know this function is getting called

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

    public void pause() {
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
    }

    private void update() {
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
