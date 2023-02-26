import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MessageRegisterView extends JFrame  {


    JButton button_createMessage;
    JButton button_home;
    JLabel auth_username_label;
    JLabel password_label;
    JLabel confirm_label;
    JLabel message_codename_label;
    JLabel enterMessage_label;
    JTextField message_codename;
    JTextField password;
    JTextField confirm_password;
    JTextArea enterMessage;
    JComboBox userList;
    JScrollPane scroll;


    public MessageRegisterView() {

        setTitle("Register Form");

        // add ItemListener

        auth_username_label = new JLabel("Auth. User Name*");
        auth_username_label.setBounds(50, 50, 250, 50);
        auth_username_label.setFont(new Font("Serif", Font.BOLD, 20));

        setLayout(new FlowLayout());
        JPanel p = new JPanel();
        p.add(auth_username_label);
        add(p);


        String usernames[] = { "1", "2", "3" };

        // create checkbox
        userList = new JComboBox(usernames);
        userList.setBounds(280,50,200,50);
        add(userList);
        setLayout(null);


        password_label = new JLabel("Password*");
        password_label.setBounds(50, 200, 200, 50);
        password_label.setFont(new Font("Serif", Font.BOLD, 20));
        password = new JTextField();
        password.setBounds(150, 200, 180, 40);
        password.setAutoscrolls(true);

        confirm_label = new JLabel("Confirm password*");
        confirm_label.setBounds(340, 180, 300, 80);
        confirm_label.setFont(new Font("Serif", Font.BOLD, 20));
        confirm_password = new JTextField();
        confirm_password.setBounds(500, 200, 200, 40);
        confirm_password.setAutoscrolls(true);


        message_codename_label = new JLabel("Message Codename");
        message_codename_label.setBounds(50, 350, 300, 50);
        message_codename_label.setFont(new Font("Serif", Font.BOLD, 30));
        message_codename = new JTextField();
        message_codename.setBounds(350, 350, 200, 40);
        message_codename.setFont(new Font("Serif", Font.BOLD, 20));


        enterMessage = new JTextArea(20, 45);
        enterMessage.setFont(new Font("Serif",Font.BOLD,20));
        enterMessage.setBounds(350,500,400,100);
        enterMessage_label = new JLabel("ENTER YOUR MESSAGE*");
        enterMessage_label.setBounds(50, 500, 300, 50);
        enterMessage_label.setFont(new Font("Serif", Font.BOLD, 20));
        enterMessage.setAutoscrolls(true);


        button_createMessage = new JButton("Create Message");
        button_createMessage.setFont(new Font("Serif", Font.BOLD, 20));
        button_createMessage.setBounds(50, 850, 250, 100);
        set_button_createMessage();



        button_home = new JButton("Home");
        button_home.setFont(new Font("Serif", Font.BOLD, 20));
        button_home.setBounds(450, 850, 250, 100);
        set_button_home();


        createRegisterView();
    }

    public void createRegisterView() {

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        add(message_codename_label);
        add(auth_username_label);
        add(confirm_label);
        add(enterMessage_label);
        add(password_label);
        add(password);
        add(message_codename);
        add(enterMessage);
        add(confirm_password);
        add(button_createMessage);
        add(button_home);


        setSize(800, 1200);
        new BorderLayout(0,0);

        setVisible(true);

    }



    public void set_button_home(){
        button_home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
                HomePage hp = new HomePage();
                hp.setVisible(true);
            }
        });
    }
    public void set_button_createMessage(){

        button_createMessage.addActionListener(new ActionListener() {


            public void actionPerformed(ActionEvent ae) {
                String mssg=enterMessage.getText();
                if(mssg.equals("")) {
                    JOptionPane.showMessageDialog(null, "You must write a message!");
                }
                else if(password.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "You must write a password!");
                }else if(confirm_password.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "You must write the password again!");
                }else if(!confirm_password.getText().equals(password.getText())){
                    JOptionPane.showMessageDialog(null, "Written passwords are different!");
                }else if(message_codename.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "You must write a message codename!");
                }
                else {
                    try {
                        if(!MessageFileProcess.messageRead(message_codename.getText())){
                                JOptionPane.showMessageDialog(null, "There is already a message with the same id!");
                        }else{
                            try {
                                MessageFileProcess.writeMessageData(message_codename.getText(),mssg,password.getText(),
                                        userList.getSelectedItem().toString());
                                JOptionPane.showMessageDialog(null, "Your message is created");
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });


    }

}

