package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements CalendarView.OnDateChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CalendarView calendar;
    private int current_day, current_month, current_year;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
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
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        calendar = view.findViewById(R.id.calendarView); // get the reference of CalendarView
        calendar.setOnDateChangeListener(this);
        getCurrentDate();

        return view;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int selectedYear, int selectedMonth, int selectedDay) {
        String day = String.valueOf(selectedDay);
        if(selectedDay < 10)
            day = "0"+selectedDay;
        int correct_month = selectedMonth+1;
        String month = String.valueOf(correct_month);
        if(correct_month < 10)
            month = "0"+correct_month;
        Intent intent = new Intent(getActivity(), NewAgenda.class);
        intent.putExtra("date", day+"/"+month+"/"+selectedYear);
        startActivity(intent);
    }

    public void getCurrentDate() {
        long dateLong = calendar.getDate();
        Locale locale = new Locale("pt", "BR");

        SimpleDateFormat day = new SimpleDateFormat("dd", locale);
        SimpleDateFormat month = new SimpleDateFormat("MM", locale);
        SimpleDateFormat year = new SimpleDateFormat("yyyy", locale);

        current_day = Integer.parseInt(day.format(dateLong));
        current_month = Integer.parseInt(month.format(dateLong));
        current_year = Integer.parseInt(year.format(dateLong));
    }
}