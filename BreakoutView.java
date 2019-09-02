package com.cartmell.travis.tcartmellfinal;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BreakoutView extends View implements TimeAnimator.TimeListener, View.OnTouchListener, MediaPlayer.OnCompletionListener {
    int w;
    int h;
    float offset = (2f/11f) * 10f;
    ArrayList<Brick> brickList = new ArrayList<>();
    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.blip);//this + playBlip makes it pretty laggy
    MediaPlayer mpDead = MediaPlayer.create(getContext(), R.raw.dead);
    int blipID = -1;
    int deadID = -2;
    Paint brickPaint = new Paint();
    Paint ballPaint = new Paint();
    Paint paddlePaint = new Paint();
    Paddle paddle;
    Ball ball = new Ball(150f/40f);
    TimeAnimator timer;
    public static final float xMax = 300;
    public static final float yMax = 150;
    public static int BRICKS = 5;
    public static int SCORE = 0;
    public static  int BRICK_DURA = 2;
    ArrayList<Integer> randomNumbers = new ArrayList<>();
    ArrayList<Integer> calledRandomNumbers = new ArrayList<>();
    ArrayList<Brick> bricksToDraw = new ArrayList<>();
    public static int LIVES = 3;
    public static int LEVEL = 1;
    private TextView tv_level;
    private TextView tv_ball;
    private TextView tv_brick;
    private TextView tv_score;
    String score = getResources().getString(R.string.score);
    String balls = getResources().getString(R.string.balls);
    String bricks = getResources().getString(R.string.bricks);
    String level = getResources().getString(R.string.level);

    public BreakoutView(Context context) {
        super(context);
        init();
    }

    public BreakoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreakoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTextView(TextView tv_level, TextView tv_ball, TextView tv_score, TextView tv_brick) {
        this.tv_level = tv_level;
        this.tv_ball = tv_ball;
        this.tv_score = tv_score;
        this.tv_brick = tv_brick;
    }

    public void init() {
        setLayerType(this.LAYER_TYPE_SOFTWARE, null);
        mp.setVolume(0.5f,0.5f);
        paddle = new Paddle(420f/10f, 150f/20f - 20f/11f);
        timer = new TimeAnimator();
        timer.setTimeListener(this);
        timer.start();
        setOnTouchListener(this);
        pause();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int realHeight = MeasureSpec.getSize(heightMeasureSpec);
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);
        double w = 300;
        double h = 150;
        double widthRatio = (w/h) * realHeight;
        double heightRatio = (h/w) * realWidth;

        double high = Math.min(widthRatio * realHeight, heightRatio * realWidth);

        if (high == widthRatio * realHeight) {
            setMeasuredDimension((int) widthRatio, realHeight);
        }

        else {
            setMeasuredDimension( realWidth, (int) heightRatio);
        }
    }

    private void playBlip(int id) {
        mp.start();
        /*if (mp!=null && id == blipID) {
            mp.pause();
            mp.seekTo(0);
            mp.start();
        }
        else {
            if (mp!=null) mp.release();
            blipID = id;
            mp = MediaPlayer.create(getContext(), id);
            mp.setOnCompletionListener(this);
            mp.setVolume(0.5f,0.5f);
            mp.start();
        }*/
    }

    private void playDead(int id) {
        mpDead.start();
        /*if (mpDead!=null && id == blipID) {
            mpDead.pause();
            mpDead.seekTo(0);
            mpDead.start();
        }
        else {
            if (mpDead!=null) mpDead.release();
            deadID = id;
            mpDead = MediaPlayer.create(getContext(), id);
            mpDead.setOnCompletionListener(this);
            mpDead.setVolume(0.5f,0.5f);
            mpDead.start();
        }*/
    }

    @Override
    public void onCompletion(MediaPlayer amp){
        amp.release();
        mp = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRGB( 0,0,0);
        float width = (float) w/300f;
        float height = (float) h/150f;
        canvas.scale(width, height);
        brickPaint.setColor(Color.RED);
        ballPaint.setColor(Color.MAGENTA);
        paddlePaint.setColor(Color.CYAN);

        canvas.save();
        drawBricks(canvas);
        canvas.restore();

        canvas.save();
        ball.draw(canvas, ballPaint);
        //canvas.drawRect(ball.getRect(), ballPaint);
        canvas.restore();

        canvas.save();
        paddle.draw(canvas, paddlePaint);
        canvas.restore();

        super.onDraw(canvas);
    }

    private void makeBricks() {
        float brickWidth = (28f/10f) * 10f;
        float brickHeight = (15f/20f - 2f/11f) * 10f;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Brick brick = new Brick(brickWidth, brickHeight);
                brick.setDurability(BRICK_DURA);
                brick.setxLoc(i*(brickWidth)+(offset)*i + offset);
                brick.setyLoc(j*(brickHeight)+(offset)*j + offset);
                brick.makeRect();
                brickList.add(brick);
            }
        }
    }

    private void drawBricks (Canvas canvas) {
        for (int i = 0; i < bricksToDraw.size(); i++) {
            canvas.save();
            bricksToDraw.get(i).draw(canvas, brickPaint);
            canvas.restore();
        }
    }

    @Override
    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
        if (ball.getxLoc() <= 0) {
            playBlip(R.raw.blip);
            ball.setVelocityX(-ball.getVelocityX());
        }
        if (ball.getxLoc() >= xMax) {
            playBlip(R.raw.blip);
            ball.setVelocityX(-ball.getVelocityX());
        }
        if (ball.getyLoc() >= yMax) {
            playDead(R.raw.dead);
            reset();
        }
        if (ball.getyLoc() <= 0) {
            playBlip(R.raw.blip);
            ball.setVelocityY(-ball.getVelocityY());
        }

        ball.setyLoc(ball.getyLoc()+ball.getVelocityY());
        ball.setxLoc(ball.getxLoc()+ball.getVelocityX());

        RectF ballRect = ball.getRect();
        for (int i = 0; i < bricksToDraw.size(); i++) {
            if (ballRect.intersect(bricksToDraw.get(i).getRect())) {
                Brick brick = bricksToDraw.get(i);
                playBlip(R.raw.blip);
                brick.erase();

                if (ball.getxLoc() >= brick.getxLoc() && ball.getxLoc() <= (brick.getxLoc()+brick.getWidth()))
                    ball.setVelocityY(-ball.getVelocityY());
                else
                    ball.setVelocityX((-ball.getVelocityX()));

                //tv_score.setText(score + String.valueOf(++SCORE));//I like it here more, but w/e
                if (brick.getDurability() == 0) {
                    tv_score.setText(score + String.valueOf(++SCORE));
                    bricksToDraw.remove(i);
                }
                tv_brick.setText(bricks + String.valueOf(bricksToDraw.size()));

                if (bricksToDraw.size() == 0)
                    nextLevel();
            }
        }
        if (ballRect.intersect(paddle.getRect())) {
            playBlip(R.raw.blip);
            ball.setVelocityY(-ball.getVelocityY());
        }
        invalidate();
    }

    private void nextLevel() {
        LIVES++;
        reset();
        tv_level.setText(level + String.valueOf(++LEVEL));
        Toast.makeText(getContext(), "Made it to level " + LEVEL, Toast.LENGTH_SHORT).show();
        calledRandomNumbers = new ArrayList<>();
        //randomNumbers = new ArrayList<>();
        bricksToDraw = new ArrayList<>();
        brickList = new ArrayList<>();
        tv_brick.setText(bricks + String.valueOf(BRICKS));
        ball.setVelocityX(1.33f*ball.getVelocityX());
        ball.setVelocityY(1.33f*ball.getVelocityY());
        makeBricks();
        generateRandomBricks();

    }

    private void reset() {
        if (LIVES == 0)
            gameOver();
        tv_ball.setText(balls + String.valueOf(--LIVES));
        ball.reset();
        paddle.reset();
        timer.pause();
    }

    public void set(int brick_count, String ball_count, String brick_hit_count) {
        LIVES = Integer.parseInt(ball_count);
        BRICKS = brick_count;
        BRICK_DURA = Integer.parseInt(brick_hit_count);
        brickList = new ArrayList<>();

        makeBricks();
        tv_score.setText(score + String.valueOf(SCORE));
        tv_brick.setText(bricks + String.valueOf(BRICKS));
        tv_ball.setText(balls + String.valueOf(LIVES));
        tv_level.setText(level + String.valueOf(LEVEL));
        Log.e("***", String.valueOf(BRICKS));
        generateRandomBricks();
    }

    private void generateRandomBricks() {
        randomNumbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            randomNumbers.add(new Integer(i));
        }
        //bricksToDraw = new ArrayList<>();

        Collections.shuffle(randomNumbers);
       // bricksToDraw.clear();
        for (int i = 0; i < BRICKS; i++) {
            bricksToDraw.add(brickList.get(randomNumbers.remove(0)));
        }

        for (int i = bricksToDraw.size(); i > BRICKS; i--)

            bricksToDraw.remove(i-1);
    }

    private void gameOver() {
        timer.end();
        Toast.makeText(getContext(), "You are dead", Toast.LENGTH_LONG).show();
    }

    public void pause() {
        timer.pause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() < w/2 && event.getY() > h/4) {
                paddle.setDirectionX(1);//left
            } else if (event.getX() > w/2 && event.getY() > h/4) {
                paddle.setDirectionX(2);//right
            }
            if (event.getX() > w/4 && event.getX() < 3*w/4 && event.getY() < h/4) {
                if (timer.isPaused())
                    timer.resume();
                else
                    timer.pause();
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            paddle.setDirectionX(0);
        }*/
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (timer.isPaused())
                timer.resume();
            else
                timer.pause();
        }
        return true;
    }

    public void leftPress(MotionEvent event) {//have to click once first to make it work for now.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            paddle.setDirectionX(1);//left
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            paddle.setDirectionX(0);
        }
    }

    public void rightPress(MotionEvent event) {//have to click once first to make it work for now.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            paddle.setDirectionX(2);//right
        }
        else if (event.getAction() == MotionEvent.ACTION_UP){
            paddle.setDirectionX(0);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        timer.pause();
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putSerializable("bricksToDraw", bricksToDraw);
        bundle.putSerializable("calledRandomNumbers", calledRandomNumbers);
        bundle.putSerializable("ball", ball);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Log.e("***","onRestoreInstanceState");
        bricksToDraw = (ArrayList<Brick>) bundle.getSerializable("bricksToDraw");
//        int change = Math.abs(bricksToDraw.size()-BRICKS);
//        for (int i = 0; i < change; i++)
//            bricksToDraw.remove(0);
        for (Brick b : bricksToDraw)
            b.makeRect();
        calledRandomNumbers = (ArrayList<Integer>) bundle.getSerializable("calledRandomNumbers");
        ball = (Ball) bundle.getSerializable("ball");
        makeBricks();
        //init();

        state = bundle.getParcelable("instanceState");
        super.onRestoreInstanceState(state);
    }
}
