package com.example.minigame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.minigame.MainThread.canvas;

public class GameView extends SurfaceView {

    /** Main game loop. */
    private MainThread gameThread ;

    /** Indicates whether game is over or not. */
    public static boolean isGameOver;

    /** Current context for this game view. */
    private Context gameContext;

    /** Geoff player in the middle of the screen. */
    private Geoff happyGeoff;

    /** Keeping track of enemy count and kill count to update game difficulty */
    private int enemiesKilled = 0;

    /** Keeping track of how many times Geoff has been hit, to make Geoff's image change. */
    private int lives;

    /** The setup and punchline of the jokes that Geoff says at the very end. Acquired through API. */
    private String setup;
    private String punchline;

    //Need these to draw
    private Paint paint;
    private Paint paint1;
    private SurfaceHolder surfaceHolder;
    public static ArrayList<CharacterSprite> wave1 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave2 = new ArrayList<>();
    private ArrayList<CharacterSprite> wave3 = new ArrayList<>();
    private int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap myBackground;
    private Bitmap pauseButton;
    public static Bitmap studentImage;
    SharedPreferences.Editor e;
    private JSONObject object;
    //need these to store high scores
    int[] highScores = new int[3];
    SharedPreferences sharedPreferences;
    private boolean working = false;

    //Constructor
    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        gameThread = new MainThread(this, surfaceHolder, context);
        setFocusable(true);
        gameContext = context;
        //right now, the game is not over and the user is playing the game
        isGameOver = false;
        //initialize drawing stuff
        paint = new Paint();
        paint1 = new Paint();
        myBackground = BitmapFactory.decodeResource(getResources(),R.drawable.gameactivitybackground).copy(Bitmap.Config.ARGB_8888, true);;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pleasepause).copy(Bitmap.Config.ARGB_8888, true);;
        pauseButton = Bitmap.createScaledBitmap(bitmap, 200, 200, false).copy(Bitmap.Config.ARGB_8888, true);;
        happyGeoff = new Geoff(context);
        studentImage = BitmapFactory.decodeResource(getResources(),R.drawable.studenttemp).copy(Bitmap.Config.ARGB_8888, true);;
        //lives (to make the geoff image change)
        lives = 2;
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
                //run startEnemySpawn(System.currentTimeMillis())
                gameThread.start();

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        });

        //initializing the array high scores with the previous values
        sharedPreferences = context.getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);

        //initializing high scores array with 0
        highScores[0] = sharedPreferences.getInt("highScore1",0);
        highScores[1] = sharedPreferences.getInt("highScore2",0);
        highScores[2] = sharedPreferences.getInt("highScore3",0);

        //initializing shared preferences with 0
        e = sharedPreferences.edit();
        e.putInt("highScore1", 0);
        e.putInt("highScore2", 0);
        e.putInt("highScore3", 0);
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
            //lets render the enemies onto the list here
            //wave1 is the list of enemies
            //each time, we loop through the array list of enemies
            //and draw each enemy to the canvas
            for (int i = 0; i < wave1.size(); i++) {
                wave1.get(i).draw(canvas1);
            }

            //draw the current score to the canvas
            paint1.setColor(Color.WHITE);
            paint1.setTextSize(150);
            canvas1.drawText(Integer.toString(enemiesKilled), 45, 150, paint1);
        }
    }
    public void pause() {
        //when the game is paused
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
        gameThread = new MainThread(this, surfaceHolder, gameContext);
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
                    pause();
                }
        }
        //check collision depending on which wave it is
        synchronized (getHolder()) {
            for (int i = 0; i < wave1.size(); i++) {
                CharacterSprite currentChar = wave1.get(i);
                if (currentChar.isClicked(motionEvent.getX(), motionEvent.getY())) {
                    wave1.remove(currentChar);
                    enemiesKilled++;
                    break;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void update() {
        //if game is over, then display respective dialog
        if (isGameOver) {
            gameThread.setRunning(false);

            //if applies, add high score to high scores array
            for (int i = 0; i <= 2; i++) {
                System.out.println("adding high scores to high scores array");
                if (highScores[i] < enemiesKilled) {
                    for (int j = i; j <= 1; j++) {
                        highScores[j + 1] = highScores[j];
                    }
                    highScores[i] = enemiesKilled;
                    break;
                }
            }
            //putting the high scores array into shared preferences
            for (int i = 1; i < 4; i++) {
                System.out.println("adding high scores array into shared preferences");
                e.putInt("highScore" + i, highScores[i - 1]);
            }
            e.apply();
            //MAIN: if geoff is hit, game over is false, playing is false
            //if new position clashes with geoff
            //EXTRA: lives and pic changes
        }

        //we'll have a counter with the amount of enemies
        //every time you create an enemy, you increase the counter
        //first we add one enemy to the arraylist and once that enemy is killed, then you create another enemy
        for (int i = 0; i < wave1.size(); i++) {
            wave1.get(i).move();
            //find out if there has been a collision and if so, set isgameover to true
            if (Rect.intersects(happyGeoff.getDetectCollision(), wave1.get(i).getDetectCollision())) {
                wave1.get(i).setXPosition(3000);
                lives--;
                if (lives == 1) {
                    //make geoff turn pouty
                    happyGeoff.setBitmap();
                }
                if (lives == 0) {
                    isGameOver = true;
                }
            }
        }
    }

    public void gameOverDialog() {
        working = false;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(gameContext);
                builder.setMessage("Game Over!");

                //builder.setMessage("Geoff's joke of the game: " + joke);
                // Instantiate the RequestQueue.
                String url ="https://official-joke-api.appspot.com/random_joke";
                RequestQueue queue = Volley.newRequestQueue(gameContext);
                // Request a string response from the provided URL
                System.out.println("MAKING REQUEST AFTER THIS");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                                setup = jsonObject.get("setup").getAsString();
                                punchline = jsonObject.get("punchline").getAsString();
                                builder.setMessage("Game Over! To make it up, here's a joke: " + setup + " " + punchline);
                                builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // user clicked main menu
                                        Intent intent = new Intent(gameContext, MainActivity.class);
                                        gameContext.startActivity(intent);
                                    }
                                });
                                builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked play again
                                        Intent intent = new Intent(gameContext, GameActivity.class);
                                        gameContext.startActivity(intent);
                                        gameThread.setRunning(true);
                                        gameThread.start();
                                    }
                                });
                                builder.create().show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("im a failure");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
    }
    
}

