package com.example.lab4.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * <p>
 * Класс, который является входной точкой для описания БД для Room. В Аннотации к классу
 * указывается список сущностей-таблиц (в нашем случае это только класс Student), версия БД
 * (необходима для вызова миграции). Флаг exportSchema отключает выгрузку в json файл схемы БД (в
 * обычном случае его оставляют включённым и этот файл добавляют в репозиторий, для сохранения
 * истории изменения схемы БД).
 * </p>
 * <p>
 * Для всех Data access objects (DAO) (см. {@link DAO}) должны быть написаны абстрактные
 * методы, возвращающие их {@link #studentDao()}
 * </p>
 * <p>
 * Библиотека Room использует эти классы за основу для кодогенарации методов работы с БД,
 * используя информацию, которую мы указываем в аннотациях. Когда мы создаём инстанс нашей БД
 * через {@link Room#databaseBuilder(Context, Class, String)} (см. метод {@link #getInstance(Context)})
 * Room возвращает нам инстанс уже сгенерированной реализации БД.
 * </p>
 */
@Database(entities = {Student.class, Group.class}, version = 1, exportSchema = false)
public abstract class Lab4Database extends RoomDatabase {

    private static Lab4Database db;

    /**
     * Синглетон, в котором происходит инициализация и настройка самой БД. Используем синглетон,
     * т.к. инстанс класса БД должен быть только один (т.ё. в нём происходит управление
     * соединением к БД).
     */
    @NonNull
    public static Lab4Database getInstance(@NonNull Context context) {
        if (db == null) {
            synchronized (Lab4Database.class) {
                if (db == null) {
                    db = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Lab4Database.class,
                            "lab4_database"
                    )
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return db;
    }

    public abstract DAO studentDao();
}
