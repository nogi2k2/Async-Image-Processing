package com.image.imageprocessing.io;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FileImageIO implements ImageReadInf{

    @Override
    public <T> BufferedImage readImage(T src){
        try{
            String path = (String) src;
            File filePath = new File(path);
            return ImageIO.read(filePath);
        }catch (IOException ex){
            System.err.println("Image File Path is Invalid: " + ex.getMessage());
            return null;
        }catch (ClassCastException cce){
            System.err.println("File Path Must be a String");
        }
    }

    @Override
    public <T> void saveImage(BufferedImage image, T dest){
        try {
            String path = (String) dest;
            File filePath = new File(path);
            int index = path.lastIndexOf('.');
            String extension = "";
            if (index > 0){
                extension = path.substring(index+1);
            }

            if (extension.isEmpty()){
                System.err.println("Cannot Save Image: No File Extension Provided");
                return;
            }

            ImageIO.write(image, extension, filePath);
            System.out.println("Image Saved Successfully to the Path: " + path);
            return;
        }catch (IOException ioe) {
            System.err.println("File Path Invalid: " + ioe.getMessage());
        }catch (ClassCastException cce){
            System.err.println("File Path Must be a String");
        }
    }
}
