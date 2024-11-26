package org.app;

import java.util.UUID;

public class Service {
    private UUID serviceId;
    private String name;
    private String type;
    private double price;
    private OrderTime orderTime;
    private Master master;
    private Client client;

    public Service(String name, String type, double price, OrderTime orderTime, Master master, Client client) {
        this.serviceId = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.price = price;
        this.orderTime = orderTime;
        this.master = master;
        this.client = client;
    }

    // Getters
    public UUID getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public OrderTime getOrderTime() {
        return orderTime;
    }

    public Master getMaster() {
        return master;
    }

    public Client getClient() {
        return client;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrderTime(OrderTime orderTime) {
        this.orderTime = orderTime;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return name;
    }
}
