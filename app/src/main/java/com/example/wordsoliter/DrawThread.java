package com.example.wordsoliter;


import static java.lang.Math.max;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint paint = new Paint();
    private float x, y;
    private volatile boolean isGameStarted = false, isGameFinished = false;
    private ArrayList<String> user_answer;
    private ArrayList<Integer> user_ind;
    private Context context;
    private CardDeck deck;

    {
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }




    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        user_answer = new ArrayList<>();
        user_ind = new ArrayList<>();
        this.context = context;
        this.surfaceHolder = surfaceHolder;
    }



    public void requestStop() {
        running = false;
    }

    public void touchDetector(float x, float y){
        if (isGameStarted && !isGameFinished){
            this.x = x;
            this.y = y;
            int column = deck.onTouch(x, y);
            if (!(column == -1)) {
                String letter = deck.level.get(column).get(deck.level.get(column).size() - 1);
                user_answer.add(letter);
                user_ind.add(column);
                ArrayList<Integer> answer = deck.answers_ind.get(deck.answers_ind.size() - 1);
                if (user_answer.size() == answer.size()){
                    boolean f = true;
                    for(int i = 0; i < user_answer.size(); i++){
                        if (user_answer.get(i).charAt(0) != deck.answers_words.get(deck.answers_words.size() - 1). charAt(i)){
                            Toast.makeText(context, "Неправильное слово", Toast.LENGTH_SHORT).show();
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        Collections.sort(user_ind);
                        if (!user_ind.equals(deck.answers_ind.get(deck.answers_ind.size() - 1))) {
                            Toast.makeText(context, "Попробуй другие буквы", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show();
                            deck.answers_words.remove(deck.answers_words.size() - 1);
                            deck.answers_ind.remove(deck.answers_ind.size() - 1);
                            for (int i = 0; i < user_ind.size(); i++){
                                deck.level.get(user_ind.get(i)).remove(deck.level.get(user_ind.get(i)).size() - 1);
                            }
                            if (deck.answers_words.size() == 0){
                                Toast.makeText(context, "Вы победили!", Toast.LENGTH_SHORT).show();
                                isGameFinished = true;
                            }
                        }
                    }
                    user_answer.clear();
                    user_ind.clear();
                }
            }
        }
    }


    @Override
    public void run() {
        {//создание колоды
            deck = new CardDeck(0);
            Bitmap cardback = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.cardback2);
            Canvas canvas = surfaceHolder.lockCanvas();
            double scale = (double) canvas.getWidth() / deck.level.size() / cardback.getWidth();
            textPaint.setTextSize(canvas.getWidth() / (deck.level.size()));
            textPaint.setStyle(Paint.Style.FILL);
            surfaceHolder.unlockCanvasAndPost(canvas);
            cardback = Bitmap.createScaledBitmap(cardback, (int) (cardback.getWidth() * scale), (int) (cardback.getHeight() * scale), true);
            Bitmap cardfront = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.cardfront2);
            cardfront = Bitmap.createScaledBitmap(cardfront, (int) (cardfront.getWidth() * scale), (int) (cardfront.getHeight() * scale), true);
            deck.setBitmaps(cardback, cardfront);
        }

        isGameStarted = true;
        isGameFinished = false;
        while (running) {

             Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                deck.drawDeck(canvas);

                {//рисование ответа пользователя
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    int answer_size = deck.answers_ind.get(deck.answers_ind.size() - 1).size();
                    if (answer_size > 0) {
                        for (int i = 0; i < deck.answers_words.get(deck.answers_words.size() - 1).length(); i++) {
                            canvas.drawRect(i * textPaint.getTextSize(), textPaint.getTextSize() * 3, (i + 1) * textPaint.getTextSize(), textPaint.getTextSize() * 4, paint);
                        }
                    }
                    for (int i = 0; i < user_answer.size(); i++) {
                        canvas.drawText(user_answer.get(i), i * textPaint.getTextSize(), textPaint.getTextSize() * 4, textPaint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}