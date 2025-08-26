package com.library.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String category;
    private double price;

    public Book(String id, String title, String author, String category, double price){
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
    }

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getCategory(){
        return category;
    }
    public double getPrice(){
        return price;
    }

    @Override
    public String toString() {
    return "Book{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", category='" + category + '\'' +
        ", price=" + price +
        '}';
    }
}
