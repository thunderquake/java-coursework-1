package org.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MasterDialog extends JDialog {
    private JTextField masterNameField;
    private JTextField masterPhoneField;
    private boolean isMasterAdded = false;
    private Master master;

    public MasterDialog(JFrame parent, Master master) {
        super(parent, "Master Details", true);
        this.master = master;

        setLayout(new GridLayout(3, 2));

        // Master name field
        add(new JLabel("Master Name:"));
        masterNameField = new JTextField(master != null ? master.getFullName() : "");
        add(masterNameField);

        // Master phone number field
        add(new JLabel("Phone Number:"));
        masterPhoneField = new JTextField(master != null ? master.getPhone() : "");
        add(masterPhoneField);

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
        String name = masterNameField.getText().trim();
        String phone = masterPhoneField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Master name cannot be empty.");
            return;
        }

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number cannot be empty.");
            return;
        }

        if (master == null) {
            master = new Master(name, phone);
        } else {
            master.setFullName(name);
            master.setPhone(phone);
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
