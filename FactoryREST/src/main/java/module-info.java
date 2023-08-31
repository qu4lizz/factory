module qu4lizz.factoryrest {
    requires redis.clients.jedis;
    requires com.google.gson;
    requires jakarta.ws.rs;
    requires jakarta.servlet;
    requires java.logging;

    exports qu4lizz.factoryrest;
    exports qu4lizz.factoryrest.service;
    exports qu4lizz.factoryrest.model;
    exports qu4lizz.factoryrest.exceptions;

}