package org.app;

public class Master extends User {
    private String address;
    private String specialization;

    public Master(String xeroxPlaceName, String fullName, String phone, String address, String specialization) {
        super(fullName, phone);
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
}
