package org.app;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Master extends User {
    private UUID masterId;
    private List<Service> linkedServices;
    private String address;
    private String specialization;

    public Master(String fullName, String phone, String address, String specialization) {
        super(fullName, phone);
        this.masterId = UUID.randomUUID();
        this.linkedServices = new ArrayList<>();
        this.address = address;
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public UUID getMasterId() {
        return masterId;
    }

    public List<Service> getLinkedServices() {
        return linkedServices;
    }

    public void addService(Service service) {
        if (!linkedServices.contains(service)) {
            linkedServices.add(service);
        }
    }

    public void removeService(Service service) {
        linkedServices.remove(service);
    }

    @Override
    public String toString() {
        return getFullName() + " (" + specialization + ")";
    }
}
