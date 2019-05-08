import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UDPServer{
    public static void main(String[] args) throws SocketException {
        String filename = "botAnswersFaker.txt";
        List<String> botResponses = new ArrayList<String>();

        try {
            botResponses = Files.readAllLines(Paths.get(filename));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        DatagramSocket serverSock = new DatagramSocket(8001);
        System.out.println("Server listening on port 8001!");

        byte[] recvMsg = new byte[1024];
        byte[] sendMsg;
        String responseMsg;

        while(true){
            try {
                // Receive client packet (at the first iteration, it's the first client message)
                DatagramPacket pkt_to_receive = new DatagramPacket(recvMsg, recvMsg.length);
                serverSock.receive(pkt_to_receive);

                // Evaluate which message from the file will be sent back to the client
                String recvMsgStr = new String(pkt_to_receive.getData());
                responseMsg = getResponse(botResponses, recvMsgStr);

                // Send response back to client, using the IP and PORT info from the received packet.
                sendMsg = responseMsg.getBytes();
                DatagramPacket pkt_to_send = new DatagramPacket(sendMsg, sendMsg.length, pkt_to_receive.getAddress(), pkt_to_receive.getPort());
                serverSock.send(pkt_to_send);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
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

                if(word.length() > 3 && res.indexOf(word) != -1){
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
            botResponse = responseList.get( randomSelector.nextInt( responseList.size() ) );
        }
        else{
            botResponse = possibleResponses.get( randomSelector.nextInt( possibleResponses.size() ) );
        }

        return botResponse;
    }
}