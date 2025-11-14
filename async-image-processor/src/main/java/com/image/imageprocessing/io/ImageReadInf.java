package com.image.imageprocessing.io;

import java.awt.image.BufferedImage;

public interface ImageReadInf {
    <T> BufferedImage readImage(T src);
    <T> void saveImage(BufferedImage image, T dest);
}
