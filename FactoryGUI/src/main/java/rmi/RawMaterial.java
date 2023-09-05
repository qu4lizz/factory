package rmi;

import java.io.Serializable;

public class RawMaterial implements Serializable {
    private int id;
    private String name;
    private double quantity;
    private double price;
    private static int nextId = 1;
    public RawMaterial() { }

    public RawMaterial(String name, double quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        id = nextId++;
    }

    public RawMaterial(int id, String name, double quantity, double price) {
        this(name, quantity, price);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RawMaterial) {
            RawMaterial item = (RawMaterial) obj;
            return item.getId() == this.id;
        }
        return false;
    }
}