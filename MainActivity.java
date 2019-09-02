package com.cartmell.travis.tcartmellfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BreakoutView breakoutView;
    TextView ball_count;
    TextView level_count;
    TextView brick_count;
    TextView score_count;
    Button buttonLeft;
    Button buttonRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        breakoutView = findViewById(R.id.gameView);
        ball_count = findViewById(R.id.ball_count);
        level_count = findViewById(R.id.level_count);
        brick_count = findViewById(R.id.brick_count);
        score_count = findViewById(R.id.score_count);

        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                breakoutView.leftPress(event);
                return true;
            }
        });

        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                breakoutView.rightPress(event);
                return true;
            }
        });

        ball_count.append(String.valueOf(BreakoutView.LIVES));
        level_count.append(String.valueOf(BreakoutView.LEVEL));
        brick_count.append(String.valueOf(BreakoutView.BRICKS));
        score_count.append(String.valueOf(BreakoutView.SCORE));

        breakoutView.setTextView(level_count,ball_count,score_count,brick_count);

        if (savedInstanceState==null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            breakoutView.set(pref.getInt("bricks_amount", 5), pref.getString("balls", "3"), pref.getString("bricks", "2"));
        }
    }

    @Override
    protected void onPause() {
        breakoutView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        //breakoutView.set(pref.getInt("bricks_amount", 5), pref.getString("balls", "3"), pref.getString("bricks", "2"));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Toast.makeText(this, "Final, Winter 2019, Travis Cartmell", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
