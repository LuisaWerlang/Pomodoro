package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pomodoro.utils.DatabaseHelper;
import com.example.pomodoro.utils.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NewAgenda extends AppCompatActivity {

    private DatabaseHelper helper;
    private EditText et_date, et_hour, et_notify;
    private int id;
    private Spinner tvActivity;
    private List<String> activities;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_agenda);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = getIntent().getIntExtra("id", 0);
        String date = getIntent().getStringExtra("date");
        String hour = getIntent().getStringExtra("hour");
        String hour_notify = getIntent().getStringExtra("hour_notify");

        LinearLayout ll_delete = findViewById(R.id.ll_delete);

        TextView tv_agenda = findViewById(R.id.tv_agenda);
        if(id != 0) {
            tv_agenda.setText("Editar Agenda "+id);
        } else {
            ll_delete.setVisibility(View.INVISIBLE);
        }

        helper = new DatabaseHelper(this);

        Utils utils = new Utils(this);
        String query = "SELECT * FROM activities";
        activities = utils.listActivities(query, activities);

        tvActivity = findViewById(R.id.tvActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activities);
        tvActivity.setAdapter(adapter);

        et_date = findViewById(R.id.date);
        et_date.setEnabled(false);
        et_date.setText(date);

        et_hour = findViewById(R.id.hour);
        et_hour.setText(hour);
        et_notify = findViewById(R.id.notify);
        et_notify.setText(hour_notify);

        TextView select_hour = findViewById(R.id.select_hour);
        select_hour.setOnClickListener(view -> selectHour(et_hour));

        TextView select_hour_notify = findViewById(R.id.select_hour_notify);
        select_hour_notify.setOnClickListener(view -> selectHour(et_notify));

        Button save = findViewById(R.id.save_agenda);
        save.setOnClickListener(view -> onSaveAgenda());

        Button delete = findViewById(R.id.delete_agenda);
        delete.setOnClickListener(view -> onDeleteAgenda());
    }

    @SuppressLint("SimpleDateFormat")
    public void selectHour(EditText editText) {
        try {
            Calendar c = Calendar.getInstance();
            int mhour = c.get(Calendar.HOUR_OF_DAY);
            int mMin = c.get(Calendar.MINUTE);

            String hour = editText.getText().toString();
            if (!hour.equals("")) {
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                long finalHour = Objects.requireNonNull(hourFormat.parse(hour)).getTime() / 1000;
                Date timeD = new Date(finalHour * 1000);
                SimpleDateFormat sdf = new SimpleDateFormat("HH");
                mhour = Integer.parseInt(sdf.format(timeD));
                sdf = new SimpleDateFormat("mm");
                mMin = Integer.parseInt(sdf.format(timeD));
            }
            @SuppressLint("SetTextI18n") TimePickerDialog time = new TimePickerDialog(NewAgenda.this, (timePicker, hourOfDay, minute) -> editText.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute)), mhour, mMin, true);
            time.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onSaveAgenda() {
        String date = et_date.getText().toString();
        String hour = et_hour.getText().toString();
        String hour_notify = et_notify.getText().toString();
        int activity_id = tvActivity.getId();
        Log.e("teste",""+activity_id);

        if ((date.equals(""))||(hour.equals(""))||(hour_notify.equals(""))||(tvActivity.getSelectedItem().equals("Selecione")))
            Toast.makeText(this, "Informe todos os campos para salvar", Toast.LENGTH_SHORT).show();
        else {
            SQLiteDatabase db = helper.getWritableDatabase();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

            try {
                long finalDate = Objects.requireNonNull(dateFormat.parse(date)).getTime() / 1000;
                long finalHour = Objects.requireNonNull(hourFormat.parse(hour)).getTime() / 1000;
                long finalHourNotify = Objects.requireNonNull(hourFormat.parse(hour_notify)).getTime() / 1000;

                ContentValues values = new ContentValues();
                values.put("date", finalDate);
                values.put("hour", finalHour);
                values.put("hour_notify", finalHourNotify);
                values.put("activities_id", activity_id);

                long result;
                if (id != 0) {
                    String[] where = new String[]{String.valueOf(id)};
                    result = db.update("agenda", values, "id = ?", where);
                } else {
                    result = db.insert("agenda", null, values);
                }
                if (result != -1) {
                    Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    onBack();
                } else {
                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDeleteAgenda() {
        AlertDialog.Builder box = new AlertDialog.Builder(this);
        box.setTitle("Excluindo...");
        box.setIcon(android.R.drawable.ic_menu_delete);
        box.setMessage("Tem certeza que deseja excluir esta agenda?");
        box.setPositiveButton("Sim", (dialogInterface, i) -> DeleteAgenda());
        box.setNegativeButton("Não", (dialogInterface, i) -> {});
        box.show();
    }

    public void DeleteAgenda() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] where = new String[]{String.valueOf(id)};
        long result = db.delete("agenda", "id = ?", where);
        if (result != -1) {
            Toast.makeText(this, "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
            onBack();
        } else {
            Toast.makeText(this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onBack() {
        helper.close();
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("screen", "AgendaFragment");
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}