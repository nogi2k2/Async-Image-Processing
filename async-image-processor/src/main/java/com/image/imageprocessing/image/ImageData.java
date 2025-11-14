package com.image.imageprocessing.image;

import java.awt.image.BufferedImage;

public class ImageData {
    private BufferedImage image;
    private int i;
    private int j;
    private int x;
    private int y;

    public ImageData(BufferedImage image, int i, int j, int x, int y){
        this.image = image;
        this.i = i;
        this.j = j;
        this.x = x;image
        this.y = y;
    }

    public void setI(int i){this.i = i;}
    public int getI(){return this.i;}

    public void setJ(int j){this.j = j;}
    public int getJ(){return this.j;}

    public void setX(int x){this.x = x;}
    public int getX(){return this.x;}

    public void setY(int y){this.y = y;}
    public int getY(){return this.y;}
}
