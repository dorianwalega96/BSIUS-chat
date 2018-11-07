package GUI;

import Messages.JsonMessage;
import Messages.MessageType;
import Networking.SocketService;

import javax.swing.*;
import java.awt.*;

import static Messages.MessageType.LOGIN;
import static Messages.MessageType.REG;
import static Networking.SocketService.login;
import static Networking.SocketService.register;

public class MainWindow extends JFrame {

    private JPanel loginPanel = new JPanel();
    private JPanel registerPanel = new JPanel();

    private TextField username = new TextField();
    private TextField password = new TextField();
    private JButton loginButton = new JButton("Login");

    private TextField newUsername = new TextField();
    private TextField newPassword = new TextField();
    private TextField newPasswordRepeat = new TextField();
    private JButton registerButton = new JButton("Register");

    private JLabel infolabel = new JLabel("");

    private String loginString;

    private SocketService socketService;

    public MainWindow(){
        super("CZAT9000");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setVisible(true);
        setLayout(new FlowLayout());

        add(loginPanel);
        add(registerPanel);

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
            JsonMessage message = login(new JsonMessage(LOGIN, username.getText(), password.getText()));
            if (message != null && message.getMsgType().equals(LOGIN)) {
                if (message.getP1().equals("true")) {
                    new ChatWindow(loginString);
                    dispose();
                } else {
                    infolabel.setText("Wrong username or password");
                }
            }
        });

        registerButton.addActionListener(e ->
                register(new JsonMessage(REG, newUsername.getText(), newPassword.getText(), newPasswordRepeat.getText())));

    }

}
