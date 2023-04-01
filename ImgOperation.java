import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.util.Map;
import java.awt.Graphics;
import java.awt.font.TextAttribute;

/**
 * 
 * This class contains methods for image operations such as writing text on
 * image, text from image, embedding in image, and extracting signature from
 * image.
 */
public class ImgOperation {

    /**
     * 
     * a 3D integer array representing the RGB color values of each in the given
     * BufferedImage.
     * The first dimension represents the y-coordinate of the pixel, the second
     * dimension represents the x-coordinate of the pixel,
     * and the third dimension represents the color channel (0 for red, 1 for green,
     * 2 for blue).
     * 
     * @param bufferedImage the input BufferedImage
     * @return a 3D integer array representing the RGB color values of each pixel in
     *         the given BufferedImage
     */
    // getting color array from image -> [y][x][0/1/2] -> r/g/b
    public static Integer[][][] getColorArrayFromImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        Integer[][][] colorArray = new Integer[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                colorArray[y][x][0] = red;
                colorArray[y][x][1] = green;
                colorArray[y][x][2] = blue;

            }
        }

        return colorArray;

    }

    /**
     * 
     * Returns a 4D boolean array representing the binary values of each color
     * channel of each pixel in the given BufferedImage.
     * The first dimension represents the y-coordinate of the pixel, the second
     * dimension represents the x-coordinate of the pixel,
     * the third dimension represents the color channel (0 for red, 1 for green, 2
     * for blue), and the fourth dimension represents the bit position (0 for MSB, 7
     * for LSB).
     * 
     * @param bufferedImage the input BufferedImage
     * @return a 4D boolean array representing the binary values of each color
     *         channel of each pixel in the given BufferedImage
     */

    public static boolean[][][][] getBitArrayFromImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean[][][][] bitArray = new boolean[height][width][3][8];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                bitArray[y][x][0] = getBitArrayFromColor(red);
                bitArray[y][x][1] = getBitArrayFromColor(green);
                bitArray[y][x][2] = getBitArrayFromColor(blue);

            }
        }

        return bitArray;
    }

    /**
     * 
     * Returns a boolean array representing the binary value of the given color.
     * The array has a length of 8, with the first element representing the MSB and
     * the last element representing the LSB.
     * 
     * @param color the input color
     * @return a boolean array representing the binary value of the given color
     */
    public static boolean[] getBitArrayFromColor(int color) {
        boolean[] bitArray = new boolean[8];
        String colorString = Integer.toBinaryString(color);
        int index = 7;
        for (int i = colorString.length() - 1; i >= 0; i--) {
            bitArray[index] = ('1' == colorString.charAt(i));
            // System.out.println(colorArr[index]+" "+colorString.charAt(i));
            index--;
        }

        return bitArray;
    }

    /**
     * 
     * a BufferedImage object created from the given 3D integer array representing
     * RGB color values of each pixel in the image.
     * The first dimension represents the y-coordinate of the pixel, the second
     * dimension represents the x-coordinate of the pixel,
     * and the third dimension represents the color channel (0 for red, 1 for green,
     * 2 for blue).
     * 
     * @param colorArray the input 3D integer array representing the RGB color
     *                   values of each pixel in the image
     * @return a BufferedImage object created from the given 3D integer array
     */

    public static BufferedImage getImageFromColorArray(Integer[][][] colorArray) {

        int width = colorArray[0].length;
        int height = colorArray.length;

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);

                // Retrieving the R G B values
                int red = colorArray[y][x][0];
                int green = colorArray[y][x][1];
                int blue = colorArray[y][x][2];

                // // Creating new Color object
                Color color = new Color(red, green, blue);

                // // Setting new Color object to the image
                bufferedImage.setRGB(x, y, color.getRGB());

            }
        }

        return bufferedImage;
    }

    /**
     * 
     * Returns a BufferedImage object created from the given 4D boolean array
     * representing binary values of each color channel of each pixel in the image.
     * The first dimension represents the y-coordinate of the pixel, the second
     * dimension represents the x-coordinate of the pixel,
     * the third dimension represents the color channel (0 for red, 1 for green, 2
     * for blue), and the fourth dimension represents the bit position (0 for MSB, 7
     * for LSB).
     *
     * @param bitArray the input 4D boolean array representing the binary values of
     *                 each color channel of each pixel in the image
     * @return a BufferedImage object created from the given 4D boolean array
     */
    public static BufferedImage getBufferedImageFromBitArray(boolean[][][][] bitArray) {
        int width = bitArray.length;
        int height = bitArray[0].length;
        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // Retrieving the R G B values
                int red = getColorFromBitArray(bitArray[y][x][0]);
                int green = getColorFromBitArray(bitArray[y][x][1]);
                int blue = getColorFromBitArray(bitArray[y][x][2]);

                // // Creating new Color object
                Color color = new Color(red, green, blue);
                // // Setting new Color object to the image
                bufferedImage.setRGB(x, y, color.getRGB());

            }
        }

        return bufferedImage;
    }

    /**
     * 
     * Returns an integer color value from the given boolean array representing the
     * binary value of a color channel.
     * The array has a length of 8, with the first element representing the MSB and
     * the last element representing the LSB.
     *
     * @param bitArray the input boolean array representing the binary value of a
     *                 color channel
     * @return an integer color value
     */
    private static int getColorFromBitArray(boolean[] bitArray) {

        int color = 0;
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i]) {
                color += (int) Math.pow(2, 7 - i);
            }
        }

        return color;
    }

    /**
     * 
     * Returns a HashMap containing the coordinates of different regions and
     * segments of the image.
     * The first key represents the region number, the second key represents the
     * segment number, and the third key represents the start or end coordinate (x
     * or y).
     *
     * @param imgCoordinates an integer array containing the coordinates of the
     *                       image (x1, y1, x2, y2)
     * @return a HashMap containing the coordinates of different regions and
     *         segments of the image
     */
    // getiing all regions
    public static HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> getCoordinatesFromCoverImg(
            int[] imgCoordinates) {

        // region no -> segment no -> start/end -> x/y coordinates
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> regions = new HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>>();

        int xs = imgCoordinates[0];
        int ys = imgCoordinates[1];
        int xe = imgCoordinates[2];
        int ye = imgCoordinates[3];

        regions.put("region-1", getRegionCoordinates(new int[] { xs, ys, xe / 2, ye / 2 }));
        regions.put("region-2", getRegionCoordinates(new int[] { xe / 2 + 1, ys, xe, ye / 2 }));
        regions.put("region-3", getRegionCoordinates(new int[] { xs, ye / 2 + 1, xe / 2, ye }));
        regions.put("region-4", getRegionCoordinates(new int[] { xe / 2 + 1, ye / 2 + 1, xe, ye }));

        return regions;
    }

    /**
     * 
     * Returns a HashMap containing the coordinates of different regions and
     * segments of the image * The first key represents the region number, the
     * second key represents the segment number, and the third key represents the
     * start or end coordinate (x or y).
     *
     * @param regionCoordinates an integer array containing the coordinates of the
     *                          region (x1, y1, x2, y2)
     * @return a HashMap containing the coordinates of different segments of the
     *         region
     */
    // getting all segments
    private static HashMap<String, HashMap<String, HashMap<String, Integer>>> getRegionCoordinates(
            int[] regionCoordinates) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> region = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();

        int xs = regionCoordinates[0];
        int ys = regionCoordinates[1];
        int xe = regionCoordinates[2];
        int ye = regionCoordinates[3];

        region.put("segment-1", getSegmentCoordinates(new int[] { xs, ys, xe, ys + (ye - ys) / 4 }));
        region.put("segment-2",
                getSegmentCoordinates(new int[] { xs, ys + (ye - ys) / 4 + 1, xe, ys + (ye - ys) / 2 }));
        region.put("segment-3",
                getSegmentCoordinates(new int[] { xs, ys + (ye - ys) / 2 + 1, xe, ys + ((ye - ys) * 3) / 4 }));
        region.put("segment-4", getSegmentCoordinates(new int[] { xs, ys + ((ye - ys) * 3) / 4 + 1, xe, ye }));

        return region;
    }

    /**
     * 
     * Returns a HashMap containing the start and end coordinates of a segment.
     *
     * @param segmentCoordinates an integer array containing the coordinates of the
     *                           segment (x1, y1, x2, y2)
     * @return a HashMap containing the start and end coordinates of the segment
     */
    // getting start and end coordinates
    private static HashMap<String, HashMap<String, Integer>> getSegmentCoordinates(int[] segmentCoordinates) {

        HashMap<String, HashMap<String, Integer>> segment = new HashMap<String, HashMap<String, Integer>>();

        int xs = segmentCoordinates[0];
        int ys = segmentCoordinates[1];
        int xe = segmentCoordinates[2];
        int ye = segmentCoordinates[3];

        HashMap<String, Integer> start = new HashMap<String, Integer>();
        start.put("x", xs);
        start.put("y", ys);

        HashMap<String, Integer> end = new HashMap<String, Integer>();
        end.put("x", xe);
        end.put("y", ye);

        segment.put("start", start);
        segment.put("end", end);

        return segment;
    }

    /**
     * 
     * Returns a HashMap containing the coordinates of different fragments of the
     * image.
     * The first key represents the fragment number, the second key represents the
     * start or end coordinate (x or y), and the third key represents the x or y
     * coordinate value.
     *
     * @param bufferedImage the input image
     * @return a HashMap containing the coordinates of different fragments of the
     *         image
     */

    // getting the fragment coordinates of an image
    // fragment no -> start/end -> x/y coordinates
    public static HashMap<String, HashMap<String, HashMap<String, Integer>>> generateFragmentCoordinates(
            BufferedImage bufferedImage) {
        HashMap<String, HashMap<String, HashMap<String, Integer>>> fragCoordinates = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
        int xs = 0;
        int ys = 0;
        int xe = bufferedImage.getWidth() - 1;
        int ye = bufferedImage.getHeight() - 1;

        fragCoordinates.put("fragment-1", getFragmentCoordinates(new int[] { xs, ys, xe / 2, ye / 2 }));
        fragCoordinates.put("fragment-2", getFragmentCoordinates(new int[] { xe / 2 + 1, ys, xe, ye / 2 }));
        fragCoordinates.put("fragment-3", getFragmentCoordinates(new int[] { xs, ye / 2 + 1, xe / 2, ye }));
        fragCoordinates.put("fragment-4", getFragmentCoordinates(new int[] { xe / 2 + 1, ye / 2 + 1, xe, ye }));

        return fragCoordinates;
    }

    /**
     * 
     * Returns a HashMap containing the start and end coordinates of a fragment.
     *
     * @param fragCord an integer array containing the coordinates of the fragment
     *                 (x1, y1, x2, y2)
     * @return a HashMap containing the start and end coordinates of the fragment
     */

    private static HashMap<String, HashMap<String, Integer>> getFragmentCoordinates(int[] fragCord) {
        HashMap<String, HashMap<String, Integer>> fragment = new HashMap<String, HashMap<String, Integer>>();

        int xs = fragCord[0];
        int ys = fragCord[1];
        int xe = fragCord[2];
        int ye = fragCord[3];

        HashMap<String, Integer> start = new HashMap<String, Integer>();
        start.put("x", xs);
        start.put("y", ys);

        HashMap<String, Integer> end = new HashMap<String, Integer>();
        end.put("x", xe);
        end.put("y", ye);

        fragment.put("start", start);
        fragment.put("end", end);

        return fragment;
    }

    /**
     * 
     * Writes text on the given image at the specified coordinates and saves the
     * output image to the given file path.
     * 
     * @param coverImg           the input image file
     * @param sectionCoordinates a HashMap containing the coordinates of different
     *                           sections of the image
     * @param chequeData         a HashMap containing the data to be written on the
     *                           image
     * @param coverOutImg        the output image file path
     */

    public static void writeTextOnImage(File coverImg,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> sectionCoordinates,
            HashMap<String, String> chequeData, File coverOutImg) {

        BufferedImage bufferedCoverImg = null;
        try {
            bufferedCoverImg = ImageIO.read(coverImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graphics g = bufferedCoverImg.getGraphics();
        g.setFont(g.getFont().deriveFont(30f));
        g.setColor(new Color(10, 10, 10));
        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        // adding space bettween letters as an text attribute
        attributes.put(TextAttribute.TRACKING, 0.25);
        g.setFont(g.getFont().deriveFont(attributes));
        int xOffset = 5;
        int yOffset = 20;

        for (String key : chequeData.keySet()) {
            if (key.equals("cheque") || key.equals("amount")) {
                yOffset = 30;
            } else {
                yOffset = 20;
            }
            g.drawString(chequeData.get(key),
                    sectionCoordinates.get(key).get("start").get("x") + xOffset,
                    sectionCoordinates.get(key).get("start").get("y") + yOffset);
        }

        g.dispose();
        try {
            ImageIO.write(bufferedCoverImg, "png", coverOutImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * Embeds the given signature file in the given cover image and saves the output
     * image to the specified file path.
     * 
     * @param signPath       the path of the signature file to be embedded
     * @param coverImagePath the path of the cover image file
     * @param outputPath     the output file path for the embedded image
     * @throws IOException if an I/O error occurs while reading or writing the files
     */
    public static void embedSignatureInImage(String signPath, String coverImagePath, String outputPath)
            throws IOException {

        File sign = new File(signPath);
        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(sign);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int) sign.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();
        // System.out.println(arr.length);
        /// System.out.println(Arrays.toString(arr));

        embed(arr, new File(coverImagePath), outputPath);
    }

    /**
     * 
     * Embeds the given signature bytes in the given cover image and saves the image
     * to the specified file. The method reads the cover image file and the
     * signature bytes, and then embeds the signature bytes in the cover image by
     * modifying the RGB values of the pixels in a specific region of the image. The
     * method uses the getCoordinatesFromCoverImg method to get the coordinates of
     * the region in which the signature is to be embedded. The method then iterates
     * over the pixels in the region and modifies the RGB values of the pixels to
     * embed the signature bytes. The method saves the modified image to the
     * specified output file path.
     * 
     * @param signBytes  a byte array containing the signature bytes to be embedded
     * @param image      the cover image file
     * @param outputPath the output file path for the embedded image
     * @throws IOException if an I/O error occurs while reading or writing the files
     */

    // embedding sign file bytes
    public static void embed(byte[] signBytes, File image, String outputPath) throws IOException {
        BufferedImage img = ImageIO.read(image);
        int width = img.getWidth();
        int height = img.getHeight();
        // storing the image coordinates for getting the segmented coordinates
        int[] imgCoordinates = { 0, 0, width - 1, height - 1 };

        // storing segmented coordinates in segmentedImgCoordinateMap : using methods
        // from GenerateCoordinates class
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> segmentedImgCoordinateMap = getCoordinatesFromCoverImg(
                imgCoordinates);

        int xs = segmentedImgCoordinateMap.get("region-4").get("segment-2").get("start").get("x");
        int ys = segmentedImgCoordinateMap.get("region-4").get("segment-2").get("start").get("y");
        int xe = segmentedImgCoordinateMap.get("region-4").get("segment-3").get("end").get("x");
        int ye = segmentedImgCoordinateMap.get("region-4").get("segment-3").get("end").get("y");

        int c = 0;

        boolean embedded = false;
        int cur = 0;
        int xOffset = 30;
        int yOffset = 20;

        for (int y = ys + yOffset; y < ye - yOffset && !embedded; y++) {
            for (int x = xs + xOffset; x < xe - xOffset && !embedded; x++) {
                // Retrieving contents of a pixel
                int pixel = img.getRGB(x, y);
                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                red = signBytes[cur++] + 128;
                // green = signBytes[cur++] + 128;
                blue = signBytes[cur++] + 128;
                if (cur == signBytes.length) {
                    // embedded = true;
                    // System.out.println("embedded");
                    c++;
                    cur = 0;
                }
                // Creating new Color object
                color = new Color(red, green, blue);
                // Setting new Color object to the image
                img.setRGB(x, y, color.getRGB());
            }
        }
        // Saving the modified image
        File file = new File(outputPath);
        ImageIO.write(img, "png", file);
        System.out.println("# signature embedded in the image " + c + " times...");

    }

    /**
     * 
     * Reads text from the given image at the specified coordinates and saves the
     * extracted data to a text file.
     * 
     * @param stegoCover         the input image file
     * @param sectionCoordinates a HashMap containing the coordinates of different
     *                           sections of the image
     * @param chequeDataPath     the output file path for the extracted data
     * @throws IOException if an I/O error occurs while reading or writing the files
     */

    public static void readTextFromImage(File stegoCover,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> sectionCoordinates, String chequeDataPath)
            throws IOException {
        HashMap<String, String> chequeData = new HashMap<String, String>();
        BufferedImage bufferedCoverImg = null;
        try {
            bufferedCoverImg = ImageIO.read(stegoCover);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : sectionCoordinates.keySet()) {
            int xs = sectionCoordinates.get(key).get("start").get("x");
            int ys = sectionCoordinates.get(key).get("start").get("y");
            int xe = sectionCoordinates.get(key).get("end").get("x") + 5;
            int ye = sectionCoordinates.get(key).get("end").get("y") + 5;
            if (key.equals("date") || key.equals("name")) {
                // xs -= 10;
                ys -= 5;
            }

            BufferedImage subImg = bufferedCoverImg.getSubimage(xs, ys, xe - xs, ye - ys);

            try {
                ImageIO.write(subImg, "png", new File(chequeDataPath + "/" + key + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String extractedText = OCR.performOCR(new File(chequeDataPath + "/" + key + ".png")).trim();

            chequeData.put(key, extractedText);
            // System.out.println(key + " : " + extractedText);

        }
        String chequeDataText = "";
        chequeDataText += "name-" + chequeData.get("name") + System.lineSeparator() +
                "amount-" + chequeData.get("amount") + System.lineSeparator() +
                "date-" + chequeData.get("date") + System.lineSeparator() +
                "cheque-" + chequeData.get("cheque").substring(0, 6) + " " +
                chequeData.get("cheque").substring(6, 15) + " " +
                chequeData.get("cheque").substring(15, 21) + " " +
                chequeData.get("cheque").substring(21, 23);
        System.out.println("# extracted text data : " + System.lineSeparator() + chequeDataText);

        // passing file instance in filewriter
        FileWriter wr = new FileWriter(new File(chequeDataPath + "/extracted-cheque-data.txt"));

        // calling writer.write() method with the string
        wr.write(chequeDataText);

        // flushing the writer
        wr.flush();

        // closing the writer
        wr.close();
    }

    /**
     * 
     * Extracts the signature from the given cover image and saves it to the
     * specified file path.
     * 
     * @param coverPath      the path of the cover image file
     * @param extractionPath the output file path for the extracted signature
     * @param hideCount      the number of times the signature is hidden in the
     *                       image
     * @throws IOException if an I/O error occurs while reading or writing the files
     */
    public static void extractSignatureFromImage(String coverPath, String extractionPath, int hideCount)
            throws IOException {
        byte[] signBytes = extract(new File(coverPath), hideCount);
        // System.out.println(Arrays.toString(extract(new
        // File("SignEmbedding/embedded-cover.png"))));
        // System.out.println(Arrays.toString(signBytes));

        File file = new File(extractionPath);
        OutputStream os = new FileOutputStream(file);

        // Starting writing the bytes in it
        os.write(signBytes);

        os.close();
    }

    /**
     * 
     * Extracts the embedded signature bytes from the given image file.
     * 
     * @param image     the input image file
     * @param hideCount the number of times the signature is hidden in the image
     * @return a byte array containing the extracted signature bytes
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static byte[] extract(File image, int hideCount) throws IOException {
        BufferedImage img = ImageIO.read(image);
        int width = img.getWidth();
        int height = img.getHeight();

        // storing the image coordinates for getting the segmented coordinates
        int[] imgCoordinates = { 0, 0, width - 1, height - 1 };

        // storing segmented coordinates in segmentedImgCoordinateMap : using methods
        // from GenerateCoordinates class
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> segmentedImgCoordinateMap = getCoordinatesFromCoverImg(
                imgCoordinates);

        int xs = segmentedImgCoordinateMap.get("region-4").get("segment-2").get("start").get("x");
        int ys = segmentedImgCoordinateMap.get("region-4").get("segment-2").get("start").get("y");
        int xe = segmentedImgCoordinateMap.get("region-4").get("segment-3").get("end").get("x");
        int ye = segmentedImgCoordinateMap.get("region-4").get("segment-3").get("end").get("y");
        int[] pixels = new int[width * height];

        byte[] signBytes = new byte[256];

        int c = hideCount;
        int[] sumArr = new int[256];

        boolean extracted = false;
        int cur = 0;

        int xOffset = 30;
        int yOffset = 20;

        for (int y = ys + yOffset; y < ye - yOffset && !extracted; y++) {
            for (int x = xs + xOffset; x < xe - xOffset && !extracted; x++) {
                // Retrieving contents of a pixel
                int pixel = img.getRGB(x, y);
                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                // Modifying the RGB values

                // red = signBytes[cur++] + 128;
                // // green = signBytes[cur++] + 128;
                // blue = signBytes[cur++] + 128;

                sumArr[cur++] += red - 128;
                sumArr[cur++] += blue - 128;

                if (cur == 256) {
                    // extracted = true;

                    c--;
                    cur = 0;
                    if (c == 0) {
                        extracted = true;
                    }
                }
            }
        }
        // Saving the modified image
        System.out.println("# signatyre extracted successfully..." + hideCount + " times...");
        // System.out.println(c);
        // System.out.println(Arrays.toString(sumArr));

        for (int i = 0; i < signBytes.length; i++) {

            signBytes[i] = (byte) (sumArr[i] / hideCount);
            // System.out.println(sumArr[i] / 256);
        }
        return signBytes;

    }

}
