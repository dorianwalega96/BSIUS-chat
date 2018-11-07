package GUI;

import Messages.JsonMessage;
import Messages.MessageType;
import Networking.SocketService;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

import static Messages.MessageType.LOGIN;

public class MainWindow extends JFrame {

    JPanel loginPanel = new JPanel();
    JPanel registerPanel = new JPanel();

    TextField username = new TextField();
    TextField password = new TextField();
    JButton loginButton = new JButton("Login");

    TextField newUsername = new TextField();
    TextField newPassword = new TextField();
    TextField newPasswordRepeat = new TextField();
    JButton registerButton = new JButton("Register");

    JLabel infolabel = new JLabel("");

    private String loginString;

    private SocketService socketService;



    public MainWindow(){
        super("CZAT9000");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setVisible(true);
        setLayout(new FlowLayout());

        add(loginPanel);
        add(registerPanel);

        //setupComponents();

        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.PAGE_AXIS));

        loginPanel.add(username);
        loginPanel.add(password);
        loginPanel.add(loginButton);

        registerPanel.add(newUsername);
        registerPanel.add(newPassword);
        registerPanel.add(newPasswordRepeat);
        registerPanel.add(registerButton);

        loginPanel.add(infolabel);


        socketService = SocketService.getInstance();

        loginButton.addActionListener(e -> {
            loginString = username.getText();
            JsonMessage message = SocketService.login(new JsonMessage(LOGIN, username.getText(), password.getText()));
            if (message != null && message.getMsgType().equals(LOGIN)) {
                System.out.println(message.toString());
                if (message.getP1().equals("true")) {
                    new ChatWindow(loginString);
                    dispose();
                } else {
                    infolabel.setText("Wrong username or password");
                }
            }
        });


        registerButton.addActionListener(e -> {
            socketService.register(new JsonMessage(MessageType.REG, newUsername.getText(), newPassword.getText(), newPasswordRepeat.getText()));
        });


    }

    private void setupComponents(){
        username.setSize(150, 25);
        password.setSize(150, 25);
        loginButton.setSize(150, 25);


        newUsername.setSize(150, 25);
        newPassword.setSize(150, 25);
        newPasswordRepeat.setSize(150, 25);
        registerButton.setSize(150, 25);
    }

}
