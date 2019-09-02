package com.cartmell.travis.tcartmellfinal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.Serializable;

public class Ball implements Serializable {
    private float radius;
    private float xLoc = 15f * 10f;
    private float yLoc = 13f * 10f;
    private float velocityX = 1f;
    private float velocityY = 1f;

    public Ball(float radius) {
        this.radius = radius;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(xLoc,yLoc,radius,paint);
    }

    public RectF getRect() {
        return new RectF(xLoc-radius,yLoc-radius,xLoc + radius,yLoc + radius);
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setxLoc(float xLoc) {
        this.xLoc = xLoc;
    }

    public void setyLoc(float yLoc) {
        this.yLoc = yLoc;
    }

    public float getxLoc() {
        return xLoc;
    }

    public float getyLoc() {
        return yLoc;
    }

    public void reset() {
        xLoc = 15f * 10f;
        yLoc = 13f * 10f;

        if (velocityX < 0)
            velocityX = -velocityX;
        if (velocityY < 0)
            velocityY = -velocityY;
    }
}
