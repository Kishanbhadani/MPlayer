package com.example.mplayer;

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

public class OpenMusic extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        Updateseek.interrupt();
    }

    TextView textView;
    ImageView play,next,prev;
    SeekBar seekBar;

    ArrayList<File> song;
    String textName;
    int position;
    MediaPlayer mediaPlayer;
    Thread Updateseek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_music);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        song = (ArrayList)bundle.getParcelableArrayList("songlist");
        textName= intent.getStringExtra("currentsong");
        textView.setText(textName);
        textView.setSelected(true);  // for marquee
        position = intent.getIntExtra("position",0);

        Uri uri = Uri.parse(song.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
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

        // seek update
        Updateseek = new Thread(){
            @Override
            public void run() {
                int curntPosition = 0;
                try{
                    while (curntPosition<mediaPlayer.getDuration()){
                        curntPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(curntPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                }
                else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position = position-1;
                }
                else{
                    position = song.size()-1;
                }
                Uri uri = Uri.parse(song.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textName = song.get(position).getName().toString();
                textView.setText(textName);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=song.size()-1){
                    position = position+1;
                }
                else{
                    position = 0;
                }
                Uri uri = Uri.parse(song.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textName = song.get(position).getName().toString();
                textView.setText(textName);
            }
        });
    }
}