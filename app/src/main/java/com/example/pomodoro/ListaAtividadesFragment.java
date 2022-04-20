package com.example.pomodoro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link ListaAtividadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaAtividadesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper helper;
    private List<Map<String, Object>> activities;
    private final String[] from = {"name", "description"};//, "concluido"};
    private final int[] to = {R.id.nome, R.id.descricao};//, R.id.checkbox};

    public ListaAtividadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaAtividadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaAtividadesFragment newInstance(String param1, String param2) {
        ListaAtividadesFragment fragment = new ListaAtividadesFragment();
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
        View view = inflater.inflate(R.layout.fragment_lista_atividades, container, false);

        helper = new DatabaseHelper(getActivity());
        String query = "SELECT * FROM activities";
        activities = listActivities(query);

        // Aqui você instancia sua ListView
        ListView activities_list = view.findViewById(R.id.list_view_atividades);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), activities, R.layout.listagem, from, to);
        activities_list.setAdapter(adapter);

        activities_list.setOnItemClickListener((adapterView, view12, i, l) -> {

        });

        ImageView new_activity = view.findViewById(R.id.new_activity);
        new_activity.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new NewActivityFragment()).commit();
        });

        return view;
    }

    private List<Map<String, Object>> listActivities(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        activities = new ArrayList<>();
        for(int i=0; i<cursor.getCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int concluded = cursor.getInt(3);
            String time = cursor.getString(4);
            item.put("name", name);
            item.put("description", description);
            item.put("time", time);
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