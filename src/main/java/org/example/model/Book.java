package org.example.model;

public class Book {
    private int id;
    private String name;
    private String title;
    private String author;
    private int year;

    public Book(String name, String title, String author, int year) {
        this.name = name;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book(int id, String name, String title, String author, int year) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.author = author;
        this.year = year;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", name=" + name + ", title=" + title + ", author=" + author + ", year=" + year + "]";
    }
}
