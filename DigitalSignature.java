import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class DigitalSignature {

    /**
     * 
     * Generates a digital signature for the given input file using RSA encryption
     * with PK1Padding.
     * The method reads the private key from the specified file path, generates a
     * hash of the input file,
     * and encrypts the hash using the private key to generate the digital
     * signature. The method saves the
     * generated signature to the specified file path.
     *
     * @param signPath      the path of the directory containing the private key
     *                      file
     * @param inputDataPath the path of the input file for which the signature is to
     *                      be generated
     * @throws NoSuchAlgorithmException  if the specified algorithm is not available
     * @throws InvalidKeySpecException   if the given key specification is
     *                                   inappropriate for the given key factory
     * @throws InvalidKeyException       if the given key is inappropriate for
     *                                   initializing the cipher
     * @throws BadPaddingException       if the padding of the input data is not
     *                                   valid
     * @throws IllegalBlockSizeException if the block size of the input data is not
     *                                   valid
     * @throws IOException               if an I/O error occurs while reading or
     *                                   writing the files
     */
    public static void generateSignature(String signPath, String inputDataPath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(signPath + "/keys/privateKey.key"));
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pvt);

            String inputPath = inputDataPath;
            String outputPath = signPath + "/hashed-cheque-data.txt";
            generateHash(inputPath, outputPath);

            try (FileInputStream in = new FileInputStream(signPath + "/hashed-cheque-data.txt");
                    FileOutputStream out = new FileOutputStream(signPath + "/digital-sign.txt")) {
                processFile(cipher, in, out);
                System.out.println("# signature generated successfully....");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }

    /**
     * 
     * Processes the input stream using the specified cipher and writes the output
     * to output stream.
     * The method reads the input stream in chunks of 1024 bytes, updates the cipher
     * with each chunk, and writes the output to the output stream.
     * After processing the entire input stream, the method finalizes the cipher and
     * writes the final output to the output stream.
     *
     * @param ci  the cipher to be used for processing the input stream
     * @param in  the input stream to be processed
     * @param out the output stream to which the processed data is to be written
     * @throws javax.crypto.IllegalBlockSizeException if the block size of the input
     *                                                data is not valid
     * @throws javax.crypto.BadPaddingException       if the padding of the input
     *                                                data is not valid
     * @throws java.io.IOException                    if an I/O error occurs while
     *                                                reading or writing the files
     */
    static private void processFile(Cipher ci, InputStream in, OutputStream out)
            throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {
        byte[] ibuf = new byte[1024];
        int len;
        while ((len = in.read(ibuf)) != -1) {
            byte[] obuf = ci.update(ibuf, 0, len);
            if (obuf != null)
                out.write(obuf);
        }
        byte[] obuf = ci.doFinal();
        if (obuf != null)
            out.write(obuf);
    }

    /**
     * 
     * Generates a SHA-256 hash of the given input file and saves it to the
     * specified file path.
     *
     * @param inputFilePath  the path of the input file for which the hash is to be
     *                       generated
     * @param OutputFilePath the output file path for the generated hash
     * @throws IOException              if an I/O error occurs while reading or
     *                                  writing the files
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     */
    public static void generateHash(String inputFilePath, String OutputFilePath)
            throws IOException, NoSuchAlgorithmException {
        File file = new File(inputFilePath);

        // Note: Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)

        // Creating an object of BufferedReader class
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String input = "";
        String line;
        // Condition holds true till
        // there is character in a string
        while ((line = br.readLine()) != null) {
            input += line;
        }
        // Print the string
        // System.out.println(input); // printing the original input
        String hash = toHexString(getSHA(input));
        // System.out.println(getSHA(input).length); //size of hash
        // System.out.println(hash ); // printing the hash
        // System.out.println(toHexString(getSHA("hi there")) );
        Files.write(Paths.get(OutputFilePath), hash.getBytes());
    }

    /**
     * 
     * Generates a SHA-256 hash of the given input string.
     *
     * @param input the input string for which the hash is to be generated
     * @return the byte array representation of the generated hash
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 
     * Converts the given byte array to a hexadecimal string.
     *
     * @param hash the byte array to be converted
     * @return the hexadecimal string representation of the byte array
     */

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /**
     * 
     * Verifies the digital signature of the given cheque data file using RSA
     * decryption with PKCS1Padding.
     * The method reads the public key from the specified file path, decrypts the
     * extracted signature using
     * the public key, generates a hash of the received input file, and compares the
     * generated hash with the
     * decrypted signature. The method saves the verification result to the
     * specified file path.
     *
     * @param digSignPath    the path of the directory containing the public key
     *                       file and the extracted signature file
     * @param chequeDataPath the path of the received input file for which the
     *                       signature is to be verified
     * @return true if the signature is verified successfully, false otherwise
     */
    public static boolean verifySignature(String digSignPath, String chequeDataPath) {
        boolean isMatching = false;
        String verificationResult = "## DIGITAL SIGNATURE VERIFICATION RESULT : ";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(digSignPath + "/key/publicKey.pub"));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pub = kf.generatePublic(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pub);
            // server-assets\dig-sign\extarcted-digital-sign.txt
            try (FileInputStream in = new FileInputStream(digSignPath + "/extracted-digital-sign.txt");
                    // server-assets\dig-sign\verfication
                    FileOutputStream out = new FileOutputStream(digSignPath +
                            "/verification/decrypted-dig-sign.txt")) {
                processFile(cipher, in, out);

                String inputPath = chequeDataPath + "/extracted-cheque-data.txt";
                String outputPath = digSignPath + "/verification/hashed-recieved-input.txt";
                generateHash(inputPath, outputPath);

                int res = matchFiles(digSignPath);

                if (res == -1) {
                    verificationResult += "VERIFICATION SUCCESSFUL...!";
                    isMatching = true;
                } else {
                    verificationResult += "VERIFICATION FAILED...!";
                    isMatching = false;
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            verificationResult += "VERIFICATION FAILED...!";
            isMatching = false;
        }
        System.out.println(verificationResult);
        try {
            Files.write(Paths.get(digSignPath + "/verification/verification-result.txt"),
                    verificationResult.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isMatching;
    }

    /**
     * 
     * Matches the contents of two files and returns the line number where the
     * contents differ.
     *
     * @param digSignPath the path of the directory containing the decrypted
     *                    signature file and the hashed input file
     * @return -1 if the files match, otherwise the line number where the contents
     *         differ
     * @throws IOException if an I/O error occurs while reading the files
     */
    static int matchFiles(String digSignPath) throws IOException {
        boolean isMatching = false;
        Path digSign = Paths.get(digSignPath + "/verification/decrypted-dig-sign.txt");
        Path hashedinput = Paths.get(digSignPath + "/verification/hashed-recieved-input.txt");
        // File inputFile = new
        // File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        // File outputFile = new
        // File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");

        try (BufferedReader bf1 = Files.newBufferedReader(digSign);
                BufferedReader bf2 = Files.newBufferedReader(hashedinput)) {

            int lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            if (bf2.readLine() == null) {
                return -1;
            } else {
                return lineNumber;
            }
        }
    }

}
