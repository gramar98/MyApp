package com.example.lab5.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.Repo;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Repo> repos = new ArrayList<>();


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Holder repoHolder = (Holder) holder;
        Repo repo = repos.get(position);
        repoHolder.repo.setText(repo.toString());
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public void addRepos(List<Repo> repos) {
        this.repos.addAll(repos);
    }
    public ArrayList<Repo> getRepos() {
        ArrayList<Repo> repoArrayList = new ArrayList<>();
        repoArrayList.addAll(repos);
        return repoArrayList;
    }

}

