package org.app;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FlatLightLaf.setup();

        List<Service> servicesList = new ArrayList<>();
        servicesList.add(new Service("A", "Xerox", 200.0, new OrderTime("09:00", "11:00", "2024-10-18")));
        servicesList.add(new Service("B", "Printing", 150.0, new OrderTime("10:00", "11:30", "2024-10-18")));

        SwingUtilities.invokeLater(() -> {
            ServiceUI serviceUI = new ServiceUI(servicesList);
            serviceUI.setVisible(true);
        });
    }
}
