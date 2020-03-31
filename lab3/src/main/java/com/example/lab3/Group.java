package com.example.lab3;

import androidx.annotation.NonNull;

public class Group{
    @NonNull
    public String groupName;

    public Group(@NonNull String groupName) {
        this.groupName = groupName;
    }


    @NonNull
    @Override
    public String toString() {
        return groupName;
    }

}