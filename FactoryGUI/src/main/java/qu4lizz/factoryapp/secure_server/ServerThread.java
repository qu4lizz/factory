package qu4lizz.factoryapp.secure_server;

import qu4lizz.factoryapp.service.FactoryUserService;
import qu4lizz.factoryapp.utils.ConfigUtil;
import qu4lizz.factoryapp.utils.DbUtil;
import qu4lizz.factoryapp.utils.Logger;
import qu4lizz.factoryapp.utils.SecureServerProtocol;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

public class ServerThread extends Thread {
    private final FactoryUserService factoryUserService = new FactoryUserService();
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private String username;
    private String password;

    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String request;
            do {
                request = in.readLine();

                if (request.startsWith(SecureServerProtocol.LOGIN.getValue())) {
                    boolean success = processLogin(request);
                    out.println(success ? SecureServerProtocol.SUCCESS.getValue() : SecureServerProtocol.FAIL.getValue());
                }
                else if (request.startsWith(SecureServerProtocol.ORDER.getValue())) {
                    boolean success = processOrder(request);
                    out.println(success ? SecureServerProtocol.SUCCESS.getValue() : SecureServerProtocol.FAIL.getValue());
                }
            } while (!SecureServerProtocol.END.getValue().equals(request));
        } catch (IOException e) {
            Logger.logger.log(Level.SEVERE, e.getMessage());
        }
    }

    private boolean processLogin(String request) {
        String info = request.split(SecureServerProtocol.SEPARATOR.getValue())[1];

        return factoryUserService.checkLogin(info);
    }

    private boolean processOrder(String request) {
        String info = request.split(SecureServerProtocol.SEPARATOR.getValue())[1];

        info = info.replace(',', '\n');
        String companyName = null;
        String time = null;
        String[] splitted = info.split("\n");

        for (String line : splitted) {
            if (line.contains("Order date")) {
                time = line.split(":", 2)[1];
            }
            else if (line.contains("Company name")) {
                companyName = line.split(":")[1];
                break;
            }
        }

        try {
            String ordersPath = ConfigUtil.getProperties().getProperty("orders_path");
            File file = new File(DbUtil.DATABASE_FOLDER + ordersPath + File.separator + companyName + "_" + time + ".txt");

            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(info);
            fileWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
