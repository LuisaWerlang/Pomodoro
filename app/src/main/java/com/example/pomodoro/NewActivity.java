package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.utils.DatabaseHelper;

import java.util.Objects;

public class NewActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private EditText txt_name, txt_description;
    private CheckBox checkBox;
    private int id;
    private String screen = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        int concluded = getIntent().getIntExtra("concluded", 2);

        if (getIntent().hasExtra("screen")) {
            screen = getIntent().getStringExtra("screen");
        }

        LinearLayout ll_delete = findViewById(R.id.ll_deleteActivity);
        LinearLayout ll_concludeActivity = findViewById(R.id.ll_concludeActivity);

        TextView tv_activity = findViewById(R.id.tv_newActivity);
        if (id != 0) {
            tv_activity.setText("Lista de atividades > Editar atividade");
        } else {
            ll_delete.setVisibility(View.INVISIBLE);
            ll_concludeActivity.setVisibility(View.INVISIBLE);
        }

        helper = new DatabaseHelper(this);

        txt_name = findViewById(R.id.activity_name);
        txt_name.setText(name);
        txt_description = findViewById(R.id.activity_desc);
        txt_description.setText(description);
        checkBox = findViewById(R.id.checkbox);
        if (concluded == 1)
            checkBox.setChecked(true);

        Button save = findViewById(R.id.save_activity);
        save.setOnClickListener(view -> onSaveActivity());

        Button delete = findViewById(R.id.delete_activity);
        delete.setOnClickListener(view -> onDeleteActivity());
    }

    protected void onSaveActivity() {
        String name = txt_name.getText().toString();
        String description = txt_description.getText().toString();

        if (name.equals("")) {
            Toast.makeText(this, "Informe o nome", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();

            int concluded = 2;
            if (checkBox.isChecked())
                concluded = 1;

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("description", description);
            values.put("concluded", concluded);

            long result;
            if (id != 0) {
                String[] where = new String[]{String.valueOf(id)};
                result = db.update("activities", values, "id = ?", where);
            } else {
                result = db.insert("activities", null, values);
            }
            if (result != -1) {
                Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                onBack();
            } else {
                Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onDeleteActivity() {
        AlertDialog.Builder box = new AlertDialog.Builder(this);
        box.setTitle("Excluindo...");
        box.setIcon(android.R.drawable.ic_menu_delete);
        box.setMessage("Tem certeza que deseja excluir esta atividade?");
        box.setPositiveButton("Sim", (dialogInterface, i) -> DeleteActivity());
        box.setNegativeButton("Não", (dialogInterface, i) -> {
        });
        box.show();
    }

    public void DeleteActivity() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] where = new String[]{String.valueOf(id)};
        long result = db.delete("activities", "id = ?", where);
        if (result != -1) {
            Toast.makeText(this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
            onBack();
        } else {
            Toast.makeText(this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onBack() {
        helper.close();
        SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String user_name = settings.getString("user_name", "");
        Intent it = new Intent(this, MainActivity.class);
        if(screen.equals("pomodoro")) {
           it.putExtra("screen", "PomodoroFragment");
        } else {
            it.putExtra("screen", "ActivitiesListFragment");
        }
        it.putExtra("user_name", user_name);
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}