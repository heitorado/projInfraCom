import java.net.*;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
public class UDPServer{
    public static void main(String[] args) throws SocketException {
        String filename = "botAnswers.txt";
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