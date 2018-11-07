package Networking;

import GUI.ChatWindow;
import Messages.JsonMessage;
import Messages.MessagesListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketService {

    private static SocketService instance;
    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    private ChatWindow listener;


    private static String ip = "192.168.1.104";
    private static int port = 11002;


    public static SocketService getInstance(){
        if(instance != null){
            return instance;
        } else {
            instance = new SocketService();
            return instance;
        }
    }

    private SocketService(){
        initSocket();
    }

    private void initSocket(){
        try {
            socket = new Socket(ip, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonMessage login(JsonMessage message){
        JsonMessage returnMessage = null;
        try {
            outputStream.writeObject(message.toString());
            try {
                String o = (String) inputStream.readObject();
                returnMessage = new JsonMessage(o);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public static void register(JsonMessage message){
        try {
            outputStream.writeObject(message.toString());
            outputStream.flush();
            try {
                Object o = inputStream.readObject();
                System.out.println(o.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonMessage getUsersList(JsonMessage message) {
        JsonMessage response = null;
        try {
            outputStream.writeObject(message.toString());
            outputStream.flush();

            try {
                String o = (String) inputStream.readObject();
                response = new JsonMessage(o);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void getUsersListRequest(JsonMessage message) {
        try {
            outputStream.writeObject(message.toString());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(JsonMessage message){
        try {
            outputStream.writeObject(message.toString());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ChatWindow getListener() {
        return listener;
    }

    public void setListener(ChatWindow listener) {
        this.listener = listener;
        Thread thread = new Thread(new MessagesListener(inputStream, listener));
        thread.start();
    }
}
