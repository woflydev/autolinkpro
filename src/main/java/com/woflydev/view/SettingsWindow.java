package com.woflydev.view;

import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.PRIMARY;

/**
 * Current only allows for changing of the logged-in user's password.
 * @author woflydev
 */
public class SettingsWindow extends JFrame implements ActionListener {
    public static SettingsWindow instance = null;

    private JButton changePasswordButton;
    private JButton notificationSettingsButton;
    private JButton backButton;

    public SettingsWindow() {
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Settings", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        changePasswordButton = new JButton("Change Password");
        notificationSettingsButton = new JButton("Notification Settings");
        backButton = new JButton("Back");
        StyleUtils.applyButtonStyle(backButton, PRIMARY);

        changePasswordButton.setPreferredSize(new Dimension(200, 40));
        notificationSettingsButton.setPreferredSize(new Dimension(200, 40));
        backButton.setPreferredSize(new Dimension(200, 40));

        changePasswordButton.setFocusPainted(false);
        notificationSettingsButton.setFocusPainted(false);
        backButton.setFocusPainted(false);

        changePasswordButton.addActionListener(this);
        notificationSettingsButton.addActionListener(this);
        backButton.addActionListener(this);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(changePasswordButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        //mainPanel.add(notificationSettingsButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        footerPanel.setLayout(new BorderLayout());

        backButton.setPreferredSize(new Dimension(100, 30));
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        backPanel.setOpaque(false);
        backPanel.add(backButton);

        footerPanel.add(backPanel, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changePasswordButton) {
            changePasswordDialog();
        } else if (e.getSource() == notificationSettingsButton) {
            // rip notifications
        } else if (e.getSource() == backButton) {
            HomeWindow.open();
            dispose();
        }
    }

    private void changePasswordDialog() {
        JDialog changePasswordDialog = new JDialog(this, "Change Password", true);
        changePasswordDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        StyleUtils.applyButtonStyle(confirmButton, PRIMARY);
        confirmButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            if (UserUtils.authenticate(Globals.CURRENT_USER_EMAIL, oldPassword)) {
                if (newPassword.length() >= 6) {
                    UserUtils.updatePassword(Globals.CURRENT_USER_EMAIL, newPassword);
                    WindowUtils.infoBox("Password changed successfully!");
                    changePasswordDialog.dispose();
                } else WindowUtils.errorBox("New password must be at least 6 characters long.");
            } else WindowUtils.errorBox("Old password is incorrect.");
        });

        cancelButton.addActionListener(e -> changePasswordDialog.dispose());

        gbc.gridx = 0;
        gbc.gridy = 0;
        changePasswordDialog.add(oldPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        changePasswordDialog.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        changePasswordDialog.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        changePasswordDialog.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        changePasswordDialog.add(buttonPanel, gbc);

        InputMap inputMap = changePasswordDialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = changePasswordDialog.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "CONFIRM");
        actionMap.put("CONFIRM", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmButton.doClick();
            }
        });

        changePasswordDialog.pack();
        changePasswordDialog.setLocationRelativeTo(this);
        changePasswordDialog.setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }

    public static void open() {
        if (instance == null) {
            instance = new SettingsWindow();
            WindowUtils.register(instance);
        }
    }
}
