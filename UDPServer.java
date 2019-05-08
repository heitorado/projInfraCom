import java.net.*;
import java.util.*;
import java.io.*;

public class UDPServer{
    public static void main(String[] args) throws SocketException {
        DatagramSocket serverSock = new DatagramSocket(8001);
        System.out.println("Server listening on port 8001!");

        byte[] recvMsg = new byte[1024];
        byte[] sendMsg;

        while(true){
            try {
                // Receive client packet (at the first iteration, it's the first client message)
                DatagramPacket pkt_to_receive = new DatagramPacket(recvMsg, recvMsg.length);
                serverSock.receive(pkt_to_receive);

                // Send response back to client, using the info from the received packet.
                sendMsg = "oi".getBytes();
                DatagramPacket pkt_to_send = new DatagramPacket(sendMsg, sendMsg.length, pkt_to_receive.getAddress(), pkt_to_receive.getPort());
                serverSock.send(pkt_to_send);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
    }
}