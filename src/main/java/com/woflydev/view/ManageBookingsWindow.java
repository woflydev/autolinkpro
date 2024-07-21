package com.woflydev.view;

import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Booking;
import com.woflydev.model.Globals;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ManageBookingsWindow extends JFrame implements ActionListener {
    public static ManageBookingsWindow instance = null;

    private JButton addBookingButton;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public ManageBookingsWindow() {
        setTitle("Manage Bookings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        FileUtils.initializeSystem();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Manage Bookings", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        addBookingButton = new JButton("Add New Booking");
        addBookingButton.setPreferredSize(new Dimension(150, 40));
        addBookingButton.setBackground(new Color(0, 120, 215));
        addBookingButton.setForeground(Color.WHITE);
        addBookingButton.setFocusPainted(false);
        addBookingButton.addActionListener(this);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(addBookingButton, gbc);

        String[] columnNames = {"ID", "Car Make", "Car Model", "Customer", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookingTable = new JTable(tableModel);
        bookingTable.setCellSelectionEnabled(true);
        rowSorter = new TableRowSorter<>(tableModel);
        bookingTable.setRowSorter(rowSorter);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        updateTable();

        add(mainPanel, BorderLayout.CENTER);

        setSize(700, 400);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBookingButton) {
            addNewBooking();
        }
    }

    private void addNewBooking() {
        // Add your logic to handle adding a new booking here
        // For example, open a dialog to enter booking details
    }

    private void updateTable() {
        List<Booking> bookings = BookingUtils.getBookingList();
        String[] columnNames = {"ID", "Car Make", "Car Model", "Customer", "Actions"};
        Object[][] data;

        if (UserUtils.hasPrivilege(Globals.CURRENT_USER_EMAIL, Globals.PRIVILEGE_STAFF)) {
            data = new Object[bookings.size()][5];
            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                data[i][0] = booking.getId();
                data[i][1] = booking.getCar().getMake();
                data[i][2] = booking.getCar().getModel();
                data[i][3] = booking.getCustomerEmail();
                data[i][4] = Globals.CURRENT_PRIVILEGE <= Globals.PRIVILEGE_STAFF ? "Edit/Delete" : "";
            }
        } else {
            List<Booking> customerBookings = bookings.stream()
                    .filter(booking -> booking.getCustomerEmail().equals(Globals.CURRENT_USER_EMAIL))
                    .toList();

            data = new Object[customerBookings.size()][5];
            for (int i = 0; i < customerBookings.size(); i++) {
                Booking booking = customerBookings.get(i);
                data[i][0] = booking.getId();
                data[i][1] = booking.getCar().getMake();
                data[i][2] = booking.getCar().getModel();
                data[i][3] = booking.getCustomerEmail();
                data[i][4] = ""; // No actions for customers
            }
        }

        if (tableModel != null) {
            tableModel.setDataVector(data, columnNames);

            bookingTable.setRowHeight(75);
            bookingTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Edit", "Delete"}));
            bookingTable.getColumn("Actions").setCellEditor(
                    new ButtonEditor(new JCheckBox(), new String[]{"Edit", "Delete"}, new Runnable[]{
                            () -> {
                                int row = bookingTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                editBooking(id);
                            },
                            () -> {
                                int row = bookingTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                BookingUtils.deleteBooking(id);
                                updateTable();
                            }
                    }));
        }
    }

    private void editBooking(String bookingId) {
        // Add your logic to handle editing a booking here
        // For example, open a dialog to edit booking details
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageBookingsWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
