package com.example.wordsoliter;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class OpenDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = OpenDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "WordSoliter.db";
    private static final int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "statistic";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_GAMESEWON = "gameseasywon";
    public final static String COLUMN_GAMESMWON = "gamesmediumwon";
    public final static String COLUMN_GAMESHWON = "gameshardwon";
    public final static String COLUMN_GAMESIWON = "gamesinsanewon";
    public final static String COLUMN_WORDSMADE = "wordsmade";
    public final static String COLUMN_HINTSUSED = "hintused";

    public OpenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GAMESEWON + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_GAMESMWON + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_GAMESHWON + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_GAMESIWON + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_WORDSMADE + " INTEGER NOT NULL DEFAULT 0, "
                + COLUMN_HINTSUSED + " INTEGER NOT NULL DEFAULT 0); ";


        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}