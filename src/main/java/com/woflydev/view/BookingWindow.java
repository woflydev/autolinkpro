package com.woflydev.view;

import com.woflydev.controller.CarUtils;
import com.woflydev.controller.FileUtils;
import com.woflydev.controller.WindowUtils;
import com.woflydev.model.Car;
import com.woflydev.view.table.ButtonEditor;
import com.woflydev.view.table.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookingWindow extends JFrame implements ActionListener {
    public static BookingWindow instance = null;

    private JTable carTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public BookingWindow() {
        setTitle("New Booking");
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

        JLabel headingLabel = new JLabel("Choose a Car", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(new Color(0, 120, 215)); // Blue color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(headingLabel, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(150, 25));
        searchField.addActionListener(e -> filterTable());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        filterCombo = new JComboBox<>(new String[]{"Make", "Model"});
        filterCombo.addActionListener(e -> filterTable());
        searchPanel.add(filterCombo);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        mainPanel.add(searchPanel, gbc);

        String[] columnNames = {"ID", "Make", "Model", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        carTable = new JTable(tableModel);
        carTable.setCellSelectionEnabled(true);
        rowSorter = new TableRowSorter<>(tableModel);
        carTable.setRowSorter(rowSorter);
        JScrollPane scrollPane = new JScrollPane(carTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        updateTable();

        add(mainPanel, BorderLayout.CENTER);

        setSize(700, 500);
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

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedFilter = (String) filterCombo.getSelectedItem();

        RowFilter<DefaultTableModel, Object> searchFilter = RowFilter.regexFilter("(?i)" + searchText, 1, 2);
        RowFilter<DefaultTableModel, Object> filter;

        if ("Make".equals(selectedFilter)) {
            filter = RowFilter.regexFilter("(?i)" + searchText, 1);
        } else if ("Model".equals(selectedFilter)) {
            filter = RowFilter.regexFilter("(?i)" + searchText, 2);
        } else {
            filter = RowFilter.regexFilter(".*", 1, 2);
        }

        RowFilter<DefaultTableModel, Object> combinedFilter = RowFilter.andFilter(List.of(searchFilter, filter));
        rowSorter.setRowFilter(combinedFilter);
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
