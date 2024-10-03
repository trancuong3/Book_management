package org.example.Swing;

import org.example.DAO.BookDAO;
import org.example.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        String[] columnNames = {"ID", "Name", "Title", "Author", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel lblSearchId = new JLabel("Search by ID:");
        txtSearchId = new JTextField(10);
        btnSearch = new JButton("Search");
        btnResetSearch = new JButton("Reset");
        searchPanel.add(lblSearchId);
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        panel.add(inputPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(lblName, gbc);

        txtName = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(txtName, gbc);

        JLabel lblTitle = new JLabel("Title:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(lblTitle, gbc);

        txtTitle = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(txtTitle, gbc);

        JLabel lblAuthor = new JLabel("Author:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(lblAuthor, gbc);

        txtAuthor = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(txtAuthor, gbc);

        JLabel lblYear = new JLabel("Year:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(lblYear, gbc);

        txtYear = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        inputPanel.add(txtYear, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd = new JButton("Add");
        buttonPanel.add(btnAdd);
        btnAdd.setBackground(new Color(60, 179, 113));

        btnUpdate = new JButton("Update");
        buttonPanel.add(btnUpdate);
        btnUpdate.setBackground(new Color(30, 144, 255));
        btnDelete = new JButton("Delete");
        buttonPanel.add(btnDelete);
        btnDelete.setBackground(new Color(220, 20, 60));
        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);
        btnClear.setBackground(new Color(105, 105, 105));

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchBookById());
        btnResetSearch.addActionListener(e -> resetSearch());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    loadBookDetails(selectedRow);
                }
            }
        });

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
            int id = (int) tableModel.getValueAt(selectedRow, 0);
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

            int id = (int) tableModel.getValueAt(selectedRow, 0);
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
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
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
