package com.irfanandarafifsatrio.myquote.vm;

import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.irfanandarafifsatrio.myquote.databinding.ItemQuoteBinding;
import com.irfanandarafifsatrio.myquote.model.Quote;
import com.irfanandarafifsatrio.myquote.utils.StringHelper;

/**
 * Created by irfanandarafifsatrio on 12/14/16.
 *
 */

public class QuoteItemVM extends RecyclerView.ViewHolder  {

    public ObservableField<String> bText = new ObservableField<>("");
    public ObservableField<String> bDate = new ObservableField<>("");

    public QuoteItemVM(ItemQuoteBinding binding, Quote data) {
        super(binding.getRoot());
        bText.set(" \" "+data.getText()+" \" ");
        bDate.set(StringHelper.getDate(data.getDate()));
    }
}
