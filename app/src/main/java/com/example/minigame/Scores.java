package com.example.minigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Scores extends AppCompatActivity {
    SharedPreferences highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        Button back = findViewById(R.id.goBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //when the high scores button is clicked, launch scores activity
                Intent intent = new Intent(Scores.this, MainActivity.class);
                startActivity(intent);
            }
        });

        highScores = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);


        //getting text views for high scores
        TextView highScore1 = findViewById(R.id.highscore1);
        TextView highScore2 = findViewById(R.id.highscore2);
        TextView highScore3 = findViewById(R.id.highscore3);

        highScores = getSharedPreferences("HIGH_SCORES", Context.MODE_PRIVATE);
        System.out.println("setting high score in the text view");
        highScore1.setText("1. " + String.valueOf(highScores.getInt("highScore1", -1)));
        highScore2.setText("2. " + String.valueOf(highScores.getInt("highScore2", 0)));
        highScore3.setText("3. " + String.valueOf(highScores.getInt("highScore3", 0)));



    }
}

