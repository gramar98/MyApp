package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private static final String EXTRA_STUDENT = "student";

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, AddStudentActivity.class);
    }

    public static Student getResultStudent(@NonNull Intent intent) {
        return intent.getParcelableExtra(EXTRA_STUDENT);
    }

    private final GroupCache groupCache = GroupCache.getInstance();

    private final StudentsCache studentsCache = StudentsCache.getInstance();

    private Spinner spinner;
    private EditText firstName;
    private EditText secondName;
    private EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_activity_add_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firstName = findViewById(R.id.first_name);
        secondName = findViewById(R.id.second_name);
        lastName = findViewById(R.id.last_name);
        spinner = findViewById(R.id.spinner);


        ArrayAdapter<Group> adapter = new ArrayAdapter<Group>(this,
                android.R.layout.simple_spinner_item, groupCache.getGroups());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            Student student = (Student) arguments.get("Student");
            firstName.setText(student.firstName);
            secondName.setText(student.secondName);
            lastName.setText(student.lastName);
            List<Group> Groups = groupCache.getGroups();
            for(int i = 0; i<Groups.size(); i++)
            {
                if(Groups.get(i).groupName.equals(student.group.groupName))
                {
                    spinner.setSelection(adapter.getPosition(groupCache.get(i)));
                    break;
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab3_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Если пользователь нажал "назад", то просто закрываем Activity
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        // Если пользователь нажал "Сохранить"
        if (item.getItemId() == R.id.action_save) {
            // Создаём объект студента из введенных
            Student student = new Student(
                    firstName.getText().toString(),
                    secondName.getText().toString(),
                    lastName.getText().toString(),
                    (Group) spinner.getSelectedItem()
            );

            // Проверяем, что все поля были указаны
            if (TextUtils.isEmpty(student.firstName) ||
                    TextUtils.isEmpty(student.secondName) ||
                    TextUtils.isEmpty(student.lastName)) {
                // Класс Toast позволяет показать системное уведомление поверх всего UI
                Toast.makeText(this, R.string.lab3_error_empty_fields, Toast.LENGTH_LONG).show();
                return true;
            }

            if (studentsCache.contains(student)) {
                Toast.makeText(this, R.string.lab3_error_already_exists, Toast.LENGTH_LONG).show();
                return true;
            }

            Intent data = new Intent();

            data.putExtra(EXTRA_STUDENT, student);

            setResult(RESULT_OK, data);
            // Закрываем нашу Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
