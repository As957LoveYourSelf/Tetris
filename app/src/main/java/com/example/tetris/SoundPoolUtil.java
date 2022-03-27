package com.example.tetris;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolUtil {
    private  SoundPool soundPool;

    public SoundPoolUtil(Context context){
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        soundPool.load(context, R.raw.click, 1);
        soundPool.load(context, R.raw.clearline, 1);
        soundPool.load(context, R.raw.clear2line, 1);
        soundPool.load(context, R.raw.cleanmoreline, 1);
    }

    public void play(int id){
        soundPool.play(id,1,1,1,0,1);
    }
}
