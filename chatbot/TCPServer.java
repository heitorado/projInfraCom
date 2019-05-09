import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TCPServer {
    public static void main(String[] args) {

        String filename = "botAnswers.txt";
        List<String> botResponses = new ArrayList<String>();

        try {
            botResponses = Files.readAllLines(Paths.get(filename));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

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
            output.writeObject("Hi I am a chatbot running on TCP. Ask me for quotes in the format \"Tell me a quote from {series/movie name}\"");
            
            // Initializes the string for storing sent and received messages
            String msg = "";
            
            while(true){
                // While here this makes sense for a chatbot, it's kind of an stop-and-wait. The server sends the message, and waits for the next one from the client to send another.
                // Then it prints the response and the cycle continues. For having bidirectional send/receive, threads would be needed.
                msg = (String) input.readObject();
                System.out.println("Client says - " + msg);

                msg = getResponse(botResponses, msg);

                output.writeObject( msg );
                output.flush();
                System.out.println("Server Response - " + msg);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    static String getResponse(List<String> responseList, String clientMessage){

        int id = responseList.size()-1;

        if(clientMessage.equals("Tell me a quote from Back To The Future") ){
            id = 0;
        }
        else if(clientMessage.equals("Tell me a quote from Ghostbusters")){
            id = 1;
        }
        else if(clientMessage.equals("Tell me a quote from Harry Potter")){
            id = 2;
        }
        else if(clientMessage.equals("Tell me a quote from The Hitchhiker's Guide To The Galaxy")){
            id = 3;
        }
        else if(clientMessage.equals("Tell me a quote from Star Wars")){
            id = 4;
        }
        else if(clientMessage.equals("Tell me a quote from V For Vendetta")){
            id = 5;
        }
        else if(clientMessage.equals("Tell me a quote from BoJack Horseman")){
            id = 6;
        }
        else if(clientMessage.equals("Tell me a quote from Friends")){
            id = 7;
        }
        else if(clientMessage.equals("Tell me a quote from How I Met Your Mother")){
            id = 8;
        }
        else if(clientMessage.equals("Tell me a quote from Rick And Morty")){
            id = 9;
        }
        else if(clientMessage.equals("Tell me a quote from Simpsons")){
            id = 10;
        }
        else if(clientMessage.equals("Tell me a quote from South Park")){
            id = 11;
        }
        else if(clientMessage.equals("Tell me a quote from The Fresh Prince Of Bel Air")){
            id = 12;
        }

        return responseList.get(id);
    }
}