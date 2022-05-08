package com.example.wordsoliter;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

public class CardDeck {
    LevelGenerator.Level level;
    Bitmap cardback;
    Bitmap cardfront;

    public CardDeck(LevelGenerator.Level level){
        this.level = level;
    }

    public void setBitmaps(Bitmap cardback, Bitmap cardfront){
        this.cardback = cardback;
        this.cardfront = cardfront;
    }

    public void drawDeck(Canvas canvas, ArrayList<Integer> user_ind){
        float x = 0;
        float h = (float) cardback.getHeight() / 10;
        int textsize = 150;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
            for (int i = 0; i < level.size(); i++) {
                ArrayList<String> column = level.get(i);
                int y = 0;
                for(int j = 0; j < column.size() - 1; j++){
                    canvas.drawBitmap(cardback, x, y, paint);
                    y += h;
                }
                if (!user_ind.contains(i))  canvas.drawBitmap(cardfront, x, y, paint);
                x += (float) canvas.getWidth() / level.size();
            }
            for (int i = 0; i < level.size(); i++) {
                if (level.get(i).size() > 0 && !user_ind.contains(i)) {
                    Rect boundsText = new Rect();
                    String letter = level.get(i).get(level.get(i).size() - 1);
                    paint.getTextBounds(letter, 0, 1, boundsText);
                    x = i * cardback.getWidth() + ((float) cardback.getWidth()  - boundsText.width()) / 2;
                    float y = (level.get(i).size() - 1) * h + ((float) cardfront.getHeight() + boundsText.height()) / 2;
                    canvas.drawText(letter, x, y, paint);
                }
            }
    }

    public void drawUserAnswer(Canvas canvas, ArrayList<String> user_answer){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        int h = cardback.getHeight() / 10;
        int answer_size = level.answers_ind.get(level.answers_ind.size() - 1).size();
        if (user_answer.size() > 0) {
            for (int i = 0; i < user_answer.size(); i++) {
                Rect boundsText = new Rect();
                String letter = user_answer.get(i);
                paint.getTextBounds(letter, 0, 1, boundsText);
                float x = i * cardback.getWidth() + ((float) cardback.getWidth()  - boundsText.width()) / 2;
                float y = cardfront.getHeight() * 4 + ((float) cardfront.getHeight() + boundsText.height()) / 2;
                canvas.drawBitmap(cardfront, cardfront.getWidth() * i, cardfront.getHeight() * 4, paint);
                canvas.drawText(letter, x, y, paint);
            }
        }
    }

    public int onTouch(float x, float y){
        int column = (int) (x / cardback.getWidth());
        if((level.get(column).size() - 1) * ((float) cardback.getHeight() / 10) < y && (level.get(column).size() - 1) * ((float) cardback.getHeight() / 10) + cardback.getHeight() > y){
            return column;
        }
        else return -1;
    }


}
