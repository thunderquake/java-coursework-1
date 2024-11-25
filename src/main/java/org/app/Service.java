package org.app;

import java.util.UUID;

public class Service {
    private UUID serviceId;
    private String name;
    private String type;
    private double price;
    private OrderTime orderTime;

    public Service(String name, String type, double price, OrderTime orderTime) {
        this.serviceId = UUID.randomUUID();
        this.name = name;
        this.type = type;
        this.price = price;
        this.orderTime = orderTime;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(OrderTime orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", orderTime=" + orderTime +
                '}';
    }
}
