package com.example.pomodoro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class Activities {
    public int id, concluded;
    public String name;
    public String description;

    private final DatabaseHelper helper;

    public Activities(Context context) {
        helper = new DatabaseHelper(context);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConcluded() {
        return concluded;
    }

    public void setConcluded(int concluded) {
        this.concluded = concluded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Activities> listActivities(Context context) {
        List<Activities> activities = new ArrayList<>();
        String query = "SELECT * FROM activities";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount(); i++) {
            Activities activity = new Activities(context);
            activity.setId(cursor.getInt(0));
            activity.setName(cursor.getString(1));
            activity.setDescription(cursor.getString(2));
            activity.setConcluded(cursor.getInt(3));
            activities.add(activity);
            cursor.moveToNext();
        }
        cursor.close();
        helper.close();
        return activities;
    }
}
