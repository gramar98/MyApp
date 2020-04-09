package com.example.lab3.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.AddStudentActivity;
import com.example.lab3.R;
import com.example.lab3.Student;

public class StudentHolder extends RecyclerView.ViewHolder {

    public final TextView student;

    public StudentHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_student, parent, false));
        student = itemView.findViewById(R.id.student);
        student.setTextSize(20);
    }

}
