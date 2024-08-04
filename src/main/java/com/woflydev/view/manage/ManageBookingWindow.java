package com.woflydev.view.manage;

import com.woflydev.controller.BookingUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.UserUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.obj.Booking;
import com.woflydev.model.Globals;
import com.woflydev.view.util.table.CustomJTable;
import com.woflydev.view.util.table.button.ButtonEditor;
import com.woflydev.view.util.table.button.ButtonRenderer;
import com.woflydev.view.util.table.DateTimeCellRenderer;
import com.woflydev.view.util.table.NonEditableTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageBookingWindow extends JFrame implements ActionListener {
    public static ManageBookingWindow instance = null;

    private CustomJTable bookingTable;
    private NonEditableTableModel tableModel;
    private TableRowSorter<NonEditableTableModel> rowSorter;

    private String[] columnNames = {"ID", "Car Make", "Car Model", "Driver", "Start Date", "End Date", "Actions"};

    public ManageBookingWindow() {
        setTitle("Manage Bookings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(900, 500);
        setLocationRelativeTo(null);

        FileUtils.initializeSystem();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Manage Bookings", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        tableModel = new NonEditableTableModel(columnNames, 0);
        bookingTable = new CustomJTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        bookingTable.setRowSorter(rowSorter);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        updateTable();

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // intellij keeps yelling at me if i remove this
    }

    // this method is called every table refresh, to populate cells
    public void updateTable() {
        List<Booking> bookings = BookingUtils.getBookingList();
        Object[][] data;

        if (UserUtils.hasPrivilege(Globals.CURRENT_USER_EMAIL, Globals.PRIVILEGE_STAFF)) {
            data = new Object[bookings.size()][7];
            for (int i = 0; i < bookings.size(); i++) {
                populateCell(bookings, data, i);
                data[i][6] = "Edit/Delete";
            }
        } else {
            List<Booking> customerBookings = bookings.stream()
                    .filter(booking -> booking.getCustomerEmail().equals(Globals.CURRENT_USER_EMAIL))
                    .toList();

            data = new Object[customerBookings.size()][7];
            for (int i = 0; i < customerBookings.size(); i++) {
                populateCell(customerBookings, data, i);
                data[i][6] = "";
            }
        }

        if (tableModel != null) {
            tableModel.setDataVector(data, columnNames);

            bookingTable.setRowHeight(75);
            bookingTable.getColumn("Start Date").setCellRenderer(new DateTimeCellRenderer());
            bookingTable.getColumn("End Date").setCellRenderer(new DateTimeCellRenderer());
            bookingTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Edit", "Delete"}));
            bookingTable.getColumn("Actions").setCellEditor(
                    new ButtonEditor(new JCheckBox(), new String[]{"Edit", "Delete"}, new Runnable[]{
                            () -> {
                                int row = bookingTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                EditBookingWindow.open(id);
                                updateTable();
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

    // this method fills data for individual cells
    private void populateCell(List<Booking> bookings, Object[][] data, int i) {
        Booking booking = bookings.get(i);
        data[i][0] = booking.getId();
        data[i][1] = booking.getCar().getMake();
        data[i][2] = booking.getCar().getModel();
        data[i][3] = booking.getDriverFullName();
        data[i][4] = booking.getStart();
        data[i][5] = booking.getEnd();
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageBookingWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
