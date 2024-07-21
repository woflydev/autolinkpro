package com.woflydev.view;

import com.woflydev.controller.CarUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Car;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookingWindow extends JFrame implements ActionListener {
    public static BookingWindow instance = null;

    private JTable carTable;
    private DefaultTableModel tableModel;

    public BookingWindow() {
        setTitle("Create Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        FileUtils.initializeSystem();

        // Main panel for table
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Heading label
        JLabel headingLabel = new JLabel("Create Booking", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // Allow the heading to occupy some vertical space
        mainPanel.add(headingLabel, gbc);

        // Initialize tableModel before setting up the table
        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        carTable = new JTable(tableModel);
        carTable.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(carTable);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Allow the table to take up remaining vertical space
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        // Set initial table data
        updateTable();

        add(mainPanel, BorderLayout.CENTER);

        setSize(700, 400);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // java yells at me if i dont do this
    }

    private void bookCar(String carId) { BookingDetailsWindow.open(carId); }

    private void updateTable() {
        List<Car> cars = CarUtils.getCarList();
        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        Object[][] data = new Object[cars.size()][4];

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            data[i][0] = car.getId();
            data[i][1] = car.getMake();
            data[i][2] = car.getModel();
            data[i][3] = "Book";
        }

        if (tableModel != null) {
            tableModel.setDataVector(data, columnNames);

            carTable.setRowHeight(75);
            carTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Book"}));
            carTable.getColumn("Actions").setCellEditor(
                    new ButtonEditor(new JCheckBox(), new String[]{"Book"}, new Runnable[]{
                            () -> {
                                int row = carTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                bookCar(id);
                            }
                    }));
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new BookingWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
