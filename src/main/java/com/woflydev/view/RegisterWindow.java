package com.woflydev.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.User;
import com.woflydev.controller.hash.BCryptHash;
import com.woflydev.model.Globals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class RegisterWindow extends JFrame implements ActionListener {
    private static RegisterWindow instance = null;

    private JTextField firstNameField, lastNameField, emailField, licenseField;
    private JPasswordField passwordField;
    private JSpinner dobSpinner;
    private JButton registerBtn, backBtn;

    private List<User> users;

    private RegisterWindow() {
        setTitle("AutoLinkPro - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 120, 215));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel[] labels = {
                new JLabel("First Name:"), new JLabel("Last Name:"),
                new JLabel("Email:"), new JLabel("Password:"),
                new JLabel("License:"), new JLabel("Date of Birth:")
        };

        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        licenseField = new JTextField(20);

        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));

        JComponent[] components = {
                firstNameField, lastNameField, emailField,
                passwordField, licenseField, dobSpinner
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            mainPanel.add(labels[i], gbc);

            gbc.gridx = 1;
            mainPanel.add(components[i], gbc);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        registerBtn = new JButton("Register");
        registerBtn.setPreferredSize(new Dimension(120, 30));
        registerBtn.setBackground(new Color(0, 120, 215));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(this);

        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 30));
        backBtn.setBackground(new Color(0, 120, 215));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            LoginWindow.open();
            dispose();
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(450, 500);
        setLocationRelativeTo(null);

        FileUtils.initializeSystem();
        users = FileUtils.loadListFromDisk("users.json", User[].class);
        if (users == null) {
            users = new ArrayList<>();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String license = licenseField.getText();
            Date dob = (Date) dobSpinner.getValue();
            LocalDate dobLocal = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            String hashedPassword = BCryptHash.hashString(password);

            // Assuming CUSTOMER privilege for new users
            User newUser = new User(firstName, lastName, email, hashedPassword, Globals.PRIVILEGE_CUSTOMER);

            users.add(newUser);
            FileUtils.saveToDisk(users, "users.json");

            WindowUtils.infoBox("Registration successful");
            LoginWindow.open();
            dispose();
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new RegisterWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
