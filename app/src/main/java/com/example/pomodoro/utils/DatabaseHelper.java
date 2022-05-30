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

        db.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY NOT NULL," +
                "user TEXT," +
                "password TEXT, " +
                "user_name TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS activities (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT, " +
                            "description TEXT, " +
                            "concluded INTEGER);");

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
                "activity_name TEXT, " +
                "eventID int);");

        db.execSQL("CREATE TABLE IF NOT EXISTS question (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "question TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS answer (" +
                "id INTEGER PRIMARY KEY NOT NULL, " +
                "user_id INTEGER, " +
                "user TEXT," +
                "question TEXT," +
                "CT INTEGER," +
                "C INTEGER," +
                "ND INTEGER," +
                "D INTEGER," +
                "DT INTEGER," +
                "observation TEXT, " +
                "pomodoro INTEGER);"); //1-com pomodoro, 2-sem pomodoro

        String sql2 = "INSERT INTO question " +
                "(id, question) " +
                "VALUES " +
                "(1, 'Realizo as atividades da função satisfatoriamente, apresentando bons resultados, em tempo adequado, sem retrabalho e não conformidades.')," +
                "(2, 'Gerencio bem o meu tempo, fazendo gestão de prioridades no trabalho.')," +
                "(3, 'Demonstro entusiasmo, determinação e motivação na execução das atividades, contagiando e motivando o restante da equipe de trabalho em direção aos resultados esperados.')," +
                "(4, 'Eu me sinto no domínio das coisas em função de preparação e planejamento cuidadoso para vencer as atividades e tarefas durante o trabalho.')," +
                "(5, 'Eu gasto muito tempo em atividades secundárias durante o trabalho como correspondências supérfluas.')," +
                "(6, 'Eu sinto que desperdiço muito tempo durante o trabalho.')," +
                "(7, 'As demandas de outras pessoas, durante o trabalho, desviam-me do meu caminho para atingir objetivos e metas essenciais.')," +
                "(8, 'Eu corro o dia inteiro, mas faço poucas coisas que contribuam para as principais prioridades da minha organização no trabalho.')," +
                "(9, 'Eu gasto muito do meu tempo, durante o trabalho, em atividades de pouca relevância para as minhas prioridades, mas que requerem atenção imediata (por exemplo: interrupções inúteis, reuniões sem importância, telefonemas e e-mails dispensáveis).')," +
                "(10, 'Eu consigo manter o foco e a atenção enquanto realizo as minhas atividades, sem distrair-me frequentemente com o celular, durante o trabalho.')";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
