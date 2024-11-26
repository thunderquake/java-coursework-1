package org.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServiceUI extends JFrame {
    private JTabbedPane tabbedPane;
    private List<Service> services;
    private List<Master> masters;
    private List<Client> clients;

    private JList<String> serviceList, masterList, clientList;
    private DefaultListModel<String> serviceModel, masterModel, clientModel;

    // Detail Panel to show the selected item's data
    private JTextArea detailTextArea;

    public ServiceUI(List<Service> services, List<Master> masters, List<Client> clients) {
        this.services = services;
        this.masters = masters;
        this.clients = clients;

        setTitle("Service Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // Service tab
        JPanel servicePanel = createTab("Services", services, serviceModel = new DefaultListModel<>(), serviceList = new JList<>(serviceModel));
        tabbedPane.add("Services", servicePanel);

        // Master tab
        JPanel masterPanel = createTab("Masters", masters, masterModel = new DefaultListModel<>(), masterList = new JList<>(masterModel));
        tabbedPane.add("Masters", masterPanel);

        // Client tab
        JPanel clientPanel = createTab("Clients", clients, clientModel = new DefaultListModel<>(), clientList = new JList<>(clientModel));
        tabbedPane.add("Clients", clientPanel);

        // Adding detail panel to display selected element's data
        detailTextArea = new JTextArea(10, 30);
        detailTextArea.setEditable(false);
        JScrollPane detailScrollPane = new JScrollPane(detailTextArea);
        add(detailScrollPane, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);

        // Add a ChangeListener to automatically select the first item in the list when a tab is selected
        tabbedPane.addChangeListener(e -> onTabSelected());

        refreshAllTabs();
        setVisible(true);
    }

    private void onTabSelected() {
        int selectedIndex = tabbedPane.getSelectedIndex();

        switch (selectedIndex) {
            case 0: // Services tab
                if (serviceList.isSelectionEmpty()) {
                    serviceList.setSelectedIndex(0);  // Select the first item in the list
                }
                break;
            case 1: // Masters tab
                if (masterList.isSelectionEmpty()) {
                    masterList.setSelectedIndex(0);  // Select the first item in the list
                }
                break;
            case 2: // Clients tab
                if (clientList.isSelectionEmpty()) {
                    clientList.setSelectedIndex(0);  // Select the first item in the list
                }
                break;
        }
    }

    private <T> JPanel createTab(String title, List<T> items, DefaultListModel<String> model, JList<String> list) {
        JPanel panel = new JPanel(new BorderLayout());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> onItemSelected(title, list.getSelectedValue()));

        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add " + title);
        JButton editButton = new JButton("Edit " + title);
        JButton deleteButton = new JButton("Delete " + title);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> onAdd(title));
        editButton.addActionListener(e -> onEdit(title, list.getSelectedValue()));
        deleteButton.addActionListener(e -> onDelete(title, list.getSelectedValue(), items, model));

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void onItemSelected(String title, String selectedItem) {
        if (selectedItem == null) {
            return;
        }

        String details = "";
        switch (title) {
            case "Services":
                Service service = findServiceByName(selectedItem);
                if (service != null) {
                    details = "Service Name: " + service.getName() + "\n" +
                            "Type: " + service.getType() + "\n" +
                            "Price: " + service.getPrice() + "\n" +
                            "Start Time: " + service.getOrderTime().getStartTime() + "\n" +
                            "End Time: " + service.getOrderTime().getEndTime() + "\n" +
                            "Date: " + service.getOrderTime().getDate() + "\n" +
                            "Master: " + service.getMaster().getFullName() + "\n" +
                            "Client: " + service.getClient().getFullName();
                }
                break;
            case "Masters":
                Master master = findMasterByName(selectedItem);
                if (master != null) {
                    details = "Master Name: " + master.getFullName() + "\n" +
                            "Phone: " + master.getPhone();  // Assuming phone number is available
                }
                break;
            case "Clients":
                Client client = findClientByName(selectedItem);
                if (client != null) {
                    details = "Client Name: " + client.getFullName() + "\n" +
                            "Phone: " + client.getPhone();
                }
                break;
        }
        detailTextArea.setText(details);
    }

    private void onAdd(String title) {
        switch (title) {
            case "Services":
                ServiceDialog serviceDialog = new ServiceDialog(this, null, masters, clients);
                serviceDialog.setVisible(true);
                if (serviceDialog.isServiceAdded()) {
                    services.add(serviceDialog.getService());
                    refreshAllTabs();
                }
                break;
            case "Masters":
                MasterDialog masterDialog = new MasterDialog(this, null);
                masterDialog.setVisible(true);
                if (masterDialog.isMasterAdded()) {
                    masters.add(masterDialog.getMaster());
                    refreshAllTabs();
                }
                break;
            case "Clients":
                ClientDialog clientDialog = new ClientDialog(this, null);
                clientDialog.setVisible(true);
                if (clientDialog.isClientAdded()) {
                    clients.add(clientDialog.getClient());
                    refreshAllTabs();
                }
                break;
        }
    }

    private void onEdit(String title, String selectedItem) {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.");
            return;
        }

        switch (title) {
            case "Services":
                Service service = findServiceByName(selectedItem);
                if (service != null) {
                    ServiceDialog serviceDialog = new ServiceDialog(this, service, masters, clients);
                    serviceDialog.setVisible(true);
                    if (serviceDialog.isServiceAdded()) {
                        refreshAllTabs();
                    }
                }
                break;
            case "Masters":
                Master master = findMasterByName(selectedItem);
                if (master != null) {
                    MasterDialog masterDialog = new MasterDialog(this, master);
                    masterDialog.setVisible(true);
                    if (masterDialog.isMasterAdded()) {
                        refreshAllTabs();
                    }
                }
                break;
            case "Clients":
                Client client = findClientByName(selectedItem);
                if (client != null) {
                    ClientDialog clientDialog = new ClientDialog(this, client);
                    clientDialog.setVisible(true);
                    if (clientDialog.isClientAdded()) {
                        refreshAllTabs();
                    }
                }
                break;
        }
    }

    private void onDelete(String title, String selectedItem, List<?> list, DefaultListModel<String> model) {
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        // Remove the item from the list
        list.removeIf(item -> item.toString().equals(selectedItem));

        // Refresh the tab after deletion
        refreshAllTabs();

        // Check which list was modified and update the selected item accordingly
        switch (title) {
            case "Services":
                if (!serviceModel.isEmpty()) {
                    serviceList.setSelectedIndex(0);  // Select the first item in the Services list
                }
                break;
            case "Masters":
                if (!masterModel.isEmpty()) {
                    masterList.setSelectedIndex(0);  // Select the first item in the Masters list
                }
                break;
            case "Clients":
                if (!clientModel.isEmpty()) {
                    clientList.setSelectedIndex(0);  // Select the first item in the Clients list
                }
                break;
        }
    }


    private Service findServiceByName(String name) {
        return services.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    private Master findMasterByName(String name) {
        return masters.stream().filter(m -> m.getFullName().equals(name)).findFirst().orElse(null);
    }

    private Client findClientByName(String name) {
        return clients.stream().filter(c -> c.getFullName().equals(name)).findFirst().orElse(null);
    }

    private void refreshAllTabs() {
        serviceModel.clear();
        services.forEach(service -> serviceModel.addElement(service.getName()));

        masterModel.clear();
        masters.forEach(master -> masterModel.addElement(master.getFullName()));

        clientModel.clear();
        clients.forEach(client -> clientModel.addElement(client.getFullName()));
    }
}
