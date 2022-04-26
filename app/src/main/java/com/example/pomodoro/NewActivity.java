package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pomodoro.utils.DatabaseHelper;
import java.util.Objects;

public class NewActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private EditText txt_name, txt_description, txt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        helper = new DatabaseHelper(this);

        txt_name = findViewById(R.id.activity_name);
        txt_description = findViewById(R.id.activity_desc);
        txt_time = findViewById(R.id.activity_time);

        Button save = findViewById(R.id.save_activity);
        save.setOnClickListener(view -> onSaveActivity());
    }

    protected void onSaveActivity() {
        String name = txt_name.getText().toString();
        String description = txt_description.getText().toString();
        String time = txt_time.getText().toString();

        if (name.equals("")) {
            Toast.makeText(this, "Informe o nome", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("description", description);
            values.put("time", time);
            values.put("concluded", 2);

            long result = db.insert("activities", null, values);
            if (result != -1) {
                Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                onBack();
            } else {
                Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onBack() {
        helper.close();
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("screen", "ActivitiesListFragment");
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}