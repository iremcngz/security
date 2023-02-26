import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Map<String,String> map_passwords = new HashMap<String,String>();
        map_passwords.put("1","001a");
        map_passwords.put("2","001b");
        map_passwords.put("3","002");
        Hash hup = new Hash(map_passwords,"user_data.txt"); //add users to userdata file
        HomePage h = new HomePage();

    }
}
