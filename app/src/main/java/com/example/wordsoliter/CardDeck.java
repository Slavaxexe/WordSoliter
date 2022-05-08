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
    Bitmap emptycard;
    int h;

    public CardDeck(LevelGenerator.Level level){
        this.level = level;
    }

    public void setBitmaps(Bitmap cardback, Bitmap cardfront, Bitmap emptycard){
        this.cardback = cardback;
        this.cardfront = cardfront;
        this.emptycard = emptycard;
    }

    public void drawCard(Canvas canvas, Bitmap card, float x, float y, String letter, Paint paint){
        canvas.drawBitmap(card, x, y, paint);
        if (letter != null){
            Rect boundsText = new Rect();
            paint.getTextBounds(letter, 0, 1, boundsText);
            float xl = x + ((float) cardback.getWidth()  - boundsText.width()) / 2;
            float yl = y + ((float) cardfront.getHeight() + boundsText.height()) / 2;
            canvas.drawText(letter, xl, yl, paint);
        }
    }

    public void drawDeck(Canvas canvas, ArrayList<Integer> user_ind){
        float x = 0;
        h = cardback.getHeight() / 10;
        int textsize = 150;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
            for (int i = 0; i < level.size(); i++) {
                ArrayList<String> column = level.get(i);
                int y = 0;
                for(int j = 0; j < column.size() - 1; j++){
                    drawCard(canvas, cardback, x, y, null, paint);
                    y += h;
                }
                if (!user_ind.contains(i) && level.get(i).size() != 0)  drawCard(canvas, cardfront, x, y, level.get(i).get(level.get(i).size() - 1), paint);;
                x += (float) canvas.getWidth() / level.size();
            }
    }

    public void drawUserAnswer(Canvas canvas, ArrayList<String> user_answer, int x){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        for (int i = 0; i < level.answers_words.get(level.answers_words.size() - 1).length(); i++){
            drawCard(canvas, emptycard, emptycard.getWidth() * i, cardfront.getHeight() * 4, null, paint);
        }
        if (user_answer.size() > 0) {
            for (int i = 0; i < user_answer.size() - x; i++) {
                String letter = user_answer.get(i);
                drawCard(canvas, cardfront, cardfront.getWidth() * i, cardfront.getHeight() * 4, letter, paint);
            }
        }
    }

    public int[] getCoordsEmptyCard(int i){

        return new int[]{cardfront.getWidth() * i, cardfront.getHeight() * 4};
    }

    public ArrayList<Integer> onTouch(float x, float y){
        int column = (int) (x / cardback.getWidth());
        ArrayList<Integer> a = new ArrayList<>();
        if((level.get(column).size() - 1) * (h) < y && (level.get(column).size() - 1) * (h) + cardback.getHeight() > y){
            a.add(column);
            a.add(column * cardback.getWidth());
            a.add((level.get(column).size() - 1) * (h));
        }
        else {
            a.add(-1);
        }
        return (a);
    }


}
