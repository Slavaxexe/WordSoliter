package com.example.wordsoliter;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSession extends Thread {
    private LevelGenerator.Level level;
    private float[] cardcoords = new float[2];
    private String cardletter;
    private int cardc = -1;
    private int tier;
    private OpenDbHelper dbHelper;
    private SharedPreferences shPr;
    Bitmap cardfront, cardback, emptycard, background;
    private boolean cf = false;
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private volatile boolean isGameStarted = false, isGameFinished = false;
    private String[] user_answer;
    private ArrayList<Integer> user_ind;
    private Context context;
    private CardDeck deck;

    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }


    public GameSession(Context context, SurfaceHolder surfaceHolder, int tier) {
        user_ind = new ArrayList<>();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        this.tier = tier;
    }


    public void requestStop() {
        running = false;
    }

    public void OnTouch(MotionEvent event) {
        if (isGameStarted && !isGameFinished) {
            float x = event.getX(), y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // нажатие
                    int column = Math.round(deck.onTouch(x, y).get(0));
                    if (column != -1 && !cf && !user_ind.contains(column)) {
                        cardcoords[0] = x;
                        cardcoords[1] = y;
                        cardletter = level.get(column).get(level.get(column).size() - 1);
                        cardc = column;
                        cf = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE: // движение
                    cardcoords[0] = x;
                    cardcoords[1] = y;
                    break;
                case MotionEvent.ACTION_UP: // отпускание
                case MotionEvent.ACTION_CANCEL:
                    int ind = deck.onUp(x, y);
                    if (ind != -1 && cardletter != null) {
                        if (user_answer[ind].equals("!")) {
                            user_answer[ind] = cardletter;
                            user_ind.add(cardc);
                        }
                    }
                    cardc = -1;
                    cardletter = null;
                    cf = false;
                    break;
            }
            checkAnswers();
            if (!isGameFinished) {
                user_answer = new String[level.answers_words.get(level.answers_words.size() - 1).length()];
                Arrays.fill(user_answer, "!");
                user_ind.clear();
            }
        }
    }

    public void returnToMenu() {
        isGameFinished = true;
        Activity activity = (Activity) context;
        activity.finish();
    }

    public void checkAnswers() {
        if (!Arrays.asList(user_answer).contains("!")) {
            boolean f = true;
            for (int i = 0; i < user_answer.length; i++) {
                if (user_answer[i].charAt(0) != level.answers_words.get(level.answers_words.size() - 1).charAt(i)) {
                    Toast.makeText(context, "Неправильное слово", Toast.LENGTH_SHORT).show();
                    f = false;
                    break;
                }
            }
            if (f) {
                Collections.sort(user_ind);
                if (!user_ind.equals(level.answers_ind.get(level.answers_ind.size() - 1))) {
                    Toast.makeText(context, "Попробуй другие буквы", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = shPr.edit();
                    Resources res = context.getResources();
                    editor.putInt("money", shPr.getInt("money", res.getInteger(R.integer.moneydefualt)) + res.getInteger(R.integer.moneyforword));
                    editor.apply();
                    String query;
                    Cursor cursor;
                    ContentValues values = new ContentValues();
                    query = "SELECT " + OpenDbHelper.COLUMN_WORDSMADE +
                            " FROM " + OpenDbHelper.TABLE_NAME +
                            " WHERE " + OpenDbHelper._ID + " = 1";
                    cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
                    cursor.moveToFirst();
                    values.put(OpenDbHelper.COLUMN_WORDSMADE, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_WORDSMADE)) + 1);
                    dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                            values,
                            null, null);
                    cursor.close();
                    level.answers_words.remove(level.answers_words.size() - 1);
                    level.answers_ind.remove(level.answers_ind.size() - 1);
                    for (int i = 0; i < user_ind.size(); i++) {
                        level.get(user_ind.get(i)).remove(level.get(user_ind.get(i)).size() - 1);
                    }
                    if (level.answers_words.size() == 0) {

                        isGameFinished = true;
                        values = new ContentValues();
                        switch (tier) {
                            case 1:
                                query = "SELECT " + OpenDbHelper.COLUMN_GAMESEWON +
                                        " FROM " + OpenDbHelper.TABLE_NAME +
                                        " WHERE " + OpenDbHelper._ID + " = 1";
                                cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
                                cursor.moveToFirst();
                                values.put(OpenDbHelper.COLUMN_GAMESEWON, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_GAMESEWON)) + 1);
                                dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                                        values,
                                        null, null);
                                cursor.close();
                                break;
                            case 2:
                                query = "SELECT " + OpenDbHelper.COLUMN_GAMESMWON +
                                        " FROM " + OpenDbHelper.TABLE_NAME +
                                        " WHERE " + OpenDbHelper._ID + " = 1";
                                cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
                                cursor.moveToFirst();
                                values.put(OpenDbHelper.COLUMN_GAMESMWON, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_GAMESMWON)) + 1);
                                dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                                        values,
                                        null, null);
                                cursor.close();
                                break;
                            case 3:
                                query = "SELECT " + OpenDbHelper.COLUMN_GAMESHWON +
                                        " FROM " + OpenDbHelper.TABLE_NAME +
                                        " WHERE " + OpenDbHelper._ID + " = 1";
                                cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
                                cursor.moveToFirst();
                                values.put(OpenDbHelper.COLUMN_GAMESHWON, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_GAMESHWON)) + 1);
                                dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                                        values,
                                        null, null);
                                cursor.close();
                                break;
                            case 4:
                                query = "SELECT " + OpenDbHelper.COLUMN_GAMESIWON +
                                        " FROM " + OpenDbHelper.TABLE_NAME +
                                        " WHERE " + OpenDbHelper._ID + " = 1";
                                cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
                                cursor.moveToFirst();
                                values.put(OpenDbHelper.COLUMN_GAMESIWON, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_GAMESIWON)) + 1);
                                dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                                        values,
                                        null, null);
                                cursor.close();
                                break;

                        }
                        editor = shPr.edit();
                        res = context.getResources();
                        editor.putInt("money", shPr.getInt("money", res.getInteger(R.integer.moneydefualt)) + res.getInteger(R.integer.moneyforgame));
                        editor.apply();
                        returnToMenu();
                    }
                }
            }
        }
    }

    public void getHint() {
        Resources res = context.getResources();
        if (shPr.getInt("money", res.getInteger(R.integer.moneydefualt)) >= res.getInteger(R.integer.moneyforhint)) {
            Toast.makeText(context, level.answers_words.get(level.answers_words.size() - 1), Toast.LENGTH_LONG).show();
            String query;
            Cursor cursor;
            ContentValues values = new ContentValues();
            query = "SELECT " + OpenDbHelper.COLUMN_HINTSUSED +
                    " FROM " + OpenDbHelper.TABLE_NAME +
                    " WHERE " + OpenDbHelper._ID + " = 1";
            cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            values.put(OpenDbHelper.COLUMN_HINTSUSED, cursor.getInt(cursor.getColumnIndexOrThrow(OpenDbHelper.COLUMN_HINTSUSED)) + 1);
            dbHelper.getWritableDatabase().update(OpenDbHelper.TABLE_NAME,
                    values,
                    null, null);
            cursor.close();
            SharedPreferences.Editor editor = shPr.edit();
            editor.putInt("money", shPr.getInt("money", res.getInteger(R.integer.moneydefualt)) - res.getInteger(R.integer.moneyforhint));
            editor.apply();
        } else {
            Toast.makeText(context, "Не хватает монет", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run() {
        onCreate();
        user_answer = new String[level.answers_words.get(level.answers_words.size() - 1).length()];
        Arrays.fill(user_answer, "!");
        for (String i : user_answer) Log.v("agde", i);
        isGameStarted = true;
        isGameFinished = false;
        Log.e("card", String.valueOf(cardcoords[0]));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        while (running && !isGameFinished) {

            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(background, 0, 0, paint);
                if (level.answers_words.size() > 0) {
                    deck.drawDeck(canvas, user_ind, cardc);
                    deck.drawUserAnswer(canvas, user_answer);
                    if (cardletter != null) {
                        deck.drawCard(canvas, deck.cardfront, cardcoords[0] - deck.cardfront.getWidth() / 2, cardcoords[1] - deck.cardfront.getHeight() / 2, cardletter, paint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void onCreate() {
        dbHelper = new OpenDbHelper(context);
        LevelGenerator levelGenerator = null;
        try {
            InputStream is = context.getAssets().open("dictionary.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> dict = new ArrayList<>();
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                dict.add(line);
                i++;
            }
            levelGenerator = new LevelGenerator(dict);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert levelGenerator != null;
        level = levelGenerator.generateLevel(tier);
        deck = new CardDeck(level);
        shPr = context.getSharedPreferences("com.example.wordsoliter", Context.MODE_PRIVATE);

        switch (shPr.getInt("cardchosen", 1)) {
            case 1:
                cardback = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardback1);
                cardfront = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardfront1);
                emptycard = BitmapFactory.decodeResource(context.getResources(), R.drawable.emptycard1);
                break;
            case 2:
                cardback = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardback2);
                cardfront = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardfront2);
                emptycard = BitmapFactory.decodeResource(context.getResources(), R.drawable.emptycard2);
                break;
            case 3:
                cardback = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardback3);
                cardfront = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardfront3);
                emptycard = BitmapFactory.decodeResource(context.getResources(), R.drawable.emptycard3);
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        deck.setsize(canvas.getWidth(), canvas.getHeight());
        switch (shPr.getInt("backgroundchosen", 1)) {
            case 1:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background1);
                break;
            case 2:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
                break;
            case 3:
                background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
        }

        double scale = (double) canvas.getWidth() / 8 / cardback.getWidth();
        cardback = Bitmap.createScaledBitmap(cardback, (int) (cardback.getWidth() * scale), (int) (cardback.getHeight() * scale), true);
        cardfront = Bitmap.createScaledBitmap(cardfront, (int) (cardfront.getWidth() * scale), (int) (cardfront.getHeight() * scale), true);
        emptycard = Bitmap.createScaledBitmap(emptycard, (int) (emptycard.getWidth() * scale), (int) (emptycard.getHeight() * scale), true);
        background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
        surfaceHolder.unlockCanvasAndPost(canvas);
        deck.setBitmaps(cardback, cardfront, emptycard);
    }
}
