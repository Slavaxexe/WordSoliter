package com.example.wordsoliter;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class DrawThread extends Thread {
    private LevelGenerator.Level level;
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
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

    public void OnTouch(float x, float y){
        if (isGameStarted && !isGameFinished){
            int column = deck.onTouch(x, y);
            if (!(column == -1)) {
                String letter = level.get(column).get(level.get(column).size() - 1);
                user_answer.add(letter);
                user_ind.add(column);
                ArrayList<Integer> answer = level.answers_ind.get(level.answers_ind.size() - 1);
                if (user_answer.size() == answer.size()){
                    boolean f = true;
                    for(int i = 0; i < user_answer.size(); i++){
                        if (user_answer.get(i).charAt(0) != level.answers_words.get(level.answers_words.size() - 1). charAt(i)){
                            Toast.makeText(context, "Неправильное слово", Toast.LENGTH_SHORT).show();
                            f = false;
                            break;
                        }
                    }
                    if (f) {
                        Collections.sort(user_ind);
                        if (!user_ind.equals(level.answers_ind.get(level.answers_ind.size() - 1))) {
                            Toast.makeText(context, "Попробуй другие буквы", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show();
                            level.answers_words.remove(level.answers_words.size() - 1);
                            level.answers_ind.remove(level.answers_ind.size() - 1);
                            for (int i = 0; i < user_ind.size(); i++){
                                level.get(user_ind.get(i)).remove(level.get(user_ind.get(i)).size() - 1);
                            }
                            if (level.answers_words.size() == 0){
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
                Bitmap cardback = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.cardback2);
                Canvas canvas = surfaceHolder.lockCanvas();
                double scale = (double) canvas.getWidth() / deck.level.size() / cardback.getWidth();
                surfaceHolder.unlockCanvasAndPost(canvas);
                cardback = Bitmap.createScaledBitmap(cardback, (int) (cardback.getWidth() * scale), (int) (cardback.getHeight() * scale), true);
                Bitmap cardfront = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.cardfront2);
                cardfront = Bitmap.createScaledBitmap(cardfront, (int) (cardfront.getWidth() * scale), (int) (cardfront.getHeight() * scale), true);
                deck.setBitmaps(cardback, cardfront);
                {// подсказки
                    for (int i = level.answers_words.size() - 1; i > -1; i--) {
                        Log.e("Answers", level.answers_words.get(i));
                    }
                }
            }
        }

        isGameStarted = true;
        isGameFinished = false;
        while (running) {

             Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                deck.drawDeck(canvas, user_ind);
                deck.drawUserAnswer(canvas, user_answer);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}