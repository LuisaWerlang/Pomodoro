package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pomodoro.utils.DatabaseHelper;
import com.example.pomodoro.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText pomodoroTime;
    private EditText shortBreakTime;
    private EditText longBreakTime;
    private TextView tv_alarm_sound;
    private TextView tvclock_name;
    private DatabaseHelper helper;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        helper = new DatabaseHelper(getActivity());

        String alarm_name = "";
        String clock_name = "";
        String pomodoro_time = "";
        String short_break_time = "";
        String long_break_time = "";

        Bundle mBundle;
        mBundle = getArguments();
        if(mBundle != null) {
            alarm_name = mBundle.getString("settings_sound");
            clock_name = mBundle.getString("settings_clock");
            pomodoro_time = mBundle.getString("settings_pomodoro_time");
            short_break_time = mBundle.getString("settings_short_break_time");
            long_break_time = mBundle.getString("settings_long_break_time");
        }

        Utils utils = new Utils(getActivity());
        utils.findSettings();
        if(pomodoro_time.isEmpty())
            pomodoro_time = String.valueOf(utils.getPomodoroTime());
        if(short_break_time.isEmpty())
            short_break_time = String.valueOf(utils.getShortBreakTime());
        if(long_break_time.isEmpty())
            long_break_time = String.valueOf(utils.getLongBreakTime());
        if(alarm_name.isEmpty())
            alarm_name = utils.getAlarmName();
        if(clock_name.isEmpty())
            clock_name = utils.getClockName();
        utils.close();

        pomodoroTime = v.findViewById(R.id.pomodoroTime);
        pomodoroTime.setText(pomodoro_time);//padrão 25

        shortBreakTime = v.findViewById(R.id.shortBreakTime);
        shortBreakTime.setText(short_break_time);//padrão 5

        longBreakTime = v.findViewById(R.id.longBreakTime);
        longBreakTime.setText(long_break_time);//padrão 15

        String finalClock_name = clock_name;
        tv_alarm_sound = v.findViewById(R.id.alarm_sound);
        tv_alarm_sound.setText(alarm_name);
        tv_alarm_sound.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AlarmSound.class);
            intent.putExtra("sound_name", tv_alarm_sound.getText().toString());
            intent.putExtra("clock_name", tvclock_name.getText().toString());
            intent.putExtra("pomodoro_time", pomodoroTime.getText().toString());
            intent.putExtra("short_break_time", shortBreakTime.getText().toString());
            intent.putExtra("long_break_time", longBreakTime.getText().toString());
            startActivity(intent);
        });

        tvclock_name = v.findViewById(R.id.clock_name);
        tvclock_name.setOnClickListener(view1 -> {
            if(!finalClock_name.isEmpty()) {
                Intent intent = new Intent(getActivity(), ClockSound.class);
                intent.putExtra("sound_name", tv_alarm_sound.getText().toString());
                intent.putExtra("clock_name", tvclock_name.getText().toString());
                intent.putExtra("pomodoro_time", pomodoroTime.getText().toString());
                intent.putExtra("short_break_time", shortBreakTime.getText().toString());
                intent.putExtra("long_break_time", longBreakTime.getText().toString());
                startActivity(intent);
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch clock_sound = v.findViewById(R.id.clock_sound);
        clock_sound.setOnClickListener(view1 -> {
            if(clock_sound.isChecked()) {
                Intent intent = new Intent(getActivity(), ClockSound.class);
                intent.putExtra("sound_name", tv_alarm_sound.getText().toString());
                intent.putExtra("clock_name", tvclock_name.getText().toString());
                intent.putExtra("pomodoro_time", pomodoroTime.getText().toString());
                intent.putExtra("short_break_time", shortBreakTime.getText().toString());
                intent.putExtra("long_break_time", longBreakTime.getText().toString());
                startActivity(intent);
            }
            else {
                tvclock_name.setText("");
            }
        });

        if(!clock_name.isEmpty()) {
            tvclock_name.setText(clock_name);
            clock_sound.setChecked(true);
        }

        Button salvar = v.findViewById(R.id.save_settings);
        salvar.setOnClickListener(view -> onSaveSettings());

        return v;
    }

    protected void onSaveSettings() {
        String pomodoro  = pomodoroTime.getText().toString();
        String short_break = shortBreakTime.getText().toString();
        String long_break = longBreakTime.getText().toString();
        String alarm_sound = tv_alarm_sound.getText().toString();
        String clock_sound = tvclock_name.getText().toString();

        if (pomodoro.equals("") || short_break.equals("") || long_break.equals("") ) {
            Toast.makeText(getActivity(), "Informe os tempos para salvar", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pomodoro_time", Integer.parseInt(pomodoro));
            values.put("short_break_time", Integer.parseInt(short_break));
            values.put("long_break_time", Integer.parseInt(long_break));
            values.put("alarm_sound", alarm_sound);
            values.put("clock_sound", clock_sound);

            String[] where = new String[]{"1"};
            long result = db.update("settings", values, "id = ?", where);
            if (result != -1) {
                Toast.makeText(getActivity(), "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}