import java.io.FileOutputStream;

import net.sf.ntru.encrypt.EncryptionKeyPair;
import net.sf.ntru.encrypt.EncryptionParameters;
import net.sf.ntru.encrypt.NtruEncrypt;
import net.sf.ntru.encrypt.EncryptionPublicKey;
import net.sf.ntru.encrypt.EncryptionPrivateKey;


public class dec {
    private static final NtruEncrypt ntru =
        new NtruEncrypt(EncryptionParameters.APR2011_743_FAST);

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

    public static void generateKeyPair(String publicKeyFile, String privateKeyFile) throws Exception {
        System.out.println("NTRU key pair generation starts...");

        EncryptionKeyPair kp = ntru.generateKeyPair();

        EncryptionPublicKey pub = kp.getPublic();
        byte[] pubBytes = pub.getEncoded();

        try (FileOutputStream fos = new FileOutputStream(publicKeyFile)) {
            fos.write(pubBytes);
        }

        System.out.println("Public key saved to: " + publicKeyFile);
        System.out.println("Public key length: " + pubBytes.length + " bytes");
        System.out.println("Public key hex: " + bytesToHex(pubBytes));

        EncryptionPrivateKey priv = kp.getPrivate();
        byte[] privBytes = priv.getEncoded();

        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write(privBytes);
        }

        System.out.println("Private key saved to: " + privateKeyFile);
        System.out.println("Private key length: " + privBytes.length + " bytes");
        System.out.println("Private key hex: " + bytesToHex(privBytes));
    }

    public static void main(String[] args) {
        try {
            String publicKeyFile = "dormadsa-public.bin";
            String privateKeyFile = "dormadsa-private.bin";

            // Optional: allow filenames from command line
            if (args.length == 2) {
                publicKeyFile = args[0];
                privateKeyFile = args[1];
            }

            generateKeyPair(publicKeyFile, privateKeyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
