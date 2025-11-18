package com.image.imageprocessing;

import java.awt.image.BufferedImage;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.image.imageprocessing.filter.BoxBlurFilter;
import com.image.imageprocessing.filter.BrightnessFilter;
import com.image.imageprocessing.filter.ContrastFilter;
import com.image.imageprocessing.filter.GreyScaleFilter;
import com.image.imageprocessing.filter.ImageFilter;
import com.image.imageprocessing.filter.InvertFilter;
import com.image.imageprocessing.filter.SaturationFilter;
import com.image.imageprocessing.image.DrawMultipleImagesOnCanvas;
import com.image.imageprocessing.io.FileImageIO;
import com.image.imageprocessing.io.ImageReadInf;
import com.image.imageprocessing.processor.ImageProcessor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App extends Application{
    private static final String OUTPUT_DIRECTORY = "D:/Work/Projects/Java-backend/Async-Image-Processing/async-image-processor/src/main/outputs/";
    private static final String BASE_DIRECTORY = "D:/Work/Projects/Java-backend/Async-Image-Processing/async-image-processor/src/main/inputs/";
    private static final int TILE_SIZE = 10;
    private String currentImageName;

    private ImageProcessor processor;
    private DrawMultipleImagesOnCanvas drawCanvas;
    private ImageReadInf imageIO;
    private BufferedImage inputImage;
    private Scanner scanner;

    private void runCliMenu(){
        while (true) {
            try {
                int mode = getProcessingMode();
                if (mode == 0) break; 

                ImageFilter filter = getFilterFromUser();
                if (filter == null) continue; 

                drawCanvas.clearCanvas();
                System.out.println("\nStarting Image Processing");
                long startTime = System.nanoTime();

                if (mode == 1) {
                    System.out.println("Mode: SYNCHRONOUS");
                    processor.processImageSync(inputImage, TILE_SIZE, filter, drawCanvas);
                } else {
                    System.out.println("Mode: ASYNCHRONOUS");
                    processor.processImageAsync(inputImage, TILE_SIZE, filter, drawCanvas);
                }

                long endTime = System.nanoTime();
                long durationMs = (endTime - startTime) / 1_000_000; 

                System.out.println("----------------------------------------");
                System.out.println("Completed Processing");
                System.out.println("Total processing time: " + durationMs + " ms");
                System.out.println("----------------------------------------\n");

                System.out.println("Saving final processed Output");
                BufferedImage resultImage = filter.filter(inputImage);

                String originalName = this.currentImageName;
                String modeName = (mode == 1)? "Sync":"Async";
                String filterName = filter.getClass().getSimpleName();

                int dotIndex = originalName.lastIndexOf('.');
                String namePart = (dotIndex == -1)? originalName: originalName.substring(0, dotIndex);
                String extension = (dotIndex == -1)? ".png": originalName.substring(dotIndex);
                String newFileName = String.format("%s_%s_%s%s", namePart, modeName, filterName, extension);

                String fullOutputPath = OUTPUT_DIRECTORY + newFileName;
                imageIO.saveImage(resultImage, fullOutputPath);
                System.out.println("----------------------------------------\n");

            } catch (InputMismatchException ime) {
                System.err.println("Invalid input - Please enter a number");
                scanner.next(); 
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Exiting Main Menu");
        processor.shutdown();
        Platform.exit();
    }

    private int getProcessingMode(){
        System.out.println("Select Processing Mode:");
        System.out.println("  1. Synchronous (Sequential on one thread)");
        System.out.println("  2. Asynchronous (Parallel on " + Runtime.getRuntime().availableProcessors() + " threads)");
        System.out.println("  0. Exit");
        System.out.print("Choice: ");
        return scanner.nextInt();
    }

    private ImageFilter getFilterFromUser(){
        System.out.println("\nSelect Filter:");
        System.out.println("  1. Grayscale");
        System.out.println("  2. Brightness");
        System.out.println("  3. Contrast");
        System.out.println("  4. Saturation");
        System.out.println("  5. Invert");
        System.out.println("  6. Box Blur (3x3)");
        System.out.println("  0. Cancel");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return new GreyScaleFilter();
            case 2:
                System.out.print("  Enter brightness value: ");
                int brightness = scanner.nextInt();
                return new BrightnessFilter(brightness);
            case 3:
                System.out.print("  Enter contrast factor: ");
                double contrast = scanner.nextDouble();
                return new ContrastFilter(contrast);
            case 4:
                System.out.print("  Enter saturation factor: ");
                float saturation = scanner.nextFloat();
                return new SaturationFilter(saturation);
            case 5:
                return new InvertFilter();
            case 6:
                return new BoxBlurFilter(3); 
            case 0:
            default:
                System.out.println("Exiting Filter Menu");
                return null;
        }
    }

    @Override
    public void start(Stage primaryStage){
        processor = new ImageProcessor();
        drawCanvas = DrawMultipleImagesOnCanvas.getInstance();
        imageIO = new FileImageIO();
        scanner = new Scanner(System.in);

        System.out.println("----------------------------------------");
        System.out.println("Base image directory is set to: " + BASE_DIRECTORY);
        System.out.print("Please enter the image file name with the extension: ");
        String imageName = scanner.nextLine();
        this.currentImageName = imageName;
        String fullImagePath = BASE_DIRECTORY + imageName;

        System.out.println("Loading image from: " + fullImagePath);
        inputImage = imageIO.readImage(fullImagePath).orElse(null);

        if (inputImage == null) {
            System.err.println("----------------------------------------");
            System.err.println("FATAL: Failed to load image from: " + fullImagePath);
            System.err.println("Please check that the BASE_DIRECTORY is correct and the file exists");
            System.err.println("----------------------------------------");
            Platform.exit();
            return;
        }
        System.out.println("Image loaded (" + inputImage.getWidth() + "x" + inputImage.getHeight() + ")");
        drawCanvas.initialize(primaryStage, inputImage.getWidth(), inputImage.getHeight());
        Thread cliThread = new Thread(this::runCliMenu);
        cliThread.setDaemon(true); 
        cliThread.start();
    }

    @Override
    public void stop(){
        System.out.println("Shutting Down");
        if (processor != null){processor.shutdown();}
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
