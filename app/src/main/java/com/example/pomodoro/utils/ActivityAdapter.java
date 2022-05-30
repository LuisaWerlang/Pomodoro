package com.example.pomodoro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pomodoro.R;

import java.util.List;

public class ActivityAdapter extends ArrayAdapter<Activities> {

    private final LayoutInflater inflater;
    private final int resourceId;
    private final List<Activities> items;

    public ActivityAdapter(@NonNull Context context, int resource, List<Activities> items) {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
        this.items = items;
    }

    public static class ItemsRepositorio {
        Activities activities;
        TextView txt_name, txt_description;
    }

    private void setupItem(ActivityAdapter.ItemsRepositorio repositorio) {
        repositorio.txt_name.setText(repositorio.activities.getName());
        repositorio.txt_description.setText(repositorio.activities.getDescription());
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityAdapter.ItemsRepositorio repositorio;
        convertView = inflater.inflate(resourceId, parent, false);

        repositorio = new ItemsRepositorio();
        repositorio.activities = items.get(position);
        TextView tvName = convertView.findViewById(R.id.name);
        repositorio.txt_name = tvName;
        TextView tvDescription = convertView.findViewById(R.id.description);
        repositorio.txt_description = tvDescription;

        if (items.get(position).getConcluded() == 1) {
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDescription.setPaintFlags(tvDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        convertView.setTag(repositorio);
        setupItem(repositorio);

        return convertView;
    }
}
