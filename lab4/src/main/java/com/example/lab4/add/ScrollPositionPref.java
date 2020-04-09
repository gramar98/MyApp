package com.example.lab4.add;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScrollPositionPref {
    private static final String PREF_POSITION = "position";
    private final SharedPreferences prefs;

    public ScrollPositionPref(@NonNull Context context) {
        prefs = context.getSharedPreferences("scroll_position", Context.MODE_PRIVATE);
    }
    @Nullable
    public int getScrollPosition() {
        return prefs.getInt(PREF_POSITION, 0);
    }
    public void set(
            @Nullable int position
    ) {
        prefs.edit()
                .putInt(PREF_POSITION, position)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

}
