package com.library.protocol;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int ADD_BOOK = 1;
    public static final int SEARCH_BY_TITLE = 2;
    public static final int GET_BOOKS_BY_CATEGORY = 3;
    public static final int UPDATE_BOOK = 4;
    public static final int DELETE_BOOK = 5;
    public static final int EXIT = 6;

    private final int type;
    private final Serializable data;
    private final String keyword;  

  
    private Request(int type, Serializable data, String keyword) {
        this.type = type;
        this.data = data;
        this.keyword = keyword;
    }

    
    public static Request addBook(Serializable book) { 
        return new Request(ADD_BOOK, book, null); 
    }
    public static Request search(String keyword) { 
        return new Request(SEARCH_BY_TITLE, null, keyword); 
    }
    public static Request byCategory() { 
        return new Request(GET_BOOKS_BY_CATEGORY, null, null); 
    }
    public static Request exitRequest() { 
        return new Request(EXIT, null, null); 
    }
    public static Request updateBook(Serializable book) {
        return new Request(UPDATE_BOOK, book, null);
    }
    public static Request deleteBook(String id) {
        return new Request(DELETE_BOOK, null, id); // reuse keyword to carry id
    }

    // Getter/Setter
    public int getType() {
        return type;
    }

    public Serializable getData() {
        return data;
    }

    public String getKeyword() {
        return keyword;
    }

}
