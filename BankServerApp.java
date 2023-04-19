import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 
 * The BankServerApp class is responsible for executing the server-side process
 * of cheque system.
 * It the cheque data and digital signature from the stego cover image, extracts
 * the secret image,
 * compares it with the original secret image, and verifies the digital
 * signature. The class displays the
 * progress and results of each step on the console using the ConsoleOutput
 * class. The main method of the
 * class calls various methods from the TextOnImage, ImgOperation, TDS,
 * DigitalSignature, and ImageComparision
 * classes to perform the required operations. The class also uses the
 * TransferData class to receive files
 * from the client.
 */

public class BankServerApp {
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex = 2;

    /**
     * 
     * The main method of the BankServerApp class is responsible for executing the
     * server process of the cheque system.
     * It reads the cheque data and secret image from the stego cover image using
     * OCR and transform domain steganography respectively.
     * It then extracts the digital signature from the stego cover image and
     * verifies it using the public key.
     * Finally, it compares the extracted secret image with the original secret
     * image and displays the validation result on the console.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        ConsoleOutput.printSeparator(100);
        // Stego iamge
        File stegoCover = new File("server-assets/stego-cover/sign-embedded-stego-cover.png");

        Path sectionCoordinatesText = FileSystems.getDefault()
                .getPath("server-assets/stego-cover/cheque-section-coordinates.txt");
        String chedueDataPath = "server-assets/cheque-data";

        // READING TEXT FROM IMAGE USING OCR
        System.out.println("## READING TEXT FROM IMAGE USING OCR...");
        TextOnImage.readTextDataFromCover(stegoCover, sectionCoordinatesText, chedueDataPath);
        sc.nextLine();
        ConsoleOutput.printSeparator(100);

        // EXTRACTING DIGITAL SIGNATURE FROM IMAGE
        System.out.println("## EXTRACTING DIGITAL SIGNATURE FROM COVER IMAGE...");
        try {
            ImgOperation.extractSignatureFromImage("server-assets/stego-cover/sign-embedded-stego-cover.png",
                    "server-assets/dig-sign/extracted-digital-sign.txt", 250);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sc.nextLine();
        ConsoleOutput.printSeparator(100);

        // VERIFYING DIGITAL SIGNATURE
        System.out.println("## VERIFYING DIGITAL SIGNATURE...");
        boolean digSignVerificationResult = DigitalSignature.verifySignature("server-assets/dig-sign", chedueDataPath);
        sc.nextLine();
        ConsoleOutput.printSeparator(100);

        // EXTRACTING SECRET IMAGE FROM STEGO COVER IMAGE
        System.out.println("## EXTRACTING SECRET IMAGES FROM STEGO COVER IMAGE...");
        // fingerprint file
        int size = BankServerApp.secretImgSize;

        String extractionPath = "server-assets/extracted-fPrints";
        // extracting the fingerprint from the stego image file using transform domain
        // steganography
        TDS.extractSecretImage(stegoCover, size, extractionPath);
        sc.nextLine();
        ConsoleOutput.printSeparator(100);
        System.out.println("## COMPARING EXTRACTED SECRET IMAGES... ");
        // fingerprint image matching new double[4][4][2];

        double[][][] fingerprintMatchResult = ImageComparison.matchSecretImages(extractionPath,
                "server-assets/secret-images/thumb-" + size + "x" + size + ".png");

        boolean comparisionResult = ImageComparison.analyzeMatchResult(fingerprintMatchResult,
                "server-assets/image-comparision/secret-images-comparision-analysis.txt");
        System.out.println("# all secret images matched successfully ? : " + comparisionResult);

        sc.nextLine();
        ConsoleOutput.printSeparator(100);
        boolean finalResult = comparisionResult && digSignVerificationResult;
        System.out.println("## CHEQUE VALIDATION FINAL RESULT ##");
        ConsoleOutput.printSeparator(50);
        System.out.println("# DIGITAL SIGNATURE VALIDATION SUCCESSFUL ? : " + digSignVerificationResult);
        System.out.println("# SECRET IMAGE COMPARISION SUCCESSFUL ? : " + comparisionResult);
        ConsoleOutput.printSeparator(50);
        System.out.println("# CHEQUE VALIDATION SUCCESSFUL ? : " + finalResult);
        ConsoleOutput.printSeparator(100);

        
        
    }

}
