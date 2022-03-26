package com.example.tetris.models;


public class ColorModel {

    private String color = "#C0C0C0";

    public String switchBoxColor(int type){
        switch (type){
            case 0:
                // O O
                // O O
                color = "#784315";
                break;
            case 1:
                // O
                // O O O
                color = "#F2F051";
                break;
            case 2:
                // O O O O
                color = "#3282F6";
                break;
            case 3:
                //     O
                // O O O
                color = "#8E403A";
                break;
            case 4:
                //   O
                // O O O
                color = "#732BF5";
                break;
            case 5:
                //   O
                // O O
                // O
                color = "#F09B59";
                break;
            case 6:
                // O
                // O O
                //   O
                color = "#DB3022";
                break;
        }
        return color;
    }

}
