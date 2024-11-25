package org.app;

public abstract class User {
    private static String xeroxPlaceName;
    private String fullName;
    private String phone;

    public User(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public static String getXeroxPlaceName() {
        return xeroxPlaceName;
    }

    public static void setXeroxPlaceName(String xeroxPlaceName) {
        User.xeroxPlaceName = xeroxPlaceName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
