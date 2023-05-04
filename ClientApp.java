import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

public class ClientApp {
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex = 2;

    /**
     * 
     * The main method of the ClientApp class is responsible for executing the
     * entire client-side process of the cheque system. It takes user input to proceed with
     * each step of the process and displays the progress and results on the
     * console. The method first reads the cover image file and cheque data text
     * file, and then writes the cheque data on the cover image using the
     * TextOnImage class. It then hides the secret image file inside the cover image
     * file using transform domain steganography with the TDS class. Next, it
     * generates a digital signature for the cheque data using the private key and
     * saves it in a file using the DigitalSignature class. The method then embeds
     * the digital signature in the stego cover image using the ImgOperation class.
     * Finally, it sends the required files to the server using the TransferData
     * class. The method displays the progress and results of each step on the
     * console using the ConsoleOutput class.
     */

    public static void main(String[] args) {
        ConsoleOutput.pressEnter();
        ConsoleOutput.printSeparator(100);
        // cover image file
        File coverImg = new File("client-assets/cover-img/hdfc.png");

        // Path sectionCoordinatesText =
        // Path.of("client-assets/cover-img/cheque-section-coordinates.txt");
        Path sectionCoordinatesText = FileSystems.getDefault()
                .getPath("client-assets/cover-img/cheque-section-coordinates.txt");
        Path chequeDataText = FileSystems.getDefault().getPath("client-assets/input-text/cheque-data.txt");

        File coverOutImg = new File("client-assets/cover-img/hdfc-cover.png");

        System.out.println("## WRITING TEXT ON IMAGE...");
        TextOnImage.writeChequeDataOnCover(coverImg, sectionCoordinatesText, chequeDataText, coverOutImg);
        ConsoleOutput.pressEnter();
        ConsoleOutput.printSeparator(100);

        // // fingerprint file
        int size = ClientApp.secretImgSize;
        File tPrint = new File("client-assets/secret-images/thumb-" + size + "x" + size + ".png");

        // Output File
        File stegoCover = new File("client-assets/stego-output/stego-cover.png");

        System.out.println("## HIDING SECRET IMAGE IN CHEQUE COVER IMAGE...");
        // hiding the fingerprint file as a secret inside the cover image file using
        // transform domain steganography
        TDS.hideSecretImage(coverOutImg, tPrint, stegoCover);
        ConsoleOutput.pressEnter();

        ConsoleOutput.printSeparator(100);

        System.out.println("## GENERATING DIGITAL SIGNATURE FORM CHEQUE DATA...");

        DigitalSignature.generateSignature("client-assets/dig-sign", "client-assets/input-text/cheque-data.txt");
        ConsoleOutput.pressEnter();
        ConsoleOutput.printSeparator(100);

        System.out.println("## EMBEDDING DIGITAL SIGNATURE IN CHEQUE COVER IMAGE...");

        try {
            ImgOperation.embedSignatureInImage("client-assets/dig-sign/digital-sign.txt",
                    "client-assets/stego-output/stego-cover.png",
                    "client-assets/stego-output/sign-embedded-stego-cover.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConsoleOutput.pressEnter();
        ConsoleOutput.printSeparator(100);

        System.out.println("## SENDING DATA TO SERVER...");
        try {
            TransferData.sendToServer("client-assets", "server-assets");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ConsoleOutput.printSeparator(100);
    }

}