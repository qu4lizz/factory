package qu4lizz.factoryapp.service;

import qu4lizz.factoryapp.utils.ConfigUtil;
import qu4lizz.factoryapp.utils.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.logging.Level;

public class PromotionService {
    private final String host;
    private final int port;
    private MulticastSocket socket;

    public PromotionService() {
        Properties properties = ConfigUtil.getProperties();
        host = properties.getProperty("multicast_ip");
        port = Integer.parseInt(properties.getProperty("multicast_port"));

        try {
            socket = new MulticastSocket();
            InetAddress address = InetAddress.getByName(host);
            System.out.println("Multicast running on " + host + ":" + port);
            socket.joinGroup(address);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }

    }

    public void sendPromotion(String message) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(host), port);
        socket.send(packet);
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.leaveGroup(InetAddress.getByName(host));
            socket.close();
        }
    }
}