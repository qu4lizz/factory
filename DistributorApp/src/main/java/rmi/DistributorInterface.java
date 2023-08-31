package rmi;

import java.rmi.*;
import java.util.List;

public interface DistributorInterface extends Remote {
    List<RawMaterial> getRawMaterials() throws RemoteException;

    boolean buyRawMaterial(int id, double quantity) throws RemoteException;
}
