package dev.feiyang.sereneme.Features;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import dev.feiyang.sereneme.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup navigation
        NavController navController = Navigation.findNavController(this, R.id.fragmentHost);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        NavigationUI.setupWithNavController(bottomNavView, navController);


    }




}