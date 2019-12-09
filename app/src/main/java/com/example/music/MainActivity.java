package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public Cursor cursor1;
    public int pos = 0 ;
    public SeekBar jindutiaoSb;
    int maxtime =0;
    TextView nowtime;
    TextView alltime;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (mediaPlayer.isPlaying()) {
                int progress = mediaPlayer.getCurrentPosition();
                jindutiaoSb.setProgress(progress);
                nowtime.setText(formatTime(progress));
        }
                updataProgress();
            return true;
        }
    });

    private void updataProgress() {
            Message msg = Message.obtain();//发送一条空消息
            handler.sendMessageDelayed(msg, 1000);//间隔1s发送
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         nowtime = findViewById(R.id.nowtime);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
        }
        jindutiaoSb = (SeekBar) findViewById(R.id.playSeekBar);
        jindutiaoSb.setProgress(0);
        jindutiaoSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

         alltime = findViewById(R.id.alltime);
        FindMusic findMusic = new FindMusic(this);
        final ArrayList<MusicInfo> musicInfoArrayList = findMusic.getMusicInfo();
        ListView listView = findViewById(R.id.listview);
        ListViewAdapter adapter = new ListViewAdapter(musicInfoArrayList,MainActivity.this,listView,mediaPlayer,pos,jindutiaoSb,alltime);
        listView.setAdapter(adapter);
        StaticV.pos =pos;
        Button button1 = findViewById(R.id.play);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticV.pos =pos;
                   if(mediaPlayer.isPlaying()){
                       mediaPlayer.pause();
                   }
                   else{
                       new Thread(new SeekBarThread(handler,mediaPlayer)).start();
                       mediaPlayer.start();}
                   Log.i("here", "onClick: click1 ");

            }
        });






        Button buttonnext = findViewById(R.id.next);
        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pos= (pos+1)%(musicInfoArrayList.size());
                    StaticV.pos =pos;
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicInfoArrayList.get(pos).getData());
                StaticV.mediaPlayer =mediaPlayer;
                mediaPlayer.prepare();
                mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(mediaPlayer.getDuration());
                    alltime.setText(formatTime(mediaPlayer.getDuration()));
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
                    pos--;
                    if(pos==-1)
                        pos=musicInfoArrayList.size()-1;
                    StaticV.pos =pos;
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(musicInfoArrayList.get(pos).getData());
                    StaticV.mediaPlayer =mediaPlayer;
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(mediaPlayer.getDuration());
                    alltime.setText(formatTime(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        updataProgress();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    pos= (pos+1)%(musicInfoArrayList.size());
                    StaticV.pos =pos;
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(StaticV.musicInfoArrayList.get(StaticV.pos).getData());
                    StaticV.mediaPlayer =mediaPlayer;
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    jindutiaoSb.setProgress(0);
                    jindutiaoSb.setMax(mediaPlayer.getDuration());
                    alltime.setText(formatTime(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private String formatTime(int length) {

        Date date = new Date(length);//调用Date方法获值

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式

        String TotalTime = simpleDateFormat.format(date);//转化为需要形式

        return TotalTime;

    }

    public void sendMessege (Handler handler){
        handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
    }
    @Override
    public void onRequestPermissionsResult (int requestCode , String[] permissions , int[] grantResult){
        switch (requestCode){
            case 1:
                if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    //
                }
                else {
                    Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
                    break;
        }
    }
}
