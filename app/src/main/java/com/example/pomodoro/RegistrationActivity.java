package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pomodoro.utils.DatabaseHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txt_user, txt_password;
    String user_name;
    private DatabaseHelper helper;
    private String user,password;

    public static final String DATABASE_NAME = "pomodoro";
    public static final String url = "jdbc:mysql://pomodoro.cdoluywguze8.us-east-1.rds.amazonaws.com:3306/" +
            DATABASE_NAME;
    public static final String db_username = "admin", db_password = "98657808";
    public static final String TABLE_NAME = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        helper = new DatabaseHelper(this);

        txt_user = findViewById(R.id.txt_user);
        txt_password = findViewById(R.id.txt_password);
        Button register = findViewById(R.id.button_new);
        register.setOnClickListener(view -> onSave());
        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(view -> onBack());
    }

    public void onSave() {
        user = txt_user.getText().toString();
        password = txt_password.getText().toString();
        user_name = user;

        if (password.equals("") && user.equals("")) {
            Toast.makeText(getApplicationContext(), "Informe todos os dados para o cadastro!", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM user WHERE user = '"+user+"' AND password = '"+password+"'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                Toast.makeText(getApplicationContext(), "Já existe usuário cadastrado! Faça o Login para continuar.", Toast.LENGTH_SHORT).show();
                cursor.close();
            } else {
                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", user);
                values.put("password", password);

                long result = db.insert("user", null, values);
                if (result != -1) {
                    Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    save();
                } else {
                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void save() throws SQLException {
        new Thread(() -> {
            //do your work
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, db_username, db_password);
                Statement statement = connection.createStatement();
                statement.execute("INSERT INTO " + TABLE_NAME + "(user, password) VALUES('" + user + "', '" + password + "')");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        onBack();
    }

    protected void onBack() {
        helper.close();
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}