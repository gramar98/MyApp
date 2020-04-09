package com.example.lab4.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4.add.AddStudentActivity;
import com.example.lab4.db.Group;
import com.example.lab4.db.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static  final int GROUP = 0;
    public static final int STUDENT = 1;

    private List<SparseArray> studentsGroups = new ArrayList<>();

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case STUDENT:
                return new StudentHolder(parent);
            case GROUP:
                return new GroupHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case STUDENT:
                StudentHolder studentHolder = (StudentHolder) holder;
                SparseArray student = studentsGroups.get(position);
                studentHolder.student.setText(
                        student.get(1) + " " + student.get(2) + " " + student.get(3)
                );
                studentHolder.student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Student st= new Student(student.get(1).toString(), student.get(2).toString(),student.get(3).toString(), Integer.parseInt(student.get(4).toString()));
                        Intent intent = new Intent(context, AddStudentActivity.class);
                        intent.putExtra("Student", st);
                        context.startActivity(intent);
                    }
                });
                break;
            case GROUP:
                GroupHolder groupHolder = (GroupHolder) holder;
                SparseArray group = studentsGroups.get(position);
                groupHolder.student.setText(
                        "\nGroup: \n" + group.get(1).toString() + "\nStudents:"
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return studentsGroups.size();
    }

    @Override
    public int getItemViewType(int position) {

        return Integer.parseInt(studentsGroups.get(position).get(0).toString());
    }

    public void setStudentGroups(List<SparseArray> students) {
        this.studentsGroups = students;
    }
}
