package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Objects;

public class ClockSound extends AppCompatActivity {
    private RadioGroup radioGroup;
    private MediaPlayer mp;
    private String sound_name;
    private String pomodoro_time;
    private String short_break_time;
    private String long_break_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_sound);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sound_name = getIntent().getStringExtra("sound_name");
        String clock_name = getIntent().getStringExtra("clock_name");
        pomodoro_time = getIntent().getStringExtra("pomodoro_time");
        short_break_time = getIntent().getStringExtra("short_break_time");
        long_break_time = getIntent().getStringExtra("long_break_time");

        LinearLayout layout = findViewById(R.id.layout);
        radioGroup = new RadioGroup(this);

        // adding Radio Group
        layout.addView(radioGroup);
        int index = 0;

        // creating Radio buttons Object//
        for(int x=0; x <= 6; x++) {
            RadioButton radioButtonView = new RadioButton(this);
            String name;
            if(x==0)
                name = "Sem som";
            else
                name = "Clock " + x;
            radioButtonView.setText(name);
            radioButtonView.setId(x);
            if(name.equals(clock_name)) { index = x; }
            radioButtonView.setHeight(100);
            String alarm_name = "clock_" + x;
            radioButtonView.setOnClickListener(view -> {
                int alarm_sound = 0;
                switch (alarm_name) {
                    case "clock_1":
                        alarm_sound = R.raw.clock_1;
                        break;
                    case "clock_2":
                        alarm_sound = R.raw.clock_2;
                        break;
                    case "clock_3":
                        alarm_sound = R.raw.clock_3;
                        break;
                    case "clock_4":
                        alarm_sound = R.raw.clock_4;
                        break;
                    case "clock_5":
                        alarm_sound = R.raw.clock_5;
                        break;
                    case "clock_6":
                        alarm_sound = R.raw.clock_6;
                        break;
                }
                releasePlayer();
                if(alarm_sound != 0) {
                    mp = MediaPlayer.create(view.getContext(), alarm_sound);
                    mp.setOnCompletionListener(MediaPlayer::release);
                    mp.start();
                }
            });
            radioGroup.addView(radioButtonView);
        }
        radioGroup.check(radioGroup.getChildAt(index).getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            releasePlayer();
            String alarm_name = null;
            int selection = radioGroup.getCheckedRadioButtonId();
            for (int x = 0; x <= 6; x++) {
                if (x == selection) {
                    if(x==0)
                        alarm_name = "";
                    else
                        alarm_name = "Clock " + x;
                }
            }
            SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String user_name = settings.getString("user_name", "");
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("settings_sound", sound_name);
            it.putExtra("settings_clock", alarm_name);
            it.putExtra("settings_pomodoro_time", pomodoro_time);
            it.putExtra("settings_short_break_time", short_break_time);
            it.putExtra("settings_long_break_time", long_break_time);
            it.putExtra("user_name", user_name);
            startActivity(it);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void releasePlayer() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}