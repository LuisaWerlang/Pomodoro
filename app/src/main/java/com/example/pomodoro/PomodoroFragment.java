package com.example.pomodoro;

import android.annotation.SuppressLint;
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

    private Button start;
    private int pomodoro_time;
    private TextView tv;
    private MediaPlayer mp;
    private int pomodoro_amount = 0;
    private DatabaseHelper helper;
    private List<String> activities;
    private Spinner tvActivity;

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
        String alarm_name = utils.getAlarmName();

        tv = view.findViewById(R.id.tvCountDownTimer);
        tv.setText(pomodoro_time+" : 00");

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
        String query = "SELECT * FROM activities";
        activities = utils.listActivities(query, activities);
        utils.close();

        tvActivity = view.findViewById(R.id.tvActivity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        tvActivity.setAdapter(adapter);

        start = view.findViewById(R.id.start);
        int finalAlarm_sound = alarm_sound;
        start.setOnClickListener(view1 -> {
            if(tvActivity.getSelectedItem().equals("Selecione"))
                Toast.makeText(getActivity(), "Selecione a sua atividade!", Toast.LENGTH_SHORT).show();
            else {
                startChronometer(tv, finalAlarm_sound);
                start.setEnabled(false);
                tvActivity.setEnabled(false);
            }
        });

        return view;
    }

    public void startChronometer(TextView tv, int alarm_sound) {
        int time = 1;
        if(start.getText().equals("Iniciar Pomodoro")) {
            pomodoro_amount++;
        }
        else if(start.getText().equals("Iniciar pausa curta"))
            time = 2;
        else if(start.getText().equals("Iniciar pausa longa")) {
            time = 3;
            pomodoro_amount = 0;
        }

        releasePlayer();
        mp = MediaPlayer.create(getActivity(), alarm_sound);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.setOnCompletionListener(mp -> {
            // TODO Auto-generated method stub
            mp = null;
        });

        MyCountDownTimer timer = new MyCountDownTimer(getActivity(), tv, (long) pomodoro_time*60*1000, 1000, start, time, mp, pomodoro_amount, tvActivity);
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
    public void onDestroy() {
        helper.close();
        releasePlayer();
        super.onDestroy();
    }
}