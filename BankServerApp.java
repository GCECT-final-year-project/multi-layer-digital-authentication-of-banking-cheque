import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

public class BankServerApp {
    static int matrixInterval = 1;
    static int secretImgSize = 56;
    static int startingFragIndex = 2;

    public static void main(String[] args) {

        // Stego iamge
        File stegoCover = new File("server-assets/stego-cover/sign-embedded-stego-cover.png");

        Path sectionCoordinatesText = FileSystems.getDefault()
                .getPath("server-assets/stego-cover/cheque-section-coordinates.txt");
        String chedueDataPath = "server-assets/cheque-data";
        TextOnImage.readTextDataFromCover(stegoCover, sectionCoordinatesText, chedueDataPath);

        try {
            ImgOperation.extractSignatureFromImage("server-assets/stego-cover/sign-embedded-stego-cover.png",
                    "server-assets/dig-sign/extracted-digital-sign.txt", 250);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DigitalSignature.verifySignature("server-assets/dig-sign", chedueDataPath);
        // fingerprint file
        int size = BankServerApp.secretImgSize;

        String extractionPath = "server-assets/extracted-fPrints";
        // extracting the fingerprint from the stego image file using transform domain
        // steganography
        TDS.extractSecretImage(stegoCover, size, extractionPath);

        addSeparator();
        System.out.println("Fingerprint Image Matching... ");
        // fingerprint image matching new double[4][4][2];

        double[][][] fingerprintMatchResult = ImageComparision.matchSecretImages(extractionPath,
                "server-assets/secret-images/thumb-" + size + "x" + size + ".png");

        ImageComparision.analyzeMatchResult(fingerprintMatchResult, "server-assets/image-comparision");

        addSeparator();
    }

    private static void addSeparator() {

        System.out.println("#################################################################################");
    }
}
