package org.app;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.util.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Setup the UI theme
        FlatLightLaf.setup();

        // Creating dummy data for Masters
        List<Master> mastersList = new ArrayList<>();
        mastersList.add(new Master( "Master One", "Skill One"));
        mastersList.add(new Master("Master Two", "Skill Two"));

        // Creating dummy data for Clients
        List<Client> clientsList = new ArrayList<>();
        clientsList.add(new Client("Client One", "1234567890"));
        clientsList.add(new Client("Client Two", "0987654321"));

        // Creating dummy data for Services
        List<Service> servicesList = new ArrayList<>();
        servicesList.add(new Service( "A", "Xerox", 200.0, new OrderTime("09:00", "11:00", "2024-10-18"), mastersList.get(0), clientsList.get(0)));
        servicesList.add(new Service( "B", "Printing", 150.0, new OrderTime("10:00", "11:30", "2024-10-18"), mastersList.get(1), clientsList.get(1)));

        // Launching the UI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ServiceUI serviceUI = new ServiceUI(servicesList, mastersList, clientsList);
            serviceUI.setVisible(true);
        });
    }
}

