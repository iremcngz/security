import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MessageFileProcess {
    String content="";

    //is there already a message with the same id?
    public static boolean messageRead(String id) throws IOException {
        FileReader in = new FileReader("message_data.txt");
        BufferedReader br = new BufferedReader(in);
        String st;
        while ((st = br.readLine()) != null) {
            String[] line = st.split(" ");
            if (line[0].equals(id)) {
                in.close();
                return false;
            }
        }
        in.close();
        return true;
    }
    //check if message credentials - entered input match
    public boolean MessageCheck(String message_ids, String message_password,String receiver) throws IOException, NoSuchAlgorithmException {
        FileReader in = new FileReader("message_data.txt");
        String hashedMessagePassword = Hash.convertHexString(Hash.hash(message_password));
        BufferedReader br = new BufferedReader(in);
        String st;
        while ((st = br.readLine()) != null) {
            //message file content = line
            String[] line = st.split(" ");
            if (line[0].equals(message_ids) && line[2].equals(hashedMessagePassword) && line[3].equals(receiver)) {
                content = line[1];
                in.close();
                return true;
            }
        }
        in.close();
        return false;
    }
    //check if user matches with his/her name and password
    public boolean checkUserAndPassword(String user_id, String userPassword,String userFile) throws IOException, NoSuchAlgorithmException {
        FileReader reader = new FileReader(userFile);
        BufferedReader br = new BufferedReader(reader);
        String hashedPass = Hash.hashMessagePassword(userPassword);
        String st;
        boolean flag=false;
        while ((st=br.readLine())!= null) {
            String[] line = st.split(" ");
            if(line[0].equals(user_id) && line[1].equals(hashedPass)){
                flag= true;
                break;
            }
        }
        reader.close();
        return flag;
    }



    public static void writeMessageData(String message_id, String plain_content, String password,
                                        String authorized_username) throws IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("message_data.txt",true));
        EncDecOperations encordec = new EncDecOperations();
        String encrypted = encordec.encrypt(plain_content);
        String hashed = Hash.hashMessagePassword(password);
        String newMsg = message_id + " " + encrypted + " " +hashed+ " " + authorized_username;
        writer.write(newMsg +"\n");
        writer.close();
    }

}
