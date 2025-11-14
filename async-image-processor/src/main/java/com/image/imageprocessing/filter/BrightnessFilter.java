package com.image.imageprocessing.filter;

import java.awt.image.BufferedImage;

public class BrightnessFilter implements ImageFilter{
    private final int brightness;

    public BrightnessFilter(int brightness){
        this.brightness = brightness;
    }

    @Override
    public BufferedImage fitler(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage brightenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y<height; y++){
            for (int x = 0; x<width; x++){
                int rgb = image.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;

                int newR = clamp(r + this.brightness);
                int newG = clamp(g + this.brightness);
                int newB = clamp(b + this.brightness);

                int newRGB = (a << 24) | (newR << 16) | (newG << 8) | (newB);
                brightenedImage.setRGB(x, y, newRGB);
            }
        }
        return brightenedImage;
    }

    public int clamp(int value){
        if (value < 0){return 0;}
        if (value > 255){return 255;}
        return value;
    }
}
