package com.example.wordsoliter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuInflater;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bnv=  findViewById(R.id.bottomNavigationView);
        Set<Integer> a = new HashSet<>();
        a.add(R.id.start); a.add(R.id.shop); a.add(R.id.statistics);
        NavHostFragment navHostFragment =
                (NavHostFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration barConfiguration = new AppBarConfiguration.Builder(a).build();
        NavigationUI.setupWithNavController(bnv, navController);
        //NavigationUI.setupActionBarWithNavController(this, navController, barConfiguration);
        getSupportActionBar().hide();
    }


}