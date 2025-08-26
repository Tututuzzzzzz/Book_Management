package com.library.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

import com.library.model.Book;

public class Library implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Book> books = new ArrayList<>();

    public synchronized boolean addBook(Book book) {
        for (Book b : books){
            if (b.getId().equals(book.getId())) {
                return false;
            }
        }
        books.add(book);
        return true;
    }

    public synchronized List<Book> searchByTitle(String keyword){
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public synchronized Map<String, List<Book>> getBooksByCategory() {
        Map<String, List<Book>> bookMap = new HashMap<>();
        for (Book b : books) {
            bookMap.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }
        return bookMap;
    }

    public synchronized boolean updateBook(Book updated) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(updated.getId())) {
                books.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteBook(String id) {
        return books.removeIf(b -> b.getId().equals(id));
    }


    public synchronized void saveToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(books));
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadFromFile(File file) throws IOException, ClassNotFoundException {
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                books = (List<Book>) obj;
            }
        }
    }
}

