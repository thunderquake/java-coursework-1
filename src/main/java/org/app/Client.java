package org.app;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {
    private List<Service> assignedServices;

    public Client(String fullName, String phone) {
        super(fullName, phone);
        this.assignedServices = new ArrayList<>();
    }

    public List<Service> getAssignedServices() {
        return assignedServices;
    }

    public void addService(Service service) {
        assignedServices.add(service);
    }

    public void removeService(Service service) {
        assignedServices.remove(service);
    }
}
