package com.example.wordsoliter;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class OpenDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = OpenDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "WordSoliter.db";
    private static final int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "skins";

    public final static String _ID = BaseColumns._ID;
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_PRICE = "price";
    public final static String COLUMN_OWNED = "owned";
    public final static String COLUMN_CHOSEN = "chosen";

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
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " INTGER NOT NULL DEFAULT 100, "
                + COLUMN_OWNED + " BOOLEAN NOT NULL DEFAULT 0, "
                + COLUMN_CHOSEN + " BOOLEAN NOT NULL DEFAULT 0);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}