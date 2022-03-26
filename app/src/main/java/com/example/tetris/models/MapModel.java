package com.example.tetris.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class MapModel {
    //地图
    private final boolean [][] map;

    private final Paint linePaint;
    private final Paint mapPaint;
    private final Paint statePaint;

    public MapModel(boolean [][] map){
        this.map = map;

        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setAntiAlias(true);

        mapPaint = new Paint();
        mapPaint.setColor(Color.LTGRAY);
        mapPaint.setAntiAlias(true);

        statePaint = new Paint();
        statePaint.setColor(Color.RED);
        statePaint.setAntiAlias(true);
        statePaint.setTextSize(150);
    }

    public boolean[][] getMap() {
        return map;
    }

    public int getLength(){
        return this.map.length;
    }

    //消行操作
    public Object[] cleanLine(){
        int l = 0;
        for (int y = map[0].length-1; y > 0; y--){
            if (checkCleanLine(y)){
                //执行消行
                deleteLine(y);
                l++;
                y++;
            }
        }
        return new Object[]{this.map, l};
    }

    private void deleteLine(int y){
        for (int dy = y; dy > 0; dy--){
            for (int x = 0; x < map.length ; x++){
                map[x][dy] = map[x][dy-1];
            }
        }
    }

    //消行判断
    public boolean checkCleanLine(int y){
        for (boolean[] booleans : map) {
            if (!booleans[y]) {
                return false;
            }
        }
        return true;
    }

    public void cleanMap(){
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y < map[0].length; y++){
                map[x][y] = false;
            }
        }
    }


    public boolean checkOver(Point[] box){
        for (Point point: box){
            if (map[point.x][point.y])
                return true;
        }
        return false;
    }

    //边界判断
    public boolean checkBoundary(int x, int y){
        return (x<0||y<0||x>=map.length||y>=map[0].length|| map[x][y]);
    }

    //==============================================================================================
    // Draw Block
    //==============================================================================================
    public void drawMap(Canvas canvas, int boxSize){
        //地图更新（堆积）
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y < map[0].length; y++){
                if (map[x][y]){
                    canvas.drawRect(x*boxSize, y*boxSize, (x+1)*boxSize, (y+1)*boxSize, mapPaint);
                }
            }
        }
    }

    public void drawMapLine(Canvas canvas, boolean openGuideLine, int boxSize, View gameView){
        if (openGuideLine){
            //地图辅助线
            for (int x = 0; x < map.length; x++){
                canvas.drawLine(x*boxSize, 0, x*boxSize, gameView.getHeight(),linePaint);
            }
            for (int y = 0; y < map[0].length; y++) {
                canvas.drawLine(0, y*boxSize, gameView.getWidth(),y*boxSize, linePaint);
            }
        }
    }

    public void drawGameState(Canvas canvas, boolean isPause, boolean isOver, View gameView){
        if (isPause&&!isOver){
            canvas.drawText("游戏暂停",
                    (gameView.getWidth()-statePaint.measureText("游戏暂停"))/2,
                    gameView.getHeight()/2,
                    statePaint);
        }
        if (isOver){
            canvas.drawText("游戏结束!",
                    (gameView.getWidth()-statePaint.measureText("游戏结束!"))/2,
                    gameView.getHeight()/2,
                    statePaint);
        }
    }
}
