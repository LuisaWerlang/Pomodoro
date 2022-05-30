package com.example.pomodoro.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    public String question_name;
    public String answer_observation;
    public int answer_option, ct, c, nd, d, dt;
    private final DatabaseHelper helper;

    public Questions(Context context) {
        helper = new DatabaseHelper(context);
        this.answer_observation = "";
    }

    public String getAnswer_observation() {
        return answer_observation;
    }

    public void setAnswer_observation(String answer_observation) {
        this.answer_observation = answer_observation;
    }

    public int getAnswer_option() {
        return answer_option;
    }

    public void setAnswer_option(int answer_option) {
        this.answer_option = answer_option;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public List<Questions> listQuestions(Context context) {
        List<Questions> questions = new ArrayList<>();
        String query = "SELECT * FROM question";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Questions question = new Questions(context);
            question.setQuestion_name(cursor.getString(1));
            questions.add(question);
            cursor.moveToNext();
        }
        cursor.close();
        helper.close();
        return questions;
    }
}
