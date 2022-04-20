package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pomodoro.utils.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements OnDateSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int current_day, current_month, current_year;

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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView); // get the reference of CalendarView
        getCurrentDate();

        calendarView.setSelectedDate(CalendarDay.from(current_year, current_month,current_day));
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(current_year, current_month, current_day))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.setOnDateChangedListener(this);

        //Collection<CalendarDay> calendarDays = null;

        List<CalendarDay> calendarDays = Arrays.asList(CalendarDay.from(2022,4,20));

        calendarView.addDecorators(new EventDecorator(
               getResources().getColor(R.color.red), calendarDays));

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //buscar se existe eventos na data selecionada
        //se existir, mostrar os eventos com um botão de + caso quiser cadastrar novo
        //permitir editar as horas
        //permitir excluir

        String day = String.valueOf(date.getDay());
        if(date.getDay() < 10)
            day = "0"+date.getDay();
        String month = String.valueOf(date.getMonth());
        if(date.getMonth() < 10)
            month = "0"+date.getMonth();
        Intent intent = new Intent(getActivity(), NewAgenda.class);
        intent.putExtra("date", day+"/"+month+"/"+date.getYear());
        startActivity(intent);
    }

    public void getCurrentDate() {
        Date dateLong = new Date();
        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat day = new SimpleDateFormat("dd", locale);
        SimpleDateFormat month = new SimpleDateFormat("MM", locale);
        SimpleDateFormat year = new SimpleDateFormat("yyyy", locale);
        current_day = Integer.parseInt(day.format(dateLong));
        current_month = Integer.parseInt(month.format(dateLong));
        current_year = Integer.parseInt(year.format(dateLong));
    }
}