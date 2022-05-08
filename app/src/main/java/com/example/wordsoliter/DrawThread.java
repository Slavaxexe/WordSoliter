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
    private ArrayList<Animation> animation = new ArrayList<>();
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private volatile boolean isGameStarted = false, isGameFinished = false;
    private ArrayList<String> user_answer, anim_letters = new ArrayList<>();
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
            int column = Math.round(deck.onTouch(x, y).get(0));
            if (!(column == -1)) {
                String letter = level.get(column).get(level.get(column).size() - 1);
                if (!user_ind.contains(column)) {
                    user_answer.add(letter);
                    user_ind.add(column);
                    animation.add(new Animation(deck.onTouch(x, y).get(1),
                            deck.onTouch(x, y).get(2),
                            deck.getCoordsEmptyCard(user_answer.size() - 1)[0],
                            deck.getCoordsEmptyCard(user_answer.size() - 1)[1],
                            letter));

                    ArrayList<Integer> answer = level.answers_ind.get(level.answers_ind.size() - 1);
                    if (user_answer.size() == answer.size()) {
                        boolean f = true;
                        for (int i = 0; i < user_answer.size(); i++) {
                            if (user_answer.get(i).charAt(0) != level.answers_words.get(level.answers_words.size() - 1).charAt(i)) {
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
                        user_answer.clear();
                        user_ind.clear();
                    }
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

        isGameStarted = true;
        isGameFinished = false;
        int speed = 5;
        while (running) {

             Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                if (level.answers_words.size() > 0) {
                    deck.drawDeck(canvas, user_ind);
                    deck.drawUserAnswer(canvas, user_answer, animation.size());
                    for (int i = 0; i < animation.size(); i++){
                        Animation card = animation.get(i);
                        if(card.speedx > 0) card.x1 = Math.min(card.x1 + card.speedx, card.x2);
                        else card.x1 = Math.max(card.x1 + card.speedx, card.x2);
                        if(card.speedy > 0) card.y1 = Math.min(card.y1 + card.speedy, card.y2);
                        else card.y1 = Math.max(card.y1 + card.speedy, card.y2);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setTextSize(100);
                        deck.drawCard(canvas, deck.cardfront, card.x1, card.y1, card.letter, paint);
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
            this.speedy = 100;
            this.speedx = Math.round(x2 - x1) / ((y2 - y1) / (speedy));
        }
    }
}