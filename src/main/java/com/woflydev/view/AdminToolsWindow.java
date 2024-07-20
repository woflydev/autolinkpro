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
        setLayout(new BorderLayout());

        // Main panel for buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Admin Tools", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(headingLabel, gbc);

        // Create buttons
        manageCustomersButton = new JButton("Manage Customers");
        manageStaffButton = new JButton("Manage Staff");
        manageCarsButton = new JButton("Manage Cars");

        manageCustomersButton.setPreferredSize(new Dimension(200, 40));
        manageStaffButton.setPreferredSize(new Dimension(200, 40));
        manageCarsButton.setPreferredSize(new Dimension(200, 40));

        manageCustomersButton.setBackground(new Color(0, 120, 215));
        manageStaffButton.setBackground(new Color(0, 120, 215));
        manageCarsButton.setBackground(new Color(0, 120, 215));

        manageCustomersButton.setForeground(Color.WHITE);
        manageStaffButton.setForeground(Color.WHITE);
        manageCarsButton.setForeground(Color.WHITE);

        manageCustomersButton.setFocusPainted(false);
        manageStaffButton.setFocusPainted(false);
        manageCarsButton.setFocusPainted(false);

        manageCustomersButton.addActionListener(this);
        manageStaffButton.addActionListener(this);
        manageCarsButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(manageCustomersButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(manageStaffButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(manageCarsButton, gbc);

        // Add panels
        add(mainPanel, BorderLayout.CENTER);

        setSize(400, 350);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
