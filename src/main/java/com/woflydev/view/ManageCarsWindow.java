package com.woflydev.view;

import com.woflydev.controller.CarUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.model.Car;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

public class ManageCarsWindow extends JFrame implements ActionListener {
    public static ManageCarsWindow instance = null;

    private JButton addCarButton;
    private JTable carTable;
    private DefaultTableModel tableModel;

    public ManageCarsWindow() {
        setTitle("Manage Cars");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        FileUtils.initializeSystem();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addCarButton = new JButton("Add New Car");
        addCarButton.addActionListener(this);
        buttonPanel.add(addCarButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Initialize tableModel before setting up the table
        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        carTable = new JTable(tableModel);
        carTable.setCellSelectionEnabled(true);

        // Add the table to the scroll pane
        JScrollPane scrollPane = new JScrollPane(carTable);
        add(scrollPane, BorderLayout.CENTER);

        // Set initial table data
        updateTable();

        setSize(700, 400);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCarButton) {
            addNewCar();
        }
    }

    private void addNewCar() {
        JTextField makeField = new JTextField(20);
        JTextField modelField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Make:"));
        panel.add(makeField);
        panel.add(Box.createHorizontalStrut(15)); // Spacer
        panel.add(new JLabel("Model:"));
        panel.add(modelField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter new car details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String make = makeField.getText();
            String model = modelField.getText();

            if (!make.isEmpty() && !model.isEmpty()) {
                Car newCar = new Car(UUID.randomUUID().toString(), make, model);
                CarUtils.addCar(newCar);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "Make and Model cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<Car> cars = FileUtils.loadListFromDisk("cars.json", Car[].class);
        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        Object[][] data = cars != null ? new Object[cars.size()][4] : new Object[0][4];

        if (cars != null) {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                data[i][0] = car.getId();
                data[i][1] = car.getMake();
                data[i][2] = car.getModel();
                data[i][3] = "Delete"; // Action button text
            }
        }

        // Ensure tableModel is initialized before setting data
        if (tableModel != null) {
            tableModel.setDataVector(data, columnNames);

            // Reapply renderer and editor
            carTable.getColumn("Actions").setCellRenderer(new ButtonRenderer("Delete"));
            carTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", () -> {
                    int row = carTable.getSelectedRow();
                    String id = (String) tableModel.getValueAt(row, 0);
                    CarUtils.deleteCar(id);
                    updateTable();
            }));
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageCarsWindow();
            instance.setVisible(true);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
