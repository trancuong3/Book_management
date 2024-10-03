package org.example.Swing;

import org.example.DAO.BookDAO;
import org.example.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BookManagerGUI extends JFrame {
    private BookDAO bookDAO = new BookDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtYear;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JTextField txtSearchId;
    private JButton btnSearch;
    private JButton btnResetSearch;

    public BookManagerGUI() {
        setTitle("Book Management");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(panel);

        // Tùy chỉnh Header của Bảng
        String[] columnNames = {"ID", "Name", "Title", "Author", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Tùy chỉnh Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setReorderingAllowed(false);

        // Tùy chỉnh Sắp Xếp và Tìm Kiếm
        table.setAutoCreateRowSorter(true);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Thêm màu nền xen kẽ cho các hàng
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color EVEN_ROW_COLOR = new Color(240, 240, 240);
            private final Color ODD_ROW_COLOR = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(EVEN_ROW_COLOR);
                    } else {
                        c.setBackground(ODD_ROW_COLOR);
                    }
                } else {
                    c.setBackground(new Color(100, 149, 237)); // Màu xanh khi chọn
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel Tìm Kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel lblSearchId = new JLabel("Search by ID:");
        lblSearchId.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearchId = new JTextField(10);
        txtSearchId.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        btnResetSearch = new JButton("Reset");
        btnResetSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(lblSearchId);
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Panel Nhập Liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        panel.add(inputPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblName, gbc);

        txtName = new JTextField(15);
        txtName.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(txtName, gbc);

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblTitle, gbc);

        txtTitle = new JTextField(15);
        txtTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(txtTitle, gbc);

        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblAuthor, gbc);

        txtAuthor = new JTextField(15);
        txtAuthor.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(txtAuthor, gbc);

        JLabel lblYear = new JLabel("Year:");
        lblYear.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblYear, gbc);

        txtYear = new JTextField(15);
        txtYear.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(txtYear, gbc);

        // Panel Nút Chức Năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBackground(new Color(60, 179, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        buttonPanel.add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 14));
        btnUpdate.setBackground(new Color(30, 144, 255));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        buttonPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        buttonPanel.add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Arial", Font.BOLD, 14));
        btnClear.setBackground(new Color(105, 105, 105));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        buttonPanel.add(btnClear);

        // Thêm Action Listener cho các nút
        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchBookById());
        btnResetSearch.addActionListener(e -> resetSearch());

        // Thêm Listener cho bảng khi click vào hàng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    // Đảm bảo lấy chỉ số hàng đúng khi có sorter
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    loadBookDetails(modelRow);
                }
            }
        });

        // Tải dữ liệu vào bảng
        refreshTable();
    }

    private void loadBookDetails(int selectedRow) {
        txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtTitle.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtAuthor.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtYear.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private void addBook() {
        try {
            String name = txtName.getText().trim();
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());

            if (name.isEmpty() || title.isEmpty() || author.isEmpty()) {
                showWarning("Please fill in all fields.");
                return;
            }

            Book book = new Book(name, title, author, year);
            bookDAO.addBook(book);
            refreshTable();
            clearFields();
            showSuccess("Book added successfully!");
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for Year.");
        }
    }

    private void updateBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a book to update.");
            return;
        }

        try {
            // Đảm bảo lấy chỉ số hàng đúng khi có sorter
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            String name = txtName.getText().trim();
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            int year = Integer.parseInt(txtYear.getText().trim());

            if (name.isEmpty() || title.isEmpty() || author.isEmpty()) {
                showWarning("Please fill in all fields.");
                return;
            }

            Book updatedBook = new Book(id, name, title, author, year);
            bookDAO.updateBook(updatedBook);
            refreshTable();
            clearFields();
            showSuccess("Book updated successfully!");
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers for Year.");
        }
    }

    private void deleteBook() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                showWarning("Please select a book to delete.");
                return;
            }

            // Đảm bảo lấy chỉ số hàng đúng khi có sorter
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0);
            bookDAO.deleteBook(id);
            refreshTable();
            clearFields();
            showSuccess("Book deleted successfully!");
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtYear.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            Object[] row = {book.getId(), book.getName(), book.getTitle(), book.getAuthor(), book.getYear()};
            tableModel.addRow(row);
        }
    }

    private void searchBookById() {
        String searchIdText = txtSearchId.getText().trim();
        if (searchIdText.isEmpty()) {
            showWarning("Please enter an ID to search.");
            return;
        }
        try {
            int searchId = Integer.parseInt(searchIdText);
            Book foundBook = bookDAO.getBookById(searchId);
            if (foundBook != null) {
                highlightBookInTable(searchId);
            } else {
                showInfo("No book found with the provided ID.");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for ID.");
        }
    }

    private void highlightBookInTable(int searchId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int) tableModel.getValueAt(i, 0) == searchId) {
                // Đảm bảo lấy chỉ số hàng đúng khi có sorter
                int viewRow = table.convertRowIndexToView(i);
                table.setRowSelectionInterval(viewRow, viewRow);
                table.scrollRectToVisible(table.getCellRect(viewRow, 0, true));
                break;
            }
        }
    }

    private void resetSearch() {
        txtSearchId.setText("");
        refreshTable();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
