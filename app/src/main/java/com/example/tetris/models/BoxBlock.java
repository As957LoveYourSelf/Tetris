package com.example.tetris.models;

import android.graphics.Point;

import java.util.Random;

public class BoxBlock {

    //方块实体
    private Point[] box = new Point[]{};
    private Point[] nextbox = new Point[]{};
    //方块尺寸
    int boxSize;

    private int type;

    MapModel map;

    public BoxBlock(int boxSize, MapModel mapModel){
        this.boxSize = boxSize;
        this.map = mapModel;
    }

    public Point[] getBox() {
        return box;
    }

    public int getBoxSize() {
        return boxSize;
    }

    public int getType() {
        return type;
    }

    public Point[] getNextbox() {
        return nextbox;
    }

    //生成方块
    public Point[] createBox(){
        if (this.box.length == 0){
            this.box = generateBox();
        }
        else {
            this.box = nextbox;
        }
        nextbox = generateBox();
        return this.box;
    }

    private Point[] generateBox(){
        Random random = new Random();
        Point[] _box = new Point[]{};
        //方块类型
        this.type = random.nextInt(7);
        switch (type){
            case 0:
                // O O
                // O O
                _box = new Point[]{new Point(4,0), new Point(4,1), new Point(5,0), new Point(5,1)};
                break;
            case 1:
                // O
                // O O O
                _box = new Point[]{new Point(4,1), new Point(3,0), new Point(3,1), new Point(5,1)};
                break;
            case 2:
                // O O O O
                _box = new Point[]{new Point(3,0), new Point(4,0), new Point(5,0), new Point(6,0)};
                break;
            case 3:
                //     O
                // O O O
                _box = new Point[]{new Point(4,1), new Point(5,0), new Point(3,1), new Point(5,1)};
                break;
            case 4:
                //   O
                // O O O
                _box = new Point[]{new Point(4,1), new Point(4, 0), new Point(3,1), new Point(5,1)};
                break;
            case 5:
                //   O
                // O O
                // O
                _box = new Point[]{new Point(4,1), new Point(3,1), new Point(4,0), new Point(3,2)};
                break;
            case 6:
                // O
                // O O
                //   O
                _box = new Point[]{new Point(4,1), new Point(3,1), new Point(3,0), new Point(4,2)};
                break;
        }
        return _box;
    }


    //移动方法
    public Object[] move(int x, int y){
        for (Point point : box) {
            if (map.checkBoundary(point.x + x, point.y + y)){
                return new Object[]{false, this.box};
            }
        }
        for (Point point : box) {
            point.x += x;
            point.y += y;
        }
        //返回修改后状态
        return new Object[]{true, this.box};
    }


    public Point[] rotate(){
        for (Point point : box) {
            int tempx = -point.y + box[0].y + box[0].x;
            int tempy = point.x - box[0].x + box[0].y;
            if (map.checkBoundary(tempx, tempy)){
                return this.box;
            }
        }
        for (Point point : box) {
            int tempx = -point.y + box[0].y + box[0].x;
            int tempy = point.x - box[0].x + box[0].y;
            point.x = tempx;
            point.y = tempy;
        }
        return this.box;
    }
}
