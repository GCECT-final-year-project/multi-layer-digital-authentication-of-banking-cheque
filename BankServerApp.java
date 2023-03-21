import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class BankServerApp {
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex=2;
    public static void main(String[] args) {

         //Stego iamge
         File stegoCover = new File("server-assets/stego-cover/sign-embedded-stego-cover.png");

        Path sectionCoordinatesText = FileSystems.getDefault().getPath("server-assets/stego-cover/cheque-section-coordinates.txt");
        String chedueDataPath = "server-assets/cheque-data";
        TextOnImage.readTextDataFromCover(stegoCover, sectionCoordinatesText, chedueDataPath);


        try {
            ImgOperation.extractSignatureFromImage("server-assets/stego-cover/sign-embedded-stego-cover.png", "server-assets/dig-sign/extracted-digital-sign.txt",250);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DigitalSignature.verifySignature("server-assets/dig-sign", chedueDataPath);
        // fingerprint file
        int size=BankServerApp.secretImgSize;
    
       


        String extractionPath = "server-assets/extracted-fPrints";
        //extracting the fingerprint from the stego image file using transform domain steganography
        TDS.extractSecretImage(stegoCover, size, extractionPath);

        //fingerprint image matching
        

    }
}
