package com.example.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtil {

    public CacheUtil(){ }

    public void setCache(Context context, int maxScore){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor edit = context.getSharedPreferences("scoreData", Context.MODE_PRIVATE).edit();
        edit.putInt("maxScore", maxScore);
        edit.commit();
    }

    public int getCache(Context context, int defaultValue){
        SharedPreferences maxScore = context.getSharedPreferences("scoreData", Context.MODE_PRIVATE);
        return maxScore.getInt("maxScore", defaultValue);
    }
}
