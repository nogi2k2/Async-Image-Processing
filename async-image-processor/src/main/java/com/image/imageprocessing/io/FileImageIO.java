package com.image.imageprocessing.io;

import java.util.Optional;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FileImageIO implements ImageReadInf{

    @Override
    public <T> Optional<BufferedImage> readImage(T src){
       if (src instanceof String path){
            try {
                File imageFile = new File(path);
                return Optional.ofNullable(ImageIO.read(imageFile));
            } catch (Exception ioe){
                System.err.println("Unable to read Image from path: " + path + " | " + ioe.getMessage());
            }
       }else{
            String type = (src == null)? "Null": src.getClass().getName();
            System.err.println("ReadImage expects a String path. Type passed: " + type);
       }
       return Optional.empty();
    }

    @Override
    public <T> void saveImage(BufferedImage image, T dest){
        if (dest instanceof String path){
            try {
                File outputFile = new File(path);
                File parentDir = outputFile.getParentFile();

                if (parentDir != null && !parentDir.exists()){
                    if (!parentDir.mkdirs()){
                        System.err.println("Could not create parent directories for: " + path);
                        return;
                    }
                }

                int index = path.lastIndexOf('.');
                String extension = "";
                if (index > 0){extension = path.substring(index+1);}
                if (extension.isEmpty()){
                    System.err.println("Can't Save Image: No File Extension Provided in path: " + path);
                    return;
                }

                ImageIO.write(image, extension, outputFile);
                System.out.println("Image saved successfully to: " + outputFile.getAbsolutePath());

            } catch (Exception ioe){
                System.err.println("File path Invalid: " + ioe.getMessage()); 
            }
        }else {
            String type = (dest == null)? "Null" : dest.getClass().getName();
            System.err.println("SaveImage expects a String path. Type passed: " + type);
        }
    }
}
