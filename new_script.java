import java.io.*;
// import RealWorldNtruEncryptionExample;
// import net.sf.ntru.encrypt.EncryptionKeyPair;
// import net.sf.ntru.encrypt.EncryptionParameters;
// import net.sf.ntru.encrypt.NtruEncrypt;
// import net.sf.ntru.encrypt.EncryptionPublicKey;
// import net.sf.ntru.encrypt.EncryptionPrivateKey;

public class new_script {

    public static void genkey() throws Exception {
        RealWorldNtruEncryptionExample example = new RealWorldNtruEncryptionExample();
        example.generateKeyPair("dormadsa-public.key", "dormadsa-private.key");
    }
    public static void main(String[] args) {
        try {
                // RealWorldNtruEncryptionExample ntruExample = new RealWorldNtruEncryptionExample();
                genkey();
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
    }
}
