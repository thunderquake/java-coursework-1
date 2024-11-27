package org.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MasterDialog extends JDialog {
    private JTextField masterNameField;
    private JTextField masterPhoneField;
    private JTextField masterAddressField; // Нове поле для адреси
    private JTextField masterSpecializationField; // Нове поле для спеціалізації
    private boolean isMasterAdded = false;
    private Master master;

    public MasterDialog(JFrame parent, Master master) {
        super(parent, "Master Details", true);
        this.master = master;

        setLayout(new GridBagLayout()); // Use GridBagLayout for better spacing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around each component
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components stretch horizontally

        // Поле для імені майстра
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Master Name:"), gbc);
        gbc.gridx = 1;
        masterNameField = new JTextField(master != null ? master.getFullName() : "", 20);
        add(masterNameField, gbc);

        // Поле для номера телефону
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        masterPhoneField = new JTextField(master != null ? master.getPhone() : "", 20);
        add(masterPhoneField, gbc);

        // Поле для адреси
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        masterAddressField = new JTextField(master != null ? master.getAddress() : "", 20);
        add(masterAddressField, gbc);

        // Поле для спеціалізації
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 1;
        masterSpecializationField = new JTextField(master != null ? master.getSpecialization() : "", 20);
        add(masterSpecializationField, gbc);

        // Кнопки
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        String name = masterNameField.getText().trim();
        String phone = masterPhoneField.getText().trim();
        String address = masterAddressField.getText().trim();
        String specialization = masterSpecializationField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Master name cannot be empty");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty");
            return;
        }

        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty");
            return;
        }

        if (specialization.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Specialization cannot be empty");
            return;
        }

        if (master == null) {
            master = new Master(name, phone, address, specialization);
        } else {
            master.setFullName(name);
            master.setPhone(phone);
            master.setAddress(address);
            master.setSpecialization(specialization);
        }

        isMasterAdded = true;
        dispose();
    }

    public boolean isMasterAdded() {
        return isMasterAdded;
    }

    public Master getMaster() {
        return master;
    }
}
