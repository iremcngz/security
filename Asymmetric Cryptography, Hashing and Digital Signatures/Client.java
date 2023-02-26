import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Client {

    static String keyFile = "public.key";
    static String userName="irel";
    static String serialNumber="1234-5678-9101";
    static String mac=getMacAdress();
    static String diskId;
    static PublicKey publicKey;
    static byte[] hashedAndSigned;
    public static String plaintext;
    static String motherBoardID;
    static {
        diskId = getDSerialNumber("C");
    }
    static {
        try {
            motherBoardID = getMotherBoardSerialNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBytesFromFile(String file) throws IOException {
        File f = new File(file);
        byte[] buffer = new byte[(int)f.length()];
        FileInputStream is = new FileInputStream(file);
        is.read(buffer);
        is.close();
        return  buffer;
    }

    public static String getMacAdress(){
        InetAddress ip;
        String macAdress="";
        try {

            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
            macAdress=sb.toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e){
            e.printStackTrace();
        }
        return macAdress;
    }


    public static String getDSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }

    static String getMotherBoardSerialNumber() throws IOException {
        File file = File.createTempFile("realhowto", ".vbs");
        file.deleteOnExit();
        FileWriter fw = new java.io.FileWriter(file);

        String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
                + "For Each objItem in colItems \n" + "    Wscript.StdOut.Writeline objItem.SerialNumber \n"
                + "    exit for  ' do the first cpu only! \n" + "Next \n";
        fw.write(vbs);
        fw.close();
        Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line, result = new String();
        while ((line = input.readLine()) != null) {
            result += line;
        }
        if (result.equalsIgnoreCase(" ")) {
            System.out.println("Result is empty");
        }
        input.close();
        return result;
    }
    //concate all IDs and serial numbers
    static {
            plaintext = userName+"$"+serialNumber+"$"+mac+"$"+diskId+"$"+motherBoardID;
    }


    public static byte[] encryptContent(String content) throws  NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher encipher = Cipher.getInstance("RSA");
        encipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encipher.doFinal(contentBytes);
        return encryptedMessageBytes;
    }

    public static boolean verificationProcess(byte[] signatureToCheck) throws SignatureException, NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        byte[] plaint=plaintext.getBytes(StandardCharsets.UTF_8);
        String hashed=LicenseManager.hashContent(plaint);
        byte[] hashedbyte =hashed.getBytes(StandardCharsets.UTF_8);
        signature.update(hashedbyte);
        boolean isSigned;
        try {
            isSigned=signature.verify(signatureToCheck);
        }catch (SignatureException s){
            isSigned=false;
        }
        return isSigned;
    }

    public static void main(String[] args) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, SignatureException {

        System.out.println("Client started...");
        System.out.println("My MAC: "+mac);
        System.out.println("My Disk ID: "+diskId);
        System.out.println("My Motherboard ID: "+motherBoardID);
        System.out.println("LicenseManager service started... ");
        byte[] publicKeyBytes = getBytesFromFile(keyFile);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(publicKeySpec);
        File f = new File("license.txt");
        if (f.exists()){
            if(verificationProcess(getBytesFromFile("license.txt"))){
                System.out.println("Succeed. The license is correct");
            }else{
                System.out.println("Client -- The license file has been broken!!");
                licenseProcess(true);
            }
        } else{
            System.out.println("Client -- License file is not found");
            licenseProcess(false);
        }
    }

    public static void licenseProcess(boolean broken) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, SignatureException, IOException {
        System.out.println("Client -- Raw License Text : "+plaintext);
        byte[] encryptedPlainText=encryptContent(plaintext);
        LicenseManager.encryptedUniqueIDs=encryptedPlainText;
        System.out.println("Client -- Encrypted license text : "+ Base64.getEncoder()
                .encodeToString(encryptedPlainText));
        System.out.println("Client -- MD5 License text : "+LicenseManager.hashContent(plaintext.getBytes()));
        LicenseManager.serverRequested();
        if(!broken){
            System.out.println("Client -- License not found.");
        }
        if(verificationProcess(hashedAndSigned)){
            writeToLicenseFile(hashedAndSigned);
            System.out.println("Client -- Succeed. The license file content is secured and signed by the server");
        }
    }
    public static void writeToLicenseFile(byte[] signature) throws IOException {
        OutputStream outStream = null;
        ByteArrayOutputStream byteOutStream = null;
        try {
            outStream = new FileOutputStream("license.txt");
            byteOutStream = new ByteArrayOutputStream();
            byteOutStream.write(signature);
            byteOutStream.writeTo(outStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outStream.close();
        }
    }
}
