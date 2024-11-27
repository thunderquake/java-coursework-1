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

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Client Name:"), gbc);

        gbc.gridx = 1;
        clientNameField = new JTextField(client != null ? client.getFullName() : "", 20);
        add(clientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        clientPhoneField = new JTextField(client != null ? client.getPhone() : "", 20);
        add(clientPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::onSave);
        add(saveButton, gbc);

        gbc.gridx = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onSave(ActionEvent e) {
        String name = clientNameField.getText().trim();
        String phone = clientPhoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Client name cannot be empty");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty");
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
