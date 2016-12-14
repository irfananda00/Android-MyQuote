package com.irfanandarafifsatrio.myquote.utils;

/**
 * Created by irfanandarafifsatrio on 10/5/16.
 *
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onItemEdit(int position);
}