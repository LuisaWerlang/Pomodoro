package com.example.pomodoro;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pomodoro.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.pomodoro.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().hasExtra("settings_sound")) {
            SettingsFragment fragment = new SettingsFragment();
            Bundle bundle = new Bundle();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            bundle.putString("settings_sound", getIntent().getStringExtra("settings_sound"));
            bundle.putString("settings_clock", getIntent().getStringExtra("settings_clock"));
            bundle.putString("settings_pomodoro_time", getIntent().getStringExtra("settings_pomodoro_time"));
            bundle.putString("settings_short_break_time", getIntent().getStringExtra("settings_short_break_time"));
            bundle.putString("settings_long_break_time", getIntent().getStringExtra("settings_long_break_time"));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment, "NewFragmentTag");
            fragmentTransaction.commit();
        } else if (getIntent().hasExtra("screen")) {
            String fragment = getIntent().getStringExtra("screen");
            if(fragment.equals("AgendaFragment")) {
                AgendaFragment agenda = new AgendaFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, agenda)
                        .commit();
            }
        } else {
            PomodoroFragment fragment = new PomodoroFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            PomodoroFragment fragment = new PomodoroFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_list) {
            // abrindo um novo fragment
            ListaAtividadesFragment fragment = new ListaAtividadesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_quizz) {
            // abrindo um novo fragment
            QuizzFragment fragment = new QuizzFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_grupos) {
            GroupFragment fragment = new GroupFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_agenda) {
            AgendaFragment fragment = new AgendaFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_tutorial) {
            TutorialFragment fragment = new TutorialFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_config) {
            SettingsFragment fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}