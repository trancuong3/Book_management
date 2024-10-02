package org.example.DAO;

import org.example.model.Book;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/book_management";
    private static final String USER = "root";
    private static final String PASSWORD = "trancuong365421";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books(name, title, author, year) VALUES(?,?,?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getYear());
            pstmt.executeUpdate();
        } catch (SQLException e) {

            System.out.println("Error adding book: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error adding book: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE books SET name = ?, title = ?, author = ?, year = ? WHERE id = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getYear());
            pstmt.setInt(5, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {

            System.out.println("Error updating book: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error updating book: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            updateBookIdsAfterDeletion(id);
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error deleting book: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"), rs.getString("name"), rs.getString("title"), rs.getString("author"), rs.getInt("year"));
            }
        } catch (SQLException e) {

            System.out.println("Error retrieving book: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error retrieving book: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("title"), rs.getString("author"), rs.getInt("year")));
            }
        } catch (SQLException e) {

            System.out.println("Error retrieving books: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error retrieving books: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return books;
    }

    private void updateBookIdsAfterDeletion(int deletedId) {
        String sql = "UPDATE books SET id = id - 1 WHERE id > ?";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deletedId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating book IDs: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error updating book IDs: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
