package com.example.pomodoro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.pomodoro.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivitiesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesListFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper helper;
    private List<Map<String, Object>> activities;
    private final String[] from = {"name", "description"};//, "concluido"};
    private final int[] to = {R.id.name, R.id.description};//, R.id.checkbox};

    public ActivitiesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivitiesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivitiesListFragment newInstance(String param1, String param2) {
        ActivitiesListFragment fragment = new ActivitiesListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activities_list, container, false);

        helper = new DatabaseHelper(getActivity());
        String query = "SELECT * FROM activities";
        activities = listActivities(query);

        // Aqui você instancia sua ListView
        ListView activities_list = view.findViewById(R.id.list_view_atividades);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), activities, R.layout.listagem, from, to);
        activities_list.setAdapter(adapter);
        activities_list.setOnItemClickListener(this);

        ImageView new_activity = view.findViewById(R.id.new_activity);
        new_activity.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), NewActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Map<String, Object> item = activities.get(position);
        String name = Html.fromHtml(String.valueOf(item.get("name")), Html.FROM_HTML_MODE_LEGACY).toString();

        Intent intent = new Intent(getActivity(), NewActivity.class);
        intent.putExtra("id", (int) item.get("id"));
        intent.putExtra("name", name);
        intent.putExtra("description", (String) item.get("description"));
        intent.putExtra("concluded", (int) item.get("concluded"));
        startActivity(intent);
    }

    private List<Map<String, Object>> listActivities(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        activities = new ArrayList<>();
        for(int i=0; i<cursor.getCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int concluded = cursor.getInt(3);
            SpannableString textoRiscado = new SpannableString(name);
            if(concluded==1) {
                textoRiscado.setSpan(new StrikethroughSpan(), 0, textoRiscado.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            item.put("id", id);
            item.put("name", textoRiscado);
            item.put("description", description);
            item.put("concluded", concluded);
            activities.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return activities;
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}