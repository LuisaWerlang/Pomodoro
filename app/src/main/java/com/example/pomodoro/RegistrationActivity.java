package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.pomodoro.utils.DatabaseHelper;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txt_user, txt_password;
    String user_name;
    private DatabaseHelper helper;
    private AmazonDynamoDBClient client;
    private String user,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        helper = new DatabaseHelper(this);

        txt_user = findViewById(R.id.txt_user);
        txt_password = findViewById(R.id.txt_password);
        Button register = findViewById(R.id.button_new);
        register.setOnClickListener(view -> onSave());
        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(view -> onBack());
    }

    public void onSave() {
        user = txt_user.getText().toString();
        password = txt_password.getText().toString();
        user_name = user;

        if (password.equals("") && user.equals("")) {
            Toast.makeText(getApplicationContext(), "Informe todos os dados para o cadastro!", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "SELECT * FROM user WHERE user = " + user + " AND password = " + password;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                Toast.makeText(getApplicationContext(), "Já existe usuário cadastrado! Faça o Login para continuar.", Toast.LENGTH_SHORT).show();
                cursor.close();
            } else {
                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user", user);
                values.put("password", password);

                long result = db.insert("user", null, values);
                if (result != -1) {
                    Toast.makeText(this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    // Initialize the Amazon Cognito credentials provider
                    CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                            getApplicationContext(),
                            "us-east-1:9c0fbd68-0f73-43df-a261-97339827e959", // Identity pool ID
                            Regions.US_EAST_2 // Region
                    );
                    client = Region.getRegion(Regions.US_EAST_2).createClient(
                            AmazonDynamoDBClient.class,
                            credentialsProvider,
                            new ClientConfiguration());
                    Thread thread = new Thread(save(result));
                    thread.start();
                    onBack();
                } else {
                    Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Runnable save(long result) {
        return () -> {
            /*
            Table table = Table.loadTable(client,"user");

            Document document = new Document();
            document.put("user_id",result);
            document.put("user",user);
            document.put("password",password);
            try {
                table.putItem(document);
            } catch (Exception exp) {
                exp.printStackTrace();
            }

             */
        };
    }

    protected void onBack() {
        helper.close();
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}