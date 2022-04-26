package com.example.pomodoro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utils {
    private final DatabaseHelper helper;

    private int pomodoro_time;
    private int short_break_time;
    private int long_break_time;
    private String alarm_name;
    private String clock_name;

    private int agenda_id;
    private String agenda_date;
    private String agenda_hour;
    private String agenda_hour_notify;
    private List<Map<String, Object>> agendas;
    private List<CalendarDay> calendarDays;

    public Utils(Context context) {
        helper = new DatabaseHelper(context);
        this.pomodoro_time = 25;
        this.short_break_time = 5;
        this.long_break_time = 15;
        this.alarm_name = "Sound 1";
        this.clock_name = "";
    }

    public void findSettings() {
        String query = "SELECT * FROM settings WHERE id = 1";

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

    @SuppressLint("SimpleDateFormat")
    public void findAgendas(int id, String finalDate) {
        try {
            String query;
            if (id != 0) {
                query = "SELECT * FROM agenda WHERE id = " + id + " ORDER BY hour";
            } else if (!finalDate.equals("")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                long finalAgendaDate = Objects.requireNonNull(dateFormat.parse(finalDate)).getTime() / 1000;
                query = "SELECT * FROM agenda WHERE date = " + finalAgendaDate + " ORDER BY hour";
            } else {
                query = "SELECT * FROM agenda ORDER BY hour";
            }

            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            agendas = new ArrayList<>();
            calendarDays = new ArrayList<>();
            SimpleDateFormat sdf;
            for (int i = 0; i < cursor.getCount(); i++) {
                Date date = new Date(cursor.getInt(1) * 1000L);
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                String agendaDate = sdf.format(date);
                Date hour = new Date(cursor.getInt(2) * 1000L);
                sdf = new SimpleDateFormat("HH:mm");
                String agendaHour = sdf.format(hour);
                Date hour_notify = new Date(cursor.getInt(3) * 1000L);
                String agendaHourNotify = sdf.format(hour_notify);

                this.agenda_id = cursor.getInt(0);
                this.agenda_date = agendaDate;
                this.agenda_hour = agendaHour;
                this.agenda_hour_notify = agendaHourNotify;
                Map<String, Object> item = new HashMap<>();
                item.put("id", this.agenda_id);
                item.put("date", this.agenda_date);
                item.put("hour", this.agenda_hour);
                item.put("hour_notify", this.agenda_hour_notify);
                item.put("activity_name", cursor.getString(4));
                agendas.add(item);

                sdf = new SimpleDateFormat("dd");
                int day = Integer.parseInt(sdf.format(date));
                sdf = new SimpleDateFormat("MM");
                int month = Integer.parseInt(sdf.format(date));
                sdf = new SimpleDateFormat("yyyy");
                int year = Integer.parseInt(sdf.format(date));
                calendarDays.add(CalendarDay.from(year, month, day));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<String> listActivities(String query, List<String> activities) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        activities = new ArrayList<>();
        activities.add("Selecione");
        for(int i=0; i<cursor.getCount(); i++) {
            String nome = cursor.getString(1);
            activities.add(nome);
            cursor.moveToNext();
        }
        cursor.close();
        return activities;
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

    public String getClockName() {
        return this.clock_name;
    }

    public List<CalendarDay> getCalendarDays() {
        return this.calendarDays;
    }

    public List<Map<String, Object>> getAgendas() {
        return this.agendas;
    }

    public int getAgendaId() {
        return this.agenda_id;
    }

    public void close() {
        helper.close();
    }
}
