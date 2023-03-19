import java.io.File;

public class OCR {
    public static void main(String[] args) {
        System.out.println(App.performOCR(new File("server-assets/stego-cover/sign-embedded-stego-cover.png")));
    }
}
