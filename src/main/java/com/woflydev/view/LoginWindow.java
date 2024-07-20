package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.woflydev.controller.FileUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;

public class LoginWindow extends JFrame implements ActionListener {
    private static LoginWindow instance = null;

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn, registerBtn;

    public LoginWindow() {
        setTitle("AutoLinkPro - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel for login
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Add logo or title
        JLabel logoLabel = new JLabel("AutoLinkPro", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(logoLabel, gbc);

        // User email
        JLabel userLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        mainPanel.add(userLabel, gbc);

        userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        mainPanel.add(userField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(passLabel, gbc);

        passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(passField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 30));
        loginBtn.setBackground(new Color(0, 120, 215));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this);

        registerBtn = new JButton("Register");
        registerBtn.setPreferredSize(new Dimension(100, 30));
        registerBtn.setBackground(new Color(0, 120, 215));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(e -> RegisterWindow.open());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 250);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email = userField.getText();
            String password = new String(passField.getPassword());

            if (UserUtils.authenticate(email, password)) {
                Globals.CURRENT_USER_EMAIL = email;
                WindowUtils.infoBox("Login successful");
                HomeWindow.open();
                dispose();
            } else {
                WindowUtils.errorBox("Invalid email or password");
            }
        }
    }

    public static void open() {
        if (instance == null) {
            FileUtils.initializeSystem();
            instance = new LoginWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
