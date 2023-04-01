import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * The TextOnImage class provides methods for writing text on an image and text
 * from an image.
 * 
 * It also contains a method for combining these functionalities to write cheque
 * data on a cover image.
 */

public class TextOnImage {

    /**
     * 
     * Writes the given cheque data on the specified sections of the cover image and
     * saves the output image to a file.
     * 
     * @param coverImg           The input cover image file
     * @param sectionCoordinates A HashMap containing the coordinates of different
     *                           sections of the image
     * @param chequeData         A HashMap containing the data to be written on the
     *                           image
     * @param coverOutImg        The output image file path
     */

    public static void writeChequeDataOnCover(File coverImg, Path sectionCoordinatesText, Path chequeDataText,
            File coverOutImg) {

        HashMap<String, HashMap<String, HashMap<String, Integer>>> sectionCoordinates = getSectionCoordinates(
                sectionCoordinatesText);

        HashMap<String, String> chequeData = getChequeDataFromText(chequeDataText);

        // System.out.println(Arrays.asList(sectionCoordinates));
        System.out.println("# Cheque Data : " + Arrays.asList(chequeData));

        ImgOperation.writeTextOnImage(coverImg, sectionCoordinates, chequeData, coverOutImg);

    }

    /**
     * 
     * Reads the cheque data from a text file and returns it as a HashMap.
     * 
     * @param chequeDataText The path of the text file containing the cheque data
     * @return A HashMap containing the cheque data
     */
    private static HashMap<String, String> getChequeDataFromText(Path chequeDataText) {

        HashMap<String, String> chequeData = new HashMap<String, String>();

        // Creating an object of Scanner class
        Scanner sc;
        try {
            sc = new Scanner(chequeDataText);

            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] strArr = line.split("-");
                chequeData.put(strArr[0], strArr[1]);

            }
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return chequeData;
    }

    /**
     * 
     * Returns a HashMap containing the coordinates of different sections of the
     * image, read from a text file.
     * 
     * @param sectionCoordinatesText The path of the text file containing the
     *                               section coordinates
     * @return A HashMap containing the section coordinates
     */

    private static HashMap<String, HashMap<String, HashMap<String, Integer>>> getSectionCoordinates(
            Path sectionCoordinatesText) {

        HashMap<String, HashMap<String, HashMap<String, Integer>>> sectionCoordinates = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();

        // Creating an object of Scanner class
        Scanner sc;
        try {
            sc = new Scanner(sectionCoordinatesText);

            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] strArr = line.split(" ");
                sectionCoordinates.put(strArr[0],
                        new HashMap<String, HashMap<String, Integer>>() {
                            {
                                put("start",
                                        new HashMap<String, Integer>() {
                                            {
                                                put("x", Integer.valueOf(strArr[1]));
                                                put("y", Integer.valueOf(strArr[2]));

                                            }
                                        });
                                put("end",
                                        new HashMap<String, Integer>() {
                                            {
                                                put("x", Integer.valueOf(strArr[3]));
                                                put("y", Integer.valueOf(strArr[4]));

                                            }
                                        });
                            }

                        });

            }
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sectionCoordinates;
    }

    /**
     * 
     * Reads text from the given image at the specified coordinates and saves the
     * extracted data to a text file.
     * 
     * @param stegoCover         The input image file
     * @param sectionCoordinates A HashMap containing the coordinates of different
     *                           sections of the image
     * @param chequeDataPath     The output file path for the extracted data
     * @throws IOException if an I/O error occurs while reading or writing the files
     */

    public static void readTextDataFromCover(File stegoCover, Path sectionCoordinatesText, String chequeDataPath) {

        HashMap<String, HashMap<String, HashMap<String, Integer>>> sectionCoordinates = getSectionCoordinates(
                sectionCoordinatesText);
        // System.out.println(Arrays.asList(sectionCoordinates));
        try {
            ImgOperation.readTextFromImage(stegoCover, sectionCoordinates, chequeDataPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // System.out.println("READING TEXT FROM IMAGE DONE...");

    }
}
