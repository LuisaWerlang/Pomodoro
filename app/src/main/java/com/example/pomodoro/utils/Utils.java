package com.example.pomodoro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Utils {
    private final DatabaseHelper helper;
    private int pomodoro_time;
    private int short_break_time;
    private int long_break_time;
    private String alarm_name;
    private String clock_name;

    public Utils(Context context) {
        helper = new DatabaseHelper(context);
        this.pomodoro_time = 25;
        this.short_break_time = 5;
        this.long_break_time = 15;
        this.alarm_name = "Sound 1";
        this.clock_name = "";
    }

    public void findSettings() {
        String query = "SELECT * FROM configs WHERE id = 1";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount(); i++) {
            this.pomodoro_time = cursor.getInt(1);
            this.short_break_time = cursor.getInt(2);
            this.long_break_time = cursor.getInt(3);
            this.alarm_name = cursor.getString(4);
            this.clock_name = cursor.getString(5);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public int getPomodoroTime() {
        return this.pomodoro_time;
    }

    public int getShortBreakTime() {
        return this.short_break_time;
    }

    public int getLongBreakTime() {
        return this.long_break_time;
    }

    public String getAlarmName() {
        return this.alarm_name;
    }

    public String getClockName() { return this.clock_name; }

    public void close() {
        helper.close();
    }
}
