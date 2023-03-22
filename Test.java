import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
    public static void main(String[] args) {
        // Reading file from local directory by
        // creating object of File class
        File fileA = new File("server-assets/extracted-fPrints/secret-reg-1-seg-1.png");
        File fileB = new File("server-assets/extracted-fPrints/secret-reg-4-seg-2.png");

        System.out.println("Color comparison : "+ImageComparision.compareByColorDifference(fileA, fileB)+"%");
        //System.out.println("Both images are extactly same ? " + strictlyCompareImage(fileA, fileB));

        System.out.println("Data buffer objects comparision : " + ImageComparision.compareImageByDataBufferObjects(fileA, fileB) + "%");

        //System.out.println("compare by pixelgrabber : ");
        //compareImageByPixelGrabber(fileA, fileB);
System.out.println(Character.toString((char)10003));
System.out.println(String.format("%.3f", 16.667523654362d));
    }

  

}
