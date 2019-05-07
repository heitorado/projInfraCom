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
            output.writeObject("Hi I am a chatbot running on TCP. Talk to me. Or don't. Whatever.");
            
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
        
        String[] wordArray = clientMessage.split(" ");
        List<String> wordsList = Arrays.asList(wordArray);        

        List<String> possibleResponses = new ArrayList<String>();

        String botResponse = "";
        int responseScore = 0;

        for(String res : responseList) {
            for(String word : wordsList) {
                if(res.indexOf(word) != -1){
                    responseScore++;
                }
            }

            if(responseScore > 0){
                possibleResponses.add(res);
                responseScore = 0;
            }
        }

        Random randomSelector = new Random();

        if(possibleResponses.isEmpty()){
            botResponse = responseList.get( randomSelector.nextInt(responseList.size()) );
        }
        else{
            botResponse = possibleResponses.get( randomSelector.nextInt(possibleResponses.size()) );
        }

        return botResponse;
    }
}