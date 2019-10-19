package com.example.nunezjonathan_poc.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.daos.EventDao;
import com.example.nunezjonathan_poc.daos.HealthDao;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Event;
import com.example.nunezjonathan_poc.models.Health;
import com.example.nunezjonathan_poc.utils.Converters;

@Database(entities = {Child.class, Event.class, Health.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ChildDao childDao();
    public abstract EventDao eventDao();
    public abstract HealthDao healthDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "database-name")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
