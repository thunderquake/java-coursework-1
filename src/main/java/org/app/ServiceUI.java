package org.app;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServiceUI extends JFrame {
    private JTabbedPane tabbedPane;
    private List<Service> services;
    private List<Master> masters;
    private List<Client> clients;

    private JList<String> serviceList, masterList, clientList;
    private DefaultListModel<String> serviceModel, masterModel, clientModel;

    private JTextArea detailTextArea;

    public ServiceUI(List<Service> services, List<Master> masters, List<Client> clients) {
        this.services = services;
        this.masters = masters;
        this.clients = clients;

        setTitle("Service Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenuBar();

        tabbedPane = new JTabbedPane();

        JPanel servicePanel = createTab("Services", services, serviceModel = new DefaultListModel<>(), serviceList = new JList<>(serviceModel));
        tabbedPane.add("Services", servicePanel);

        JPanel masterPanel = createTab("Masters", masters, masterModel = new DefaultListModel<>(), masterList = new JList<>(masterModel));
        tabbedPane.add("Masters", masterPanel);

        JPanel clientPanel = createTab("Clients", clients, clientModel = new DefaultListModel<>(), clientList = new JList<>(clientModel));
        tabbedPane.add("Clients", clientPanel);

        detailTextArea = new JTextArea(10, 30);
        detailTextArea.setEditable(false);
        JScrollPane detailScrollPane = new JScrollPane(detailTextArea);
        add(detailScrollPane, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> onTabSelected());

        refreshAllTabs();
        setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveToFile());

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> openFromFile());

        fileMenu.add(saveMenuItem);
        fileMenu.add(openMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void onTabSelected() {
        int selectedIndex = tabbedPane.getSelectedIndex();

        switch (selectedIndex) {
            case 0:
                if (serviceList.isSelectionEmpty()) {
                    serviceList.setSelectedIndex(0);
                }
                break;
            case 1:
                if (masterList.isSelectionEmpty()) {
                    masterList.setSelectedIndex(0);
                }
                break;
            case 2:
                if (clientList.isSelectionEmpty()) {
                    clientList.setSelectedIndex(0);
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

        if (title.equals("Services")) {
            JButton expensiveServiceButton = new JButton("The most expensive service");
            JButton averagePriceButton = new JButton("Average service price");

            buttonPanel.add(expensiveServiceButton);
            buttonPanel.add(averagePriceButton);

            expensiveServiceButton.addActionListener(e -> showMostExpensiveService());

            averagePriceButton.addActionListener(e -> showAverageServicePrice());

        }


        if (title.equals("Masters")) {
            JButton showSpecializationsButton = new JButton("Show specializations");
            buttonPanel.add(showSpecializationsButton);

            showSpecializationsButton.addActionListener(e -> showMasterSpecializations());
        }

        if (title.equals("Clients")) {
            JButton showClientCountButton = new JButton("Show client count for service in period");
            buttonPanel.add(showClientCountButton);

            showClientCountButton.addActionListener(e -> showClientCountForPeriod());
        }

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showMostExpensiveService() {
        double maxPrice = Double.MIN_VALUE;
        Service mostExpensiveService = null;

        for (Service service : services) {
            double price = service.getPrice();
            if (price > maxPrice) {
                maxPrice = price;
                mostExpensiveService = service;
            }
        }

        if (mostExpensiveService != null) {
            JOptionPane.showMessageDialog(this,
                    "Most Expensive Service:\n" +
                            "Service Name: " + mostExpensiveService.getName() + "\n" +
                            "Price: " + mostExpensiveService.getPrice());
        } else {
            JOptionPane.showMessageDialog(this, "No services available");
        }
    }

    private void showAverageServicePrice() {
        double totalPrice = 0;
        int serviceCount = 0;

        for (Service service : services) {
            totalPrice += service.getPrice();
            serviceCount++;
        }

        if (serviceCount > 0) {
            double averagePrice = totalPrice / serviceCount;
            JOptionPane.showMessageDialog(this,
                    "Average service price: " + averagePrice);
        } else {
            JOptionPane.showMessageDialog(this, "No services available");
        }
    }



    private void showClientCountForPeriod() {
        String startDateStr = JOptionPane.showInputDialog(this, "Enter start date (DD-MM-YYYY):");
        String endDateStr = JOptionPane.showInputDialog(this, "Enter end date (DD-MM-YYYY):");

        if (startDateStr == null || endDateStr == null) {
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            int clientCount = getClientCount(startDateStr, formatter, endDateStr);

            JOptionPane.showMessageDialog(this, "Number of clients with services in the given period: " + clientCount);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd-MM-yyyy.");
        }
    }

    private int getClientCount(String startDateStr, DateTimeFormatter formatter, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        int clientCount = 0;

        for (Client client : clients) {
            for (Service service : client.getLinkedServices()) {
                LocalDate serviceDate = LocalDate.parse(service.getOrderTime().getDate(), formatter);

                if ((serviceDate.isEqual(startDate) || serviceDate.isEqual(endDate))
                        || (serviceDate.isAfter(startDate) && serviceDate.isBefore(endDate))) {
                    clientCount++;
                    break;
                }
            }
        }
        return clientCount;
    }


    private void showMasterSpecializations() {
        StringBuilder specializations = new StringBuilder();
        for (Master master : masters) {
            specializations.append(master.getFullName()).append(": ").append(master.getSpecialization()).append("\n");
        }

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText(specializations.toString());

        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Masters' specializations", JOptionPane.INFORMATION_MESSAGE);
    }


    private void onItemSelected(String title, String selectedItem) {
        if (selectedItem == null) {
            return;
        }

        StringBuilder details = new StringBuilder();
        switch (title) {
            case "Services":
                Service service = findServiceByName(selectedItem);
                if (service != null) {
                    details = new StringBuilder("Service Name: " + service.getName() + "\n" +
                            "Type: " + service.getType() + "\n" +
                            "Price: " + service.getPrice() + "\n" +
                            "Start Time: " + service.getOrderTime().getStartTime() + "\n" +
                            "End Time: " + service.getOrderTime().getEndTime() + "\n" +
                            "Date: " + service.getOrderTime().getDate() + "\n" +
                            "Master: " + service.getMaster().getFullName() + "\n" +
                            "Client: " + service.getClient().getFullName());
                }
                break;
            case "Masters":
                Master master = findMasterByName(selectedItem);
                if (master != null) {
                    details = new StringBuilder("Master Name: " + master.getFullName() + "\n" +
                            "Phone: " + master.getPhone() + "\n" +
                            "Address: " + master.getAddress() + "\n" +
                            "Specialization: " + master.getSpecialization());
                }
                break;

            case "Clients":
                Client client = findClientByName(selectedItem);
                if (client != null) {
                    details = new StringBuilder("Client Name: " + client.getFullName() + "\n" +
                            "Phone: " + client.getPhone() + "\n");

                    details.append("\nLinked Services:\n");
                    if (client.getLinkedServices().isEmpty()) {
                        details.append("No linked services");
                    } else {
                        for (Service s : client.getLinkedServices()) {
                            details.append("- ").append(s.getName()).append(" (").append(s.getType()).append(", ").append(s.getPrice()).append(")\n");
                        }
                    }
                }
                break;
        }
        detailTextArea.setText(details.toString());
    }

    private void onAdd(String title) {
        switch (title) {
            case "Services":
                ServiceDialog serviceDialog = new ServiceDialog(this, null, masters, clients);
                serviceDialog.setVisible(true);
                if (serviceDialog.isServiceAdded()) {
                    Service newService = serviceDialog.getService();
                    services.add(newService);

                    Client client = newService.getClient();
                    if (!client.getLinkedServices().contains(newService)) {
                        client.addService(newService);
                    }
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
                        Service updatedService = serviceDialog.getService();
                        Client client = service.getClient();

                        client.removeService(service);

                        int index = services.indexOf(service);
                        services.set(index, updatedService);

                        client.addService(updatedService);

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
            JOptionPane.showMessageDialog(this, "Please select an item to delete");
            return;
        }

        list.removeIf(item -> item.toString().equals(selectedItem));

        switch (title) {
            case "Services":
                Service service = findServiceByName(selectedItem);
                if (service != null) {
                    Client client = service.getClient();
                    client.removeService(service);
                    services.remove(service);
                }
                break;

            case "Masters":
                Master master = findMasterByName(selectedItem);
                if (master != null) {
                    masters.remove(master);
                }
                break;

            case "Clients":
                Client client = findClientByName(selectedItem);
                if (client != null) {
                    clients.remove(client);
                }
                break;
        }

        refreshAllTabs();

        switch (title) {
            case "Services":
                if (!model.isEmpty()) {
                    serviceList.setSelectedIndex(0);
                }
                break;
            case "Masters":
                if (!model.isEmpty()) {
                    masterList.setSelectedIndex(0);
                }
                break;
            case "Clients":
                if (!model.isEmpty()) {
                    clientList.setSelectedIndex(0);
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


    private void saveToFile() {
        if (services.size() <= 2) {
            JOptionPane.showMessageDialog(this, "At least 3 services are required to save the data");
            return;
        }

        StringBuilder data = new StringBuilder();

        data.append("Services:\n");
        for (Service service : services) {
            data.append(service.getName()).append(";")
                    .append(service.getType()).append(";")
                    .append(service.getPrice()).append(";")
                    .append(service.getOrderTime().getStartTime()).append(";")
                    .append(service.getOrderTime().getEndTime()).append(";")
                    .append(service.getOrderTime().getDate()).append(";")
                    .append(service.getMaster().getFullName()).append(";")
                    .append(service.getClient().getFullName()).append("\n");
        }

        data.append("Masters:\n");
        for (Master master : masters) {
            data.append(master.getFullName()).append(";")
                    .append(master.getPhone()).append(";")
                    .append(master.getAddress()).append(";")
                    .append(master.getSpecialization()).append("\n");
        }

        data.append("Clients:\n");
        for (Client client : clients) {
            data.append(client.getFullName()).append(";")
                    .append(client.getPhone()).append(";");

            List<String> linkedServiceNames = new ArrayList<>();
            for (Service service : client.getLinkedServices()) {
                linkedServiceNames.add(service.getName());
            }
            data.append(String.join(",", linkedServiceNames)).append("\n");
        }

        String filename = "data_" + new Random().nextInt(100000) + ".txt";
        Path path = Paths.get(filename);

        try {
            Files.write(path, data.toString().getBytes());
            JOptionPane.showMessageDialog(this, "Data saved to file: " + filename);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data to file: " + ex.getMessage());
        }
    }


    private void openFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path filePath = fileChooser.getSelectedFile().toPath();

        try {
            List<String> lines = Files.readAllLines(filePath);

            services.clear();
            masters.clear();
            clients.clear();

            String currentSection = "";
            for (String line : lines) {
                if (line.equals("Services:") || line.equals("Masters:") || line.equals("Clients:")) {
                    currentSection = line;
                    continue;
                }

                String[] parts = line.split(";");
                switch (currentSection) {
                    case "Services:":
                        Service service = new Service(
                                parts[0],
                                parts[1],
                                Double.parseDouble(parts[2]),
                                new OrderTime(parts[3], parts[4], parts[5]),
                                new Master(parts[6], "", "", ""),
                                new Client(parts[7], "")
                        );
                        services.add(service);
                        break;

                    case "Masters:":
                        masters.add(new Master(
                                parts[0],
                                parts[1],
                                parts[2],
                                parts[3]
                        ));
                        break;

                    case "Clients:":
                        Client client = new Client(parts[0], parts[1]);
                        if (parts.length > 2 && !parts[2].isEmpty()) {
                            String[] linkedServices = parts[2].split(",");
                            for (String serviceName : linkedServices) {
                                for (Service s : services) {
                                    if (s.getName().equals(serviceName.trim())) {
                                        client.getLinkedServices().add(s);
                                        s.setClient(client);
                                    }
                                }
                            }
                        }
                        clients.add(client);
                        break;
                }
            }

            refreshAllTabs();
            JOptionPane.showMessageDialog(this, "Data loaded from file: " + filePath.getFileName());
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
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
