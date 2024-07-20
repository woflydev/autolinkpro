package com.woflydev.view;

import com.woflydev.controller.UserUtils;
import com.woflydev.model.Globals;
import com.woflydev.model.Staff;
import com.woflydev.model.User;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageStaffWindow extends JFrame implements ActionListener {
    public static ManageStaffWindow instance = null;

    private JButton addStaffButton;
    private JTable staffTable;
    private DefaultTableModel tableModel;

    public ManageStaffWindow() {
        setTitle("Manage Staff");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addStaffButton = new JButton("Add New Staff");
        addStaffButton.addActionListener(this);
        buttonPanel.add(addStaffButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Initialize tableModel before setting up the table
        String[] columnNames = {"First Name", "Last Name", "Email", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        staffTable = new JTable(tableModel);
        staffTable.setCellSelectionEnabled(true);
        staffTable.setAutoCreateRowSorter(true);

        // Add the table to the scroll pane
        JScrollPane scrollPane = new JScrollPane(staffTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set initial table data
        updateTable();

        setSize(700, 400);
        setLocationRelativeTo(null);
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

        JPanel panel = new JPanel();
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(Box.createHorizontalStrut(15)); // Spacer
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(Box.createHorizontalStrut(15)); // Spacer
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(Box.createHorizontalStrut(15)); // Spacer
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter new staff details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                Staff newStaff = new Staff(firstName, lastName, email, password);
                UserUtils.addUser(newStaff);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<User> users = UserUtils.getUsers();
        String[] columnNames = {"First Name", "Last Name", "Email", "Actions"};
        Object[][] data = users.stream()
                .filter(user -> user.getPrivilege() == Globals.PRIVILEGE_STAFF)
                .map(user -> new Object[]{
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        "Delete" // Action button text
                })
                .toArray(Object[][]::new);

        tableModel.setDataVector(data, columnNames);

        // Reapply renderer and editor
        staffTable.getColumn("Actions").setCellRenderer(new ButtonRenderer("Delete"));
        staffTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", () -> {
            int row = staffTable.getSelectedRow();
            if (row >= 0) {
                String email = (String) tableModel.getValueAt(row, 2);
                UserUtils.deleteUser(email);
                updateTable();
            }
        }));
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageStaffWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
