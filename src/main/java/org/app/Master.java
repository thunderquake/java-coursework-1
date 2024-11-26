package org.app;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Master extends User {
    private UUID masterId;
    private List<Service> linkedServices;

    public Master(String fullName, String phone) {
        super(fullName, phone);
        this.masterId = UUID.randomUUID();
        this.linkedServices = new ArrayList<>();
    }

    // Getters
    public UUID getMasterId() {
        return masterId;
    }

    public List<Service> getLinkedServices() {
        return linkedServices;
    }

    // Link or unlink services
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
        return getFullName();
    }
}
