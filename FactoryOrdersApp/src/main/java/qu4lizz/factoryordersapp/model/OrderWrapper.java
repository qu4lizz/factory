package qu4lizz.factoryordersapp.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "order")
public class OrderWrapper {
    private String email;
    private String companyName;
    private List<Candy> items = new ArrayList<>();
    private LocalDateTime orderDate = LocalDateTime.now();

    public OrderWrapper() { }

    public OrderWrapper(List<Candy> items, String email, String companyName) {
        this.items = items;
        this.companyName = companyName;
        this.email = email;
        this.orderDate = LocalDateTime.now();
    }

    @XmlElementWrapper(name = "items")
    @XmlElement(name="item")
    public List<Candy> getItems() {
        return items;
    }

    public void setItems(List<Candy> items) {
        this.items = items;
    }

    @XmlElement(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name="companyName")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @XmlElement(name="date")
    public String getOrderDate() {
        return orderDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateLocalized() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        return orderDate.format(formatter);
    }
}