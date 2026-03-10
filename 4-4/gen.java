import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionPrivateKey;
import net.sf.ntru.encrypt.NtruEncrypt;
import net.sf.ntru.encrypt.EncryptionParameters;

public class gen {

    private static final NtruEncrypt ntru =
        new NtruEncrypt(EncryptionParameters.APR2011_743_FAST);

    public static byte[] readAllBytes(String filename) throws Exception {
        File file = new File(filename);
        byte[] data = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            int totalRead = 0;
            while (totalRead < data.length) {
                int bytesRead = fis.read(data, totalRead, data.length - totalRead);
                if (bytesRead == -1) {
                    break;
                }
                totalRead += bytesRead;
            }
        }

        return data;
    }

    public static void writeBytes(String filename, byte[] data) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }

    public static String bytesToHex(byte[] data) {
        char[] hexArray = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(hexArray[(b >> 4) & 0x0F]);
            sb.append(hexArray[b & 0x0F]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                System.out.println("Usage: java NtruDecryptFromFile <privatekey.bin> <ciphertext.bin> <outputfile>");
                return;
            }

            String privateKeyFile = args[0];
            String ciphertextFile = args[1];
            String outputFile = args[2];

            byte[] privateKeyBytes = readAllBytes(privateKeyFile);
            byte[] ciphertextBytes = readAllBytes(ciphertextFile);

            EncryptionPrivateKey privateKey = new EncryptionPrivateKey(privateKeyBytes);

            // Only the private key is needed for decryption here
            EncryptionKeyPair kp = new EncryptionKeyPair(privateKey, null);

            byte[] recoveredPlaintext = ntru.decrypt(ciphertextBytes, kp);

            writeBytes(outputFile, recoveredPlaintext);

            System.out.println("Ciphertext hex: " + bytesToHex(ciphertextBytes));
            System.out.println("Recovered plaintext: " + new String(recoveredPlaintext));
            System.out.println("Recovered plaintext hex: " + bytesToHex(recoveredPlaintext));
            System.out.println("Plaintext written to: " + outputFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
