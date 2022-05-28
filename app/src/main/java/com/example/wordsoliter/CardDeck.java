package com.example.wordsoliter;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class CardDeck {
    LevelGenerator.Level level;
    Bitmap cardback;
    Bitmap cardfront;
    Bitmap emptycard;
    Integer width, height;
    Integer cardHeight, cardWidth;
    float scale;

    {
        scale = 0.5f;
    }

    int h;

    public CardDeck(LevelGenerator.Level level) {
        this.level = level;
    }

    public void setBitmaps(Bitmap cardback, Bitmap cardfront, Bitmap emptycard) {
        this.cardback = cardback;
        this.cardfront = cardfront;
        this.emptycard = emptycard;
        cardWidth = cardback.getWidth();
        cardHeight = cardback.getHeight();
    }

    public void setsize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void drawCard(Canvas canvas, Bitmap card, float x, float y, String letter, Paint paint) {
        canvas.drawBitmap(card, x, y, paint);
        if (letter != null) {
            Rect boundsText = new Rect();
            paint.getTextBounds(letter, 0, 1, boundsText);
            float xl = x + ((float) cardWidth - boundsText.width()) / 2;
            float yl = y + ((float) cardHeight + boundsText.height()) / 2;
            canvas.drawText(letter, xl, yl, paint);
        }
    }

    public void drawDeck(Canvas canvas, Integer[] user_ind, int cardc) {
        float x = 0;
        h = cardHeight / 6;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        for (int i = 0; i < level.size(); i++) {
            ArrayList<String> column = level.get(i);
            int y = 0;
            for (int j = 0; j < column.size() - 1; j++) {
                drawCard(canvas, cardback, x, y, null, paint);
                y += h;
            }
            if (!Arrays.asList(user_ind).contains(i) && level.get(i).size() != 0 && i != cardc)
                drawCard(canvas, cardfront, x, y, level.get(i).get(level.get(i).size() - 1), paint);
            x += (float) width / level.size();
        }
    }

    public void drawUserAnswer(Canvas canvas, String[] user_answer) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        for (int i = 0; i < level.answers_words.get(level.answers_words.size() - 1).length(); i++) {
            drawCard(canvas, emptycard, cardWidth * i, height * scale, null, paint);
        }
        for (int i = 0; i < user_answer.length; i++) {
            String letter = user_answer[i];
            if (!letter.equals("!"))
                drawCard(canvas, cardfront, cardWidth * i, height * scale, letter, paint);
        }
    }



    public ArrayList<Integer> onTouch(float x, float y) {
        int column = (int) (x / cardWidth);
        ArrayList<Integer> a = new ArrayList<>();
        if ((level.get(column).size() - 1) * (h) < y && (level.get(column).size() - 1) * (h) + cardHeight > y) {
            a.add(column);
            a.add(column * cardWidth);
            a.add((level.get(column).size() - 1) * (h));
        } else {
            a.add(-1);
        }
        return (a);
    }

    public int onUp(float x, float y) {
        if (y > (float) height * scale && y < (float) height * scale + cardHeight) {
            if (x > 0 && x < cardWidth * level.answers_words.get(level.answers_words.size() - 1).length()) {
                return (int) (x / cardWidth);
            }
        }
        return -1;
    }


}
