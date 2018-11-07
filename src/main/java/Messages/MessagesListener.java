package Messages;

import GUI.ChatWindow;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessagesListener implements Runnable {

    private ObjectInputStream inputStream;
    private ChatWindow viewCallback;

    public MessagesListener(ObjectInputStream input, ChatWindow viewCallback) {
        this.inputStream = input;
        this.viewCallback = viewCallback;
    }

    @Override
    public synchronized void run() {
        while(true) {
            try {
                String message = (String) inputStream.readObject();
                if(message != null) {
                    viewCallback.messageReceived(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
