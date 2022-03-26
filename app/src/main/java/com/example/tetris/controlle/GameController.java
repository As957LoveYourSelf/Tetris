package com.example.tetris.controlle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.tetris.R;
import com.example.tetris.models.BoxBlock;
import com.example.tetris.models.MapModel;
import com.example.tetris.models.ScoresModel;


public class GameController implements View.OnClickListener {

    private final Activity activity;
    //==============================================================================================
    // Values
    //==============================================================================================
    //游戏区域的宽和高
    private int xWidth, xHeight;

    private boolean gameGoing = false;
    private boolean openGuideLine = true;

    //游戏区域控件
    View gameView;
    View nextBlocView;
    TextView sinceScores;
    TextView maxScores;

    Button start;
    Button lineGuide;
    Button speedBtn;

    //暂停状态
    boolean isPause = false;
    //结束状态
    boolean isOver = false;

    //初始化模型
    private MapModel mapModel;
    private BoxBlock boxModel;
    private ScoresModel scoresModel;

    //游戏区域
    FrameLayout framelayout;
    FrameLayout nextBlockLayout;

    public GameController(Activity activity){
        this.activity = activity;
    }

    //方块下落速度（间隔ms）、线程
    private static int SPEED = 1000;

    //==============================================================================================
    // Thread
    //==============================================================================================
    private Thread downThread = null;

