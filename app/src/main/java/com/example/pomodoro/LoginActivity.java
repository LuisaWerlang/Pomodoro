package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pomodoro.utils.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText txt_user, txt_password;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user_name = settings.getString("user_name", "");
        int user_id = settings.getInt("user_id",0);
        if( (!user_name.equals("")) && (user_id != 0)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user_name", user_name);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }

        helper = new DatabaseHelper(this);
        txt_user = findViewById(R.id.txt_user);
        txt_password = findViewById(R.id.txt_password);

        Button button_login = findViewById(R.id.button_login);
        Button button_new = findViewById(R.id.button_new);
        button_login.setOnClickListener(view -> onClickLogin());
        button_new.setOnClickListener(view -> onClickNew());
    }

    public void onClickLogin() {
        String user = txt_user.getText().toString();
        String password = txt_password.getText().toString();

        if (user.equals("") && password.equals("")) {
            Toast.makeText(getApplicationContext(), "Informe os dados para o login!", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM user WHERE user = '"+user+"' AND password = '"+password+"'";
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "Email e senha incorretos! Verifique e tente novamente", Toast.LENGTH_SHORT).show();
            } else {
                String user_name = "";
                int user_id = 0;
                cursor.moveToFirst();
                for(int i=0; i<cursor.getCount(); i++) {
                    user_id = cursor.getInt(0);
                    user_name = cursor.getString(1);
                    cursor.moveToNext();
                }
                cursor.close();

                SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_name", user_name);
                editor.putInt("user_id", user_id);
                editor.apply();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        }
    }

    public void onClickNew() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}