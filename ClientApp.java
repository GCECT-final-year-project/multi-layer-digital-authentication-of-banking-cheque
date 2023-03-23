import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ClientApp{
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex=2;
    public static void main(String[] args) {
        ConsoleOutput.printSeparator(100);
        // cover image file
        File coverImg = new File("client-assets/cover-img/hdfc.png");


       // Path sectionCoordinatesText = Path.of("client-assets/cover-img/cheque-section-coordinates.txt");
        Path sectionCoordinatesText = FileSystems.getDefault().getPath("client-assets/cover-img/cheque-section-coordinates.txt");
        Path chequeDataText = FileSystems.getDefault().getPath("client-assets/input-text/cheque-data.txt");
    

        File coverOutImg = new File("client-assets/cover-img/hdfc-cover.png");

        System.out.println("## WRITING TEXT ON IMAGE...");
        TextOnImage.writeChequeDataOnCover(coverImg, sectionCoordinatesText, chequeDataText,coverOutImg);
        ConsoleOutput.printSeparator(100);


        // // fingerprint file
        int size=ClientApp.secretImgSize;
        File tPrint = new File("client-assets/secret-images/thumb-"+size+"x"+size+".png");


        //Output File
        File stegoCover = new File("client-assets/stego-output/stego-cover.png");

        System.out.println("## HIDING SECRET IMAGE IN CHEQUE COVER IMAGE...");
        //hiding the fingerprint file as a secret inside the cover image file using transform domain steganography
        TDS.hideSecretImage(coverOutImg, tPrint, stegoCover);

        ConsoleOutput.printSeparator(100);

        System.out.println("## GENERATING DIGITAL SIGNATURE FORM CHEQUE DATA...");

        DigitalSignature.generateSignature("client-assets/dig-sign", "client-assets/input-text/cheque-data.txt");
        
        ConsoleOutput.printSeparator(100);

        System.out.println("## EMBEDDING DIGITAL SIGNATURE IN CHEQUE COVER IMAGE...");
        
        try {
            ImgOperation.embedSignatureInImage("client-assets/dig-sign/digital-sign.txt", "client-assets/stego-output/stego-cover.png", "client-assets/stego-output/sign-embedded-stego-cover.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

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