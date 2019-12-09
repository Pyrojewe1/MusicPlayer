package com.example.music;

import android.media.MediaPlayer;
import android.os.Handler;


class SeekBarThread implements Runnable {
    public Handler handler;
    public MediaPlayer mediaPlayer;

    public SeekBarThread(Handler handler, MediaPlayer mediaPlayer) {
        this.handler = handler;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void run() {
        while (mediaPlayer != null && mediaPlayer.isPlaying() == true) {
            // 将SeekBar位置设置到当前播放位置
            handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
