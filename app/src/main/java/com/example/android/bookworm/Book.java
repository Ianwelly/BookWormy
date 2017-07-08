package com.example.android.bookworm;

/**
 * Created by Kezia on 05/07/2017.
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mUrl;

    public Book(String title, String author, String url) {
        mTitle = title;
        mAuthor = author;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getUrl(){
        return mUrl;
    }
}
