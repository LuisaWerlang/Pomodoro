package com.example.pomodoro;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pomodoro.utils.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper helper;
    private EditText txt_nome, txt_descricao, txt_tempo;
    private Button salvar, voltar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewActivityFragment newInstance(String param1, String param2) {
        NewActivityFragment fragment = new NewActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_activity, container, false);

        helper = new DatabaseHelper(getActivity());

        txt_nome = view.findViewById(R.id.activity_name);
        txt_descricao = view.findViewById(R.id.activity_desc);
        txt_tempo = view.findViewById(R.id.activity_time);

        salvar = view.findViewById(R.id.save_activity);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveActivity();
            }
        });

        voltar = view.findViewById(R.id.back_activity);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        return view;
    }

    protected void onSaveActivity() {
        String nome = txt_nome.getText().toString();
        String descricao = txt_descricao.getText().toString();
        String tempo = txt_tempo.getText().toString();

        if (nome.equals("")) {
            Toast.makeText(getActivity(), "Informe o nome", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nome", nome);
            values.put("descricao", descricao);
            values.put("tempo", tempo);
            values.put("concluido", 2);

            long result = db.insert("atividades", null, values);
            if (result != -1) {
                Toast.makeText(getActivity(), "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                onBack();
            } else {
                Toast.makeText(getActivity(), "Erro ao salvar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onBack() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, new ListaAtividadesFragment()).commit();
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}