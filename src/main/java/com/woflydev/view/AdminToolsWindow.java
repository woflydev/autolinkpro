package com.woflydev.view;

import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;
import com.woflydev.view.manage.ManageCarsWindow;
import com.woflydev.view.manage.ManageCustomersWindow;
import com.woflydev.view.manage.ManageStaffWindow;

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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Admin Tools", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(headingLabel, gbc);

        manageCustomersButton = new JButton("Manage Customers");
        manageStaffButton = new JButton("Manage Staff");
        manageCarsButton = new JButton("Manage Cars");

        manageCustomersButton.setPreferredSize(new Dimension(200, 40));
        manageStaffButton.setPreferredSize(new Dimension(200, 40));
        manageCarsButton.setPreferredSize(new Dimension(200, 40));

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

        if (UserUtils.hasPrivilege(Globals.CURRENT_USER_EMAIL, Globals.PRIVILEGE_OWNER))
            mainPanel.add(manageStaffButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        if (UserUtils.hasPrivilege(Globals.CURRENT_USER_EMAIL, Globals.PRIVILEGE_OWNER))
            mainPanel.add(manageCarsButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        setSize(400, 350);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageCustomersButton) {
            ManageCustomersWindow.open();
        } else if (e.getSource() == manageStaffButton) {
            ManageStaffWindow.open();
        } else if (e.getSource() == manageCarsButton) {
            ManageCarsWindow.open();
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new AdminToolsWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
