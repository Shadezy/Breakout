package com.cartmell.travis.tcartmellfinal;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import java.io.Serializable;

public class Brick implements Serializable {
    private float height;
    private float width;
    private float xLoc;//topLeft corner
    private float yLoc;//topLeft corner
    private int durability = 2;

    private transient RectF myRect;

    public Brick(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public void makeRect() {
        myRect = new RectF(xLoc,yLoc,xLoc + width,yLoc + height);
    }

    public RectF getRect() {
        return myRect;
    }

    public float setxLoc(float xLoc) {
        this.xLoc = xLoc;
        return xLoc;
    }

    public float setyLoc(float yLoc) {
        this.yLoc = yLoc;
        return yLoc;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getxLoc() {
        return xLoc;
    }

    public float getyLoc() {
        return yLoc;
    }

    public void erase(){
        durability--;
        if (durability == 0)
            myRect.set(0,0,0,0);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public String toString() {
        return "xLoc: " + xLoc + ", yLoc: " + yLoc;
    }

    public void draw(Canvas canvas, Paint brickPaint) {
        if (durability == 1)
            brickPaint.setColor(Color.WHITE);
        else
            brickPaint.setColor(Color.RED);
        canvas.drawRect(myRect, brickPaint);
    }
}
