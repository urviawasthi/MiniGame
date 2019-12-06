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
}