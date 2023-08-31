package qu4lizz.factoryrest.model;

public class FactoryUser {
    private String username;

    public FactoryUser() { }
    public FactoryUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
