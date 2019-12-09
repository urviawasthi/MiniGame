package com.example.minigame;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import java.util.Random;


import androidx.appcompat.app.AlertDialog;

import java.time.Instant;
import java.util.Random;

public class MainThread extends Thread {
    private GameView gameView;
    private boolean running = false;
    private SurfaceHolder holder;
    public static Canvas canvas;
    public static long start;
    public long timePreviousEnemySpawned = 0;

    //need these to draw
    private double spawnDelay = 2000;

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
        start = System.currentTimeMillis();
        System.out.println("start is " + start);
        while (running) {
            canvas = null;
            try {
                //lock the canvas to make changes to it before unlocking it
                canvas = holder.lockCanvas();

                //before we make any changes
                //we're gonna update this arraylist, and the length of the arraylist (wave1)
                //is going to be dependent on the current time
                startEnemySpawn(System.currentTimeMillis());
                synchronized (holder) {
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

    public void startEnemySpawn(long currentTime) {
        //generate a random speed for each student
        Random rand =  new Random();
        int speed = rand.nextInt(5) + 2;
        //if the spawn delay gets to be below a certain value, we decrease the rate of the decrease of the spawn delay
        //to make it easier for the user
        if (spawnDelay < 800) {
            if (spawnDelay + timePreviousEnemySpawned < currentTime) {
                CharacterSprite enemy = new CharacterSprite(GameView.studentImage, speed);
                GameView.wave1.add(enemy);
                timePreviousEnemySpawned = currentTime;
                spawnDelay -= 5;
            }
        } else { //initial game logic: as the game goes on, the spawn delay decreases
            if (spawnDelay + timePreviousEnemySpawned < currentTime) {
                CharacterSprite enemy = new CharacterSprite(GameView.studentImage, speed);
                GameView.wave1.add(enemy);
                timePreviousEnemySpawned = currentTime;
                spawnDelay -= 25;
            }
        }
    }
}

