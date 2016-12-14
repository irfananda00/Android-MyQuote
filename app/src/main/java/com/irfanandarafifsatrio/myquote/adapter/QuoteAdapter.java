package com.irfanandarafifsatrio.myquote.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfanandarafifsatrio.myquote.R;
import com.irfanandarafifsatrio.myquote.databinding.ItemQuoteBinding;
import com.irfanandarafifsatrio.myquote.databinding.RowFooterBottomBinding;
import com.irfanandarafifsatrio.myquote.model.ListQuote;
import com.irfanandarafifsatrio.myquote.model.Quote;
import com.irfanandarafifsatrio.myquote.preferences.PrefQuote;
import com.irfanandarafifsatrio.myquote.utils.ItemTouchHelperAdapter;
import com.irfanandarafifsatrio.myquote.utils.OnLoadMoreListener;
import com.irfanandarafifsatrio.myquote.vm.MainActivity;
import com.irfanandarafifsatrio.myquote.vm.QuoteItemVM;
import com.irfanandarafifsatrio.myquote.vm.RowFooterVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by irfanandarafifsatrio on 9/26/16.
 */

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.BindingHolder> implements ItemTouchHelperAdapter {

    public final static int VIEW_TYPE_ITEM = 0;
    public final static int VIEW_TYPE_LOADING = 1;
    private static final String TAG = "QuoteAdapter";
    private final Context context;
    private final RecyclerView rv;
    List<Quote> mList = new ArrayList<>();
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mOnLoadMoreListener;
    public boolean isLoadMoreAvailable = false;
    private boolean showloading;
    private ListQuote listQuote;
    private Quote deletedQuote;

    public QuoteAdapter(Context context, List<Quote> mList, RecyclerView mRecyclerView) {
        this.context = context;
        this.mList = mList;
        this.rv = mRecyclerView;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (isLoadMoreAvailable && mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void showLoading() {
        showloading = true;
        mList.add(null);
        notifyItemInserted(mList.size() - 1);
    }

    private void removeLoading() {
        if (showloading) {
            mList.remove(mList.size() - 1);
            notifyItemRemoved(mList.size());
            showloading = false;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        removeLoading();
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public QuoteAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            RowFooterBottomBinding binding = DataBindingUtil.inflate(LayoutInflater.
                    from(parent.getContext()), R.layout.row_footer_bottom, parent, false);
            return new FooterBindingHolder(binding);
        } else {
            ItemQuoteBinding binding = DataBindingUtil.inflate(LayoutInflater.
                    from(parent.getContext()), R.layout.item_quote, parent, false);
            return new RowBindingHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        if (holder instanceof FooterBindingHolder) {
            //footer
            ((FooterBindingHolder) holder).getBinding().setVm(new RowFooterVM());
        } else {
            //item
            ((RowBindingHolder) holder).getBinding().setVm(new QuoteItemVM((
                    (RowBindingHolder) holder).getBinding(), mList.get(position)));
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(final int position) {
        //delete from preference
        listQuote = PrefQuote.load(context);
        deletedQuote = listQuote.getQuoteList().get(position);
        listQuote.getQuoteList().remove(position);
        PrefQuote.save(listQuote, context);
        mList.remove(position);
        notifyItemRemoved(position);

        //show undo
        Snackbar snackbar = Snackbar
                .make(rv, "Quote Removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add again to preference
                        listQuote.getQuoteList().add(position, deletedQuote);
                        PrefQuote.save(listQuote, context);
                        mList.add(position, deletedQuote);
                        notifyItemInserted(position);
                        rv.scrollToPosition(position);
                    }
                });
        snackbar.show();

        ((MainActivity) context).isDataExist();
    }

    @Override
    public void onItemEdit(int position) {
        listQuote = PrefQuote.load(context);
        int pos = -1;
        String text = null;
        for (int i = 0; i < listQuote.getQuoteList().size(); i++) {
            if (listQuote.getQuoteList().get(i).getText().equals(mList.get(position).getText())) {
                pos = i;
                text = listQuote.getQuoteList().get(i).getText();
                break;
            }
        }
        if (pos != -1 && text != null)
            ((MainActivity) context).edit(text, pos);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }
    }

    public static class FooterBindingHolder extends BindingHolder {

        private RowFooterBottomBinding binding;

        public FooterBindingHolder(RowFooterBottomBinding binding) {
            super(binding);
            this.binding = binding;
        }

        public RowFooterBottomBinding getBinding() {
            return this.binding;
        }
    }

    public static class RowBindingHolder extends BindingHolder {

        private ItemQuoteBinding binding;

        public RowBindingHolder(ItemQuoteBinding binding) {
            super(binding);
            this.binding = binding;
        }

        public ItemQuoteBinding getBinding() {
            return this.binding;
        }
    }
}
