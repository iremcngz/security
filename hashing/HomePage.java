import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HomePage extends JFrame {

    JLabel welcome;
    JButton button_access;
    JButton button_leave_message;

    public HomePage() {

        setTitle("Main Page");
        welcome = new JLabel("Welcome to MessageBox");
        welcome.setFont(new Font("Serif",Font.BOLD,50));
        welcome.setBounds(120, 100, 600, 100);
        button_access = new JButton("Access");
        button_access.setFont(new Font("Serif",Font.BOLD,30));
        button_access.setBounds(230, 300, 300, 100);
        set_button_access();
        button_leave_message = new JButton("Leave a message");
        button_leave_message.setFont(new Font("Serif",Font.BOLD,30));
        button_leave_message.setBounds(230, 450, 300, 100);
        set_button_leave_message();
        createHomePage();
    }

    public void set_button_access(){
        button_access.addActionListener(new ActionListener() {
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

    public void set_button_leave_message(){
        button_leave_message.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //setVisible(false);
                dispose();
                MessageRegisterView mrv = null;
                try {
                    mrv = new MessageRegisterView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mrv.setVisible(true);
            }
        });
    }
    public void createHomePage(){

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        add(welcome, BorderLayout.CENTER);
        add(button_access);
        add(button_leave_message);

        setSize(800, 800);
        setLayout(null);

        //frame.pack();

        setVisible(true);

    }

}
