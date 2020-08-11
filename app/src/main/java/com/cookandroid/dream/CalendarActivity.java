package com.cookandroid.dream;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cookandroid.dream.R;

public class CalendarActivity extends AppCompatActivity {
    ImageView btn_toolbar_back11;

    private String id, date1, no, mood, content, updateFlag, today = "NON";
    Button toButton;
    dbHelper helper;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn_toolbar_back11 = (ImageView) findViewById(R.id.btn_toolbar_back11);
        btn_toolbar_back11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
