import java.io.File;
import java.nio.file.Path;


public class BankServerApp {
    public static void main(String[] args) {
        

        // fingerprint file
        int size=32;
    
        //Output File
        File stegoCover = new File("server-assets/stego-cover/stego-cover.png");


        String extractionPath = "server-assets/extracted-fPrints";
        //extracting the fingerprint from the stego image file using transform domain steganography
        TDS.extractSecretImage(stegoCover, size, extractionPath);


    }
}
