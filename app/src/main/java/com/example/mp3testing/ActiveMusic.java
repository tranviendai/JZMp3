package com.example.mp3testing;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3testing.Model.SongModel;
import com.example.mp3testing.PlayerIndex.MyIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveMusic extends AppCompatActivity {
    CircleImageView imgMusic;
    TextView nameMusic, nameSinger, timeStart, timeEnd;
    Button btPrev, btNext,btPlay;
    SeekBar seek;
    ImageView btShuffle,btRepeat;
    Thread updateSeekbar;
    ArrayList<SongModel> music;
    SongModel musicModel;
    MediaPlayer mediaPlayer = MyIndex.getInstance();
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_music);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgMusic = findViewById(R.id.imgMusic);
        nameMusic = findViewById(R.id.musicName);
        nameSinger = findViewById(R.id.singerName);
        timeEnd = findViewById(R.id.timeEnd);
        timeStart = findViewById(R.id.timeStart);
        seek = findViewById(R.id.seekBar);
        btNext = findViewById(R.id.btnNext);
        btPrev = findViewById(R.id.btnPrev);
        btPlay = findViewById(R.id.btnPlay);
        btRepeat = findViewById(R.id.btnRepeat);
        btShuffle = findViewById(R.id.btnShuffle);
        view = findViewById(R.id.activeMusic);
        MyIndex.shuffle = true;
        MyIndex.repeat = true;
        if(Settings.switchColor){
            view.setBackgroundResource(R.color.black);
        }
        else{
            view.setBackgroundResource(R.color.bg_main);
        }

        music = (ArrayList<SongModel>) getIntent().getSerializableExtra("musics");
        setResourcesWithMusic();
        btShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyIndex.shuffle){
                    MyIndex.shuffle = false;
                    btShuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
                    btRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
                }
                else{
                    MyIndex.shuffle = true;
                    btShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
                }
            }
        });

        btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyIndex.repeat){
                    MyIndex.repeat = false;
                    btRepeat.setImageResource(R.drawable.ic_baseline_repeat_on_24);
                    btShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
                }
                else{
                    MyIndex.repeat = true;
                    btRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
                }
            }
        });

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    btPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    btPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });

        //Next - Prev
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MyIndex.shuffle){
                    MyIndex.currentIndex = getRandom(music.size() -1);
                }
                    if (MyIndex.currentIndex == music.size() - 1) {
                        MyIndex.currentIndex = -1;
                    } else {
                        MyIndex.currentIndex += 1;
                        mediaPlayer.reset();
                        setResourcesWithMusic();
                }
                startAnimation(imgMusic,0f,360f);
            }
        });
        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MyIndex.shuffle){
                    MyIndex.currentIndex = getRandom(music.size() -1);
                }
                if (MyIndex.currentIndex == 0)
                    MyIndex.currentIndex = music.size();
                else {
                    MyIndex.currentIndex -= 1;
                    mediaPlayer.reset();
                    setResourcesWithMusic();
                }
                startAnimation(imgMusic,360f,0f);
            }
        });

        //seekUpdate
        updateSeekbar = new Thread() {
            @Override
            public void run() {
                int total = mediaPlayer.getDuration();
                int currentPostion = 0;
                while (currentPostion < total) {
                    try {
                        sleep(500);
                        currentPostion = mediaPlayer.getCurrentPosition();
                        seek.setProgress(currentPostion);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seek.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        seek.getThumb().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == mediaPlayer.getDuration()){
                    setResourcesWithMusic();
                    seek.setProgress(mediaPlayer.getCurrentPosition());
                }
                if(progress == mediaPlayer.getDuration() && MyIndex.repeat==true){
                    if (MyIndex.currentIndex == music.size() - 1) {
                        MyIndex.currentIndex = -1;
                    } else {
                        MyIndex.currentIndex += 1;
                        mediaPlayer.reset();
                        setResourcesWithMusic();
                    }
                    seek.getProgress();
                    startAnimation(imgMusic,0f,360f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(seekBar!=null){
                    seek.getProgress();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endTime = createTime(mediaPlayer.getDuration());
        timeEnd.setText(endTime);
        final Handler handler = new Handler();
        final int delay = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                timeStart.setText(currentTime);
                handler.postDelayed(this, delay);

            }
        }, delay);

    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }
    public void startAnimation(View view,float f, float r){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgMusic,"rotation",f,r);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animator.start();
    }
    void setResourcesWithMusic() {
        music = (ArrayList<SongModel>) getIntent().getSerializableExtra("musics");
        musicModel = music.get(MyIndex.currentIndex);

        nameSinger.setText(musicModel.getSinger());
        nameMusic.setText(musicModel.getNameMusic());
        timeEnd.setText(createTime(Integer.parseInt(musicModel.getDuration())));
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicModel.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        time = time + min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        mediaPlayer.stop();
        return super.onSupportNavigateUp();
    }
}