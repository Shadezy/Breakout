package com.cartmell.travis.tcartmellfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Paddle {

    private float width;
    private float height;
    private float xLoc = 15f * 10f;
    private float yLoc = 14f * 10f;
    private float offset = 0.2f * 10f;
    private int directionX;
    private RectF myRect;


    public Paddle(float w, float h) {
        this.width = w;
        this.height = h;
        myRect = new RectF(xLoc,yLoc,xLoc + width,yLoc + height);
    }

    public RectF getRect() {
        return myRect;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }

    public void reset() {
        xLoc = 15f * 10f;
        yLoc = 14f * 10f;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (xLoc <= 0) {
            xLoc = offset;
            myRect = new RectF(xLoc-width/2,yLoc,xLoc + width/2,yLoc + height);
            canvas.drawRect(myRect, paint);

        }
        else if (xLoc >= BreakoutView.xMax) {
            xLoc = BreakoutView.xMax - offset;
            myRect = new RectF(xLoc-width/2,yLoc,xLoc + width/2,yLoc + height);
            canvas.drawRect(myRect, paint);
        }

        else {
            if (directionX == 0) {

            } else if (directionX == 1) {
                xLoc -= offset;
            } else if (directionX == 2) {
                xLoc += offset;
            }
            myRect = new RectF(xLoc-width/2,yLoc,xLoc + width/2,yLoc + height);
            canvas.drawRect(myRect, paint);
        }
    }
}
