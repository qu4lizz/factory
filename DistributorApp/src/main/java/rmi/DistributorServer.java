package rmi;

import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.List;

public class DistributorServer implements DistributorInterface {
    private ObservableList<RawMaterial> rawMaterials;
    private final Object lock = new Object();

    public DistributorServer(ObservableList<RawMaterial> list) {
        rawMaterials = list;
    }

    public void setRawMaterials(ObservableList<RawMaterial> rawMaterials) {
        this.rawMaterials = rawMaterials;
    }

    @Override
    public List<RawMaterial> getRawMaterials() throws RemoteException {
        //synchronized (lock) { //TODO: remove sync and check
        return rawMaterials.stream().toList();
    }

    @Override
    public boolean buyRawMaterial(int id, double quantity) throws RemoteException {
        RawMaterial target = rawMaterials.stream().filter(x -> x.getId() == id).findFirst().orElse(null);

        if (target == null || target.getQuantity() < quantity)
            return false;

        target.setQuantity(target.getQuantity() - quantity);

        return false;
    }
}
