package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.constraintlayout.solver.widgets.WidgetContainer;

public class lab2ViewsContainer extends LinearLayout {

    private int minViewsCount;
    private int viewsCount;
    private int incriment = 1;
    private int idMaxValue = -1;
    private double MaxValue = -1;

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
        this(context, attrs, 0);
    }

    /**
     * Конструктор, вызывается при инфлейте View, когда у View указан дополнительный стиль.
     * Почитать про стили можно здесь https://developer.android.com/guide/topics/ui/look-and-feel/themes
     *
     * @param attrs атрибуты, указанные в XML. Стандартные android атрибуты обрабатываются внутри родительского класса.
     *              Здесь необходимо только обработать наши атрибуты.
     */
    public lab2ViewsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Свои атрибуты описываются в файле res/values/attrs.xml
        // Эта строчка объединяет возможные применённые к View стили
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Lab2ViewsContainer, defStyleAttr, 0);

        minViewsCount = a.getInt(R.styleable.Lab2ViewsContainer_lab2_minViewsCount, 0);
        if (minViewsCount < 0) {
            throw new IllegalArgumentException("minViewsCount can't be less than 0");
        }

        // Полученный TypedArray необходимо обязательно очистить.
        a.recycle();

        setViewsCount(minViewsCount);
    }

    /**
     * Программно создаём {@link TextView} и задаём его атрибуты, альтернативно можно описать его в
     * xml файле и инфлейтить его через класс LayoutInflater.
     */
    public void incrementViews() {
        TextView textView = new TextView(getContext());
        textView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
        textView.setTextSize(16);
        textView.setText(String.valueOf(viewsCount++));
        // У каждого View, который находится внутри ViewGroup есть LayoutParams,
        // в них содержится информация для лэйаута компонентов.
        // Базовая реализация LayoutParams содержит только определение ширины и высоты
        // (то, что мы указываем в xml в атрибутах layout_widget и layout_height).
        // Получить их можно через метод getLayoutParams у View. Метод addView смотрит, если у View
        // не установлены LayoutParams, то создаёт дефолтные, вызывая метод generateDefaultLayoutParams
        addView(textView);
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
        if(idMaxValue == -1)
        {
            idMaxValue = incriment;
            MaxValue =  val;
        }
        else if (MaxValue < val)
        {
            idMaxValue = incriment;
            MaxValue = val;
        }
        for (int i = 1; i<incriment+1; i++)
        {
            ProgressBar cur = (ProgressBar) findViewById(i);
            cur.getProgressDrawable().setColorFilter(idMaxValue==i?Color.RED:Color.BLACK, PorterDuff.Mode.SRC_IN);

        }
        incriment++;
    }

    public void setViewsValues(double[] numbers, ArrayList<String> qualities) {
        if (numbers.length == 0) {
            return;
        }
        removeAllViews();
        incriment = 1;
        idMaxValue = -1;
        for (int i = 0; i < numbers.length; i++) {
            String q1 = qualities.get(i);
            double v1 = numbers[i];
            AddView(v1,q1);
        }
    }
    public void setViewsCount(int viewsCount) {
        if (this.viewsCount == viewsCount) {
            return;
        }
        viewsCount = viewsCount < minViewsCount ? minViewsCount : viewsCount;

        removeAllViews();
        this.viewsCount = 0;
        for (int i = 0; i < viewsCount; i++) {
            incrementViews();
        }
    }
    public int getViewsCount() {
        return viewsCount;
    }

    /**
     * Метод трансформирует указанные dp в пиксели, используя density экрана.
     */
    @Px
    public int dpToPx(float dp) {
        if (dp == 0) {
            return 0;
        }
        float density = getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * dp);
    }

}
