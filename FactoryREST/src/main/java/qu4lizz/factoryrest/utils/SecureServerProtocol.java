package qu4lizz.factoryrest.utils;

public enum SecureServerProtocol {
    // requests
    LOGIN("LOGIN"),
    ORDER("ORDER"),
    END("END"),
    // responses
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),

    // utils
    SEPARATOR("###");

    private String value;
    SecureServerProtocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
