import java.net.*;
import java.util.*;
import java.io.*;

public class TCPClient {
    
    public static void main(String[] args) {
        int port = 8001;
        String address = "127.0.0.1";

        try {
            Socket client = new Socket(address, port);

            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            output.flush();

            // Initialize string that will be used to store sent/received messages
            String msg = "";
            // Receives and prints the welcome message (Always the first message)
            msg = (String)input.readObject();
            System.out.println("Chatbot Says > " + msg);


            // To get user input from keyboard
            Scanner keyboard = new Scanner(System.in);

            while(true){
                // Here, it's kind of an stop-and-wait. The client sends the message, and waits for a response.
                // Then it prints the response and the cycle continues. For having bidirectional send/receive, threads would be needed.
                msg = keyboard.nextLine();

                // Just before sending we start a timer for computing the RTT
                long pktSentTime = System.nanoTime();
                output.writeObject(msg);
                output.flush();
                System.out.println("Me > " + msg);
                
                msg = (String)input.readObject();
                // And just as the packet is received, we calculate the RTT
                System.out.println("RTT: " + ((System.nanoTime()-pktSentTime)/1000) + "μs");
                System.out.println("Chatbot Says > " + msg);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}