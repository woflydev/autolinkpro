package com.woflydev.view.manage;

import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Globals;
import com.woflydev.model.entity.Staff;
import com.woflydev.view.util.table.CustomJTable;
import com.woflydev.view.util.table.button.ButtonEditor;
import com.woflydev.view.util.table.button.ButtonRenderer;
import com.woflydev.view.util.table.NonEditableTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.PRIMARY;

public class ManageStaffWindow extends JFrame implements ActionListener {
    public static ManageStaffWindow instance = null;

    private JButton addStaffButton;
    private CustomJTable staffTable;
    private NonEditableTableModel tableModel;

    public ManageStaffWindow() {
        setTitle("Manage Staff");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Manage Staff", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        addStaffButton = new JButton("Add New Staff");
        addStaffButton.setPreferredSize(new Dimension(150, 40));
        addStaffButton.setFocusPainted(false);
        addStaffButton.addActionListener(this);
        StyleUtils.applyButtonStyle(addStaffButton, PRIMARY);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(addStaffButton, gbc);

        String[] columnNames = {"First Name", "Last Name", "Email", "Actions"};
        tableModel = new NonEditableTableModel(columnNames, 0);
        staffTable = new CustomJTable(tableModel);
        staffTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(staffTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(700, 400);
        setLocationRelativeTo(null);

        updateTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addStaffButton) {
            addNewStaff();
        }
    }

    private void addNewStaff() {
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter new staff details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = BCryptHash.hashString(passwordField.getText());

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                Staff newStaff = new Staff(firstName, lastName, email, password);
                UserUtils.addStaff(newStaff);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editStaff(String email) {
        Staff staff = UserUtils.getStaffByEmail(email);

        if (staff == null || staff.getPrivilege() > Globals.PRIVILEGE_STAFF) {
            JOptionPane.showMessageDialog(null, "Staff not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField firstNameField = new JTextField(staff.getFirstName(), 20);
        JTextField lastNameField = new JTextField(staff.getLastName(), 20);
        JTextField emailField = new JTextField(staff.getEmail(), 20);
        JTextField passwordField = new JPasswordField(20);

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

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Edit staff details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String newEmail = emailField.getText();
            String password = passwordField.getText().isEmpty() ? staff.getPassword() : BCryptHash.hashString(passwordField.getText());

            if (!firstName.isEmpty() && !lastName.isEmpty() && !newEmail.isEmpty()) {
                staff.setFirstName(firstName);
                staff.setLastName(lastName);
                staff.setEmail(newEmail);
                staff.setPassword(password);
                UserUtils.updateStaff(staff);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields except password must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<Staff> users = UserUtils.getStaffList();
        String[] columnNames = {"First Name", "Last Name", "Email", "Actions"};
        Object[][] data = users.stream()
                .filter(user -> user.getPrivilege() == Globals.PRIVILEGE_STAFF)
                .map(user -> new Object[]{
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        "Edit/Delete"
                })
                .toArray(Object[][]::new);

        tableModel.setDataVector(data, columnNames);

        staffTable.setRowHeight(75);
        staffTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Edit", "Delete"}));
        staffTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), new String[]{"Edit", "Delete"}, new Runnable[]{
                () -> {
                    int row = staffTable.getSelectedRow();
                    if (row >= 0) {
                        String email = (String) tableModel.getValueAt(row, 2);
                        editStaff(email);
                    }
                },
                () -> {
                    int row = staffTable.getSelectedRow();
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
            instance = new ManageStaffWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
