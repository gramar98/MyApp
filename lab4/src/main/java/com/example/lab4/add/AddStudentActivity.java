package com.example.lab4.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab4.R;
import com.example.lab4.db.Group;
import com.example.lab4.db.Lab4Database;
import com.example.lab4.db.Student;
import com.example.lab4.db.DAO;

import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";

    private boolean skipSaveToPrefs;
    private TempStudentPref studentPref;

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, AddStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private DAO dao;

    private Spinner spinner;
    private EditText firstName;
    private EditText secondName;
    private EditText lastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab4_activity_add_student);

        dao = Lab4Database.getInstance(this).studentDao();

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_item, dao.getAllGroups());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            Student student = (Student) arguments.get("Student");
            firstName.setText(student.firstName);
            secondName.setText(student.secondName);
            lastName.setText(student.lastName);
            List<Group> Groups = dao.getAllGroups();
            spinner.setSelection(adapter.getPosition(dao.getGroup(student.group_id)));

        }
        studentPref = new TempStudentPref(this);
        firstName.setText(studentPref.getFirstName());
        secondName.setText(studentPref.getSecondName());
        lastName.setText(studentPref.getLastName());
        spinner.setSelection(adapter.getPosition(dao.getGroup(studentPref.getGroupId())));

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!skipSaveToPrefs) {
            Group group = (Group)spinner.getSelectedItem();
            studentPref.set(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString(),
                    group.id
            );
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab4_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
            saveStudent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void saveStudent() {
        Group selectedGroup = (Group) spinner.getSelectedItem();
        Student student = new Student(
                firstName.getText().toString(),
                secondName.getText().toString(),
                lastName.getText().toString(),
                selectedGroup.id
        );

        // Проверяем, что все поля были указаны
        if (TextUtils.isEmpty(student.firstName) ||
                TextUtils.isEmpty(student.secondName) ||
                TextUtils.isEmpty(student.lastName)) {
            // Класс Toast позволяет показать системное уведомление поверх всего UI
            Toast.makeText(this, R.string.lab4_error_empty_fields, Toast.LENGTH_LONG).show();
            return;
        }

        if (dao.countStudents(student.firstName, student.secondName, student.lastName) > 0) {
            Toast.makeText(
                    this,
                    R.string.lab4_error_already_exists,
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        skipSaveToPrefs = true;

        studentPref.clear();

        Intent data = new Intent();
        data.putExtra(EXTRA_STUDENT, student);
        setResult(RESULT_OK, data);
        finish();
    }
}
