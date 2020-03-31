package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab3.adapter.StudentsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class lab3Activity extends AppCompatActivity {

    private final StudentsCache studentsCache = StudentsCache.getInstance();
    private final GroupCache groupCache = GroupCache.getInstance();

    private RecyclerView list;
    private FloatingActionButton fab;
    private FloatingActionButton addGroupButton;

    private static final int REQUEST_STUDENT_ADD = 1;


    private StudentsAdapter studentsAdapter;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, lab3Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity);
        setTitle(getClass().getSimpleName());
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        addGroupButton = findViewById(R.id.fab2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(studentsAdapter = new StudentsAdapter());
        StudentsToGroups();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        Toast message = Toast.makeText(this, "add group", Toast.LENGTH_LONG);

        fab.setOnClickListener(
                v -> startActivityForResult(
                        AddStudentActivity.newIntent(this),
                        REQUEST_STUDENT_ADD
                )
        );
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogLayout = getLayoutInflater().inflate(R.layout.lab3_dialog, null);
                dialog.setView(dialogLayout);
                dialog.setNeutralButton("add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText group = dialogLayout.findViewById(R.id.group);
                        String name = group.getText().toString();
                        if (name == "") {
                            message.show();
                        } else {
                            groupCache.addGroup(new Group(name));
                        }

                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            studentsCache.addStudent(student);
            StudentsToGroups();
        }
    }

    public void StudentsToGroups() {
        final int GROUP = 0;
        final int STUDENT = 1;
        boolean addNewGroup = false;
        List<Student> students = studentsCache.getStudents();
        List<SparseArray> studentsGroups = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            addNewGroup = true;
            for (SparseArray s : studentsGroups) {
                int group = Integer.parseInt(s.get(0).toString());
                if (group == GROUP && s.get(1).toString().equals(students.get(i).group.groupName)) {

                    SparseArray newStudent = new SparseArray();
                    newStudent.put(0, STUDENT);
                    newStudent.put(1, students.get(i).firstName);
                    newStudent.put(2, students.get(i).secondName);
                    newStudent.put(3, students.get(i).lastName);
                    newStudent.put(4, students.get(i).group.groupName);
                    studentsGroups.add(newStudent);
                    addNewGroup = false;
                    break;

                }
            }
            if (addNewGroup == true) {
                SparseArray newGroup = new SparseArray();
                newGroup.put(0, GROUP);
                newGroup.put(1, students.get(i).group.groupName);
                studentsGroups.add(newGroup);
                SparseArray newStudent = new SparseArray();
                newStudent.put(0, STUDENT);
                newStudent.put(1, students.get(i).firstName);
                newStudent.put(2, students.get(i).secondName);
                newStudent.put(3, students.get(i).lastName);
                newStudent.put(4, students.get(i).group.groupName);
                studentsGroups.add(newStudent);
            }


        }
        int ItemCount = studentsAdapter.getItemCount();
        studentsAdapter.setStudentGroups(studentsGroups);
        studentsAdapter.notifyItemRangeInserted(ItemCount, studentsGroups.size() - ItemCount);
    }
}

