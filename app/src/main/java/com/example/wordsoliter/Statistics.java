package com.example.wordsoliter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Statistics extends Fragment {

    private OpenDbHelper dbHelper;


    public Statistics() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics,
                container, false);
        TextView stat = view.findViewById(R.id.stat);
        dbHelper = new OpenDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                OpenDbHelper._ID,
                OpenDbHelper.COLUMN_GAMESEWON,
                OpenDbHelper.COLUMN_GAMESMWON,
                OpenDbHelper.COLUMN_GAMESHWON,
                OpenDbHelper.COLUMN_GAMESIWON,
                OpenDbHelper.COLUMN_WORDSMADE,
                OpenDbHelper.COLUMN_HINTSUSED};


        try (Cursor cursor = db.query(
                OpenDbHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)) {


            int wonEIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_GAMESEWON);
            int wonMIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_GAMESMWON);
            int wonHIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_GAMESHWON);
            int wonIIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_GAMESIWON);
            int wordsmadeIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_WORDSMADE);
            int hintUsedIndex = cursor.getColumnIndex(OpenDbHelper.COLUMN_HINTSUSED);
            while (cursor.moveToNext()) {
                int currentwonE = cursor.getInt(wonEIndex);
                int currentwonM = cursor.getInt(wonMIndex);
                int currentwonH = cursor.getInt(wonHIndex);
                int currentwonI = cursor.getInt(wonIIndex);
                int currentwordsmade = cursor.getInt(wordsmadeIndex);
                int currenthintUsed = cursor.getInt(hintUsedIndex);

                stat.setText(("\n" + "Выиграно легкий игр" + ": " + currentwonE +
                        "\n" + "Выиграно средних игр" + ": " + currentwonM +
                        "\n" + "Выиграно сложных игр" + ": " + currentwonH +
                        "\n" + "Выиграно невозможных игр" + ": " + currentwonI +
                        "\n" + "Собрано слов" + ": " + currentwordsmade +
                        "\n" + "Использовано подсказок" + ": " + currenthintUsed));
            }
        }

        return view;
    }
}