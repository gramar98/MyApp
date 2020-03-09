package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class lab2Activity extends AppCompatActivity {

    private static final String STATE_VIEWS_COUNT = "views_count";

    private static final String NAME_QUALITIES= "quality";
    private static final String NUMBERS= "number";

    ArrayList<String> qualities = new ArrayList<String>();
    ArrayList<Double> numbers = new ArrayList<Double>();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, lab2Activity.class);
    }
    private lab2ViewsContainer lab2ViewsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);
        setTitle(getClass().getSimpleName());

        lab2ViewsContainer = findViewById(R.id.container);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                EditText quality = (EditText) findViewById(R.id.quality);
                String qualityText = quality.getText().toString();

                EditText number = (EditText) findViewById(R.id.number);
                String numberText = number.getText().toString();
                double numbervalue = Double.parseDouble(numberText);
                lab2ViewsContainer.AddView(numbervalue,qualityText);
                qualities.add(qualityText);
                numbers.add(numbervalue);
            }

        });



        //findViewById(R.id.button).setOnClickListener(view -> lab2ViewsContainer.AddView(Double.parseDouble(numberText), qualityText));
        /*if (savedInstanceState != null) {
            lab2ViewsContainer.setViewsCount(savedInstanceState.getInt(STATE_VIEWS_COUNT));
        }*/
        if (savedInstanceState != null) {
            lab2ViewsContainer.setViewsValues(savedInstanceState.getDoubleArray(NUMBERS), savedInstanceState.getStringArrayList(NAME_QUALITIES));
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        double[] numbersArray = new double[numbers.size()];
        for ( int i =0; i< numbersArray.length; i++)
        {
            numbersArray[i] = numbers.get(i).doubleValue();
        }
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(NAME_QUALITIES, qualities);
        outState.putDoubleArray(NUMBERS, numbersArray);
        outState.putInt(STATE_VIEWS_COUNT, lab2ViewsContainer.getViewsCount());
    }
}
