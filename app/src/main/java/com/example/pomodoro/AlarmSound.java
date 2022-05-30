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

public class AlarmSound extends AppCompatActivity {
    private RadioGroup radioGroup;
    private MediaPlayer mp;
    private String clock_name;
    private String pomodoro_time;
    private String short_break_time;
    private String long_break_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String sound_name = getIntent().getStringExtra("sound_name");
        clock_name = getIntent().getStringExtra("clock_name");
        pomodoro_time = getIntent().getStringExtra("pomodoro_time");
        short_break_time = getIntent().getStringExtra("short_break_time");
        long_break_time = getIntent().getStringExtra("long_break_time");

        LinearLayout layout = findViewById(R.id.layout);
        radioGroup = new RadioGroup(this);

        // adding Radio Group
        layout.addView(radioGroup);
        int index = 0;
        int aux = 0;

        // creating Radio buttons Object//
        for(int x=1; x <= 8; x++) {
            RadioButton radioButtonView = new RadioButton(this);
            String name = "Sound " + x;
            radioButtonView.setText(name);
            radioButtonView.setId(x);
            if(name.equals(sound_name)) { index = aux; }
            aux++;
            radioButtonView.setHeight(100);
            String alarm_name = "sound_" + x;
            radioButtonView.setOnClickListener(view -> {
                int alarm_sound = R.raw.sound_1;
                switch (alarm_name) {
                    case "sound_1":
                        alarm_sound = R.raw.sound_1;
                        break;
                    case "sound_2":
                        alarm_sound = R.raw.sound_2;
                        break;
                    case "sound_3":
                        alarm_sound = R.raw.sound_3;
                        break;
                    case "sound_4":
                        alarm_sound = R.raw.sound_4;
                        break;
                    case "sound_5":
                        alarm_sound = R.raw.sound_5;
                        break;
                    case "sound_6":
                        alarm_sound = R.raw.sound_6;
                        break;
                    case "sound_7":
                        alarm_sound = R.raw.sound_7;
                        break;
                    case "sound_8":
                        alarm_sound = R.raw.sound_8;
                        break;
                }
                releasePlayer();
                mp = MediaPlayer.create(view.getContext(), alarm_sound);
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
            });
            radioGroup.addView(radioButtonView);
        }
        radioGroup.check(radioGroup.getChildAt(index).getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            releasePlayer();
            String alarm_name = "";
            int selection = radioGroup.getCheckedRadioButtonId();
            for (int x = 1; x <= 8; x++) {
                if (x == selection) { alarm_name = "Sound " + x; }
            }
            SharedPreferences settings = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String user_name = settings.getString("user_name", "");
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("settings_sound", alarm_name);
            it.putExtra("settings_clock", clock_name);
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