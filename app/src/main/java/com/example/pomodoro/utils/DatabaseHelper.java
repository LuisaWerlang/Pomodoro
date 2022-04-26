package com.example.pomodoro.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "pomodoro";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS activities (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT, " +
                            "description TEXT, " +
                            "concluded INTEGER, " +
                            "time TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS settings (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "pomodoro_time int, " +
                "short_break_time int, " +
                "long_break_time int, " +
                "alarm_sound TEXT, " +
                "clock_sound TEXT);");

        String sql = "INSERT INTO settings " +
                "(id, pomodoro_time, short_break_time, long_break_time, alarm_sound, clock_sound) " +
                "VALUES(1, 25, 5, 15, 'Sound 1', '')";
        db.execSQL(sql);

        db.execSQL("CREATE TABLE IF NOT EXISTS agenda (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "date int, " +
                "hour int, " +
                "hour_notify int, " +
                "activities_id INTEGER NOT NULL, FOREIGN KEY(activities_id) REFERENCES activities(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
