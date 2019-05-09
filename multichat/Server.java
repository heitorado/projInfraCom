package multichat;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

public class Server {

    public static void addUserToServer(ArrayList<User> clients_list, User c) {
        clients_list.add(c);
        return;
    }

    public static void broadcast(DatagramSocket sock, DatagramPacket pkt, ArrayList<User> clients_list) {
        try {
            String msgStr = new String(pkt.getData(), pkt.getOffset(), pkt.getLength(), "UTF-8");
            int client_id = clients_list.indexOf(new User(pkt.getAddress(), pkt.getPort())); 
            msgStr = Integer.toString(client_id) + " > " + msgStr;
            byte[] msg = msgStr.getBytes();;
            
            for (User c : clients_list) {
                try {
                    if(c.getUserIP() != pkt.getAddress() && c.getUserPort() != pkt.getPort()){
                        DatagramPacket broadcast_packet = new DatagramPacket(msg, msg.length, c.getUserIP(), c.getUserPort());
                        sock.send(broadcast_packet);
                    }
                } catch (Exception e) {
                    System.out.println("ERROR BROADCASTING: " + e);   
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR BROADCASTING: " + e);   
        }
    }
        
    public static void main(String[] args) throws SocketException {

        DatagramSocket serverSock = new DatagramSocket(8001);
        System.out.println("Server listening on port 8001!");

        ArrayList<User> clientsConnected = new ArrayList<User>();
        
        byte[] recvMsg = new byte[1024];

        while(true){
            try {
                // Receive client packet
                DatagramPacket received_packet = new DatagramPacket(recvMsg, recvMsg.length);
                serverSock.receive(received_packet);

                // Evaluate if the client has just entered the chat room or if it's a message from him
                // If it has just entered, register it on the current users list.
                // If it has sent a message, broadcast it.
                String recvStringMsg = new String(received_packet.getData(), received_packet.getOffset(), received_packet.getLength(), "UTF-8");
                if(recvStringMsg.equals("HELO")){
                    User cli = new User(received_packet.getAddress(), received_packet.getPort());
                    addUserToServer(clientsConnected, cli);
                }
                else{
                    // Broadcast the packet
                    broadcast(serverSock, received_packet, clientsConnected);
                }


            } catch (Exception e) {
                System.out.println("ERROR: " + e);   
            }
        }   
    }
}

class User {
    InetAddress user_ip;
    int user_port;

    public User(InetAddress ip, int port) {
        user_ip = ip;
        user_port = port;
    }

    public InetAddress getUserIP(){
        return this.user_ip;
    }

    public int getUserPort(){
        return this.user_port;
    }

    @Override
    public boolean equals(Object o) { 
  
        if (o == this) { 
            return true; 
        } 
  
        if (!(o instanceof User)) { 
            return false; 
        } 
          
        User other = (User) o; 
          
        return user_ip.equals(other.user_ip) && user_port == other.user_port; 
    } 

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.user_ip.hashCode();
        hash = 53 * hash + this.user_port;
        return hash;
    }


}