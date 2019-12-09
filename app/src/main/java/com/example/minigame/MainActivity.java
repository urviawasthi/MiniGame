package com.example.minigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /** This is the play button on the main screen of the minigame. */
    private Button playButton;

    /** This is the high scores button on the main screen of the minigame. */
    private Button highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //making the app landscape mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //when play button is clicked, launch a new activity: game activity
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //when the play button is clicked, launch game activity
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        //when high scores button is clicked, launch a new activity: scores activity
        highScoresButton = findViewById(R.id.highScoresButton);
        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //when the high scores button is clicked, launch scores activity
                Intent intent = new Intent(MainActivity.this, Scores.class);
                startActivity(intent);
            }
        });
    }
}
