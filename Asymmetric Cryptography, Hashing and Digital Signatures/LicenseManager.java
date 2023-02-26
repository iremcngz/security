import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class LicenseManager {

    static String[] keyFiles = {"public.key","private.key"};
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static byte[] encryptedUniqueIDs;
    static byte[] signedContent;
    static byte[] decryptedIDs;
    static String hashedDataToSign;
    public static void serverRequested() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, SignatureException {
        System.out.println("Server -- Server is being requested...");
        signProcess();
        System.out.println("Server -- Incoming Encrypted Text: "+ Base64.getEncoder()
                .encodeToString(encryptedUniqueIDs));
        System.out.println("Server -- Decrypted Text: "+new String(decryptedIDs));
        System.out.println("Server -- MD5 Plain license text: "+hashedDataToSign);
        System.out.println("Server -- Digital signature: "+Base64.getEncoder().encodeToString(signedContent));

    }


    public static void signProcess() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, SignatureException {
        decryptedIDs=decryptContent(encryptedUniqueIDs);
        String decryptedStr=new String(decryptedIDs);
        hashedDataToSign=hashContent(decryptedStr.getBytes());
        signedContent=signHashContent(hashedDataToSign);
        Client.hashedAndSigned=signedContent;
    }

    public static byte[] decryptContent(byte[] encryptedMessageBytes) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] privateKeyBytes = Client.getBytesFromFile(keyFiles[1]);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);
        Cipher decipher = Cipher.getInstance("RSA");
        decipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessageBytes = decipher.doFinal(encryptedMessageBytes);
        return decryptedMessageBytes;

    }

    public static String hashContent(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input);
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static byte[] signHashContent(String hashContent) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        byte[] hashBytes = hashContent.getBytes(StandardCharsets.UTF_8);

        signature.update(hashBytes);
        return signature.sign();
    }
}

