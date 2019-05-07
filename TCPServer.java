import java.net.*;
import java.util.*;
import java.io.*;

public class TCPServer {
    public static void main(String[] args) {

        int port = 8001;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            while(true){
                Socket client = server.accept();
                System.out.println("A client has connected: " + client.getInetAddress().getHostAddress());
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.flush();
                out.writeObject("Hi I am a chatbot running on TCP. Talk to me. Or don't. Whatever.");
                out.close();
                client.close();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}