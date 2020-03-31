package com.example.lab3;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupCache {
    private static GroupCache instance;

    public static GroupCache getInstance() {
        if (instance == null) {
            //
            synchronized (GroupCache.class) {
                if (instance == null) {
                    instance = new GroupCache();
                }
            }
        }

        return instance;
    }

    private List<Group> groups = new ArrayList<>();

    private GroupCache() {
    }

    public Group get(int index)
    {
        return groups.get(index);
    }

    @NonNull
    public List<Group> getGroups() {

        return new ArrayList<>(groups);

    }


    public void addGroup(@NonNull Group group) {
        groups.add(group);
    }

    public boolean contains(@NonNull Group group) {
        return groups.contains(group);
    }
}
