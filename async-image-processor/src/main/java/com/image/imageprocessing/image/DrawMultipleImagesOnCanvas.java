package com.image.imageprocessing.image;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DrawMultipleImagesOnCanvas {

    private static final DrawMultipleImagesOnCanvas instance = new DrawMultipleImagesOnCanvas();
    private final Queue<ImageData> queue = new LinkedBlockingQueue<>();
    private GraphicsContext gc;

    private DrawMultipleImagesOnCanvas(){}
    public static DrawMultipleImagesOnCanvas getInstance(){return instance;}

    public void addImageToQueue(ImageData imageData){queue.offer(imageData);}

    public void clearCanvas(){
        if (this.gc != null){
            Platform.runLater(() -> {
                Canvas canvas = this.gc.getCanvas();
                this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            });
        }
    }

    public void initialize(Stage primaryStage, int width, int height){
        Canvas canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        this.gc.clearRect(0, 0, width, height);

        new AnimationTimer() {
            @Override
            public void handle(long now){
                ImageData imageData = queue.poll();
                if (imageData != null){
                    drawNextImage(imageData);
                }
            }
        }.start();

        StackPane stack = new StackPane(canvas);
        Scene scene = new Scene(stack, width, height);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Async Image Processor");
        primaryStage.show();
    }

    public void drawNextImage(ImageData imageData){
        this.gc.drawImage(SwingFXUtils.toFXImage(imageData.getImage(), null),
         imageData.getI(), imageData.getJ(), imageData.getX(), imageData.getY());
    }
}
