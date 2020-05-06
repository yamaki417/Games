package com.example.kusoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView hightSocreLabel = findViewById(R.id.heightScore);

        int score =getIntent().getIntExtra("SCORE",0);
        scoreLabel.setText(score +"");

        SharedPreferences sharedPreferences = getSharedPreferences("GAME_DATA",MODE_PRIVATE);
        int hightScore = sharedPreferences.getInt("HIGH_SCORE",0);

        if (score >hightScore){
            hightSocreLabel.setText("High_SCORE :" + score);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("High_SCORE" , score);
            editor.apply();

        }else{
            hightSocreLabel.setText("High Score : " + hightScore);
        }
    }

    public void tryAgain(View view){

        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }

    @Override
    public void onBackPressed() {
    }
}
