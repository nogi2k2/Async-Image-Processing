package com.image.imageprocessing.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GreyScaleFilter implements ImageFilter{
    @Override
    public BufferedImage filter(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeigth();
        BufferedImage greyScaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y<height; y++){
            for (int x = 0; x<width; x++){
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int grey = (int) (0.2126*r + 0.7152*g + 0.0722*b);
                int grayColor = new Color(grey, grey, grey).getRGB();
                greyScaleImage.setRGB(x, y, grayColor);
            }
        }
        return greyScaleImage;
    }
}
