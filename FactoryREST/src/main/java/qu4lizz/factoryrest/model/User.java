package qu4lizz.factoryrest.model;

public class User {
    private String username;
    private String password;
    private String companyName;
    private String address;
    private String phone;
    private boolean blocked = false;
    private boolean activated = false;
    public User() { }

    public User(String username, String password, String companyName, String address, String phone) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.address = address;
        this.phone = phone;
    }
    public User(String username, String password, String companyName, String address, String phone, boolean blocked) {
        this(username, password, companyName, address, phone);
        this.blocked = blocked;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }
}
