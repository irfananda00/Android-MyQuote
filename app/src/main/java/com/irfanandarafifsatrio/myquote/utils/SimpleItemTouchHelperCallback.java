package com.irfanandarafifsatrio.myquote.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.irfanandarafifsatrio.myquote.R;

/**
 * Created by irfanandarafifsatrio on 10/5/16.
 *
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private Context ctx;
    private Drawable backgroundR;
    private Drawable backgroundL;
    private Drawable xMarkR;
    private Drawable xMarkL;
    private int xMarkMargin;
    private boolean initiated;

    private void init() {
        backgroundR = new ColorDrawable(0xFFF44336);
        xMarkR = ContextCompat.getDrawable(ctx, R.drawable.ic_delete_24dp);
        xMarkR.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        backgroundL = new ColorDrawable(0xFF4CAF50);
        xMarkL = ContextCompat.getDrawable(ctx, R.drawable.ic_edit_24dp);
        xMarkL.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        xMarkMargin = 16;
        initiated = true;
    }

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context ctx) {
        mAdapter = adapter;
        this.ctx = ctx;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.START){
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
        else if(direction == ItemTouchHelper.END){
            mAdapter.onItemEdit(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // not sure why, but this method get's called for viewholder that are already swiped away
        if (viewHolder.getAdapterPosition() == -1) {
            // not interested in those
            return;
        }

        if (!initiated) {
            init();
        }

        // draw background on right
        backgroundR.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        backgroundR.draw(c);

        // draw x mark on right
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMarkR.getIntrinsicWidth();
        int intrinsicHeight = xMarkR.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMarkR.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMarkR.draw(c);

        // draw background on left
        backgroundL.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
        backgroundL.draw(c);

        // draw x mark on left
        itemHeight = itemView.getBottom() - itemView.getTop();
        intrinsicWidth = xMarkL.getIntrinsicWidth();
        intrinsicHeight = xMarkL.getIntrinsicWidth();

        xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
        xMarkLeft = itemView.getLeft() + xMarkMargin;
        xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        xMarkBottom = xMarkTop + intrinsicHeight;
        xMarkL.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMarkL.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}