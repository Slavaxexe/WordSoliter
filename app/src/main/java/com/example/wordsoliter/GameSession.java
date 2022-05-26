package com.example.wordsoliter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private ArrayList<Animation> animation = new ArrayList<>();
    private float[] cardcoords = new float[2];
    private String cardletter;
    private int cardc = -1;
    private int tier;
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
                    if (ind != -1 &&cardletter != null) {
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
            if (!Arrays.stream(user_answer).anyMatch("!"::equals)) {
                while (animation.size() != 0) {
                }
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
                        level.answers_words.remove(level.answers_words.size() - 1);
                        level.answers_ind.remove(level.answers_ind.size() - 1);
                        for (int i = 0; i < user_ind.size(); i++) {
                            level.get(user_ind.get(i)).remove(level.get(user_ind.get(i)).size() - 1);
                        }
                        if (level.answers_words.size() == 0) {
                            Toast.makeText(context, "Вы победили!", Toast.LENGTH_SHORT).show();
                            isGameFinished = true;
                            Intent intent = new Intent(context, Menu.class);
                            intent.putExtra("mode", 4);
                            ExecutorService executor = Executors.newFixedThreadPool(1);
                            executor.submit(currentThread());
                            executor.shutdownNow();
                            context.startActivity(intent);
                        }
                    }
                }
                if (!isGameFinished) {
                    user_answer = new String[level.answers_words.get(level.answers_words.size() - 1).length()];
                    for (int i = 0; i < user_answer.length; i++) user_answer[i] = "!";
                    user_ind.clear();
                }
            }
        }
    }

    public void getHint(){
        Toast.makeText(context, level.answers_words.get(level.answers_words.size() - 1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void run() {
        {//создание колоды и уровня
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
            {
                level = levelGenerator.generateLevel(tier);
                deck = new CardDeck(level);
                shPr = context.getSharedPreferences("com.example.wordsoliter", Context.MODE_PRIVATE);
                switch (shPr.getInt("cardchosen", 1)){
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
                switch (shPr.getInt("backgroundchosen", 1)){
                    case 1:
                        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background1);
                        break;
                    case 2:
                        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
                        break;
                    case 3:
                        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
                }
                double scale = (double) canvas.getWidth() / deck.level.size() / cardback.getWidth();
                cardback = Bitmap.createScaledBitmap(cardback, (int) (cardback.getWidth() * scale), (int) (cardback.getHeight() * scale), true);
                cardfront = Bitmap.createScaledBitmap(cardfront, (int) (cardfront.getWidth() * scale), (int) (cardfront.getHeight() * scale), true);
                emptycard = Bitmap.createScaledBitmap(emptycard, (int) (emptycard.getWidth() * scale), (int) (emptycard.getHeight() * scale), true);
                background = Bitmap.createScaledBitmap(background, canvas.getWidth(), canvas.getHeight(), true);
                surfaceHolder.unlockCanvasAndPost(canvas);
                deck.setBitmaps(cardback, cardfront, emptycard);
                {// подсказки
                    for (int i = level.answers_words.size() - 1; i > -1; i--) {
                        Log.e("Answers", level.answers_words.get(i));
                    }
                }
            }
        }
        user_answer = new String[level.answers_words.get(level.answers_words.size() - 1).length()];
        for (int i = 0; i < user_answer.length; i++) user_answer[i] = "!";
        for (String i : user_answer) Log.v("agde", i);
        isGameStarted = true;
        isGameFinished = false;
        Log.e("card", String.valueOf(cardcoords[0]));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        while (running) {

            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(background, 0, 0, paint);
                if (level.answers_words.size() > 0) {
                    deck.drawDeck(canvas, user_ind, cardc);
                    deck.drawUserAnswer(canvas, user_answer, animation.size());
//                    for (int i = 0; i < animation.size(); i++){
//                        Animation card = animation.get(i);
//                        if(card.speedx > 0) card.x1 = Math.min(card.x1 + card.speedx, card.x2);
//                        else card.x1 = Math.max(card.x1 + card.speedx, card.x2);
//                        if(card.speedy > 0) card.y1 = Math.min(card.y1 + card.speedy, card.y2);
//                        else card.y1 = Math.max(card.y1 + card.speedy, card.y2);
//                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                        paint.setTextSize(100);
//                        deck.drawCard(canvas, deck.cardfront, card.x1, card.y1, card.letter, paint);
//                    }
//                    animation.removeIf(card -> card.x1 == card.x2 && card.y1 == card.y2);
                    if (cardletter != null) {
                        deck.drawCard(canvas, deck.cardfront, cardcoords[0] - deck.cardfront.getWidth() / 2, cardcoords[1] - deck.cardfront.getHeight() / 2, cardletter, paint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    static class Animation {
        float x1, y1, x2, y2, speedx, speedy;
        String letter;

        Animation(int x1, int y1, int x2, int y2, String letter) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.letter = letter;
            this.speedy = 10;
            this.speedx = Math.round(x2 - x1) / ((y2 - y1) / (speedy));
        }

        public void setNewCoords() {
            if (speedx > 0) x1 = Math.min(x1 + speedx, x2);
            else x1 = Math.max(x1 + speedx, x2);
            if (speedy > 0) y1 = Math.min(y1 + speedy, y2);
            else y1 = Math.max(y1 + speedy, y2);
        }
    }
}