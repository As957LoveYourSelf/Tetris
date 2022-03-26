package com.example.tetris.models;


public class ColorModel {

    private String color = "#C0C0C0";

    public String switchBoxColor(int type){
        switch (type){
            case 0:
                // O O
                // O O
                color = "#ECAD9E";
                break;
            case 1:
                // O
                // O O O
                color = "#F4606C";
                break;
            case 2:
                // O O O O
                color = "#19CAAD";
                break;
            case 3:
                //     O
                // O O O
                color = "#D6D5B7";
                break;
            case 4:
                //   O
                // O O O
                color = "#BEEDC7";
                break;
            case 5:
                //   O
                // O O
                // O
                color = "#5E78F4";
                break;
            case 6:
                // O
                // O O
                //   O
                color = "#D1BA74";
                break;
        }
        return color;
    }

}
