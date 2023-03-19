import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;



public class DigitalSignature {
    



    public static void generateSignature(String signPath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(signPath+"/keys/privateKey.key"));
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pvt);


            String inputPath = signPath+"/cheque-data.txt";
            String outputPath = signPath+"/hashed-cheque-data.txt";
            generateHash(inputPath, outputPath);

            try (FileInputStream in = new FileInputStream(signPath+"/hashed-cheque-data.txt");
            FileOutputStream out = new FileOutputStream(signPath+"/digital-sign.txt")) {
            processFile(cipher, in, out);
            System.out.println("signature generated....");
        }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
       
    }
    static private void processFile(Cipher ci,InputStream in,OutputStream out)
        throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException
        {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) out.write(obuf);
        }





    public static void generateHash(String inputFilePath, String OutputFilePath) throws IOException, NoSuchAlgorithmException{
        File file = new File(inputFilePath);
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        BufferedReader br
            = new BufferedReader(new FileReader(file));
 
        // Declaring a string variable
        String input="";
        String line;
        // Condition holds true till
        // there is character in a string
        while ((line = br.readLine()) != null){
            input += line;
        }
            // Print the string
            //System.out.println(input); // printing the original input
            String hash=toHexString(getSHA(input));
            //System.out.println(getSHA(input).length); //size of hash
            //System.out.println(hash ); // printing the hash
            // System.out.println(toHexString(getSHA("hi there")) );
            Files.write(Paths.get(OutputFilePath), hash.getBytes());
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

}
