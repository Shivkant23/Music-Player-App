package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button playButton, pauseButton, forwardButton, rewindButton;
    TextView time_txt, title_txt;
    SeekBar seekBar;

    //audio video app
    MediaPlayer mediaPlayer;

    //handlers
    Handler handler = new Handler();

    //variable
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playButton = findViewById(R.id.buttonPlay);
        pauseButton = findViewById(R.id.buttonPause);
        forwardButton = findViewById(R.id.buttonFastForward);
        rewindButton = findViewById(R.id.buttonFastRewind);

        time_txt = findViewById(R.id.timeText);
        title_txt = findViewById(R.id.title);

        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this, R.raw.astronaut);
        seekBar.setClickable(false);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp + forwardTime) <= finalTime){
                    startTime = startTime + forwardTime;
                }else{
                    Toast.makeText(getApplicationContext(), "Cant jump forward!", Toast.LENGTH_LONG).show();
                }
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if((temp - backwardTime) > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(getApplicationContext(), "Cant jump Backward!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private  void PlayMusic(){
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(oneTimeOnly == 0){
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time_txt.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)finalTime))));
        seekBar.setProgress((int) startTime);
        seekBar.postDelayed(UpdateSongTime, 100);
    }

    private Runnable UpdateSongTime = new Runnable() {

        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_txt.setText(
                    String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime))));
            seekBar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };
}