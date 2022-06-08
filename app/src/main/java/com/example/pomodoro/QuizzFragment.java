package com.example.pomodoro;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pomodoro.utils.DatabaseHelper;
import com.example.pomodoro.utils.Questions;
import com.example.pomodoro.utils.QuizzAdapter;
import com.tooltip.Tooltip;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizzFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizzFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayAdapter<Questions> adapter;
    private DatabaseHelper helper;
    private RadioGroup radioGroup;
    private String user_name;
    private int user_id, answer_pomodoro = 0;
    private ListView questions_list;
    private Button save;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();

    public static final String DATABASE_NAME = "pomodoro";
    public static final String url = "jdbc:mysql://pomodoro.cdoluywguze8.us-east-1.rds.amazonaws.com:3306/" +
            DATABASE_NAME;
    public static final String db_username = "admin", db_password = "98657808";
    public static final String TABLE_NAME = "quizz";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizzFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizzFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizzFragment newInstance(String param1, String param2) {
        QuizzFragment fragment = new QuizzFragment();
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
        View view = inflater.inflate(R.layout.fragment_quizz, container, false);

        helper = new DatabaseHelper(getActivity());

        radioGroup = view.findViewById(R.id.radioGroupPomodoro);

        questions_list = view.findViewById(R.id.lv_questions);

        Questions items = new Questions(getActivity());
        List<Questions> questions = items.listQuestions(getActivity());
        adapter = new QuizzAdapter(Objects.requireNonNull(getActivity()), R.layout.quizz_list, questions);
        questions_list.setAdapter(adapter);

        final Tooltip[] tooltip = new Tooltip[1];
        final int[] tooltip_ok = {2};

        ImageButton help = view.findViewById(R.id.help);
        help.setOnClickListener(view1 -> {
            if (tooltip_ok[0] == 1) {
                tooltip[0].dismiss();
                tooltip_ok[0] = 2;
            } else {
                tooltip_ok[0] = 1;
                tooltip[0] = new Tooltip.Builder(help)
                        .setText("Legenda:\n " +
                                "CT - Concordo Totalmente\n " +
                                " C - Concordo\n " +
                                "ND - Não concordo nem discordo\n " +
                                " D - Discordo\n" +
                                "DT - Discordo Totalmente")
                        .setGravity(Gravity.START)
                        .show();
            }
        });

        save = view.findViewById(R.id.save_questions);
        save.setOnClickListener(view1 -> saveQuizz());

        return view;
    }

    private void saveQuizz() {
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean save_ok = true;
        long result;
        ContentValues values;
        int answer = radioGroup.getCheckedRadioButtonId();
        if (answer == R.id.radioButton_sim) {
            answer_pomodoro = 1;
        } else if (answer == R.id.radioButton_nao) {
            answer_pomodoro = 2;
        }
        if (answer_pomodoro != 0) {
            SharedPreferences settings = Objects.requireNonNull(getActivity()).getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            user_name = settings.getString("user_name", "");
            user_id = settings.getInt("user_id", 0);

            for (int i = 0; i < adapter.getCount(); i++) {
                int answer_option = adapter.getItem(i).answer_option;
                if (answer_option != 0) {
                    int ct = 2, c = 2, nd = 2, d = 2, dt = 2;

                    values = new ContentValues();
                    values.put("user", user_name);
                    values.put("user_id", user_id);
                    values.put("question", adapter.getItem(i).question_name);
                    values.put("CT", ct);
                    values.put("C", c);
                    values.put("ND", nd);
                    values.put("D", d);
                    values.put("DT", dt);
                    values.put("observation", adapter.getItem(i).answer_observation);
                    values.put("pomodoro", answer_pomodoro);

                    if (answer_option == adapter.getItem(i).ct) {
                        values.put("CT", 1);
                    } else if (answer_option == adapter.getItem(i).c) {
                        values.put("C", 1);
                    } else if (answer_option == adapter.getItem(i).nd) {
                        values.put("ND", 1);
                    } else if (answer_option == adapter.getItem(i).d) {
                        values.put("D", 1);
                    } else if (answer_option == adapter.getItem(i).dt) {
                        values.put("DT", 1);
                    }

                    result = db.insert("answer", null, values);

                    if (result == -1) {
                        save_ok = false;
                        Toast.makeText(getActivity(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    save_ok = false;
                    Toast.makeText(getActivity(), "Responda a todas as perguntas!", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (save_ok) {
                progressBar = new ProgressDialog(getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Enviando os dados para o servidor... Aguarde!");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                //reset progress bar and filesize status
                progressBarStatus = 0;
                save.setEnabled(false);
                save(); //envia dados para o servidor
            }
        } else {
            Toast.makeText(getActivity(), "Responda a todas as perguntas!", Toast.LENGTH_SHORT).show();
        }
    }

    public void save() throws SQLException {
        new Thread(() -> {
            //do your work
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, db_username, db_password);
                Statement statement = connection.createStatement();
                for (int i = 0; i < adapter.getCount(); i++) {
                    int answer_option = adapter.getItem(i).answer_option;
                    int ct = 2, c = 2, nd = 2, d = 2, dt = 2, value = 0;

                    if (answer_option == adapter.getItem(i).ct) {
                        ct = 1;
                        value = 5;
                    } else if (answer_option == adapter.getItem(i).c) {
                        c = 1;
                        value = 4;
                    } else if (answer_option == adapter.getItem(i).nd) {
                        nd = 1;
                        value = 3;
                    } else if (answer_option == adapter.getItem(i).d) {
                        d = 1;
                        value = 2;
                    } else if (answer_option == adapter.getItem(i).dt) {
                        dt = 1;
                        value = 1;
                    }

                    statement.execute("INSERT INTO " + TABLE_NAME +
                            "(user_id, user, question, CT, C, ND, D, DT, pomodoro, observation, value) " +
                            "VALUES('" + user_id + "', '" + user_name + "', '" + adapter.getItem(i).question_name + "', '" +
                            ct + "', '" + c + "', '" + nd + "', '" + d + "', '" + dt + "', '" + answer_pomodoro +
                            "', '" + adapter.getItem(i).answer_observation + "', '" + value + "')");

                    progressBarStatus = progressBarStatus+10;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarHandler.post(() -> {
                        progressBar.setProgress(progressBarStatus);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                // after the job is finished:
                Toast.makeText(getActivity(), "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
                save.setEnabled(true);
                clearFields();
            });
        }).start();
    }

    public void clearFields() {
        radioGroup.clearCheck();
        questions_list.clearChoices();
        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getItem(i).setAnswer_observation("");
            adapter.getItem(i).setAnswer_option(0);
        }
        questions_list.setSelectionAfterHeaderView();
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}