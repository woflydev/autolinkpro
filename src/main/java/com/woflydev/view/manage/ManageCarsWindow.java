package com.woflydev.view.manage;

import com.woflydev.controller.CarUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.StyleUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.obj.Car;
import com.woflydev.view.util.table.CustomJTable;
import com.woflydev.view.util.table.button.ButtonEditor;
import com.woflydev.view.util.table.button.ButtonRenderer;
import com.woflydev.view.util.table.NonEditableTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.PRIMARY;

/**
 * This class lists all current cars in an interactive table.
 * @author woflydev
 */
public class ManageCarsWindow extends JFrame implements ActionListener {
    public static ManageCarsWindow instance = null;

    private JButton addCarButton;
    private CustomJTable carTable;
    private NonEditableTableModel tableModel;

    public ManageCarsWindow() {
        setTitle("Manage Cars");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        FileUtils.initializeSystem();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel headingLabel = new JLabel("Manage Cars", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        addCarButton = new JButton("Add New Car");
        addCarButton.setPreferredSize(new Dimension(150, 40));
        addCarButton.setFocusPainted(false);
        addCarButton.addActionListener(this);
        StyleUtils.applyButtonStyle(addCarButton, PRIMARY);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        mainPanel.add(addCarButton, gbc);

        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        tableModel = new NonEditableTableModel(columnNames, 0);
        carTable = new CustomJTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(carTable);
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

    private void editCar(String carId) {
        Car car = CarUtils.getCarById(carId);
        if (car == null) {
            WindowUtils.errorBox("Car not found.");
            return;
        }

        JTextField makeField = new JTextField(car.getMake(), 20);
        JTextField modelField = new JTextField(car.getModel(), 20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Make:"));
        panel.add(makeField);
        panel.add(Box.createHorizontalStrut(15)); // Spacer
        panel.add(new JLabel("Model:"));
        panel.add(modelField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Edit car details", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String make = makeField.getText();
            String model = modelField.getText();

            if (!make.isEmpty() && !model.isEmpty()) {
                car.setMake(make);
                car.setModel(model);
                CarUtils.updateCar(car);
                updateTable();
            } else {
                JOptionPane.showMessageDialog(null, "Make and Model cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTable() {
        List<Car> cars = CarUtils.getCarList();
        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        Object[][] data = new Object[cars.size()][4];

        if (cars != null) {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                data[i][0] = car.getId();
                data[i][1] = car.getMake();
                data[i][2] = car.getModel();
                data[i][3] = "Edit/Delete";
            }
        }

        if (tableModel != null) {
            tableModel.setDataVector(data, columnNames);

            carTable.setRowHeight(75);
            carTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(new String[]{"Edit", "Delete"}));
            carTable.getColumn("Actions").setCellEditor(
                    new ButtonEditor(new JCheckBox(), new String[]{"Edit", "Delete"}, new Runnable[]{
                            () -> {
                                int row = carTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                editCar(id);
                            },
                            () -> {
                                int row = carTable.getSelectedRow();
                                String id = (String) tableModel.getValueAt(row, 0);
                                CarUtils.deleteCar(id);
                                updateTable();
                            }
                    }));
        }
    }

    public static void open() {
        if (instance == null) {
            instance = new ManageCarsWindow();
            WindowUtils.register(instance);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
