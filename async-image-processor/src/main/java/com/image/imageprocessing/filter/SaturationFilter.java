package com.image.imageprocessing.filter;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class SaturationFilter implements ImageFilter{
    private final float saturationFactor;

    public SaturationFilter(float saturationFactor){
        this.saturationFactor = saturationFactor;
    }

    @Override
    public BufferedImage filter(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage saturatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        float[] hsbVals = new float[3];
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                int argb = image.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb) & 0xFF;

                Color.RGBtoHSB(r,g,b, hsbVals);
                float newSaturation = hsbVals[1] * this.saturationFactor;

                if (newSaturation > 1.0f){newSaturation = 1.0f;}
                else if (newSaturation < 0.0f){newSaturation = 0.0f;}   

                hsbVals[1] = newSaturation;
                int newRGB = Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
                newRGB = (newRGB & 0x00FFFFFF) | (a << 24);
                saturatedImage.setRGB(x, y, newRGB);
            }
        }
        return saturatedImage;
    }
}
