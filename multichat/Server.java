package multichat;
import java.net.*;
import java.util.*;

public class Server {

    public static void addUserToServer(Set<User> clients_list, User c){
        clients_list.add(c);
        return;
    }

    public static void broadcast(DatagramSocket sock, DatagramPacket pkt, Set<User> clients_list){
        byte[] msg = pkt.getData();
        int i;

        for (User c : clients_list) {
            try {
                //if(c.getUserIP() != pkt.getAddress() && c.getUserPort() != pkt.getPort()){
                    DatagramPacket broadcast_packet = new DatagramPacket(msg, msg.length, c.getUserIP(), c.getUserPort());
                    sock.send(broadcast_packet);
                //}
            } catch (Exception e) {
                System.out.println("ERROR BROADCASTING: " + e);   
            }
        }
    }

    public static void main(String[] args) throws SocketException {

        DatagramSocket serverSock = new DatagramSocket(8001);
        System.out.println("Server listening on port 8001!");

        Set<User> clientsConnected = new HashSet<User>();
        
        byte[] recvMsg = new byte[1024];
        String responseMsg;

        while(true){
            try {
                // Receive client packet
                DatagramPacket received_packet = new DatagramPacket(recvMsg, recvMsg.length);
                serverSock.receive(received_packet);
                User cli = new User(received_packet.getAddress(), received_packet.getPort());
                addUserToServer(clientsConnected, cli);

                // Broadcast the packet
                broadcast(serverSock, received_packet, clientsConnected);

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!User.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final User other = (User) obj;
        if ( this.user_ip != other.user_ip ) {
            return false;
        }

        if (this.user_port != other.user_port) {
            return false;
        }

        return true;
    }
}