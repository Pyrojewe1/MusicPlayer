package com.example.music;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewActivity extends AppCompatActivity {
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public Cursor cursor1;
    public int pos = 0 ;
    public SeekBar jindutiaoSb;
    int maxtime =0;
    TextView nowtime;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            int progress = StaticV.mediaPlayer.getCurrentPosition();
            jindutiaoSb.setProgress(progress);
            nowtime.setText(formatTime(progress));
            updataProgress();
            return true;
        }
    });
    private void updataProgress() {
        Message msg = Message.obtain();//发送一条空消息
        handler.sendMessageDelayed(msg, 1000);//间隔1s发送
    }

    private String formatTime(int length) {
        Date date = new Date(length);//调用Date方法获值
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式
        String TotalTime = simpleDateFormat.format(date);//转化为需要形式
        return TotalTime;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactivity);
        ImageView imageView = findViewById(R.id.image);
        final ObjectAnimator animator=  ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//设置动画重复次数（-1代表一直转）
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        animator.start();
        nowtime = findViewById(R.id.nowtime);
        jindutiaoSb =  findViewById(R.id.playSeekBar);
        jindutiaoSb.setMax(StaticV.mediaPlayer.getDuration());
        jindutiaoSb.setProgress(0);
        jindutiaoSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    StaticV.mediaPlayer.seekTo(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                StaticV.mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        final TextView alltime = findViewById(R.id.alltime);
        alltime.setText(formatTime(StaticV.mediaPlayer.getDuration()));
        Button button1 = findViewById(R.id.play);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StaticV.mediaPlayer.isPlaying()){
                    StaticV.mediaPlayer.pause();
                    animator.pause();
                }
                else{
                    new Thread(new SeekBarThread(handler,StaticV.mediaPlayer)).start();
                    StaticV.mediaPlayer.start();
                    animator.resume();
                }
                Log.i("here", "onClick: click1 ");

            }
        });






        Button buttonnext = findViewById(R.id.next);
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StaticV.pos++;
                    if( StaticV.pos>=StaticV.musicInfoArrayList.size())
                        StaticV.pos=0;
                    StaticV.mediaPlayer.reset();
                    StaticV.mediaPlayer.setDataSource(StaticV.musicInfoArrayList.get(StaticV.pos).getData());
                    StaticV.mediaPlayer.prepare();
                    StaticV.mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(StaticV.mediaPlayer.getDuration());
                    alltime.setText(formatTime(StaticV.mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        Button buttonlast = findViewById(R.id.last);
        buttonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StaticV.pos--;
                    if( StaticV.pos==-1)
                        StaticV.pos=StaticV.musicInfoArrayList.size()-1;
                    StaticV.mediaPlayer.reset();
                    StaticV.mediaPlayer.setDataSource(StaticV.musicInfoArrayList.get( StaticV.pos).getData());
                    StaticV.mediaPlayer.prepare();
                    StaticV.mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(StaticV.mediaPlayer.getDuration());
                    alltime.setText(formatTime(StaticV.mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        updataProgress();


        StaticV.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                try {
                    StaticV.pos++;
                    if( StaticV.pos>=StaticV.musicInfoArrayList.size())
                        StaticV.pos=0;
                    StaticV.mediaPlayer.reset();
                    StaticV.mediaPlayer.setDataSource(StaticV.musicInfoArrayList.get(StaticV.pos).getData());
                    StaticV.mediaPlayer.prepare();
                    StaticV.mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(StaticV.mediaPlayer.getDuration());
                    alltime.setText(formatTime(StaticV.mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
}
