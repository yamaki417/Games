package com.example.kusoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView black;
    private ImageView orange;
    private ImageView pink;

    // 位置
    private float boxY;
    private float orangX;
    private float orangY;
    private float pinkX;
    private float pinkY;
    private float blackX;
    private float blackY;

    // score
    private  int score = 0;

    //Handler Timer
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // status
    private boolean action_flg = false;
    private boolean start_flg = false;

    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //SoundPlayer
    private SoundPlayer soundPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new SoundPlayer(this);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);

        //Screen Size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        orange.setX(-80.0f);
        orange.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);

        scoreLabel.setText("Score : 0");



    }

    public  void changePos(){

        hintCheck();

        // Orange
        orangX -= 12;
        if (orangX < 0){
            orangX = screenWidth + 20;
            orangY = (float)Math.floor(Math.random()* frameHeight - orange.getHeight());
        }
        orange.setX(orangX);
        orange.setY(orangY);

        //black
        blackX -= 18;
        if (blackX < 0){
            blackX = screenWidth + 10;
            blackY = (float)Math.floor(Math.random()* frameHeight - orange.getHeight());
        }
        black.setX(blackX);
        black.setY(blackY);

        // pink
        //black
        pinkX -= 28;
        if (pinkX < 0){
            pinkX = screenWidth + 5550;
            pinkY = (float)Math.floor(Math.random()* frameHeight - orange.getHeight());
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        if(action_flg){
            boxY -= 20;
        } else{
            boxY += 20;
        }

        if (boxY < 0) boxY = 0;

        if (boxY  > frameHeight - boxSize) boxY = frameHeight -boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void hintCheck(){

        float orangeCenterX = orangX + orange.getWidth() / 2;
        float orangeCenterY = orangY + orange.getHeight() / 2;

        // Orage Hit
        if (0 <=  orangeCenterX && orangeCenterX <= boxSize &&
                  boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){

            orangX = -10.0f;
            score += 10;
            soundPlayer.playHitSound();

        }

        // Pink Hit
        float pinkCenterX = pinkX + pink.getWidth() / 2;
        float pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <=  pinkCenterX && pinkCenterX <= boxSize &&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){

            pinkX = -10.0f;
            score += 30;
            soundPlayer.playHitSound();

        }

        // Black Hit
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (0 <=  blackCenterX && blackCenterX <= boxSize &&
                boxY <= blackCenterY && blackCenterY <= boxY + boxSize){

            soundPlayer.playOverSound();

            // Game over

            if (timer != null){
                timer.cancel();
                timer = null;
            }
            //結果
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (start_flg == false){

            start_flg  = true;


            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = box.getY();
            boxSize = box.getHeight();



            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);

        } else{

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                // boxY -= 20;
                action_flg = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP){

                action_flg = false;
            }
        }

        return  true;
    }

    @Override
    public void onBackPressed() {
    }
}
