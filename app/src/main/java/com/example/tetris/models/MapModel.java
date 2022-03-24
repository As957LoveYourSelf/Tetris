package com.example.tetris.models;

import android.graphics.Point;

public class MapModel {
    //地图
    private final boolean [][] map;

    public MapModel(boolean [][] map){
        this.map = map;
    }

    public boolean[][] getMap() {
        return map;
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

    public boolean[][] cleanMap(){
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y < map[0].length; y++){
                map[x][y] = false;
            }
        }
        return this.map;
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
}
