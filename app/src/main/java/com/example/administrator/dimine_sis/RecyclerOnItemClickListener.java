package com.example.administrator.dimine_sis;

import android.view.View;
import android.view.ViewGroup;

public interface RecyclerOnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View v, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
