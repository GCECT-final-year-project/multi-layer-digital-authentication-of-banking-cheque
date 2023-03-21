import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;

public class TransferData {
    public static void main(String[] args) {

        //transfer(null, null);


    }
    
    public static void sendToServer(String clientPath, String serverPath) throws IOException {
        //client-assets\stego-output\sign-embedded-stego-cover.png
        //server-assets\stego-cover\sign-embedded-stego-cover.png
        //client-assets\dig-sign\keys\publicKey.pub
        //server-assets\dig-sign\key\publicKey.pub
        //client-assets\cover-img\cheque-section-coordinates.txt
        //server-assets\stego-cover\cheque-section-coordinates.txt
        copyFile(clientPath+"/stego-output/sign-embedded-stego-cover.png", serverPath+"/stego-cover/sign-embedded-stego-cover.png");
        copyFile(clientPath+"/dig-sign/keys/publicKey.pub", serverPath+"/dig-sign/key/publicKey.pub");
        copyFile(clientPath+"/cover-img/cheque-section-coordinates.txt", serverPath+"/stego-cover/cheque-section-coordinates.txt");
    }



    private static void copyFile(String sourcePath, String destPath) throws IOException { 
    File sourceFile = new File(sourcePath);
    File destFile = new File(destPath);
        
    if (!sourceFile.exists()) {
        return;
    }
    if (!destFile.exists()) {
        destFile.createNewFile();
    }
    FileChannel source = null;
    FileChannel destination = null;
    source = new FileInputStream(sourceFile).getChannel();
    destination = new FileOutputStream(destFile).getChannel();
    if (destination != null && source != null) {
        destination.transferFrom(source, 0, source.size());
    }
    if (source != null) {
        source.close();
    }
    if (destination != null) {
        destination.close();
    }


}
}
