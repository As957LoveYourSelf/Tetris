package com.example.tetris.controlle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tetris.R;
import com.example.tetris.models.BoxBlock;
import com.example.tetris.models.MapModel;
import com.example.tetris.models.ScoresModel;


public class GameController implements View.OnClickListener {



    private final Activity activity;
    //游戏区域的宽和高
    private int xWidth, xHeight;
    //游戏区域控件
    View gameView;
    View nextBlocView;
    TextView sinceScores;
    TextView maxScores;
    //暂停状态
    boolean isPause = false;
    //结束状态
    boolean isOver = false;

    //画笔，用于绘制辅助线
    public Paint linePaint;
    public Paint boxPaint;
    public Paint mapPaint;
    public Paint statePaint;
    public Paint nextboxPaint;

    private MapModel mapModel;
    private BoxBlock boxModel;
    private ScoresModel scoresModel;

    int boxSize;
    private boolean[][] map;
    private Point[] box;

    //游戏区域
    FrameLayout framelayout;
    FrameLayout nextBlockLayout;

    public GameController(Activity activity){
        this.activity = activity;
    }

    //方块下落速度（间隔ms）、线程
    int SPEED = 1000;
    private Thread downThread = null;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            gameView.invalidate();
            nextBlocView.invalidate();
            maxScores.invalidate();
            sinceScores.invalidate();
        }
    };

    //开始游戏
    private void startGame(){
        //生成方块
        this.box = boxModel.createBox();
        if (downThread == null){
            downThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            sleep(SPEED);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isOver||isPause)
                            continue;
                        moveJudge();
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            downThread.start();
        }
        this.map = mapModel.cleanMap();
        isOver = false;
        isPause = false;
    }
    //游戏结束
    private void endGame(){

    }

    //游戏暂停
    private void pauseGame(){
        isPause = !isPause;
    }
    //重新开始
    private void restartGame(){
        this.map = mapModel.cleanMap();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLeft:
                if (!isPause && !isOver)
                    this.box = (Point[]) boxModel.move(-1, 0)[1];
                break;
            case R.id.btnRight:
                if (!isPause && !isOver)
                    boxModel.move(1,0);
                break;
            case R.id.btnDown:
                if (!isPause && !isOver && this.box.length != 0){
                    while (true){
                        if (!moveJudge()){
                            break;
                        }
                    }
                }
                break;
            case R.id.btnSlowDown:
                if (!isPause && !isOver)
                    boxModel.move(0, 1);
                break;
            case R.id.btnRotate:
                if (!isPause && !isOver)
                {
                    Object[] objects = boxModel.rotate();
                    boolean b = (boolean) objects[0];
                    if (boxModel.getType() != 0 && b)
                        this.box = (Point[]) objects[1];
                }
                break;
            case R.id.start:
                startGame();
                break;
            case R.id.stop:
                pauseGame();
                break;
            case R.id.restart:
                restartGame();
                break;
            case R.id.end:
                endGame();
                break;
        }
        gameView.invalidate();
        nextBlocView.invalidate();
    }
    //初始化游戏视图
    @SuppressLint("SetTextI18n")
    public void initView(){
        //初始化画笔
        linePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setAntiAlias(true);

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE);
        boxPaint.setAntiAlias(true);

        mapPaint = new Paint();
        mapPaint.setColor(Color.LTGRAY);
        mapPaint.setAntiAlias(true);

        statePaint = new Paint();
        statePaint.setColor(Color.RED);
        statePaint.setAntiAlias(true);
        statePaint.setTextSize(100);

        nextboxPaint = new Paint();
        nextboxPaint.setColor(Color.BLACK);
        nextboxPaint.setAntiAlias(true);

        //游戏区域
        framelayout = activity.findViewById(R.id.fgame);
        //绘制游戏视图
        gameView = new View(this.activity){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                drawMap(canvas);
                drawBox(canvas);
                drawMapLine(canvas);
                drawGameState(canvas);
            }
        };
        //实例化游戏区域
        gameView.setLayoutParams(new FrameLayout.LayoutParams(xWidth, xHeight));
        gameView.setBackgroundColor(Color.parseColor("#2B2B2B"));
        framelayout.addView(gameView);

        //显示下一块区域
        nextBlockLayout = this.activity.findViewById(R.id.view_frame);
        nextBlocView = new View(this.activity){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                drawNextBox(canvas);
            }
        };
        nextBlocView.setLayoutParams(new FrameLayout.LayoutParams(-1, 200));
        nextBlocView.setBackgroundColor(Color.GRAY);
        nextBlockLayout.addView(nextBlocView);

        //显示分数区域
        maxScores = this.activity.findViewById(R.id.maxScore);
        sinceScores = this.activity.findViewById(R.id.sinceScore);
        scoresModel = new ScoresModel(maxScores.getContext());
        maxScores.setText(scoresModel.getMaxScores()+"");
        sinceScores.setText(0+"");
    }
    //初始化监听事件
    public void initListener(){
        activity.findViewById(R.id.btnLeft).setOnClickListener(this);
        activity.findViewById(R.id.btnRight).setOnClickListener(this);
        activity.findViewById(R.id.btnDown).setOnClickListener(this);
        activity.findViewById(R.id.btnRotate).setOnClickListener(this);
        activity.findViewById(R.id.start).setOnClickListener(this);
        activity.findViewById(R.id.stop).setOnClickListener(this);
        activity.findViewById(R.id.restart).setOnClickListener(this);
    }

    //初始化游戏数据
    public void initData(){
        //生成游戏区域
        int w = getScreenWidth(activity);
        xWidth = w * 7 / 10;
        xHeight = xWidth * 2;
        //生成地图
        this.mapModel = new MapModel(new boolean[10][20]);
        map = mapModel.getMap();
        //指定方块大小
        this.boxModel = new BoxBlock(xWidth/ this.map.length, this.mapModel);
        boxSize = this.boxModel.getBoxSize();
        box = this.boxModel.getBox();
    }

    private static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //快速下落方法，以及堆积判断
    @SuppressLint("SetTextI18n")
    private boolean moveJudge(){
        Object[] move = boxModel.move(0, 1);
        this.box = (Point[]) move[1];
        if ((boolean)move[0]){
            return true;
        }
        for (Point point : box){
            this.map[point.x][point.y] = true;
        }
        //生成方块
        this.box = boxModel.createBox();
        //判断消行
        Object[] objects = mapModel.cleanLine();
        this.map = (boolean[][]) objects[0];
        int dlines = (int) objects[1];
        //加分
        if (dlines != 0){
            scoresModel.addScores(dlines, maxScores.getContext());
            sinceScores.setText(scoresModel.getSinceScores()+"");
            maxScores.setText(scoresModel.getMaxScores()+"");
        }
        //判断游戏结束
        isOver = mapModel.checkOver(this.box);
        downThread.interrupt();
        return false;
    }

    private void drawMap(Canvas canvas){
        //地图更新（堆积）
        for (int x = 0; x < map.length; x++){
            for (int y = 0; y < map[0].length; y++){
                if (map[x][y]){
                    canvas.drawRect(x*boxSize, y*boxSize, (x+1)*boxSize, (y+1)*boxSize, mapPaint);
                }
            }
        }
    }

    private void drawBox(Canvas canvas){
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

    private void drawNextBox(Canvas canvas){
        Point[] nextbox = boxModel.getNextbox();
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

    private void drawMapLine(Canvas canvas){
        //地图辅助线
        for (int x = 0; x < map.length; x++){
            canvas.drawLine(x*boxSize, 0, x*boxSize, gameView.getHeight(),linePaint);
        }
        for (int y = 0; y < map[0].length; y++) {
            canvas.drawLine(0, y*boxSize, gameView.getWidth(),y*boxSize, linePaint);
        }
    }

    private void drawGameState(Canvas canvas){
        if (isPause&&!isOver){
            canvas.drawText("Pausing",
                    (gameView.getWidth()-statePaint.measureText("Pausing"))/2,
                    gameView.getHeight()/2,
                    statePaint);
        }
        if (isOver){
            canvas.drawText("Game Over!",
                    (gameView.getWidth()-statePaint.measureText("Game Over!"))/2,
                    gameView.getHeight()/2,
                    statePaint);
        }
    }
}
