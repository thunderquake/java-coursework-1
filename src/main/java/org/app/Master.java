package org.app;

import java.util.ArrayList;
import java.util.List;

public class Master extends User {
    private List<Service> linkedServices;
    private String address;
    private String specialization;

    public Master(String fullName, String phone, String address, String specialization) {
        super(fullName, phone);
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
