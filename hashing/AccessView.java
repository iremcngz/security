import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AccessView extends JFrame {

    JTextField[] fields = new JTextField[4]; //all fields
    JPasswordField[] pass = new JPasswordField[2]; //only password fields
    JButton button_view;
    JButton button_reset;
    JButton button_home;
    JCheckBox checkbox;
    String[] labels = {"Message Codename","Message Password", "Username", "User Password"};

    public AccessView() throws IOException {
        int y=50;
        int num=0;
        int num2=0;



        for (String label : labels) {
            JLabel l = new JLabel(label);
            l.setBounds(30, y, 250, 100);
            l.setFont(new Font("Serif", Font.BOLD, 30));
            add(l);

            if(num%2!=0){
                JPasswordField t = new JPasswordField();

                t.setBounds(350, y, 350, 100);
                t.setFont(new Font("Serif", Font.BOLD, 30));
                fields[num]=t;
                pass[num2]=t;
                add(t);
                num2+=1;
            }
            else{
                JTextField t = new JTextField();

                t.setBounds(350, y, 350, 100);
                t.setFont(new Font("Serif", Font.BOLD, 30));
                fields[num]=t;
                add(t);
            }

            y += 150;
            num+=1;
        }

        setTitle("Access View");
        button_view = new JButton("View");
        button_view.setFont(new Font("Serif",Font.BOLD,30));
        button_view.setBounds(50, 800, 250, 100);
        set_button_view();
        button_reset = new JButton("Reset");
        button_reset.setFont(new Font("Serif",Font.BOLD,30));
        button_reset.setBounds(450, 800, 250, 100);
        set_button_reset();
        button_home = new JButton("Home");
        button_home.setFont(new Font("Serif",Font.BOLD,30));
        button_home.setBounds(250, 950, 250, 100);
        set_button_home();

        checkbox = new JCheckBox("Show Password", false);
        checkbox.setFont(new Font("Serif",Font.BOLD,30));
        checkbox.setBounds(350, 650, 250, 100);
        set_checkbox();

        createAccessView();
    }

    public void set_button_view(){
        button_view.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {

                MessageFileProcess m=new MessageFileProcess();
                try {
                    boolean cond1 = m.MessageCheck(fields[0].getText(),fields[1].getText(),fields[2].getText());
                    boolean cond2 = m.checkUserAndPassword(fields[2].getText(),fields[3].getText(),"user_data.txt");
                    if(cond1 && cond2){
                        String content = m.content;
                        if(content.equals("")){
                            //
                        }
                        else{
                            dispose();
                            MessageView mv = new MessageView(content);
                            mv.setVisible(true);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Wrong credentials!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void set_button_reset(){
        button_reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                for(JTextField j:fields){
                    j.setText("");
                }
                checkbox.setSelected(false);
            }
        });
    }

    public void set_checkbox(){
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(checkbox.isSelected())
                    pass[1].setEchoChar((char)0); //password = JPasswordField
                else
                    pass[1].setEchoChar('*');
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


    public void createAccessView(){

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //add(show_password);
        add(button_view);
        add(button_reset);
        add(button_home);
        add(checkbox);

        setSize(800, 1200);
        setLayout(null);

        //frame.pack();

        setVisible(true);

    }

}
