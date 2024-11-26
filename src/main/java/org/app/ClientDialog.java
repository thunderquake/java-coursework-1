package org.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ClientDialog extends JDialog {
    private JTextField clientNameField;
    private JTextField clientPhoneField;
    private boolean isClientAdded = false;
    private Client client;

    public ClientDialog(JFrame parent, Client client) {
        super(parent, "Client Details", true);
        this.client = client;

        setLayout(new GridLayout(3, 2));

        // Client name field
        add(new JLabel("Client Name:"));
        clientNameField = new JTextField(client != null ? client.getFullName() : "");
        add(clientNameField);

        // Client phone number field
        add(new JLabel("Phone Number:"));
        clientPhoneField = new JTextField(client != null ? client.getPhone() : "");
        add(clientPhoneField);

        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::onSave);
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onSave(ActionEvent e) {
        String name = clientNameField.getText().trim();
        String phone = clientPhoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Client name cannot be empty.");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty.");
            return;
        }

        if (client == null) {
            client = new Client(name, phone);
        } else {
            client.setFullName(name);
            client.setPhone(phone);
        }

        isClientAdded = true;
        dispose();
    }

    public boolean isClientAdded() {
        return isClientAdded;
    }

    public Client getClient() {
        return client;
    }
}
