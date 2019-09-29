package com.example.nunezjonathan_poc.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nunezjonathan_poc.daos.BottleDao;
import com.example.nunezjonathan_poc.daos.ChildDao;
import com.example.nunezjonathan_poc.daos.MedicationDao;
import com.example.nunezjonathan_poc.daos.NurseDao;
import com.example.nunezjonathan_poc.daos.SleepDao;
import com.example.nunezjonathan_poc.daos.SoiledDiaperDao;
import com.example.nunezjonathan_poc.daos.SymptomDao;
import com.example.nunezjonathan_poc.daos.TemperatureDao;
import com.example.nunezjonathan_poc.daos.WetDiaperDao;
import com.example.nunezjonathan_poc.models.Bottle;
import com.example.nunezjonathan_poc.models.Child;
import com.example.nunezjonathan_poc.models.Medication;
import com.example.nunezjonathan_poc.models.Nurse;
import com.example.nunezjonathan_poc.models.Sleep;
import com.example.nunezjonathan_poc.models.SoiledDiaper;
import com.example.nunezjonathan_poc.models.Symptom;
import com.example.nunezjonathan_poc.models.Temperature;
import com.example.nunezjonathan_poc.models.WetDiaper;

@Database(entities = {Child.class, Sleep.class, Nurse.class,
        Bottle.class, WetDiaper.class, SoiledDiaper.class,
        Symptom.class, Medication.class, Temperature.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ChildDao childDao();
    public abstract SleepDao sleepDao();
    public abstract NurseDao nurseDao();
    public abstract BottleDao bottleDao();
    public abstract WetDiaperDao wetDiaperDao();
    public abstract SoiledDiaperDao soiledDiaperDao();
    public abstract SymptomDao symptomDao();
    public abstract MedicationDao medicationDao();
    public abstract TemperatureDao temperatureDao();

    public static AppDatabase getInstance(Context context) {
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
