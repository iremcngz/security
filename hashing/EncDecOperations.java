import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class EncDecOperations {

    private final String algo = "DES";
    private static Cipher encipher;
    private static Cipher decipher;
    private final String key = "hellowor";

    public EncDecOperations() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algo);

        encipher = Cipher.getInstance(algo);
        decipher = Cipher.getInstance(algo);

        encipher.init(Cipher.ENCRYPT_MODE, secretKey);
        decipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public String encrypt(String str) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = encipher.doFinal(utf8);
        return Base64.getEncoder().encodeToString(enc);

    }

    public String encryptedToString(byte[] enc) {
        return new String(enc);
    }

    public String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] dec = Base64.getDecoder().decode(str);
        byte[] utf8 = decipher.doFinal(dec);
        return new String(utf8, "UTF8");

    }
}


