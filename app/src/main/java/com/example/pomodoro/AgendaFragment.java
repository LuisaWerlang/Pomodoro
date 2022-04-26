package com.example.pomodoro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.pomodoro.utils.EventDecorator;
import com.example.pomodoro.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements OnDateSelectedListener, AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int current_day, current_month, current_year;
    private List<Map<String, Object>> agendas;
    private ListView agendas_list;
    private final String[] from = {"date", "hour", "hour_notify"};
    private final int[] to = {R.id.date, R.id.hour, R.id.hour_notify};
    private LinearLayout ll_add;
    private ImageButton add_agenda;

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

        agendas_list = view.findViewById(R.id.list_view_agendas);

        ll_add = view.findViewById(R.id.ll_add);
        ll_add.setVisibility(View.INVISIBLE);
        add_agenda = view.findViewById(R.id.new_agenda);

        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView); // get the reference of CalendarView
        getCurrentDate();

        //calendarView.setSelectedDate(CalendarDay.from(current_year, current_month,current_day));
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(current_year, current_month, current_day))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.setOnDateChangedListener(this);

        Utils utils = new Utils(getActivity());
        utils.findAgendas(0, "");
        List<CalendarDay> calendarDays = utils.getCalendarDays();
        utils.close();

        calendarView.addDecorators(new EventDecorator(
               getResources().getColor(R.color.red), calendarDays));

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String day = String.valueOf(date.getDay());
        if(date.getDay() < 10)
            day = "0"+date.getDay();
        String month = String.valueOf(date.getMonth());
        if(date.getMonth() < 10)
            month = "0"+date.getMonth();
        String finalDate = day+"/"+month+"/"+date.getYear();

        Utils utils = new Utils(getActivity());
        utils.findAgendas(0, finalDate);
        agendas = utils.getAgendas();
        utils.close();

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), agendas, R.layout.agenda_list, from, to);
        agendas_list.setAdapter(adapter);

        if(agendas.size() > 0) {
            agendas_list.setOnItemClickListener(this);
            ll_add.setVisibility(View.VISIBLE);
            add_agenda.setOnClickListener(view1 -> newAgenda(finalDate));
        } else {
            ll_add.setVisibility(View.INVISIBLE);
            newAgenda(finalDate);
        }
    }

    public void newAgenda(String finalDate) {
        Intent intent = new Intent(getActivity(), NewAgenda.class);
        intent.putExtra("date", finalDate);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Map<String, Object> item = agendas.get(position);
        int agenda_id = (int) item.get("id");
        String agenda_date = (String) item.get("date");
        String agenda_hour = (String) item.get("hour");
        String agenda_hour_notify = (String) item.get("hour_notify");
        Intent intent = new Intent(getActivity(), NewAgenda.class);
        intent.putExtra("id", agenda_id);
        intent.putExtra("date", agenda_date);
        intent.putExtra("hour", agenda_hour);
        intent.putExtra("hour_notify", agenda_hour_notify);
        startActivity(intent);
    }
}