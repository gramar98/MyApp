package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.lab1Activity;
import com.example.lab2.lab2Activity;
import com.example.lab3.lab3Activity;
import com.example.lab4.lab4Activity;
import com.example.lab5.lab5Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.lab1).setOnClickListener((v)-> startActivity(lab1Activity.newIntent(this)));
        findViewById(R.id.lab2).setOnClickListener((v)-> startActivity(lab2Activity.newIntent(this)));
        findViewById(R.id.lab3).setOnClickListener((v)-> startActivity(lab3Activity.newIntent(this)));
        findViewById(R.id.lab4).setOnClickListener((v)-> startActivity(lab4Activity.newIntent(this)));
        findViewById(R.id.lab5).setOnClickListener((v)-> startActivity(lab5Activity.newIntent(this)));
    }
}
