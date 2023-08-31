package rmi;

import qu4lizz.factoryapp.utils.ConfigUtil;
import qu4lizz.factoryapp.utils.DbUtil;
import qu4lizz.factoryapp.utils.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class DistributorService implements DistributorInterface {
    private Registry registry;
    private DistributorInterface stub;
    public DistributorService() {
        try {
            Properties properties = ConfigUtil.getProperties();
            String path = DbUtil.DATABASE_FOLDER + properties.getProperty("policy");
            System.setProperty("java.security.policy", path);
            if (System.getSecurityManager() == null)
                System.setSecurityManager(new SecurityManager());

            int port = Integer.parseInt(properties.getProperty("registry_port"));
            registry = LocateRegistry.createRegistry(port);
            System.out.println("RMI running on port " + port);
        } catch (Exception ex) {
            Logger.logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void connect(String name) throws RemoteException, NotBoundException {
        this.stub = (DistributorInterface)registry.lookup(name);
    }

    public String[] getDistributorList() throws RemoteException {
        return this.registry.list();
    }

    @Override
    public List<RawMaterial> getRawMaterials() throws RemoteException {
        return stub.getRawMaterials();
    }

    @Override
    public boolean buyRawMaterial(int id, double quantity) throws RemoteException {
        return stub.buyRawMaterial(id, quantity);
    }
}
