package com.example.wordsoliter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    DrawView drawView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.word_solitaire);
        drawView = new DrawView(this, getIntent().getIntExtra("mode", 1));
        setContentView(drawView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.lightbulb) {
            gethint();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gethint() {
        FragmentManager manager = getSupportFragmentManager();
        HintDialogFragment hintDialogFragment = new HintDialogFragment();
        hintDialogFragment.show(manager, "myDialog");
    }

    public void useHint() {
        drawView.getHint();
    }

    public void cancel() {
        //nothing to do
    }

    @Override
    public void onBackPressed() {
        drawView.gameSession.returnToMenu();

    }
}
