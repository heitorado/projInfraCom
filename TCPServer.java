import java.net.*;
import java.util.*;
import java.io.*;

public class TCPServer {
    public static void main(String[] args) {

        int port = 8001;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            Socket client = server.accept();
            System.out.println("A client has connected: " + client.getInetAddress().getHostAddress());
            
            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            output.flush();

            // Sends the welcome message first
            output.writeObject("Hi I am a chatbot running on TCP. Talk to me. Or don't. Whatever.");
            
            // Initializes the string for storing sent and received messages
            String msg = "";
            
            while(true){
                // While here this makes sense for a chatbot, it's kind of an stop-and-wait. The server sends the message, and waits for the next one from the client to send another.
                // Then it prints the response and the cycle continues. For having bidirectional send/receive, threads would be needed.
                msg = (String) input.readObject();
                System.out.println("Client says - " + msg);

                output.writeObject(msg);
                output.flush();
                System.out.println("Server Response - " + msg);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}