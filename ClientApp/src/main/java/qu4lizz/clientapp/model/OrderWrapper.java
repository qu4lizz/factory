package qu4lizz.clientapp.model;

import jakarta.xml.bind.annotation.XmlAttribute;
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
    private List<Candy> items;
    private LocalDateTime orderDate;

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

    @XmlElement(name="email")
    public String getEmail() {
        return email;
    }

    @XmlElement(name="companyName")
    public String getCompanyName() {
        return companyName;
    }

    @XmlElement(name="date")
    public String getOrderDate() {
        return orderDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
