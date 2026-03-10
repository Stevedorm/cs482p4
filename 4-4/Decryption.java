import java.io.File;
import java.io.FileInputStream;

import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionPrivateKey;
import net.sf.ntru.encrypt.EncryptionPublicKey;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.NtruEncrypt;

public class Decryption {

    // Same parameter set as your professor's example
    private NtruEncrypt ntru = new NtruEncrypt(EncryptionParameters.APR2011_743_FAST);

    // Key pair
    private EncryptionKeyPair kp = null;

    public void loadKeyPairFromFiles(String privateKeyFilename, String publicKeyFilename) throws Exception {
        File privateFile = new File(privateKeyFilename);
        byte[] privateKeyBytes = new byte[(int) privateFile.length()];
        FileInputStream fis1 = new FileInputStream(privateFile);
        fis1.read(privateKeyBytes);
        fis1.close();

        File publicFile = new File(publicKeyFilename);
        byte[] publicKeyBytes = new byte[(int) publicFile.length()];
        FileInputStream fis2 = new FileInputStream(publicFile);
        fis2.read(publicKeyBytes);
        fis2.close();

        EncryptionPrivateKey privateKey = new EncryptionPrivateKey(privateKeyBytes);
        EncryptionPublicKey publicKey = new EncryptionPublicKey(publicKeyBytes);

        kp = new EncryptionKeyPair(privateKey, publicKey);
    }

    public byte[] readCiphertextFile(String ciphertextFilename) throws Exception {
        File file = new File(ciphertextFilename);
        byte[] ciphertextBytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(ciphertextBytes);
        fis.close();
        return ciphertextBytes;
    }

    public byte[] decrypt(byte[] ciphertextBytes) {
        return ntru.decrypt(ciphertextBytes, kp);
    }

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                System.out.println("Usage: java Decryption <privatekey.bin> <publickey.bin> <ciphertext.bin>");
                return;
            }

            String privateKeyFile = args[0];
            String publicKeyFile = args[1];
            String ciphertextFile = args[2];

            Decryption prog = new Decryption();

            prog.loadKeyPairFromFiles(privateKeyFile, publicKeyFile);

            byte[] ciphertextBytes = prog.readCiphertextFile(ciphertextFile);
            byte[] recoveredPlaintext = prog.decrypt(ciphertextBytes);

            System.out.println("Recovered cleartext:");
            System.out.println(new String(recoveredPlaintext));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}