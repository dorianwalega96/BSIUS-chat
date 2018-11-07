package GUI;

import CipherService.MatrixCipher;
import CipherService.VigenereCipher;
import CipherService.XorCipher;
import Messages.JsonMessage;
import Networking.SocketService;
import org.json.JSONArray;

import javax.swing.*;
import java.util.ArrayList;

import static Messages.MessageType.FIND;
import static Messages.MessageType.LIST;
import static Messages.MessageType.TEXT;
import static Networking.SocketService.getUsersList;
import static Networking.SocketService.getUsersListRequest;
import static Networking.SocketService.sendMessage;

public class ChatWindow extends JFrame {

    private DefaultListModel<String> users = new DefaultListModel<>();
    private DefaultListModel<String> foundUsers = new DefaultListModel<>();

    private ArrayList<JsonMessage> messages = new ArrayList<>();

    private static String login;
    private String recip;

    private SocketService socketService;

    private JTextArea textArea1;
    private JButton button1;
    private JTextField textField1;
    private JList list1;
    private JList list2;
    private JTextField textField2;
    private JPanel rootPanel;
    private JButton button2;
    private JButton button3;

    public ChatWindow (String login){
        super("SUPERCZAT9000 - " + login);

        this.login = login;
        socketService = SocketService.getInstance();
        list1.setModel(users);
        list2.setModel(foundUsers);

        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        add(rootPanel);
        textArea1.setLineWrap(true);

        button1.setText("Send");
        button2.setText("Search");
        button3.setText("Refresh");

        JsonMessage usersMessage = getUsersList(new JsonMessage(LIST, login, "true"));
        JSONArray jsonArray = new JSONArray(usersMessage.getP1());

        for (int i=0;i<jsonArray.length();i++){
            users.addElement(jsonArray.getString(i));
        }

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.addListSelectionListener(e -> {
            if(list1.getSelectedIndex() >= 0) {
                textArea1.setText("");
                recip = users.get(list1.getSelectedIndex());
                messages.stream()
                        .filter(m -> m.getP1().equals(recip) || m.getP2().equals(recip))
                        .forEach(m -> textArea1.append(m.getP1() + ": " + m.getP3() + "\n"));
            }
        });

        button1.addActionListener(e -> {
            String text = textField1.getText();
            text = VigenereCipher.Encrypt(login, recip, text);
            text = XorCipher.Encrypt(login, recip, text);
            text = MatrixCipher.Encrypt(text);
            JsonMessage message = new JsonMessage(TEXT, login, recip, text);
            sendMessage(message);
            messages.add(message);
            textArea1.append(login + ": " + textField1.getText() + "\n");
            textField1.setText("");
        });

        button3.addActionListener(e ->
                getUsersListRequest(new JsonMessage(LIST, login, "true")));

        button2.addActionListener(e ->
                getUsersListRequest(new JsonMessage(FIND, login, textField2.getText(), "true")));

        socketService.setListener(this);
    }

    public void messageReceived(String message) {
        JsonMessage messageReceived = new JsonMessage(message);
        switch(messageReceived.getMsgType()){
            case TEXT:
                if(!messageReceived.getP2().equals("Succeeded.")) {
                    String sender = messageReceived.getP1();
                    if (sender.equals(recip)) {
                        String text = messageReceived.getP3();
                        text = MatrixCipher.Decrypt(text);
                        text = XorCipher.Decrypt(login, recip, text);
                        text = VigenereCipher.Decrypt(login, recip, text);
                        textArea1.append(messageReceived.getP1() + ": " + text + "\n");
                        messageReceived.setP3(text);
                        messages.add(messageReceived);
                    } else {
                        String text = messageReceived.getP3();
                        text = MatrixCipher.Decrypt(text);
                        text = XorCipher.Decrypt(messageReceived.getP2(), messageReceived.getP1(), text);
                        text = VigenereCipher.Decrypt(messageReceived.getP2(), messageReceived.getP1(), text);
                        messageReceived.setP3(text);
                        messages.add(messageReceived);
                    }
                }
                break;
            case LIST:
                JSONArray jsonArray = new JSONArray(messageReceived.getP1());
                System.out.println(jsonArray.toString());
                users.clear();
                for (int i=0;i<jsonArray.length();i++){
                    users.addElement(jsonArray.getString(i));
                }
                break;
            case FIND:
                JSONArray jsonFoundArray = new JSONArray(messageReceived.getP1());
                System.out.println(jsonFoundArray.toString());
                foundUsers.clear();
                for (int i=0;i<jsonFoundArray.length();i++){
                    foundUsers.addElement(jsonFoundArray.getString(i));
                }
                break;
            default:
                System.out.println(messageReceived.toString());

        }
    }
}
