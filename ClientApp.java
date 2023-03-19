import java.io.File;
import java.nio.file.Path;

public class ClientApp{
    public static void main(String[] args) {
        // cover image file
        File coverImg = new File("client-assets/cover-img/hdfc.png");


        Path sectionCoordinatesText = Path.of("client-assets/cover-img/cheque-section-coordinates.txt");
        Path chequeDataText = Path.of("client-assets/input-text/cheque-data.txt");


        File coverOutImg = new File("client-assets/cover-img/hdfc-cover.png");

        PrepareCover.writeChequeDataOnCover(coverImg, sectionCoordinatesText, chequeDataText,coverOutImg);



        // fingerprint file
        int size=32;
        File tPrint = new File("client-assets/secret-images/thumb-"+size+"x"+size+".png");


        //Output File
        File stegoCover = new File("client-assets/stego-output/stego-cover.png");


        //hiding the fingerprint file as a secret inside the cover image file using transform domain steganography
        TDS.hideSecretImage(coverOutImg, tPrint, stegoCover);
    }
   

       

    

   

}