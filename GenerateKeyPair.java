import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * 
 * The GenerateKeyPair class is responsible for generating a pair of public and
 * keys using RSA encryption.
 * 
 * The main method of this class initializes the KeyPairGenerator with RSA
 * algorithm and 2048 key size, generates the key pair,
 * 
 * and saves the public and private keys in separate files in the specified
 * directory.
 */
public class GenerateKeyPair {

    /**
     * 
     * The main method of this class generates a pair of public and private keys
     * using RSA encryption.
     * It initializes the KeyPairGenerator with RSA algorithm and 2048 key size,
     * generates the key pair,
     * and saves the public and private keys in separate files in the specified
     * directory.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            // PublicKey pub = kp.getPublic();
            // PrivateKey priv = kp.getPrivate();
            try (FileOutputStream out = new FileOutputStream("client-assets/keys/privateKey" + ".key")) {
                out.write(kp.getPrivate().getEncoded());
            }

            try (FileOutputStream out = new FileOutputStream("client-assets/keys/publicKey" + ".pub")) {
                out.write(kp.getPublic().getEncoded());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }
}
