package com.example.wordsoliter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Objects;
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
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bnv, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();
        OpenDbHelper dbHelper = new OpenDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                OpenDbHelper._ID,
                OpenDbHelper.COLUMN_GAMESEWON,
                OpenDbHelper.COLUMN_GAMESMWON,
                OpenDbHelper.COLUMN_GAMESHWON,
                OpenDbHelper.COLUMN_GAMESIWON,
                OpenDbHelper.COLUMN_WORDSMADE,
                OpenDbHelper.COLUMN_HINTSUSED };


        Cursor cursor = db.query(
                OpenDbHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (cursor.getCount() == 0) {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(OpenDbHelper.COLUMN_GAMESEWON, 0);
            values.put(OpenDbHelper.COLUMN_GAMESMWON, 0);
            values.put(OpenDbHelper.COLUMN_GAMESHWON, 0);
            values.put(OpenDbHelper.COLUMN_GAMESIWON, 0);
            values.put(OpenDbHelper.COLUMN_WORDSMADE, 0);
            values.put(OpenDbHelper.COLUMN_HINTSUSED, 0);
            long newRowId = db.insert(OpenDbHelper.TABLE_NAME, null, values);
        }
        cursor.close();
    }
    @Override
    public void onPause() {
        // save your data
        super.onPause();
    }


}