    //==============================================================================================
    // Game Control
    //==============================================================================================
    //开始游戏
    private void startGame(){
        //生成方块
        boxModel.createBox();
        mapModel.cleanMap();
        isOver = false;
        gameView.invalidate();
        nextBlocView.invalidate();
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
                        boxModel.moveJudge(mapModel,scoresModel, maxScores, sinceScores);
                        isOver = mapModel.checkOver(boxModel.getBox());
                        gameView.invalidate();
                    }
                }
            };
            downThread.start();
        }
    }
    //游戏结束
    private void endGame(){
        isPause = true;
        new AlertDialog.Builder(this.activity)
                .setTitle("结束游戏")
                .setMessage("确定结束游戏嘛？")
                .setNegativeButton("是", (dialogInterface, i) -> activity.finish())
                .setPositiveButton("否", (dialogInterface, i) -> {
                    isPause = false;
                    gameView.invalidate();
                })
                .show();
    }
    //重新开始
    private void restartGame(){
        scoresModel.setSinceScores(0);
        this.isPause = false;
        this.isOver = false;
        gameGoing = true;
        mapModel.cleanMap();
        start.setText("暂停");
        start.invalidate();
        startGame();
    }

    private void lineGuideController() {
        if (openGuideLine){
            lineGuide.setText("辅助线-开");
        }
        else {
            lineGuide.setText("辅助线-关");
        }
        openGuideLine = !openGuideLine;
        gameView.invalidate();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLeft:
                if (!isPause && !isOver)
                    boxModel.move(-1, 0, mapModel);
                break;
            case R.id.btnRight:
                if (!isPause && !isOver)
                    boxModel.move(1,0, mapModel);
                break;
            case R.id.btnDown:
                if (!isPause && !isOver && boxModel.getBoxLength() != 0){
                    while (true){
                        if (!boxModel.moveJudge(mapModel, scoresModel, maxScores, sinceScores)){
                            break;
                        }
                    }
                }
                break;
            case R.id.btnSlowDown:
                if (!isPause && !isOver)
                    boxModel.move(0, 1, mapModel);
                break;
            case R.id.btnRotate:
                if (!isPause && !isOver)
                {
                    boxModel.rotate(mapModel);
                }
                break;
            case R.id.start:
                if (isOver)
                    break;
                if (!isPause && !gameGoing || start.getText().equals("开始游戏")){
                    startGame();
                    start.setText("暂停");
                    gameGoing = true;
                    isPause = false;
                    start.invalidate();
                }
                else if (start.getText().equals("暂停")){
                    if (gameGoing){
                        start.setText("继续");
                    }
                    else {
                        start.setText("开始游戏");
                    }
                    isPause = true;
                    start.invalidate();
                }
                else if (start.getText().equals("继续")){
                    isPause = false;
                    start.setText("暂停");
                }
                break;
            case R.id.restart:
                restartGame();
                break;
            case R.id.end:
                endGame();
                break;
            case R.id.openGuideLine:
                lineGuideController();
                break;
            case R.id.speed:
                changeSpeed();
                break;
        }
        gameView.invalidate();
    }

    private void changeSpeed() {
        String str = speedBtn.getText().toString();
        if (str.equals("游戏速度-慢")){
            speedBtn.setText("游戏速度-中");
            SPEED = 800;
        }
        if (str.equals("游戏速度-中")){
            speedBtn.setText("游戏速度-快");
            SPEED = 500;
        }
        if (str.equals("游戏速度-快")){
            speedBtn.setText("游戏速度-慢");
            SPEED = 1000;
        }
        speedBtn.invalidate();
        Log.e("Speed", SPEED+"");
    }


    //==============================================================================================
    // Init Block
    //==============================================================================================

    //1. 初始化游戏数据
    public void initData(){
        //生成游戏区域
        int w = getScreenWidth(activity);
        xWidth = w * 7 / 10;
        xHeight = xWidth * 2;
        //生成地图
        this.mapModel = new MapModel(new boolean[10][20]);
        //指定方块大小
        this.boxModel = new BoxBlock(xWidth/ mapModel.getLength());

        //构件初始化
        framelayout = activity.findViewById(R.id.fgame);
        nextBlockLayout = this.activity.findViewById(R.id.view_frame);
        maxScores = this.activity.findViewById(R.id.maxScore);
        sinceScores = this.activity.findViewById(R.id.sinceScore);
        start = activity.findViewById(R.id.start);
        lineGuide = activity.findViewById(R.id.openGuideLine);
        speedBtn = activity.findViewById(R.id.speed);
    }

    //2. 初始化游戏视图
    @SuppressLint("SetTextI18n")
    public void initView(){
        //绘制游戏视图
        gameView = new View(this.activity){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                mapModel.drawMap(canvas, boxModel.getBoxSize());
                boxModel.drawBox(canvas);
                mapModel.drawMapLine(canvas, openGuideLine, boxModel.getBoxSize(), gameView);
                mapModel.drawGameState(canvas, isPause, isOver, gameView);
            }
        };
        //实例化游戏区域
        gameView.setLayoutParams(new FrameLayout.LayoutParams(xWidth, xHeight));
        gameView.setBackgroundColor(Color.parseColor("#2B2B2B"));
        framelayout.addView(gameView);

        //显示下一块区域
        nextBlocView = new View(this.activity){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                boxModel.drawNextBox(canvas, boxModel, nextBlocView);
            }
        };
        nextBlocView.setLayoutParams(new FrameLayout.LayoutParams(-1, 200));
        nextBlocView.setBackgroundColor(Color.GRAY);
        nextBlockLayout.addView(nextBlocView);

        //显示分数区域
        scoresModel = new ScoresModel(maxScores.getContext());
        maxScores.setText(scoresModel.getMaxScores()+"");
        sinceScores.setText(0+"");
    }
    //初始化监听事件
    public void initListener(){
        activity.findViewById(R.id.btnLeft).setOnClickListener(this);
        activity.findViewById(R.id.btnRight).setOnClickListener(this);
        activity.findViewById(R.id.btnDown).setOnClickListener(this);
        activity.findViewById(R.id.btnSlowDown).setOnClickListener(this);
        activity.findViewById(R.id.btnRotate).setOnClickListener(this);
        activity.findViewById(R.id.start).setOnClickListener(this);
        activity.findViewById(R.id.restart).setOnClickListener(this);
        activity.findViewById(R.id.openGuideLine).setOnClickListener(this);
        activity.findViewById(R.id.end).setOnClickListener(this);
        activity.findViewById(R.id.speed).setOnClickListener(this);
    }


    //==============================================================================================
    // Util Block
    //==============================================================================================
    private static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
