public class gen {

    private EncryptionKeyPair kp = null;
    private NtruEncryption ntru = new NtruEncryption();
    public static void genkey() throws Exception {
        // RealWorldNtruEncryptionExample example = new RealWorldNtruEncryptionExample();
        // example.generateKeyPair("dormadsa-public.key", "dormadsa-private.key");
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
