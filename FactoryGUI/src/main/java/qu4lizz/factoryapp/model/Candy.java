package qu4lizz.factoryapp.model;

import java.util.HashMap;
import java.util.Map;

public class Candy {
    private static int nextId = 1;
    private int id;
    private String name;
    private double price;
    private double quantity;

    public Candy() { }

    public Candy(String name, double price, double quantity) {
        this();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public Candy(int id, String name, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public Candy(Map<String, String> objectMap) {
        this.id = Integer.parseInt(objectMap.get("id"));
        this.name = objectMap.get("name");
        this.price = Double.parseDouble(objectMap.get("price"));
        this.quantity = Double.parseDouble(objectMap.get("quantity"));
    }

    public static void setNextId(int currentMaxId) {
        nextId = currentMaxId + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Map<String, String> toMap(){
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("id", String.valueOf(id));
        objectMap.put("name", name);
        objectMap.put("price", String.valueOf(price));
        objectMap.put("quantity", String.valueOf(quantity));

        return objectMap;
    }

    @Override
    public String toString() {
        return "Candy{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
