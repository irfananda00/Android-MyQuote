package com.irfanandarafifsatrio.myquote.model;

/**
 * Created by irfanandarafifsatrio on 12/14/16.
 */

public class Quote {
    private String text = "";
    private long date = 0;

    public Quote(String text, long date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
