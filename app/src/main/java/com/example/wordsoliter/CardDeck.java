package com.example.wordsoliter;


import static java.lang.Math.max;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {
    int levelId;
    Levels levels;
    ArrayList<ArrayList<Integer>> answers_ind;
    ArrayList<ArrayList<String>> level;
    ArrayList<String> answers_words;
    Bitmap cardback;
    Bitmap cardfront;

    public CardDeck(int id){
        //Создание уровня
        levelId = id;
        levels = new Levels();
        level = new ArrayList<>();
        answers_ind = new ArrayList<>();
        answers_words = new ArrayList<>();
        for (int i = 0; i < levels.getCountColumn(levelId); i++)
            level.add(new ArrayList<String>());
        for (String word : levels.getLevel(levelId)) {
            ArrayList<Integer> ans_ind = new ArrayList<>();
            ArrayList<Integer> possibleColumn = new ArrayList<>();
            for (int i = 0; i < levels.getCountColumn(levelId); i++) possibleColumn.add(i);
            for (int j = 0; j < word.length(); j++) {
                int a = getRandomInt(possibleColumn.size());
                ans_ind.add(possibleColumn.get(a));
                level.get(possibleColumn.get(a)).add(String.valueOf(word.charAt(j)));
                possibleColumn.remove(a);
            }
            Collections.sort(ans_ind);
            answers_words.add(word);
            answers_ind.add(ans_ind);
        }
    }

    public void setBitmaps(Bitmap cardback, Bitmap cardfront){
        this.cardback = cardback;
        this.cardfront = cardfront;
    }

    public static int getRandomInt(int max) {
        return (int) Math.floor(Math.random() * max);
    }

    public void drawDeck(Canvas canvas){
        int x = 0;
        int h = cardback.getHeight() / 10;
        int textsize = 150;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
            for (ArrayList<String> column: level) {
                int y = 0;
                for(int i = 0; i < column.size() - 1; i++){
                    canvas.drawBitmap(cardback, x, y, paint);
                    y += h;
                }
                canvas.drawBitmap(cardfront, x, y, paint);
                x += canvas.getWidth() / level.size();
            }
            for (int i = 0; i < level.size(); i++) {
                if (level.get(i).size() > 0) {
                    Rect boundsText = new Rect();
                    String letter = level.get(i).get(level.get(i).size() - 1);
                    paint.getTextBounds(letter, 0, 1, boundsText);
                    x = (cardfront.getWidth() + i * canvas.getWidth() / level.size() * 2  - boundsText.width()) / 2;
                    int y = (cardfront.getHeight() + (level.get(i).size() - 1) * h * 2 + boundsText.height()) / 2;
                    canvas.drawText(letter, x, y, paint);
                }
            }
    }

    public int onTouch(float x, float y){
        int column = (int) (x / cardback.getWidth());
        if((level.get(column).size() - 1) * (cardback.getHeight() / 10) < y && (level.get(column).size() - 1) * (cardback.getHeight() / 10) + cardback.getHeight() > y){
            return column;
        }
        else return -1;
    }


}
