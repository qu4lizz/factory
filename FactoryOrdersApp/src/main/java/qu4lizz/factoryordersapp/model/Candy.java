package qu4lizz.factoryordersapp.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Candy {
    private int id;
    private String name;
    private double price;
    private double quantity;

    public Candy() { }
    public Candy(int id, String name, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public boolean equals(Object obj) {
        if (obj instanceof Candy) {
            Candy candy = (Candy) obj;
            return candy.getName().equals(this.getName());
        }
        return false;
    }
}
