import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Hash {

    public Hash(Map<String,String> user_passwords,String filename) throws IOException, NoSuchAlgorithmException {
        hashUserPasswords(user_passwords,filename);
    }

    public void hashUserPasswords(Map<String,String> user_passwords, String filename) throws IOException, NoSuchAlgorithmException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename,false));
        writer.write("keys ------------------------------------ hashed passwords\n");
        for(String key : user_passwords.keySet()) {
            String userPassHashed = convertHexString(hash(user_passwords.get(key)));
            writer.write(key+" "+userPassHashed+"\n");
        }
        writer.close();
    }

    public static String hashMessagePassword(String password) throws IOException, NoSuchAlgorithmException {
        return convertHexString(hash(password));

    }

    public static byte[] hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String convertHexString(byte[] hashVal)
    {
        BigInteger number = new BigInteger(1, hashVal); //returns +1 when number is positive
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64){
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
