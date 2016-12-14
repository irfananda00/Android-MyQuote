package com.irfanandarafifsatrio.myquote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irfanandarafifsatrio on 12/14/16.
 *
 */

public class ListQuote {
    private List<Quote> quoteList = new ArrayList<>();

    public ListQuote(List<Quote> quoteList) {
        this.quoteList = quoteList;
    }

    public List<Quote> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<Quote> quoteList) {
        this.quoteList = quoteList;
    }
}
