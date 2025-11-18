package com.image.imageprocessing.io;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface ImageReadInf {
    <T> Optional<BufferedImage> readImage(T src);
    <T> void saveImage(BufferedImage image, T dest);
}
