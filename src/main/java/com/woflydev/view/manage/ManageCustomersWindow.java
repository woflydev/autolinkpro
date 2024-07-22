package com.woflydev.view.manage;

import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.entity.Customer;
import com.woflydev.model.Globals;
import com.woflydev.view.util.table.CustomJTable;
import com.woflydev.view.util.table.button.ButtonEditor;
import com.woflydev.view.util.table.button.ButtonRenderer;
import com.woflydev.view.util.table.NonEditableTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.PRIMARY;

public class ManageCustomersWindow extends JFrame implements ActionListener {
    public static ManageCustomersWindow instance = null;

    private JButton addCustomerButton;
    private CustomJTable customerTable;
    private NonEditableTableModel tableModel;

    public ManageCustomersWindow() {
        setTitle("Manage Customers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Manage Customers", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        addCustomerButton = new JButton("Add New Customer");
        addCustomerButton.setPreferredSize(new Dimension(150, 40));
        addCustomerButton.setFocusPainted(false);
        addCustomerButton.addActionListener(this);
        StyleUtils.applyButtonStyle(addCustomerButton, PRIMARY);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(addCustomerButton, gbc);

        String[] columnNames = {"First Name", "Last Name", "Email", "License", "Date of Birth", "Actions"};
        tableModel = new NonEditableTableModel(columnNames, 0);
        customerTable = new CustomJTable(tableModel);
        customerTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(800, 400);
        setLocationRelativeTo(null);

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
        JPasswordField passwordField = new JPasswordField(20);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);
        dobSpinner.setValue(new Date());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dobSpinner, gbc);

        // Password
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
            Date dobDate = (Date) dobSpinner.getValue();
            LocalDate dob = dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String password = BCryptHash.hashString(new String(passwordField.getPassword()));

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !license.isEmpty() && dob != null && !password.isEmpty()) {
                Customer newCustomer = new Customer(firstName, lastName, email, password, license, dob.atStartOfDay());
                UserUtils.addCustomer(newCustomer);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCustomer(String email) {
        Customer customer = UserUtils.getCustomerByEmail(email);

        if (customer == null || customer.getPrivilege() != Globals.PRIVILEGE_CUSTOMER) {
            JOptionPane.showMessageDialog(null, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField firstNameField = new JTextField(customer.getFirstName(), 20);
        JTextField lastNameField = new JTextField(customer.getLastName(), 20);
        JTextField emailField = new JTextField(customer.getEmail(), 20);
        JTextField licenseField = new JTextField(customer.getLicense(), 20);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dobSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);
        dobSpinner.setValue(Date.from(customer.getDob().atZone(ZoneId.systemDefault()).toInstant()));

        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dobSpinner, gbc);

        // Password
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
            Date dobDate = (Date) dobSpinner.getValue();
            LocalDate dob = dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String password = new String(passwordField.getPassword());

            if (!firstName.isEmpty() && !lastName.isEmpty() && !newEmail.isEmpty() && !license.isEmpty() && dob != null) {
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(newEmail);
                customer.setLicense(license);
                customer.setDob(dob.atStartOfDay());

                if (!password.isEmpty()) {
                    customer.setPassword(BCryptHash.hashString(password));
                }

                UserUtils.updateCustomer(customer);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields except password must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<Customer> users = UserUtils.getCustomerList();
        String[] columnNames = {"First Name", "Last Name", "Email", "License", "Date of Birth", "Actions"};
        Object[][] data = users.stream()
                .filter(user -> user.getPrivilege() == Globals.PRIVILEGE_CUSTOMER)
                .map(user -> {
                    Customer customer = (Customer) user;
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
                        UserUtils.deleteCustomer(email);
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
