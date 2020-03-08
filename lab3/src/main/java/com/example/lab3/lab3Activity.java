package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class lab3Activity extends AppCompatActivity {
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, lab3Activity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);
        setTitle(getClass().getSimpleName());
    }
}
