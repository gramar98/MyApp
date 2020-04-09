package com.example.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Group {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String groupName;

    public Group(@NonNull String groupName) {
        this.groupName = groupName;
    }
    @Override
    public String toString()
    {
        return groupName;
    }
}