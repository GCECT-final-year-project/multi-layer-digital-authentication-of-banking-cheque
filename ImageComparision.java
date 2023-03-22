import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageComparision {
    public static void main(String[] args) {
        // Reading file from local directory by
        // creating object of File class
        File fileA = new File("server-assets/extracted-fPrints/secret-reg-1-seg-1.png");
        File fileB = new File("server-assets/extracted-fPrints/secret-reg-4-seg-2.png");

        System.out.println("Color comparison : " + compareByColorDifference(fileA, fileB) + "%");
        // System.out.println("Both images are extactly same ? " +
        // strictlyCompareImage(fileA, fileB));

        System.out.println("Data buffer objects comparision : " + compareImageByDataBufferObjects(fileA, fileB) + "%");

        // System.out.println("compare by pixelgrabber : ");
        // compareImageByPixelGrabber(fileA, fileB);

    }

    public static double[][][] matchSecretImages(String extractionPath, String secretImgPath) {

        double[][][] matchResult = new double[4][4][2];
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                File fileA = new File(extractionPath + "/secret-reg-" + i + "-seg-" + j + ".png");
                File fileB = new File(secretImgPath);

                matchResult[i - 1][j - 1][0] = compareByColorDifference(fileA, fileB);
                matchResult[i - 1][j - 1][1] = compareImageByPixelGrabber(fileA, fileB);
            }
        }

        return matchResult;
    }

    public static double compareImageByDataBufferObjects(File fileA, File fileB) {

        double percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        return percentage;
    }

    public static boolean strictlyCompareImage(File fileA, File fileB) {
        try {
            // take buffer data from botm image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            // compare data-buffer objects //
            if (sizeA == sizeB) {
                for (int i = 0; i < sizeA; i++) {
                    if (dbA.getElem(i) != dbB.getElem(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
            return false;
        }
    }

    public static double compareByColorDifference(File fileA, File fileB) {
        // Initially assigning null
        BufferedImage imgA = null;
        BufferedImage imgB = null;

        // Try block to check for exception
        try {

            // Reading files
            imgA = ImageIO.read(fileA);
            imgB = ImageIO.read(fileB);
        }

        // Catch block to check for exceptions
        catch (IOException e) {
            // Display the exceptions on console
            System.out.println(e);
        }

        // Assigning dimensions to image
        int width1 = imgA.getWidth();
        int width2 = imgB.getWidth();
        int height1 = imgA.getHeight();
        int height2 = imgB.getHeight();

        // Checking whether the images are of same size or
        // not
        if ((width1 != width2) || (height1 != height2))

            // Display message straightaway
            System.out.println("Error: Images dimensions"
                    + " mismatch");
        else {

            // By now, images are of same size

            long difference = 0;

            // treating images likely 2D matrix

            // Outer loop for rows(height)
            for (int y = 0; y < height1; y++) {

                // Inner loop for columns(width)
                for (int x = 0; x < width1; x++) {

                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;

                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            // Total number of red pixels = width * height
            // Total number of blue pixels = width * height
            // Total number of green pixels = width * height
            // So total number of pixels = width * height *
            // 3
            double total_pixels = width1 * height1 * 3;

            // Normalizing the value of different pixels
            // for accuracy

            // Note: Average pixels per color component
            double avg_different_pixels = difference / total_pixels;

            // There are 255 values of pixels in total
            double percentage = (avg_different_pixels / 255) * 100;

            // Lastly print the difference percentage
            // System.out.println("Difference Percentage-->"+ percentage);
            return 100 - percentage;

        }
        return 0d;
    }

    static double compareImageByPixelGrabber(File fileA, File fileB) {

        // Load the images
        Image image1 = Toolkit.getDefaultToolkit().getImage(fileA.getPath());
        Image image2 = Toolkit.getDefaultToolkit().getImage(fileB.getPath());
        double percentage = 0;
        int count = 0;
        int width = 0, height = 0;
        try {

            PixelGrabber grabImage1Pixels = new PixelGrabber(image1, 0, 0, -1,
                    -1, false);
            PixelGrabber grabImage2Pixels = new PixelGrabber(image2, 0, 0, -1,
                    -1, false);

            int[] image1Data = null;

            if (grabImage1Pixels.grabPixels()) {
                width = grabImage1Pixels.getWidth();
                height = grabImage1Pixels.getHeight();
                image1Data = new int[width * height];
                image1Data = (int[]) grabImage1Pixels.getPixels();
            }

            int[] image2Data = null;

            if (grabImage2Pixels.grabPixels()) {
                width = grabImage2Pixels.getWidth();
                height = grabImage2Pixels.getHeight();
                image2Data = new int[width * height];
                image2Data = (int[]) grabImage2Pixels.getPixels();
            }

            if (image1Data.length != image2Data.length) {
                return 0;
            }
            for (int i = 0; i < image1Data.length; i++) {
                if (image1Data[i] == image2Data[i]) {
                    count++;
                }
            }

            // System.out.println("Pixels equal: "
            // + java.util.Arrays.equals(image1Data, image2Data));

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        percentage = ((double) count * 100) / (width * height);
        return percentage;
    }

    public static boolean analyzeMatchResult(double[][][] fingerprintMatchResult, String analysisResultPath) {
        StringBuffer resultString = new StringBuffer("## SECRET IMAGE COMPARISION RESULT:" + System.lineSeparator());
        String cross = "!";
        String tick = ">";
        boolean success = true;
        ArrayList<ArrayList<Integer>> mismatchAreas = new ArrayList<>();
        for (int i = 0; i < fingerprintMatchResult.length; i++) {
            for (int j = 0; j < fingerprintMatchResult[i].length; j++) {
                // for(int k=0;k<fingerprintMatchResult[i][j].length;k++) {
                // System.out.println("match"+"["+i+"]["+j+"]["+k+"]="+fingerprintMatchResult[i][j][k]);
                // }
                if (i == 3 && (j == 1 || j == 2)) {
                    continue;
                }
                boolean result = false;
                String resSign;

                if (fingerprintMatchResult[i][j][0] < 99.90 || fingerprintMatchResult[i][j][1] < 97.0) {
                    result = false;
                    mismatchAreas.add(new ArrayList<>(Arrays.asList(i + 1, j + 1)));
                    resSign = cross;
                } else {
                    result = true;
                    resSign = tick;
                }
                // String.format("%.0f", 1654621658874684.0d)
                resultString.append(
                        resSign + "   Region-" + (i + 1) + "-Segment-" + (j + 1) + ": Matched ? " + result + " || "
                                + " Matching(Color, Pixel) Percentage : "
                                + String.format("%.3f", fingerprintMatchResult[i][j][0])
                                + "% , " + String.format("%.3f", fingerprintMatchResult[i][j][1]) + "%"
                                + System.lineSeparator());

            }
        }
        if (mismatchAreas.size() > 0) {
            success = false;
            resultString.append("## SECRET IMAGE COMPARION RESULT: MISMATCHED AT " + mismatchAreas.size()
                    + " SEGMENT/SEGMENTS" + System.lineSeparator() + "## MISMATCHED SEGMENTS [ [reg,seg],...] ARE: "
                    + mismatchAreas.toString() + System.lineSeparator()
                    + "## SECRET IMAGES MATHCHING RESULT : MATCHING FAILED....!"
                    + System.lineSeparator());
        } else {
            resultString.append("## SECRET IMAGES MATHCHING RESULT : MATCHING SUCCESSFUL....!");
        }

        // System.out.println(resultString.toString());
        File file = new File(analysisResultPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        FileWriter wr;
        try {
            wr = new FileWriter(file);

            wr.write(resultString.toString());

            wr.flush();
            wr.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return success;

    }
}
