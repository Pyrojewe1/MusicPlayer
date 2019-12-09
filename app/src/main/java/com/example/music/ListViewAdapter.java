package com.example.music;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ListViewAdapter extends BaseAdapter {
    public ArrayList<MusicInfo> musicInfoArrayList = new ArrayList<MusicInfo>();
    public Context context;
    public ListView listView;
    public MediaPlayer mediaPlayer ;
    public int pos = 0 ;
    public SeekBar jindutiaoSb;
    public TextView alltime;



    public ListViewAdapter(ArrayList<MusicInfo> musicInfoArrayList, Context context, ListView listView , MediaPlayer mediaPlayer , int pos , SeekBar jindutiaoSb ,TextView alltime) {
        this.musicInfoArrayList = musicInfoArrayList;
        this.context = context;
        this.listView = listView;
        this.mediaPlayer = mediaPlayer;
        this.pos =pos;
        this.jindutiaoSb = jindutiaoSb;
        this.alltime = alltime;
    }

    @Override
    public int getCount() {
        return musicInfoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else
            view = View.inflate(context, R.layout.items, null);
        final MusicInfo musicInfo =musicInfoArrayList.get(i);
       if(musicInfo != null){
           TextView musicname = (TextView) view.findViewById(R.id.musicname);
           musicname.setText(musicInfo.getTitle());
           TextView artist = (TextView) view.findViewById(R.id.artist);
           artist.setText(musicInfo.getArtist());
           TextView album = (TextView) view.findViewById(R.id.album);
           album.setText(musicInfo.getAlbum());
           musicname.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                        try{
                            if( mediaPlayer.isPlaying() && StaticV.pos == i) {
                                Log.i("i=============================",String.valueOf(i));
                            }
                            else {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(musicInfo.getData());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                Log.i("here", "onClick: click1 ");
                                pos = i;
                                StaticV.pos = pos;
                                StaticV.mediaPlayer = mediaPlayer;
                                jindutiaoSb.setProgress(0);
                                jindutiaoSb.setMax(musicInfo.getDuration());
                                alltime.setText(formatTime(musicInfo.getDuration()));
                            }
                                Intent intent = new Intent(context, NewActivity.class);
                                context.startActivity(intent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                   }
           });

       }
        return view;
    }
    private String formatTime(int length) {

        Date date = new Date(length);//调用Date方法获值

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定需要形式

        String TotalTime = simpleDateFormat.format(date);//转化为需要形式

        return TotalTime;

    }


}
