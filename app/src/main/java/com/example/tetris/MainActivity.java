package com.example.tetris;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import com.example.tetris.controlle.GameController;

public class MainActivity extends AppCompatActivity  {

    GameController gameController = new GameController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        gameController.initData();
        gameController.initView();
        gameController.initListener();
    }
}