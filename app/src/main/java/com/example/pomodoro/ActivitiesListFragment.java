package com.example.pomodoro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.pomodoro.utils.Activities;
import com.example.pomodoro.utils.ActivityAdapter;
import java.util.List;
import java.util.Objects;

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
    private List<Activities> activities;

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

        // Aqui você instancia sua ListView
        ListView activities_list = view.findViewById(R.id.list_view_atividades);

        Activities items = new Activities(getActivity());
        activities = items.listActivities(getActivity());
        ArrayAdapter<Activities> adapter = new ActivityAdapter(Objects.requireNonNull(getActivity()), R.layout.listagem, activities);
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
        Activities item = activities.get(position);

        Intent intent = new Intent(getActivity(), NewActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("name", item.getName());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("concluded", item.getConcluded());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}