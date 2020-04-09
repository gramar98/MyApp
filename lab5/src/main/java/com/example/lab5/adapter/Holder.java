
package com.example.lab5.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;

public class Holder extends RecyclerView.ViewHolder {

    public final TextView repo;

    public Holder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_repo_layout, parent, false));
        repo = itemView.findViewById(R.id.repo);
    }
}

