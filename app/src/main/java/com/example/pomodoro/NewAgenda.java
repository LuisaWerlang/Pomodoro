package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class NewAgenda extends AppCompatActivity {

    int mhour, mMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_agenda);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String date = getIntent().getStringExtra("date");

        EditText et_date = findViewById(R.id.date);
        et_date.setEnabled(false);
        et_date.setText(date);

        EditText et_hour = findViewById(R.id.hour);

        TextView select_hour = findViewById(R.id.select_hour);
        select_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                mhour = c.get(Calendar.HOUR_OF_DAY);
                mMin = c.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(NewAgenda.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        et_hour.setText(hourOfDay+":"+minute);
                    }
                }, mhour, mMin, true);
                time.show();
            }
        });

        Button save = findViewById(R.id.save_agenda);
        save.setOnClickListener(view -> onSaveAgenda());
    }

    public void onSaveAgenda() {

    }
}