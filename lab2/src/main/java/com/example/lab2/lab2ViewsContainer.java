package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.Px;

public class lab2ViewsContainer extends LinearLayout {

    private int minViewsCount;
    private int viewsCount;
    private int incriment = 1;
    private int idMaxValue = -1;
    private double MaxValue = -1;
    ArrayList<String> qualitiesAr = new ArrayList<String>();
    ArrayList<Double> numbersAr = new ArrayList<Double>();

    /**
     * Этот конструктор используется при создании View в коде.
     */
    public lab2ViewsContainer(Context context) {
        this(context, null);
    }

    /**
     * Этот конструктор выдывается при создании View из XML.
     */
    public lab2ViewsContainer(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        AddView(5, "Категория");
    }

    @SuppressLint("DefaultLocale")
    public void AddView(double val, String quality) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        TextView qualityText = new TextView(getContext());
        //qualityText.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        //qualityText.setTextSize(16);
        qualityText.setText(String.valueOf(quality));
        qualityText.setWidth(150);
        qualityText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextView numberText = new TextView(getContext());
        //numberText.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        //numberText.setTextSize(16);
        numberText.setText(String.valueOf(val));
        numberText.setWidth(50);
        numberText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress((int)(val*10));
        //progressBar.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        progressBar.setMax(100);
        progressBar.setId(incriment);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if(idMaxValue == -1)
        {
            idMaxValue = incriment;
            MaxValue =  val;
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
        else if (MaxValue < val)
        {
            ProgressBar cur = (ProgressBar) findViewById(idMaxValue);
            cur.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            idMaxValue = incriment;
            MaxValue = val;
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }
        else {
            progressBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        }


        // У каждого View, который находится внутри ViewGroup есть LayoutParams,
        // в них содержится информация для лэйаута компонентов.
        // Базовая реализация LayoutParams содержит только определение ширины и высоты
        // (то, что мы указываем в xml в атрибутах layout_widget и layout_height).
        // Получить их можно через метод getLayoutParams у View. Метод addView смотрит, если у View
        // не установлены LayoutParams, то создаёт дефолтные, вызывая метод generateDefaultLayoutParams
        linearLayout.addView(qualityText);
        linearLayout.addView(numberText);
        linearLayout.addView(progressBar);
        addView(linearLayout);

        /*for (int i = 1; i<incriment+1; i++)
        {
            ProgressBar cur = (ProgressBar) findViewById(i);
            cur.getProgressDrawable().setColorFilter(idMaxValue==i?Color.RED:Color.BLACK, PorterDuff.Mode.SRC_IN);

        }*/
        incriment++;
        qualitiesAr.add(quality);
        numbersAr.add(val);
    }

    public void setViewsValues(double[] numbers, ArrayList<String> qualities) {
        if (numbers.length == 0) {
            return;
        }
        removeAllViews();
        numbersAr.clear();
        qualitiesAr.clear();
        incriment = 1;
        idMaxValue = -1;
        for (int i = 0; i < numbers.length; i++) {
            String q1 = qualities.get(i);
            double v1 = numbers[i];
            AddView(v1,q1);
        }
    }

    public ArrayList<String> getQualityArray() {
        return qualitiesAr;
    }
    public ArrayList<Double> getValueArray() {
        return numbersAr;
    }
}
