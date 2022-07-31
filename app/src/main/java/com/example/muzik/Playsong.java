package com.example.muzik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseekbar.interrupt();
    }

    TextView textView;
    ImageView play;
    ImageView previous;
    ImageView next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    int position;
    SeekBar seekBar;
    Thread updateseekbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.Play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        seekBar = findViewById(R.id.seekBar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("Songlist");
        textcontent = intent.getStringExtra("CurrentSong");
        textView.setText(textcontent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateseekbar = new Thread(){
            @Override
            public void run() {
                int currpos = 0;
                try {
                    while(currpos < mediaPlayer.getDuration()){
                        currpos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currpos);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseekbar.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    mediaPlayer.start();
                }
            }
        });

       previous.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position != 0){
                    position = position-1;
                }
                else{
                    position = songs.size()-1;
                }
               Uri uri = Uri.parse(songs.get(position).toString());
               mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
               mediaPlayer.start();
               play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
               seekBar.setMax(mediaPlayer.getDuration());
               textcontent = songs.get(position).getName().toString();
               textView.setText(textcontent);
           }
       });


       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mediaPlayer.stop();
               mediaPlayer.release();
               if(position != songs.size()-1){
                   position = position+1;
               }
               else{
                   position = 0;
               }
               Uri uri = Uri.parse(songs.get(position).toString());
               mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
               mediaPlayer.start();
               play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
               seekBar.setMax(mediaPlayer.getDuration());
               textcontent = songs.get(position).getName().toString();
               textView.setText(textcontent);
           }
       });

    }
}