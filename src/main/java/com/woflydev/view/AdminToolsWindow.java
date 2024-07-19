package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminToolsWindow extends JFrame implements ActionListener {
    public static AdminToolsWindow instance = null;

    private JButton manageCustomersButton;
    private JButton manageStaffButton;
    private JButton manageCarsButton;

    public AdminToolsWindow() {
        setTitle("Admin Tools");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        manageCustomersButton = new JButton("Manage Customers");
        manageStaffButton = new JButton("Manage Staff");
        manageCarsButton = new JButton("Manage Cars");

        manageCustomersButton.addActionListener(this);
        manageStaffButton.addActionListener(this);
        manageCarsButton.addActionListener(this);

        add(manageCustomersButton);
        add(manageStaffButton);
        add(manageCarsButton);

        setSize(300, 200);

        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button click events
        if (e.getSource() == manageCustomersButton) {
            System.out.println("Manage Customers button clicked");
            // Code to handle manage customers
        } else if (e.getSource() == manageStaffButton) {
            System.out.println("Manage Staff button clicked");
            // Code to handle manage staff
        } else if (e.getSource() == manageCarsButton) {
            System.out.println("Manage Cars button clicked");
            // Code to handle manage cars
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new AdminToolsWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
