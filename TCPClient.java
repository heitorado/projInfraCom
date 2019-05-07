import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

public class TCPClient {
    
    public static void main(String[] args) {
        int port = 8001;
        String address = "127.0.0.1"

        try {
            Socket client = new Socket(address, port);
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());
            Date current_date = (Date)input.readObject();
            JOptionPane.showMessageDialog(null,"Data received from server:" + current_date.toString());
            input.close();
            System.out.println("Connection closed");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}