package com.example.pomodoro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.pomodoro.R;
import java.util.List;

public class QuizzAdapter extends ArrayAdapter<Questions> {

    private final LayoutInflater inflater;
    private final int resourceId;
    private final List<Questions> items;

    public QuizzAdapter(@NonNull Context context, int resource, List<Questions> items) {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
        this.items = items;
    }

    public static class ItemsRepositorio {
        Questions questions;
        TextView txt_question;
        RadioGroup radioGroup;
        EditText et_observation;
    }

    private void setupItem(ItemsRepositorio repositorio) {
        repositorio.txt_question.setText(repositorio.questions.question_name);
        repositorio.radioGroup.check(repositorio.questions.getAnswer_option());
        String obs = String.valueOf(repositorio.questions.getAnswer_observation());
        if (obs.equals(""))
            repositorio.et_observation.setText("");
        else
            repositorio.et_observation.setText(String.valueOf(repositorio.questions.getAnswer_observation()));
    }

    private void setVal1TextChangeListener(final ItemsRepositorio repositorio) {

        repositorio.et_observation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                if(s.toString().length()>0)
                    repositorio.questions.setAnswer_observation(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        repositorio.radioGroup.setOnCheckedChangeListener((mradRadioGroup, checkedId) ->
                repositorio.questions.setAnswer_option(mradRadioGroup.getCheckedRadioButtonId()));
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemsRepositorio repositorio;
        convertView = inflater.inflate(resourceId, parent, false);

        repositorio = new ItemsRepositorio();
        repositorio.questions = items.get(position);
        repositorio.txt_question = convertView.findViewById(R.id.tv_question);
        repositorio.et_observation = convertView.findViewById(R.id.et_observation);
        repositorio.radioGroup = convertView.findViewById(R.id.radioGroup);
        repositorio.questions.ct = R.id.radioButton_ct;
        repositorio.questions.c = R.id.radioButton_c;
        repositorio.questions.nd = R.id.radioButton_nd;
        repositorio.questions.d = R.id.radioButton_d;
        repositorio.questions.dt = R.id.radioButton_dt;

        setVal1TextChangeListener(repositorio);
        convertView.setTag(repositorio);
        setupItem(repositorio);
        return convertView;
    }
}
