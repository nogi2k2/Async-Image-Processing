package com.image.imageprocessing.filter;

import java.awt.image.BufferedImage;

public class InvertFilter implements ImageFilter{
    @Override
    public BufferedImage filter(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage invertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int argb = image.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb) & 0xFF;

                int newR = 255 - r;
                int newG = 255 - g;
                int newB = 255 - b;

                int newPixel = (a << 24) | (newR << 16) | (newG << 8) | (newB);
                invertedImage.setRGB(x, y, newPixel);
            }
        }
        return invertedImage;
    }
}
