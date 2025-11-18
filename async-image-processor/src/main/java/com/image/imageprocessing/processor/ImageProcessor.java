package com.image.imageprocessing.processor;

import com.image.imageprocessing.filter.ImageFilter;
import com.image.imageprocessing.image.DrawMultipleImagesOnCanvas;
import com.image.imageprocessing.image.ImageData;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessor {
    private final ExecutorService executorService;

    public ImageProcessor(){
        int cores = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(cores);
        System.out.println("Thread Pool initialized with " + cores + " cores");
    }

    public void processImageAsync(BufferedImage image, int num, ImageFilter imageFilter, DrawMultipleImagesOnCanvas drawfn){
        int numHorizontalImages = image.getWidth() / num;
        int numVerticalImages = image.getHeight() / num;
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i<numHorizontalImages; i++){
            for (int j = 0; j<numVerticalImages; j++){
                BufferedImage subImage = image.getSubimage(i*num, j*num, num, num);
                int copyI = i;
                int copyJ = j;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        BufferedImage result = imageFilter.filter(subImage);
                        ImageData imageData = new ImageData(result, copyI * num, copyJ * num, num, num);
                        drawfn.addImageToQueue(imageData);
                    }catch(Exception e) {
                        System.err.println("Failed to process tile at " + copyI + "," + copyJ + ": " + e.getMessage());
                    }
                }, this.executorService);
                futures.add(future);
            }
        }

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }catch(Exception ex) {
            System.err.println("Error While Waiting for Image Processing to Complete: " + ex.getMessage());
        }
    }

    public void processImageSync(BufferedImage image, int num, ImageFilter imageFilter, DrawMultipleImagesOnCanvas drawfn){
        int numHorizontalImages = image.getWidth() / num;
        int numVerticalImages = image.getHeight() / num;

        for (int i = 0; i < numHorizontalImages; i++){
            for (int j = 0; j<numVerticalImages; j++){
                BufferedImage subImage = image.getSubimage(i*num, j*num, num, num);
                BufferedImage result = imageFilter.filter(subImage);
                ImageData imageData = new ImageData(result, i*num, j*num, num, num);
                drawfn.addImageToQueue(imageData);
            }
        }
    }

    public void shutdown(){
        this.executorService.shutdown();
    }
}
