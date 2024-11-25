package org.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceDialog extends JDialog {
    private JTextField nameField, typeField, priceField, startTimeField, endTimeField, dateField;
    private JButton saveButton, cancelButton;
    private boolean serviceAdded = false;
    private Service service;

    public ServiceDialog(JFrame parent, Service serviceToEdit) {
        super(parent, serviceToEdit == null ? "Додати сервіс" : "Редагувати сервіс", true);
        if (serviceToEdit != null) this.service = serviceToEdit;

        setLayout(new GridLayout(7, 2, 10, 10));

        setPreferredSize(new Dimension(400, 450));
        setSize(400, 450);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        nameField = new JTextField(service == null ? "" : service.getName());
        typeField = new JTextField(service == null ? "" : service.getType());
        priceField = new JTextField(service == null ? "" : String.valueOf(service.getPrice()));
        startTimeField = new JTextField(service == null ? "" : service.getOrderTime().getStartTime());
        endTimeField = new JTextField(service == null ? "" : service.getOrderTime().getEndTime());
        dateField = new JTextField(service == null ? "" : service.getOrderTime().getDate());

        JPanel namePanel = createInputPanel("Назва:", nameField);
        JPanel typePanel = createInputPanel("Тип:", typeField);
        JPanel pricePanel = createInputPanel("Ціна:", priceField);
        JPanel startTimePanel = createInputPanel("Час початку:", startTimeField);
        JPanel endTimePanel = createInputPanel("Час завершення:", endTimeField);
        JPanel datePanel = createInputPanel("Дата:", dateField);

        add(namePanel);
        add(typePanel);
        add(pricePanel);
        add(startTimePanel);
        add(endTimePanel);
        add(datePanel);

        saveButton = new JButton("Зберегти");
        saveButton.addActionListener(this::saveService);
        cancelButton = new JButton("Скасувати");
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel);

        setLocationRelativeTo(parent);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private void saveService(ActionEvent e) {
        if (validateFields()) {
            if (service == null) {
                service = new Service(
                        nameField.getText(),
                        typeField.getText(),
                        Double.parseDouble(priceField.getText()),
                        new OrderTime(startTimeField.getText(), endTimeField.getText(), dateField.getText())
                );
            } else {
                service.setName(nameField.getText());
                service.setType(typeField.getText());
                service.setPrice(Double.parseDouble(priceField.getText()));
                service.setOrderTime(new OrderTime(startTimeField.getText(), endTimeField.getText(), dateField.getText()));
            }
            serviceAdded = true;
            dispose();
        }
    }

    private boolean validateFields() {
        String priceText = priceField.getText();
        if (nameField.getText().isEmpty() || typeField.getText().isEmpty() ||
                priceText.isEmpty() || startTimeField.getText().isEmpty() ||
                endTimeField.getText().isEmpty() || dateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Будь ласка, заповніть всі поля.", "Помилка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ціна повинна бути числом.", "Помилка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Pattern timePattern = Pattern.compile("^(\\d{2}:\\d{2})$");
        Matcher startTimeMatcher = timePattern.matcher(startTimeField.getText());
        Matcher endTimeMatcher = timePattern.matcher(endTimeField.getText());
        if (!startTimeMatcher.matches() || !endTimeMatcher.matches()) {
            JOptionPane.showMessageDialog(this, "Час має бути у форматі ЧЧ:ММ.", "Помилка", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isServiceAdded() {
        return serviceAdded;
    }

    public Service getService() {
        return service;
    }
}
