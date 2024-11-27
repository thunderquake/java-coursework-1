package org.app;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client extends User {
    private UUID clientId;
    private List<Service> linkedServices;

    public Client(String fullName, String phone) {
        super(fullName, phone);
        this.clientId = UUID.randomUUID();
        this.linkedServices = new ArrayList<>();
    }

    public UUID getClientId() {
        return clientId;
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
        return getFullName();
    }
}
