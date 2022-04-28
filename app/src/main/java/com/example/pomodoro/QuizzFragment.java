package com.example.pomodoro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.pomodoro.utils.DatabaseHelper;
import com.tooltip.Tooltip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DatabaseHelper helper;
    private List<Map<String, Object>> questions;
    private final String[] from = {"question"};
    private final int[] to = {R.id.tv_question};
    private ListView questions_list;

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
        String query = "SELECT * FROM question";
        questions = listQuestions(query);

        // Aqui você instancia sua ListView
        questions_list = view.findViewById(R.id.lv_questions);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), questions, R.layout.quizz_list, from, to);
        questions_list.setAdapter(adapter);
        questions_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //EditText comments = adapterView.findViewById(R.id.et_observation);
                //questions.get(i).put("comment",comments.getText().toString());
            }
        });

        final Tooltip[] tooltip = new Tooltip[1];
        final int[] tooltip_ok = {2};

        ImageButton help = view.findViewById(R.id.help);
        help.setOnClickListener(view1 -> {
            if(tooltip_ok[0] == 1) {
                tooltip[0].dismiss();
                tooltip_ok[0] = 2;
            }
            else {
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

        Button save = view.findViewById(R.id.save_questions);
        save.setOnClickListener(view1 -> {
            saveQuizz();
        });

        return view;
    }

    private void saveQuizz() {
        for(int i = 0; i < questions_list.getCount(); i++) {
            //if(questions_list.isItemChecked(i) == true) {
            //Log.e("teste", questions.get(i).toString());
            //Log.e("teste",""+questions_list.getItemAtPosition(i));
            //}
            //View convertView = questions_list.getChildAt(i);
            //final EditText edAnalise = convertView.findViewById(R.id.et_observation);
            //edAnalise.getText().toString()
            //Log.e("teste",""+convertView);
        }
    }

    private List<Map<String, Object>> listQuestions(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        questions = new ArrayList<>();
        for(int i=0; i<cursor.getCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            String question = cursor.getString(1);
            item.put("question", question);
            questions.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return questions;
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}