package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.utils.DatabaseHelper;
import com.example.pomodoro.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PomodoroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomodoroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button start, new_activity;
    private int pomodoro_time, short_break_time, long_break_time;
    private TextView tv;
    private MediaPlayer mp;
    private int pomodoro_amount = 0;
    private DatabaseHelper helper;
    private Spinner tvActivity;
    private String pomodoro_text;
    private MyCountDownTimer timer=null;

    public PomodoroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomodoroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PomodoroFragment newInstance(String param1, String param2) {
        PomodoroFragment fragment = new PomodoroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        Utils utils = new Utils(getActivity());
        utils.findSettings();
        pomodoro_time = utils.getPomodoroTime();
        short_break_time = utils.getShortBreakTime();
        long_break_time = utils.getLongBreakTime();
        String alarm_name = utils.getAlarmName();
        pomodoro_amount = utils.getPomodoroAmount();
        pomodoro_text = utils.getPomodoroText();

        tv = view.findViewById(R.id.tvCountDownTimer);
        switch (pomodoro_text) {
            case "Iniciar Pomodoro":
                tv.setText(pomodoro_time + " : 00");
                break;
            case "Iniciar pausa curta":
                tv.setText(short_break_time + " : 00");
                break;
            case "Iniciar pausa longa":
                tv.setText(long_break_time + " : 00");
                break;
        }

        int alarm_sound = R.raw.sound_1;
        switch (alarm_name) {
            case "Sound 1":
                alarm_sound = R.raw.sound_1;
                break;
            case "Sound 2":
                alarm_sound = R.raw.sound_2;
                break;
            case "Sound 3":
                alarm_sound = R.raw.sound_3;
                break;
            case "Sound 4":
                alarm_sound = R.raw.sound_4;
                break;
            case "Sound 5":
                alarm_sound = R.raw.sound_5;
                break;
            case "Sound 6":
                alarm_sound = R.raw.sound_6;
                break;
            case "Sound 7":
                alarm_sound = R.raw.sound_7;
                break;
            case "Sound 8":
                alarm_sound = R.raw.sound_8;
                break;
        }

        helper = new DatabaseHelper(getActivity());
        String query = "SELECT * FROM activities WHERE concluded=2";
        List<String> activities = utils.listActivities(query);
        utils.close();

        tvActivity = view.findViewById(R.id.tvActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        tvActivity.setAdapter(adapter);

        new_activity = view.findViewById(R.id.new_activity);
        new_activity.setOnClickListener(view1 -> newActivity());

        start = view.findViewById(R.id.start);
        start.setText(pomodoro_text);
        int finalAlarm_sound = alarm_sound;
        start.setOnClickListener(view1 -> {
            if (tvActivity.getSelectedItem().equals("Selecione"))
                Toast.makeText(getActivity(), "Selecione a sua atividade!", Toast.LENGTH_SHORT).show();
            else {
                startChronometer(tv, finalAlarm_sound);
                start.setEnabled(false);
                new_activity.setEnabled(false);
                tvActivity.setEnabled(false);
            }
        });

        return view;
    }

    public void newActivity() {
        Intent intent = new Intent(getActivity(), NewActivity.class);
        intent.putExtra("screen","pomodoro");
        startActivity(intent);
    }

    public void startChronometer(TextView tv, int alarm_sound) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int time = 1;
        int clock_time = pomodoro_time;
        if (start.getText().equals("Iniciar Pomodoro")) {
            pomodoro_amount++;
            clock_time = pomodoro_time;
            values.put("pomodoro_text", "Iniciar Pomodoro");
        } else if (start.getText().equals("Iniciar pausa curta")) {
            time = 2;
            clock_time = short_break_time;
            values.put("pomodoro_text", "Iniciar pausa curta");
        } else if (start.getText().equals("Iniciar pausa longa")) {
            time = 3;
            pomodoro_amount = 0;
            clock_time = long_break_time;
            values.put("pomodoro_text", "Iniciar pausa longa");
        }
        values.put("pomodoro_amount", pomodoro_amount);
        values.put("timeon", 1);
        String[] where = new String[]{"1"};
        db.update("settings", values, "id = ?", where);

        releasePlayer();
        mp = MediaPlayer.create(getActivity(), alarm_sound);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.setOnCompletionListener(mp -> {
            // TODO Auto-generated method stub
            mp = null;
        });

        timer = new MyCountDownTimer(getActivity(), tv, (long) clock_time * 60 * 1000, 1000, start, time, mp, pomodoro_amount, tvActivity, new_activity);
        timer.start();
    }

    private void releasePlayer() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onPause() {
        if(timer!=null)
            timer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(timer!=null)
            timer.cancel();
        helper.close();
        releasePlayer();
        super.onDestroy();
    }
}