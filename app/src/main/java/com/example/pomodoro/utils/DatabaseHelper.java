package com.example.pomodoro.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "pomodoro";
    private static final int VERSAO = 3;

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS atividades (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "nome TEXT, " +
                            "descricao TEXT, " +
                            "concluido INTEGER, " +
                            "tempo TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS configs (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "tempo_pomodoro int, " +
                "tempo_pausa_curta int, " +
                "tempo_pausa_longa int, " +
                "som_alarme TEXT, " +
                "som_relogio TEXT);");

        String sql = "INSERT INTO configs " +
                "(id, tempo_pomodoro, tempo_pausa_curta, tempo_pausa_longa, som_alarme, som_relogio) " +
                "VALUES(1, 25, 5, 15, 'Sound 1', '')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
