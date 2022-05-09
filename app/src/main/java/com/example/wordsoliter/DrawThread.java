package com.example.wordsoliter;



import android.content.Context;
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

public class DrawThread extends Thread {
    private LevelGenerator.Level level;
    private ArrayList<Animation> animation = new ArrayList<>();
    private float[] cardcoords = new float[2];
    private String cardletter;
    private int cardc = -1;
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




    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        user_ind = new ArrayList<>();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
    }



    public void requestStop() {
        running = false;
    }

    public void OnTouch(MotionEvent event){
        if (isGameStarted && !isGameFinished){
            float x = event.getX(), y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // нажатие
                    int column = Math.round(deck.onTouch(x, y).get(0));
                    if (column != -1 && !cf) {
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
                    if (ind != - 1 && cardletter != null){
                        user_answer[ind] = cardletter;
                        user_ind.add(cardc);
                    }
                    Log.e("ind", ind + "");
                    cardc = -1;
                    cardletter = null;
                    cf = false;
                    break;
            }
            if (!Arrays.stream(user_answer).anyMatch("!"::equals)) {
                while (animation.size() != 0){}
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
                        }
                    }
                }
                user_answer = new String[level.answers_words.get(level.answers_words.size() - 1).length()];
                for (int i = 0; i < user_answer.length; i++) user_answer[i] = "!";
                user_ind.clear();
            }
        }
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
                level = levelGenerator.generateLevel(0);
                deck = new CardDeck(level);
                Bitmap cardback = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardback1);
                Bitmap cardfront = BitmapFactory.decodeResource(context.getResources(), R.drawable.cardfront1);
                Bitmap emptycard = BitmapFactory.decodeResource(context.getResources(), R.drawable.emptycard1);
                Canvas canvas = surfaceHolder.lockCanvas();
                double scale = (double) canvas.getWidth() / deck.level.size() / cardback.getWidth();
                surfaceHolder.unlockCanvasAndPost(canvas);
                cardback = Bitmap.createScaledBitmap(cardback, (int) (cardback.getWidth() * scale), (int) (cardback.getHeight() * scale), true);
                cardfront = Bitmap.createScaledBitmap(cardfront, (int) (cardfront.getWidth() * scale), (int) (cardfront.getHeight() * scale), true);
                emptycard = Bitmap.createScaledBitmap(emptycard, (int) (emptycard.getWidth() * scale), (int) (emptycard.getHeight() * scale), true);
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
        for (String i: user_answer) Log.v("agde", i);
        isGameStarted = true;
        isGameFinished = false;
        Log.e("card", String.valueOf(cardcoords[0]));
        while (running) {

             Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                if (level.answers_words.size() > 0) {
                    deck.drawDeck(canvas, user_ind, cardc);
                    deck.drawUserAnswer(canvas, user_answer, animation.size());
                    if (cardletter != null){
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setTextSize(100);
                        deck.drawCard(canvas, deck.cardfront, cardcoords[0] - deck.cardfront.getWidth() / 2, cardcoords[1] - deck.cardfront.getHeight() / 2, cardletter, paint);
                    }
                    animation.removeIf(card -> card.x1 == card.x2 && card.y1 == card.y2);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    class Animation{
        float x1, y1, x2, y2, speedx, speedy;
        String letter;
        Animation(int x1, int y1, int x2, int y2, String letter){
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.letter = letter;
            this.speedy = 200;
            this.speedx = Math.round(x2 - x1) / ((y2 - y1) / (speedy));
        }

        public void setNewCoords(){
            if(speedx > 0) x1 = Math.min(x1 + speedx, x2);
            else x1 = Math.max(x1 + speedx, x2);
            if(speedy > 0) y1 = Math.min(y1 + speedy, y2);
            else y1 = Math.max(y1 + speedy, y2);
        }
    }
}