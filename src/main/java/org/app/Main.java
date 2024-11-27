package org.app;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        List<Master> mastersList = new ArrayList<>();
        List<Client> clientsList = new ArrayList<>();
        List<Service> servicesList = new ArrayList<>();

        SwingUtilities.invokeLater(() -> {
            ServiceUI serviceUI = new ServiceUI(servicesList, mastersList, clientsList);
            serviceUI.setVisible(true);
        });
    }
}

