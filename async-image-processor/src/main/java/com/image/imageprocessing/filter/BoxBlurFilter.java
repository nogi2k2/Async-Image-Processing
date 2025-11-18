package com.image.imageprocessing.filter;

import java.awt.image.BufferedImage;

public class BoxBlurFilter implements ImageFilter{
    private final int kernelSize;
    private final int radius;

    public BoxBlurFilter(int size){
        if (size % 2 == 0){
            throw new IllegalArgumentException("Kernel Size must be an Odd Number");
        }

        this.kernelSize = size;
        this.radius = (size/2);
    }

    @Override
    public BufferedImage filter(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y<height; y++){
            for (int x = 0; x<width; x++){
                int totalA = 0;
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;

                for (int ky = -radius; ky <= radius; ky++){
                    for (int kx = -radius; kx <= radius; kx++){
                        int nX = clamp((x + kx), 0, width - 1);
                        int nY = clamp((y + ky), 0, height - 1);
                        int argb = image.getRGB(nX, nY);
                        
                        totalA += (argb >> 24) & 0xFF;
                        totalR += (argb >> 16) & 0xFF;
                        totalG += (argb >> 8) & 0xFF;
                        totalB += (argb) & 0xFF;
                    }
                }

                int totalPixels = kernelSize * kernelSize;
                int newA = totalA / totalPixels;
                int newR = totalR / totalPixels;
                int newG = totalG / totalPixels;
                int newB = totalB / totalPixels;

                int newARGB = (newA << 24) | (newR << 16) | (newG << 8) | (newB);
                blurredImage.setRGB(x, y, newARGB);
            }
        }
        return blurredImage;
    }

    public int clamp(int coord, int lb, int ub){
        return Math.max(lb, Math.min(ub, coord));
    }
}
