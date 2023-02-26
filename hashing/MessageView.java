import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MessageView extends JFrame{

    JTextArea display;
    JScrollPane scroll;
    JButton button_return;
    JButton button_home;
    EncDecOperations encordec;

    public MessageView(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException {

        setTitle("Message View Page");

        encordec = new EncDecOperations();
        content = encordec.decrypt(content);

        display = new JTextArea ( 40, 70);
        display.setFont(new Font("Serif",Font.BOLD,40));
        display.setBounds(150,100,400,500);
        display.append(content);

        scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scroll.setBounds(100,100,400,500);
        scroll.setSize( 600, 500 );


        button_return = new JButton("Return");
        button_return.setFont(new Font("Serif",Font.BOLD,30));
        button_return.setBounds(100, 650, 250, 100);
        set_button_return();

        button_home = new JButton("Home");
        button_home.setFont(new Font("Serif",Font.BOLD,30));
        button_home.setBounds(450, 650, 250, 100);
        set_button_home();

        createMessageView();
    }

    public void set_button_return(){
        button_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
                AccessView av = null;
                try {
                    av = new AccessView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                av.setVisible(true);
            }
        });
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

    public void createMessageView() {

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        add(scroll);
        add(button_return);
        add(button_home);
        setSize(850,900);

        setLayout(null);
        setVisible(true);

    }

}
