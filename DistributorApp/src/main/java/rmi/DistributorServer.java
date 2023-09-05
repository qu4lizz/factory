package rmi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DistributorServer implements DistributorInterface {
    private ObservableList<RawMaterial> rawMaterials;
    private final Object lock = new Object();

    public DistributorServer() {
        rawMaterials = FXCollections.observableArrayList();
    }

    public ObservableList<RawMaterial> getList() {
        return rawMaterials;
    }

    public void addMaterial(RawMaterial mat) {
        rawMaterials.add(mat);
    }

    public void removeMaterial(RawMaterial mat) {
        rawMaterials.remove(mat);
    }

    @Override
    public List<RawMaterial> getRawMaterials() throws RemoteException {
        return rawMaterials.stream().toList();
    }

    @Override
    public boolean buyRawMaterial(int id, double quantity) throws RemoteException {
        RawMaterial target = rawMaterials.stream().filter(x -> x.getId() == id).findFirst().orElse(null);

        if (target == null || target.getQuantity() < quantity)
            return false;

        target.setQuantity(target.getQuantity() - quantity);

        return true;
    }
}
