package com.example.tetris.models;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class BoxBlock {

    //方块实体
    private Point[] box = new Point[]{};
    private Point[] nextbox = new Point[]{};

    private final Paint boxPaint;
    public Paint nextboxPaint;
    //方块尺寸
    int boxSize;

    private int type;

    public BoxBlock(int boxSize){
        this.boxSize = boxSize;

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE);
        boxPaint.setAntiAlias(true);

        nextboxPaint = new Paint();
        nextboxPaint.setColor(Color.BLACK);
        nextboxPaint.setAntiAlias(true);
    }

    public BoxBlock(int boxSize, int colorBox, int colorNextBox){
        this.boxSize = boxSize;

        boxPaint = new Paint();
        boxPaint.setColor(colorBox);
        boxPaint.setAntiAlias(true);

        nextboxPaint = new Paint();
        nextboxPaint.setColor(colorNextBox);
        nextboxPaint.setAntiAlias(true);
    }

    public Point[] getBox() {
        return box;
    }

    public int getBoxSize() {
        return boxSize;
    }

    public int getBoxLength(){
        return box.length;
    }

    public int getType() {
        return type;
    }

    public Point[] getNextbox() {
        return nextbox;
    }

    //生成方块
    public void createBox(){
        if (this.box.length == 0){
            this.box = generateBox();
        }
        else {
            this.box = nextbox;
        }
        nextbox = generateBox();
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
                _box = new Point[]{new Point(4,0), new Point(3,0), new Point(5,0), new Point(6,0)};
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
    public boolean move(int x, int y, MapModel map){
        for (Point point : box) {
            if (map.checkBoundary(point.x + x, point.y + y)){
                return false;
            }
        }
        for (Point point : box) {
            point.x += x;
            point.y += y;
        }
        return true;
    }


    public void rotate(MapModel map){
        for (Point point : box) {
            int tempx = -point.y + box[0].y + box[0].x;
            int tempy = point.x - box[0].x + box[0].y;
            if (map.checkBoundary(tempx, tempy)){
                return;
            }
        }
        for (Point point : box) {
            int tempx = -point.y + box[0].y + box[0].x;
            int tempy = point.x - box[0].x + box[0].y;
            point.x = tempx;
            point.y = tempy;
        }
    }

    public void drawBox(Canvas canvas){
        //方块
        for (Point point : box) {
            canvas.drawRect(
                    point.x * boxSize,
                    point.y * boxSize,
                    (point.x+1) * boxSize,
                    (point.y+1) * boxSize,
                    boxPaint);
        }
    }

    public void drawNextBox(Canvas canvas, BoxBlock box, View nextBlocView){
        Point[] nextbox = box.getNextbox();
        int nextboxSize = nextBlocView.getWidth() / 6;
        for (Point point : nextbox) {
            canvas.drawRect(
                    (point.x-1) * nextboxSize,
                    (point.y+1) * nextboxSize,
                    point.x * nextboxSize,
                    (point.y+2) * nextboxSize,
                    nextboxPaint);
        }
    }

    //快速下落方法，以及堆积判断
    @SuppressLint("SetTextI18n")
    public boolean moveJudge(MapModel map, ScoresModel scoresModel, View maxScores, View  sinceScores){
        if (move(0, 1, map)){
            return true;
        }
        for (Point point : box){
            map.getMap()[point.x][point.y] = true;
        }
        //生成方块
        createBox();
        //判断消行
        Object[] objects = map.cleanLine();
        int dlines = (int) objects[1];
        //加分
        if (dlines != 0){
            scoresModel.addScores(dlines, maxScores.getContext());
            maxScores.invalidate();
            sinceScores.invalidate();
        }
        return false;
    }

}
