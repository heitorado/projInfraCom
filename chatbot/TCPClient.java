package chatbot;
import java.net.*;
import java.util.*;
import java.io.*;

/*
    This is a simple implementation of a chat via TCP. The client connects with the server (TCPServer.java)
    that is a chatbot with 13 quotes from some movies or TV series. Inputting the correct sentence will give
    you back a quote, while any other stuff will give you back a default sentence.

    The accepted sentences are (without the surrounding ""):

    "Tell me a quote from Back To The Future"
    "Tell me a quote from Ghostbusters"
    "Tell me a quote from Harry Potter"
    "Tell me a quote from The Hitchhiker's Guide To The Galaxy"
    "Tell me a quote from Star Wars"
    "Tell me a quote from V For Vendetta"
    "Tell me a quote from BoJack Horseman"
    "Tell me a quote from Friends"
    "Tell me a quote from How I Met Your Mother"
    "Tell me a quote from Rick And Morty"
    "Tell me a quote from Simpsons"
    "Tell me a quote from South Park"
    "Tell me a quote from The Fresh Prince Of Bel Air"
*/

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
                // And just as the packet is received, we calculate the RTT, print the message from the server and then the RTT calculated
                long rtt = (System.nanoTime()-pktSentTime)/1000;
                System.out.println("Chatbot Says > " + msg);
                System.out.println("RTT: " + rtt + "μs");

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}