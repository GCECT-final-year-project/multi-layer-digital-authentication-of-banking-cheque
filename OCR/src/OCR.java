
import net.sourceforge.tess4j.Tesseract;

import java.io.*;



public class OCR {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
   
    public static String performOCR(File image){

        try{
            Tesseract tess = new Tesseract();
            tess.setDatapath("OCR/src/tessdata");
            tess.setLanguage("eng");
            String text = tess.doOCR(image);
            //System.out.println(text);
            return text;
        }catch(Exception e){
            e.printStackTrace();
        }


        return null;
    }
}
