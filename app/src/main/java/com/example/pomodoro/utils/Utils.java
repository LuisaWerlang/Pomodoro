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
    private int pomodoro_amount;
    private String alarm_name;
    private String clock_name;
    private String pomodoro_text;
    private int timeon;

    private int agenda_id;
    private List<Map<String, Object>> agendas;
    private List<CalendarDay> calendarDays;

    public Utils(Context context) {
        helper = new DatabaseHelper(context);
        this.pomodoro_time = 25;
        this.short_break_time = 5;
        this.long_break_time = 15;
        this.alarm_name = "Sound 1";
        this.clock_name = "";
        this.pomodoro_amount = 0;
        this.pomodoro_text = "Iniciar Pomodoro";
        this.timeon = 2;
    }

    public void findSettings() {
        String query = "SELECT * FROM settings WHERE id = 1";

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            this.pomodoro_time = cursor.getInt(1);
            this.short_break_time = cursor.getInt(2);
            this.long_break_time = cursor.getInt(3);
            this.alarm_name = cursor.getString(4);
            this.clock_name = cursor.getString(5);
            this.pomodoro_amount = cursor.getInt(6);
            this.pomodoro_text = cursor.getString(7);
            this.timeon = cursor.getInt(8);
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
                Map<String, Object> item = new HashMap<>();
                item.put("id", this.agenda_id);
                item.put("date", agendaDate);
                item.put("hour", agendaHour);
                item.put("hour_notify", agendaHourNotify);
                item.put("activity_name", cursor.getString(4));
                item.put("eventID", cursor.getLong(5));
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

    public List<String> listActivities(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<String> activities = new ArrayList<>();
        activities.add("Selecione");
        for (int i = 0; i < cursor.getCount(); i++) {
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

    public int getPomodoroAmount() { return this.pomodoro_amount; }

    public String getPomodoroText() { return this.pomodoro_text; }

    public int getTimeOn() { return this.timeon; }

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
