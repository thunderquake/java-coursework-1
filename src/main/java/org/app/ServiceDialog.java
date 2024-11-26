package org.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ServiceDialog extends JDialog {
    private JTextField serviceNameField;
    private JTextField serviceTypeField;
    private JTextField servicePriceField;
    private JTextField orderDateField;
    private JTextField startHourField, startMinuteField;
    private JTextField endHourField, endMinuteField;
    private JComboBox<Master> masterComboBox;
    private JComboBox<Client> clientComboBox;

    private boolean isServiceAdded = false;
    private Service service;

    public ServiceDialog(JFrame parent, Service service, List<Master> masters, List<Client> clients) {
        super(parent, "Service Details", true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.anchor = GridBagConstraints.WEST;

        // Service Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Service Name:"), gbc);

        gbc.gridx = 1;
        serviceNameField = new JTextField(service != null ? service.getName() : "");
        serviceNameField.setPreferredSize(new Dimension(250, 25));  // Wider input field
        add(serviceNameField, gbc);

        // Service Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Service Type:"), gbc);

        gbc.gridx = 1;
        serviceTypeField = new JTextField(service != null ? service.getType() : "");
        serviceTypeField.setPreferredSize(new Dimension(250, 25));  // Wider input field
        add(serviceTypeField, gbc);

        // Service Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Service Price:"), gbc);

        gbc.gridx = 1;
        servicePriceField = new JTextField(service != null ? String.valueOf(service.getPrice()) : "");
        servicePriceField.setPreferredSize(new Dimension(250, 25));  // Wider input field
        add(servicePriceField, gbc);

        // Order Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Order Date (DD-MM-YYYY):"), gbc);

        gbc.gridx = 1;
        orderDateField = new JTextField(service != null && service.getOrderTime() != null ? service.getOrderTime().getDate() : "");
        orderDateField.setPreferredSize(new Dimension(250, 25));  // Wider input field
        add(orderDateField, gbc);

        // Start Time (Split Hour and Minute)
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Start Time:"), gbc);

        gbc.gridx = 1;
        JPanel startTimePanel = new JPanel();
        startTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        startHourField = new JTextField("", 2);
        startMinuteField = new JTextField("", 2);

        // If a service is passed, split the start time into hours and minutes
        if (service != null && service.getOrderTime() != null) {
            String startTime = service.getOrderTime().getStartTime();  // e.g. "14:30"
            String[] timeParts = startTime.split(":");
            if (timeParts.length == 2) {
                startHourField.setText(timeParts[0]);  // Set hour
                startMinuteField.setText(timeParts[1]);  // Set minute
            }
        }

        startTimePanel.add(startHourField);
        startTimePanel.add(new JLabel(":"));
        startTimePanel.add(startMinuteField);
        add(startTimePanel, gbc);

        // End Time (Split Hour and Minute)
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("End Time:"), gbc);

        gbc.gridx = 1;
        JPanel endTimePanel = new JPanel();
        endTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        endHourField = new JTextField("", 2);
        endMinuteField = new JTextField("", 2);

        // If a service is passed, split the end time into hours and minutes
        if (service != null && service.getOrderTime() != null) {
            String endTime = service.getOrderTime().getEndTime();  // e.g. "16:30"
            String[] timeParts = endTime.split(":");
            if (timeParts.length == 2) {
                endHourField.setText(timeParts[0]);  // Set hour
                endMinuteField.setText(timeParts[1]);  // Set minute
            }
        }

        endTimePanel.add(endHourField);
        endTimePanel.add(new JLabel(":"));
        endTimePanel.add(endMinuteField);
        add(endTimePanel, gbc);

        // Master Selection
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Select Master:"), gbc);

        gbc.gridx = 1;
        masterComboBox = new JComboBox<>(masters.toArray(new Master[0]));
        if (service != null && service.getMaster() != null) {
            masterComboBox.setSelectedItem(service.getMaster());
        }
        add(masterComboBox, gbc);

        // Client Selection
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Select Client:"), gbc);

        gbc.gridx = 1;
        clientComboBox = new JComboBox<>(clients.toArray(new Client[0]));
        if (service != null && service.getClient() != null) {
            clientComboBox.setSelectedItem(service.getClient());
        }
        add(clientComboBox, gbc);

        // Buttons Panel (Centered)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Align buttons to the center

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::onSave);
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        // Add button panel to the grid with constraints for centering
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2; // Span across both columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onSave(ActionEvent e) {
        String name = serviceNameField.getText().trim();
        String type = serviceTypeField.getText().trim();
        String priceText = servicePriceField.getText().trim();
        String orderDate = orderDateField.getText().trim();
        String startHour = startHourField.getText().trim();
        String startMinute = startMinuteField.getText().trim();
        String endHour = endHourField.getText().trim();
        String endMinute = endMinuteField.getText().trim();
        Master selectedMaster = (Master) masterComboBox.getSelectedItem();
        Client selectedClient = (Client) clientComboBox.getSelectedItem();

        // Validation
        if (name.isEmpty() || type.isEmpty() || priceText.isEmpty() || orderDate.isEmpty() ||
                startHour.isEmpty() || startMinute.isEmpty() || endHour.isEmpty() || endMinute.isEmpty() ||
                selectedMaster == null || selectedClient == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        int startH, startM, endH, endM;
        try {
            startH = Integer.parseInt(startHour);
            startM = Integer.parseInt(startMinute);
            endH = Integer.parseInt(endHour);
            endM = Integer.parseInt(endMinute);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Please enter valid numbers for hours and minutes.");
            return;
        }

        // Validate time ranges
        if (startH < 0 || startH > 23 || startM < 0 || startM > 59 || endH < 0 || endH > 23 || endM < 0 || endM > 59) {
            JOptionPane.showMessageDialog(this, "Time must be in the format hh:mm where hh is 0-23 and mm is 0-59.");
            return;
        }

        // Validate the order date format (DD-MM-YYYY)
        if (!isValidDate(orderDate)) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please enter a valid date in the format DD-MM-YYYY.");
            return;
        }

        // Create OrderTime object
        OrderTime orderTime = new OrderTime(startHour + ":" + startMinute, endHour + ":" + endMinute, orderDate);

        if (service == null) {
            service = new Service(name, type, Double.parseDouble(priceText), orderTime, selectedMaster, selectedClient);
        } else {
            service.setName(name);
            service.setType(type);
            service.setPrice(Double.parseDouble(priceText));
            service.setOrderTime(orderTime);
            service.setMaster(selectedMaster);
            service.setClient(selectedClient);
        }

        isServiceAdded = true;
        dispose();
    }

    private boolean isValidDate(String date) {
        // Simple validation for date in the format DD-MM-YYYY
        String[] dateParts = date.split("-");
        if (dateParts.length != 3) {
            return false;
        }

        try {
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Validate day and month ranges
            if (day < 1 || day > 31 || month < 1 || month > 12) {
                return false;
            }

            // Check month-specific validation (simplified)
            if (month == 2 && day > 29) return false;  // Feb cannot have more than 29 days
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) return false;  // Apr, Jun, Sep, Nov cannot have more than 30 days
            if (day > 31) return false;  // Months like Jan, Mar, etc. cannot have more than 31 days

            // Optionally, we could add logic to check leap years here if needed

        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }

    public boolean isServiceAdded() {
        return isServiceAdded;
    }

    public Service getService() {
        return service;
    }
}
