package com.example.tetris.models;

import android.content.Context;
import android.util.Log;

import com.example.tetris.CacheUtil;


public class ScoresModel {

    private int maxScores;
    private int sinceScores = 0;
    CacheUtil cacheUtil;

    public ScoresModel(Context context){
        cacheUtil = new CacheUtil();
        int ts = cacheUtil.getCache(context,0);
//        Log.e("ts", String.valueOf(ts));
        maxScores = Math.max(ts, maxScores);
//        Log.e("max", String.valueOf(maxScores));
    }

    public void addScores(int lines, Context context){
        if (lines == 0)
            return;
        this.sinceScores += 2*lines - 1;
        maxScores = Math.max(maxScores, sinceScores);
//        Log.e("max:", String.valueOf(maxScores));
        cacheUtil.setCache(context, maxScores);
    }

    public int getSinceScores() {
        return sinceScores;
    }

    public int getMaxScores() {
        return maxScores;
    }
}
