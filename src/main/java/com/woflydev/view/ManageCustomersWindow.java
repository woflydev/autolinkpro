package com.woflydev.view;

import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Customer;
import com.woflydev.model.Globals;
import com.woflydev.model.User;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class ManageCustomersWindow extends JFrame implements ActionListener {
    public static ManageCustomersWindow instance = null;

    private JButton addCustomerButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public ManageCustomersWindow() {
        setTitle("Manage Customers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel for buttons and table
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Manage Customers", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the heading to occupy some vertical space
        mainPanel.add(headingLabel, gbc);

        // Add Customer button
        addCustomerButton = new JButton("Add New Customer");
        addCustomerButton.setPreferredSize(new Dimension(150, 40));
        addCustomerButton.setBackground(new Color(0, 120, 215));
        addCustomerButton.setForeground(Color.WHITE);
        addCustomerButton.setFocusPainted(false);
        addCustomerButton.addActionListener(this);
        gbc.gridwidth = 1; // Reset gridwidth for button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0; // Reset weighty for button
        mainPanel.add(addCustomerButton, gbc);

        // Initialize tableModel before setting up the table
        String[] columnNames = {"First Name", "Last Name", "Email", "License", "Date of Birth", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        customerTable.setCellSelectionEnabled(true);
        customerTable.setAutoCreateRowSorter(true);
        customerTable.setFillsViewportHeight(true);

        // Add the table to the scroll pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Allow the table to take up remaining vertical space
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(800, 400);
        setLocationRelativeTo(null);

        // Set initial table data
        updateTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCustomerButton) {
            addNewCustomer();
        }
    }

    private void addNewCustomer() {
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField licenseField = new JTextField(20);
        JTextField dobField = new JTextField(20); // Expecting format YYYY-MM-DDTHH:MM

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailField, gbc);

        // License
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("License:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(licenseField, gbc);

        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Date of Birth (YYYY-MM-DDTHH:MM):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dobField, gbc);

        // Password
        JTextField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter new customer details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String license = licenseField.getText();
            String dobString = dobField.getText();
            LocalDateTime dob = null;
            try {
                dob = LocalDateTime.parse(dobString);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DDTHH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password = BCryptHash.hashString(passwordField.getText());

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !license.isEmpty() && dob != null && !password.isEmpty()) {
                Customer newCustomer = new Customer(firstName, lastName, email, password, license, dob);
                UserUtils.addUser(newCustomer);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCustomer(String email) {
        User user = UserUtils.findUserByEmail(email);

        if (user == null || user.getPrivilege() != Globals.PRIVILEGE_CUSTOMER) {
            JOptionPane.showMessageDialog(null, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = (Customer) user;

        JTextField firstNameField = new JTextField(customer.getFirstName(), 20);
        JTextField lastNameField = new JTextField(customer.getLastName(), 20);
        JTextField emailField = new JTextField(customer.getEmail(), 20);
        JTextField licenseField = new JTextField(customer.getLicense(), 20);
        JTextField dobField = new JTextField(customer.getDob().toString(), 20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailField, gbc);

        // License
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("License:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(licenseField, gbc);

        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Date of Birth (YYYY-MM-DDTHH:MM):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dobField, gbc);

        // Password
        JTextField passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Edit customer details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String newEmail = emailField.getText();
            String license = licenseField.getText();
            String dobString = dobField.getText();
            LocalDateTime dob = null;
            try {
                dob = LocalDateTime.parse(dobString);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DDTHH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String password = passwordField.getText().isEmpty() ? customer.getPassword() : BCryptHash.hashString(passwordField.getText());

            if (!firstName.isEmpty() && !lastName.isEmpty() && !newEmail.isEmpty() && !license.isEmpty() && dob != null) {
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(newEmail);
                customer.setLicense(license);
                customer.setDob(dob);
                customer.setPassword(password);
                UserUtils.updateUser(customer);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields except password must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<Customer> users = UserUtils.getCustomers();
        System.out.println(users);
        String[] columnNames = {"First Name", "Last Name", "Email", "License", "Date of Birth", "Actions"};
        Object[][] data = users.stream()
                .filter(user -> user.getPrivilege() == Globals.PRIVILEGE_CUSTOMER)
                .map(user -> {
                    Customer customer = (Customer) user;
                    System.out.println(customer.getLicense());
                    return new Object[]{
                            customer.getFirstName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getLicense(),
                            customer.getDob().toString(),
                            "Edit/Delete"
                    };
                })
                .toArray(Object[][]::new);

        tableModel.setDataVector(data, columnNames);

        customerTable.setRowHeight(75);
        customerTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Edit", "Delete"}));
        customerTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), new String[]{"Edit", "Delete"}, new Runnable[]{
                () -> {
                    int row = customerTable.getSelectedRow();
                    if (row >= 0) {
                        String email = (String) tableModel.getValueAt(row, 2);
                        editCustomer(email);
                    }
                },
                () -> {
                    int row = customerTable.getSelectedRow();
                    if (row >= 0) {
                        String email = (String) tableModel.getValueAt(row, 2);
                        UserUtils.deleteUser(email);
                        updateTable();
                    }
                }
        }));
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageCustomersWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
