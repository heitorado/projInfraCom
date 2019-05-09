import java.net.*;
import java.util.*;
import java.io.*;

public class Client implements Runnable {

    public static DatagramSocket clientSock;
        
    public static byte[] recvMsg = new byte[1024];

    @Override
    public void run(){
        while(true){
            try {
                // Receive server packet 
                DatagramPacket pkt_to_receive = new DatagramPacket(recvMsg, recvMsg.length);
                clientSock.receive(pkt_to_receive);
                // And just as the packet is received, we calculate the RTT
                //System.out.println("RTT: " + ((System.nanoTime()-pktSentTime)/1000) + "μs");
                String recvMsgStr = new String(pkt_to_receive.getData());
                System.out.println(recvMsgStr);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        clientSock = new DatagramSocket();
        InetAddress connection_ip = InetAddress.getByName("localhost");

        new Thread(new Client()).start();
        System.out.println("Client started. Type anything and press enter to chat!");

        // To get user input from keyboard
        Scanner keyboard = new Scanner(System.in);
        String userSentence;

        byte[] sendMsg;
        

        while(true){
            try {
                // Read client message from keyboard and send packet to server (it is awaiting the first message from the client)
                userSentence = keyboard.nextLine();
                sendMsg = userSentence.getBytes();
                DatagramPacket pkt_to_send = new DatagramPacket(sendMsg, sendMsg.length, connection_ip, 8001);
                // Just before sending we start a timer for computing the RTT
                long pktSentTime = System.nanoTime();
                clientSock.send(pkt_to_send);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
    }
}