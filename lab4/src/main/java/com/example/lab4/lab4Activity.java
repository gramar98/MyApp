package com.example.lab4;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab4.adapter.StudentsAdapter;
import com.example.lab4.add.AddStudentActivity;
import com.example.lab4.add.ScrollPositionPref;
import com.example.lab4.add.TempStudentPref;
import com.example.lab4.db.DAO;
import com.example.lab4.db.Group;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class lab4Activity extends AppCompatActivity {


    private DAO dao;

    private RecyclerView list;
    private FloatingActionButton fab;
    private FloatingActionButton addGroupButton;

    private EditText firstName;
    private EditText secondName;
    private EditText lastName;
    private Spinner spinner;
    private ScrollPositionPref scrollPositionPref;
    private boolean skipSaveToPrefs;

    private static final int REQUEST_STUDENT_ADD = 1;


    private StudentsAdapter studentsAdapter;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, lab4Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activity);
        setTitle(getClass().getSimpleName());
        scrollPositionPref = new ScrollPositionPref(this);
        list = findViewById(android.R.id.list);
        fab = findViewById(R.id.fab);
        addGroupButton = findViewById(R.id.fab2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        dao = Lab4Database.getInstance(this).studentDao();

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
                final View dialogLayout = getLayoutInflater().inflate(R.layout.lab4_dialog, null);
                dialog.setView(dialogLayout);
                dialog.setNeutralButton("add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText group = dialogLayout.findViewById(R.id.group);
                        String name = group.getText().toString();
                        if (name == "") {
                            message.show();
                        } else {
                            dao.insertGroup(new Group(name));
                        }

                    }
                });
                dialog.show();

            }
        });
        int scrollPosition = scrollPositionPref.getScrollPosition();
        list.scrollToPosition(scrollPosition);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!skipSaveToPrefs) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) list.getLayoutManager();
            scrollPositionPref.set(
                    linearLayoutManager.findFirstVisibleItemPosition()
            );
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STUDENT_ADD && resultCode == RESULT_OK) {
            Student student = AddStudentActivity.getResultStudent(data);

            dao.insertStudent(student);
            StudentsToGroups();
            skipSaveToPrefs = true;
        }
    }

    public void StudentsToGroups() {
        final int GROUP = 0;
        final int STUDENT = 1;
        boolean addNewGroup = false;
        List<Student> students = dao.getAllStudents();
        List<SparseArray> studentsGroups = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            addNewGroup = true;
            int startIndex = 0;
            for (SparseArray s : studentsGroups) {
                startIndex++;
                int group = Integer.parseInt(s.get(0).toString());
                if (group == GROUP && Integer.parseInt(s.get(2).toString()) == student.group_id) {
                    startIndex++;
                    SparseArray newStudent = new SparseArray();
                    newStudent.put(0, STUDENT);
                    newStudent.put(1, student.firstName);
                    newStudent.put(2, student.secondName);
                    newStudent.put(3, student.lastName);
                    newStudent.put(4, student.group_id);
                    studentsGroups.add(startIndex, newStudent);
                    addNewGroup = false;
                    break;

                }
            }
            if (addNewGroup == true) {
                SparseArray newGroup = new SparseArray();
                newGroup.put(0, GROUP);
                newGroup.put(1, dao.getGroup(student.group_id).groupName);
                newGroup.put(2, student.group_id);
                studentsGroups.add(newGroup);
                SparseArray newStudent = new SparseArray();
                newStudent.put(0, STUDENT);
                newStudent.put(1, student.firstName);
                newStudent.put(2, student.secondName);
                newStudent.put(3, student.lastName);
                newStudent.put(4, student.group_id);
                studentsGroups.add(newStudent);
            }
        }
        int ItemCount = studentsAdapter.getItemCount();
        studentsAdapter.setStudentGroups(studentsGroups);
        studentsAdapter.notifyItemRangeInserted(ItemCount, studentsGroups.size() - ItemCount);
    }
}
