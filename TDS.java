import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.Color;
import javax.imageio.ImageIO;

// Transform Domain Steganography
/**
 * 
 * This class contains methods for performing the Transform Domain Steganography
 * (TDS) algorithm for hiding and extracting
 * 
 * secret images in cover images. The algorithm uses the Discrete Cosine
 * Transform (DCT) and the Forward Transformations
 * 
 * formulas to hide and extract the secret image bits in the cover image pixels.
 * The class also contains methods for
 * 
 * formatting colors, hiding bits in colors, and performing the TDS extraction
 * and hiding operations.
 * 
 * @author [Your Name]
 * @version 1.0
 */
public class TDS {

    // Taking cover image and the image file that will be hidden and after hiding
    // storing the stego image
    /**
     * 
     * Performs the TDS hiding operation to hide the secret image in the cover
     * image. The method takes in the cover image
     * file, the secret image file, and the stego cover image file where the hidden
     * secret image will be stored.
     * 
     * @param coverImage the cover image file
     * @param fPrint     the secret image file
     * @param stegoCover the stego cover image file where the hidden secret image
     *                   will be stored
     */
    public static void hideSecretImage(File coverImage, File fPrint, File stegoCover) {

        // SECRET IMAGE
        ConsoleOutput.printSeparator(50);
        System.out.println("## FIGERPRINT IMAGE");
        BufferedImage bufferedSecretImg = null;
        try {
            bufferedSecretImg = ImageIO.read(fPrint);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int widthSecret = bufferedSecretImg.getWidth();
        int heightSecret = bufferedSecretImg.getHeight();
        System.out.println("# Width: " + widthSecret + ", Height: " + heightSecret);
        // getting the array of individual bits of the secret image
        // [y-pos][x-pos][3(rgb)][8(8 bit color)]
        boolean[][][][] bitArrSecretImg = ImgOperation.getBitArrayFromImage(bufferedSecretImg);
        System.out.println("# Total size : " + bitArrSecretImg.length * bitArrSecretImg[0].length * 3 + " bytes");

        // segmenting the secret image
        HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg = ImgOperation
                .generateFragmentCoordinates(bufferedSecretImg);
        // System.out.println(Arrays.asList(segmentsScrtImg));
        ConsoleOutput.printSeparator(50);

        // COVER IAMGE
        BufferedImage bufferedCoverImage = null;
        try {
            bufferedCoverImage = ImageIO.read(coverImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int widthCover = bufferedCoverImage.getWidth();
        int heightCover = bufferedCoverImage.getHeight();

        System.out.println("## COVER IMAGE");
        System.out.println("# Width: " + widthCover + ", Height: " + heightCover);
        System.out.println("# Total size : " + widthCover * heightCover * 3 + " bytes");
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates = ImgOperation
                .getCoordinatesFromCoverImg(new int[] { 0, 0, widthCover - 1, heightCover - 1 });
        // System.out.println(Arrays.asList(coverCoordinates));
        Integer[][][] colorArrCover = ImgOperation.getColorArrayFromImage(bufferedCoverImage);
        // System.out.println(colorArrCover.length+" , "+colorArrCover[0].length);

        ConsoleOutput.printSeparator(50);

        System.out.println("## PERFORMING TRANSFORM DOMAIN STEGANOGRAPHY...");
        // PERFORMING STEGANOGRAPHY
        // colorArrCover <-- bitArrSecretImg

        for (int reg = 1; reg <= 4; reg++) {
            for (int seg = 1; seg <= 4; seg++) {
                // System.out.println("region : " + reg + ", seg:" + seg);
                ArrayList<Integer[][]> pixelArr = getPixelArrayFromSegment(colorArrCover, coverCoordinates, reg, seg);
                // System.out.println(pixelArr.size());

                int startingFragIndex = ClientApp.startingFragIndex;
                int matrixInterval = ClientApp.matrixInterval;

                hideBitArrInPixelArr(pixelArr, bitArrSecretImg, segmentsScrtImg, startingFragIndex, matrixInterval);

                updateColorArrayFromPixelArray(pixelArr, colorArrCover, coverCoordinates, reg, seg);

            }
        }
        //

        // getting the bufferd image from color array
        bufferedCoverImage = ImgOperation.getImageFromColorArray(colorArrCover);
        // Saving the modified image
        try {
            ImageIO.write(bufferedCoverImage, "png", stegoCover);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("# secret images are hidden successfully...");
        // ConsoleOutput.printSeparator(50);

    }

    /**
     * 
     * Updates the color array of the cover image from the pixel array of secret
     * image. The method takes in the pixel array of the secret image, the color
     * array of the cover image, the coordinates of the cover image segments, the
     * region index, and the segment index. The method uses the Reverse
     * Transformations formulas to update the color array of the cover image from
     * the pixel array of the secret image.
     * 
     * @param pixelArr         the pixel array of the secret image
     * @param colorArrCover    the color array of the cover image
     * @param coverCoordinates the coordinates of the cover image segments
     * @param reg              the region index
     * @param seg              the segment index
     */

    private static void updateColorArrayFromPixelArray(ArrayList<Integer[][]> pixelArr, Integer[][][] colorArrCover,
            HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates, int reg,
            int seg) {

        int curColor = 0;

        // segment coordinates
        int xs = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("x");
        int ys = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("y");
        int xe = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("x");
        int ye = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("y");
        int width = xe - xs + 1;
        int curIndex = 0;
        for (int y = ys; y <= ye && curIndex < pixelArr.size();) {
            for (int x = xs; x <= xe && curIndex < pixelArr.size();) {
                Integer[][] arr = pixelArr.get(curIndex++);
                colorArrCover[y][x][curColor] = arr[0][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[0][1];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[1][0];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }
                colorArrCover[y][x][curColor] = arr[1][1];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return;
                }

            }

        }
    }

    /**
     * 
     * Performs the TDS hiding operation to hide the secret image bits in the cover
     * image pixels. The method takes in the
     * pixel array of the cover image, the bit array of the secret image, the
     * segment coordinates of the secret image, the
     * starting fragment index, and the matrix interval. The method uses the Forward
     * Transformations formulas to hide the
     * secret image bits in the cover image pixels.
     * 
     * @param pixelArr          the pixel array of the cover image
     * @param bitArrSecretImg   the bit array of the secret image
     * @param segmentsScrtImg   the segment coordinates of the secret image
     * @param startingFragIndex the starting fragment index
     * @param matrixInterval    the matrix interval
     */

    private static void hideBitArrInPixelArr(ArrayList<Integer[][]> pixelArr, boolean[][][][] bitArrSecretImg,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg, int startingFragIndex,
            int matrixInterval) {

        int[][][] bitMatrixArr = new int[(bitArrSecretImg.length * bitArrSecretImg[0].length * 3 * 8) / 4][2][2];

        for (int frag = 1; frag <= 4; frag++) {
            int xs = segmentsScrtImg.get("fragment-" + frag).get("start").get("x");
            int ys = segmentsScrtImg.get("fragment-" + frag).get("start").get("y");
            int xe = segmentsScrtImg.get("fragment-" + frag).get("end").get("x");
            int ye = segmentsScrtImg.get("fragment-" + frag).get("end").get("y");
            int curPosition = 0;
            for (int y = ys; y <= ye && curPosition < bitMatrixArr.length; y++) {
                for (int x = xs; x <= xe && curPosition < bitMatrixArr.length; x++) {
                    for (int color = 0; color < 3; color++) {
                        for (int bit = 0; bit < 8; bit++) {

                            bitMatrixArr[curPosition++][(frag - 1) / 2][(frag - 1)
                                    % 2] = (bitArrSecretImg[y][x][color][bit]) ? 1 : 0;
                        }
                    }

                }
            }

        }

        performTDS(bitMatrixArr, pixelArr, startingFragIndex, matrixInterval);

    }

    // Forward Transformations formulas
    /*
     * 1.
     * A1’ = (a1+a2)/2 A2’ = (a1-a2)/2
     * A3’ = (a3-a4) A4’ = a4
     * 
     * 2.
     * A1’ = (a1+a4)/2 A2’ =(a1-a4)/2
     * A3’ = (a2+a3)/2 A4’ =(a2-a3)/2
     * 
     * 3.
     * A1’ = (a1+a3)/2 A2’ =(a2+a4)/2
     * A3’ = (a1-a3)/2 A4’ =(a2-a4)/2
     * 
     */

    /**
     * 
     * Performs the TDS operation to transform the cover image pixels to bit
     * matrices using the Forward Transformations
     * formulas. The method takes in the bit matrix array, the pixel array of the
     * cover image, the starting fragment index,
     * and the matrix interval. The method uses the Forward Transformations formulas
     * to transform the cover image pixels to
     * bit matrices.
     * 
     * @param bitMatrixArr      the bit matrix array
     * @param pixelArr          the pixel array of the cover image
     * @param startingFragIndex the starting fragment index
     * @param matrixInterval    the matrix interval
     */
    private static void performTDS(int[][][] bitMatrixArr, ArrayList<Integer[][]> pixelArr, int startingFragIndex,
            int matrixInterval) {
        int curPixel = 0;

        for (int curSquare = 0; curSquare < bitMatrixArr.length; curSquare++) {

            int[][] a = new int[2][2];

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    a[i][j] = pixelArr.get(curPixel)[i][j];

                }
            }

            // Use equations to forward transform a1, a2, a3, a4 to A1’, A2’, A3’ and A4’:
            // A1’ = (a1+a3)/2 A2’ =(a2+a4)/2
            // A3’ = (a1-a3)/2 A4’ =(a2-a4)/2

            int[][] A = new int[2][2];

            A[0][0] = (a[0][0] + a[0][1]) / 2;
            A[0][1] = a[0][1];
            A[1][0] = (a[1][0] + a[1][1]) / 2;
            A[1][1] = a[1][1];

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    A[i][j] = getAvgOfBounds(A[i][j], 4);

                }
            }

            int[][] R = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    A[i][j] = getAvgOfBounds(A[i][j], 4);
                    R[i][j] = hideBitInColor(bitMatrixArr[curSquare][i][j], A[i][j]);
                }
            }

            // reverse transform
            R[0][0] = 2 * R[0][0] - R[0][1];
            R[1][0] = 2 * R[1][0] - R[1][1];
            // formating color for out of bounds

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    R[i][j] = formatColor(R[i][j]);
                }
            }

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    pixelArr.get(curPixel)[i][j] = R[i][j];
                }
            }

            // skipping pixel arrays
            curPixel += matrixInterval;
        }
    }

    /**
     * 
     * Formats the color value to ensure it is within the range of 0 to 255.
     * 
     * @param color the color value to format
     * @return the formatted color value
     */
    private static int formatColor(Integer color) {
        if (color < 0) {
            color = 0;
        } else if (color > 255) {
            color = 255;
        }
        return color;
    }

    /**
     * 
     * Hides the bit in the color value. If the bit is 1, the color value is
     * incremented by 1. If the bit is 0, the color
     * value is decremented by 1.
     * 
     * @param bit the bit to hide
     * @param r   the color value to hide the bit in
     * @return the new color value with the hidden bit
     */
    private static int hideBitInColor(int bit, int r) {
        if (bit == 1) {
            r += 1;
        } else {
            r -= 1;
        }
        return r;
    }

    /**
     * 
     * Calculates the average of the bounds of a given value and a multiple of a
     * interval.
     * 
     * @param A1  the value to calculate the average of the bounds for
     * @param val the interval to use for calculating the bounds
     * @return the average of the bounds of A1 and a multiple of val
     */
    private static int getAvgOfBounds(int A1, int val) {
        int left = (A1 / val) * val;
        int right = (A1 / val + 1) * val;

        return (left + right) / 2;
    }

    /**
     * 
     * Retrieves the pixel array from a segment of the cover image color array based
     * the given segment coordinates.
     * The method takes in the color array of the cover image, the coordinates of
     * the cover image segments, the region index,
     * and the segment index. The method uses the Reverse Transformations formulas
     * to retrieve the pixel array from the
     * cover image color array. The pixel array is returned as an ArrayList of
     * Integer 2D arrays, where each 2D array
     * represents a 2x2 pixel block in the cover image segment.
     * 
     * @param colorArrCover    the color array of the cover image
     * @param coverCoordinates the coordinates of the cover image segments
     * @param reg              the region index
     * @param seg              the segment index
     * @return an ArrayList of Integer 2D arrays representing the pixel array of the
     *         cover image segment
     */
    private static ArrayList<Integer[][]> getPixelArrayFromSegment(Integer[][][] colorArrCover,
            HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates, int reg,
            int seg) {

        ArrayList<Integer[][]> pixelArr = new ArrayList<>();

        int curColor = 0;

        // segment coordinates
        int xs = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("x");
        int ys = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("start").get("y");
        int xe = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("x");
        int ye = coverCoordinates.get("region-" + reg).get("segment-" + seg).get("end").get("y");
        int width = xe - xs + 1;

        for (int y = ys; y <= ye;) {
            for (int x = xs; x <= xe;) {
                int c1 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c2 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c3 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                int c4 = colorArrCover[y][x][curColor];
                curColor++;
                x += curColor / 3;
                curColor %= 3;
                y += x / (xe + 1);
                x = xs + (x % width);
                if (y > ye) {
                    return pixelArr;
                }
                Integer[][] arr = new Integer[][] { { c1, c2 }, { c3, c4 } };

                pixelArr.add(arr);

            }

        }

        return pixelArr;
    }

    /**
     * 
     * Performs the TDS extraction operation to extract the hidden secret image from
     * the stego cover image. The method
     * takes in the stego cover image file, the size of the secret image, and the
     * extraction path where the extracted
     * secret images will be stored.
     * 
     * @param stegoCover     the stego cover image file
     * @param size           the size of the secret image
     * @param extractionPath the path where the extracted secret images will be
     *                       stored
     */
    public static void extractSecretImage(File stegoCover, int size, String extractionPath) {

        // SECRET IMAGE
        ConsoleOutput.printSeparator(50);
        System.out.println("## FIGERPRINT IMAGE");
        System.out.println("# Width: " + size + ", Height: " + size);

        BufferedImage bufferedSecretImg = new BufferedImage(size, size,
                BufferedImage.TYPE_INT_RGB);

        boolean[][][][] bitArrSecretImg = new boolean[size][size][3][8];
        System.out.println("# Total size : " + bitArrSecretImg.length * bitArrSecretImg[0].length * 3 + " bytes");

        // segmenting the secret image
        HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg = ImgOperation
                .generateFragmentCoordinates(bufferedSecretImg);
        // System.out.println(Arrays.asList(segmentsScrtImg));
        ConsoleOutput.printSeparator(50);

        // COVER IAMGE
        BufferedImage bufferedCoverImage = null;
        try {
            bufferedCoverImage = ImageIO.read(stegoCover);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int widthCover = bufferedCoverImage.getWidth();
        int heightCover = bufferedCoverImage.getHeight();

        System.out.println("## STEGO COVER IMAGE");
        System.out.println("# Width: " + widthCover + ", Height: " + heightCover);
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, Integer>>>> coverCoordinates = ImgOperation
                .getCoordinatesFromCoverImg(new int[] { 0, 0, widthCover - 1, heightCover - 1 });
        System.out.println("# Total size : " + widthCover * widthCover * 3 + " bytes");
        // System.out.println(Arrays.asList(coverCoordinates));
        Integer[][][] colorArrCover = ImgOperation.getColorArrayFromImage(bufferedCoverImage);
        // System.out.println(colorArrCover.length+" , "+colorArrCover[0].length);

        ConsoleOutput.printSeparator(50);

        System.out.println("## EXTRACTING IMAGES...");
        // PERFORMING STEGANOGRAPHY (EXTRACTING)
        // colorArrCover --> bitArrSecretImg

        for (int reg = 1; reg <= 4; reg++) {
            for (int seg = 1; seg <= 4; seg++) {
                // System.out.println("region : " + reg + ", seg:" + seg);
                ArrayList<Integer[][]> pixelArr = getPixelArrayFromSegment(colorArrCover, coverCoordinates, reg, seg);
                // System.out.println(pixelArr.size());

                int startingFragIndex = BankServerApp.startingFragIndex;
                int matrixInterval = BankServerApp.matrixInterval;

                extractBitArrFromPixelArray(pixelArr, bitArrSecretImg, segmentsScrtImg, startingFragIndex,
                        matrixInterval);

                bufferedSecretImg = ImgOperation.getBufferedImageFromBitArray(bitArrSecretImg);

                try {
                    ImageIO.write(bufferedSecretImg, "png",
                            new File(extractionPath + "/secret-reg-" + reg + "-seg-" + seg + ".png"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        System.out.println("# all secret images extracted successfully...!");

    }

    /**
     * 
     * Extracts the bit array of the secret image from the pixel array of cover
     * image. The method takes in the pixel array of the cover image, the bit array
     * of the secret image, the segment coordinates of the secret image, the
     * starting fragment index, and the matrix interval. The method uses the Reverse
     * Transformations formulas to extract the bit array of the secret image from
     * the cover image pixels. The extracted bit array is stored in the provided
     * bitArrSecretImg parameter.
     * 
     * @param pixelArr          the pixel array of the cover image
     * @param bitArrSecretImg   the bit array of the secret image
     * @param segmentsScrtImg   the segment coordinates of the secret image
     * @param startingFragIndex the starting fragment index
     * @param matrixInterval    the matrix interval
     */

    private static void extractBitArrFromPixelArray(ArrayList<Integer[][]> pixelArr, boolean[][][][] bitArrSecretImg,
            HashMap<String, HashMap<String, HashMap<String, Integer>>> segmentsScrtImg, int startingFragIndex,
            int matrixInterval) {

        int[][][] bitMatrixArr = new int[(bitArrSecretImg.length * bitArrSecretImg[0].length * 3 * 8) / 4][2][2];

        performTDSExtraction(bitMatrixArr, pixelArr, startingFragIndex, matrixInterval);

        for (int frag = 1; frag <= 4; frag++) {
            int xs = segmentsScrtImg.get("fragment-" + frag).get("start").get("x");
            int ys = segmentsScrtImg.get("fragment-" + frag).get("start").get("y");
            int xe = segmentsScrtImg.get("fragment-" + frag).get("end").get("x");
            int ye = segmentsScrtImg.get("fragment-" + frag).get("end").get("y");
            int curPosition = 0;
            for (int y = ys; y <= ye && curPosition < bitMatrixArr.length; y++) {
                for (int x = xs; x <= xe && curPosition < bitMatrixArr.length; x++) {
                    for (int color = 0; color < 3; color++) {
                        for (int bit = 0; bit < 8; bit++) {
                            bitArrSecretImg[y][x][color][bit] = bitMatrixArr[curPosition++][(frag - 1) / 2][(frag - 1)
                                    % 2] == 1 ? true : false;

                        }
                    }

                }
            }

        }

    }

    /**
     * 
     * Performs the TDS extraction operation to extract the hidden secret image from
     * the stego cover image. The method takes in the pixel array of the cover
     * image,
     * the bit array of the secret image, the segment coordinates of the secret
     * image,
     * the starting fragment index, and the matrix interval. The method uses the
     * Reverse
     * Transformations formulas to extract the bit array of the secret image from
     * the cover image pixels. The extracted bit array is stored in the provided
     * bitArrSecretImg parameter.
     * 
     * @param pixelArr          the pixel array of the cover image
     * @param bitArrSecretImg   the bit array of the secret image
     * @param segmentsScrtImg   the segment coordinates of the secret image
     * @param startingFragIndex the starting fragment index
     * @param matrixInterval    the matrix interval
     */

    private static void performTDSExtraction(int[][][] bitMatrixArr, ArrayList<Integer[][]> pixelArr,
            int startingFragIndex, int matrixInterval) {
        int curPixel = 0;

        for (int curSquare = 0; curSquare < bitMatrixArr.length; curSquare++) {

            int[][] a = new int[2][2];

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    a[i][j] = pixelArr.get(curPixel)[i][j];

                }
            }
            // Use equations to forward transform a1, a2, a3, a4 to A1’, A2’, A3’ and A4’:
            // A1’ = (a1+a3)/2 A2’ =(a2+a4)/2
            // A3’ = (a1-a3)/2 A4’ =(a2-a4)/2
            int[][] A = new int[2][2];

            A[0][0] = (a[0][0] + a[0][1]) / 2;
            A[0][1] = a[0][1];
            A[1][0] = (a[1][0] + a[1][1]) / 2;
            A[1][1] = a[1][1];

            int[][] R = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    R[i][j] = getAvgOfBounds(A[i][j], 4);

                }
            }

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    bitMatrixArr[curSquare][i][j] = A[i][j] >= R[i][j] ? 1 : 0;
                }
            }

            // skipping pixel arrays
            curPixel += matrixInterval;
        }
    }

}
