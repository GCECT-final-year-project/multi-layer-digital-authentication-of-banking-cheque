import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ClientApp{
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex=2;
    public static void main(String[] args) {
        // cover image file
        File coverImg = new File("client-assets/cover-img/hdfc.png");


       // Path sectionCoordinatesText = Path.of("client-assets/cover-img/cheque-section-coordinates.txt");
        Path sectionCoordinatesText = FileSystems.getDefault().getPath("client-assets/cover-img/cheque-section-coordinates.txt");
        Path chequeDataText = FileSystems.getDefault().getPath("client-assets/input-text/cheque-data.txt");
    

        File coverOutImg = new File("client-assets/cover-img/hdfc-cover.png");

        TextOnImage.writeChequeDataOnCover(coverImg, sectionCoordinatesText, chequeDataText,coverOutImg);



        // // fingerprint file
        int size=ClientApp.secretImgSize;
        File tPrint = new File("client-assets/secret-images/thumb-"+size+"x"+size+".png");


        //Output File
        File stegoCover = new File("client-assets/stego-output/stego-cover.png");


        //hiding the fingerprint file as a secret inside the cover image file using transform domain steganography
        TDS.hideSecretImage(coverOutImg, tPrint, stegoCover);


        DigitalSignature.generateSignature("client-assets/dig-sign", "client-assets/input-text/cheque-data.txt");
        try {
            ImgOperation.embedSignatureInImage("client-assets/dig-sign/digital-sign.txt", "client-assets/stego-output/stego-cover.png", "client-assets/stego-output/sign-embedded-stego-cover.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TransferData.sendToServer("client-assets", "server-assets");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   

       

    

   

}