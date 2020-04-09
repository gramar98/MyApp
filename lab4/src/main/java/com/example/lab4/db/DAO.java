package com.example.lab4.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface DAO {
    @Query("SELECT * FROM student")
    List<Student> getAllStudents();

    @Insert
    void insertStudent(@NonNull Student student);

    @Query(
            "SELECT COUNT(*) FROM student WHERE " +
                    "first_name = :firstName AND " +
                    "second_name = :secondName AND " +
                    "last_name = :lastName"
    )
    int countStudents(@NonNull String firstName, @NonNull String secondName, @NonNull String lastName);

    @Query("SELECT * FROM `group`")
    List<Group> getAllGroups();

    @Query("SELECT * FROM `group` where id = :id")
    Group getGroup(@NonNull int id);

    @Insert
    void insertGroup(@NonNull Group group);
}
