package com.cashify.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mhackl on 09.06.2017.
 */

// ViewHolder wraps the view that we want to pass to the RecyclerView,
// we only needs this because RecyclerView.ViewHolder is abstract
public class ViewHolder extends RecyclerView.ViewHolder {

    private final View view;

    public ViewHolder(View v) {
        super(v);
        view = v;
    }

    public View getView() {
        return view;
    }
}