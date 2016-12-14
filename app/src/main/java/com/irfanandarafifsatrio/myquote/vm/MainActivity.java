package com.irfanandarafifsatrio.myquote.vm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.irfanandarafifsatrio.myquote.base.BaseActivity;
import com.irfanandarafifsatrio.myquote.R;
import com.irfanandarafifsatrio.myquote.adapter.QuoteAdapter;
import com.irfanandarafifsatrio.myquote.utils.NotificationHelper;
import com.irfanandarafifsatrio.myquote.utils.SimpleItemTouchHelperCallback;
import com.irfanandarafifsatrio.myquote.databinding.ActivityMainBinding;
import com.irfanandarafifsatrio.myquote.model.Quote;
import com.irfanandarafifsatrio.myquote.preferences.PrefQuote;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    public QuoteAdapter bAdapter;
    public LinearLayoutManager bLayoutManager;
    private List<Quote> mList = new ArrayList<>();
    private int nextPage = 1;

    private int editPos = -1;

    public ObservableField<Boolean> bIsInput = new ObservableField<>(false);
    public ObservableField<Boolean> bIsDataExist = new ObservableField<>(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(this);

        bLayoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(bLayoutManager);
        bAdapter = new QuoteAdapter(this, mList,binding.rv);
        binding.rv.setAdapter(bAdapter);

        ItemTouchHelper.Callback callbackp =
                new SimpleItemTouchHelperCallback(bAdapter,this);
        ItemTouchHelper touchHelperp = new ItemTouchHelper(callbackp);
        touchHelperp.attachToRecyclerView(binding.rv);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rv.getContext(),
                bLayoutManager.getOrientation());
        binding.rv.addItemDecoration(dividerItemDecoration);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_main);

//        bAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (nextPage!=0) {
//                    bAdapter.showLoading();
//                    nextPage++;
//                    loadData(nextPage);
//                }else{
//                    bAdapter.isLoadMoreAvailable = false;
//                }
//            }
//        });

        loadData(nextPage);
    }

    @Override
    public void onBackPressed() {
        if (bIsInput.get()){
            bIsInput.set(false);
            editPos = -1;
            binding.bQuote.setText("");
        }else {
            super.onBackPressed();
        }
    }

    private void loadData(int page) {
        if (PrefQuote.load(this)!=null&&PrefQuote.load(this).getQuoteList().size()!=0) {
            bIsDataExist.set(true);
            mList.addAll(PrefQuote.load(this).getQuoteList());
        }
        nextPage = 0;
    }

    public void onClickShowForm(View view){
        openKeyboard();
    }

    private void openKeyboard() {
        bIsInput.set(true);
        binding.bQuote.requestFocus();
        binding.bQuote.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(binding.bQuote, 0);
            }
        },200);
    }

    private void closeKeyboard() {
        bIsInput.set(false);
        binding.bQuote.requestFocus();
        binding.bQuote.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(binding.bQuote.getWindowToken(), 0);
            }
        },200);
    }

    public void onClickAdd(View view){
        if (!TextUtils.isEmpty(binding.bQuote.getText().toString())) {
            if (editPos==-1) {
                PrefQuote.addQuote(this, binding.bQuote.getText().toString());
                mList.add(new Quote(binding.bQuote.getText().toString(),System.currentTimeMillis()/1000));
                bAdapter.notifyItemInserted(mList.size() - 1);
            }else{
                PrefQuote.edituote(this,editPos, binding.bQuote.getText().toString());
                mList.set(editPos,new Quote(binding.bQuote.getText().toString(),System.currentTimeMillis()/1000));
                bAdapter.notifyItemChanged(editPos);
            }
            bIsDataExist.set(true);
        }
        editPos = -1;
        binding.bQuote.setText("");
        closeKeyboard();

        NotificationHelper.setAlarm(this);

//        //for test pushnotif
//        String quote = PrefQuote.getRandomQuote(this);
//        if (quote!=null)
//            NotificationHelper.sendNotification(this,quote);
    }

    public void edit(String text, int pos){
        editPos = pos;
        binding.bQuote.setText(text);
        openKeyboard();
    }

    public void isDataExist() {
        if (PrefQuote.load(this)!=null&&PrefQuote.load(this).getQuoteList().size()!=0) {
            bIsDataExist.set(true);
        }else{
            bIsDataExist.set(false);
        }
    }
}
