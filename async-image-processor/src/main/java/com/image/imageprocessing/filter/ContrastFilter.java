package com.image.imageprocessing.filter;

import java.awt.image.BufferedImage;

public class ContrastFilter implements ImageFilter{
    private final double contrastFactor;

    public ContrastFilter(double contrastFactor){
        this.contrastFactor = contrastFactor;
    }

    @Override
    public BufferedImage filter(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage contrastedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y<height; y++){
            for (int x = 0; x<width; x++){
                int rgb = image.getRGB(x, y);
                int a = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;

                int newR = (int) ((r-128) * this.contrastFactor + 128);
                int newG = (int) ((g-128) * this.contrastFactor + 128);
                int newB = (int) ((b-128) * this.contrastFactor + 128);

                newR = clamp(newR);
                newG = clamp(newG);
                newB = clamp(newB);

                int newARGB = (a << 24) | (newR << 16) | (newG << 8) | (newB);
                contrastedImage.setRGB(x, y, newARGB);
            }
        }
        return contrastedImage;
    }

    public int clamp(int value){
        return Math.max(0, Math.min(255, value));
    }
}
