package com.example.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.ArrayList;

public class FindMusic {
    private Context context;
    private static final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String[] MUSIC_PROJECTION = new String[] {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };

    FindMusic(Context context){
        this.context = context;
    }
    public ArrayList<MusicInfo> getMusicInfo() {
        ArrayList<MusicInfo> musicInfoArrayList = new ArrayList<MusicInfo>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(URI,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                MusicInfo musicInfo = new MusicInfo();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title  =  cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Media.TITLE));
                String data  =  cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Media.DATA));
                String album  =  cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM));
                String artist  =  cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST));
                int duration = cursor.getInt(cursor.getColumnIndex( MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex( MediaStore.Audio.Media.SIZE));
                musicInfo.setId(id);
                if (!TextUtils.isEmpty(title)) {
                    musicInfo.setTitle(title);
                }
                if (!TextUtils.isEmpty(data)) {
                    musicInfo.setData(data);
                }
                if (!TextUtils.isEmpty(album)) {
                    musicInfo.setAlbum(album);
                }
                if (!TextUtils.isEmpty(artist)) {
                    musicInfo.setArtist(artist);
                }
                if (duration != 0) {
                    musicInfo.setDuration(duration);
                }
                if (size != 0) {
                    musicInfo.setSize(size);
                }

                musicInfoArrayList.add(musicInfo);
            }
            while(cursor.moveToNext());
            cursor.close();
        }
        StaticV.musicInfoArrayList = musicInfoArrayList;
        return musicInfoArrayList;
    }
}
