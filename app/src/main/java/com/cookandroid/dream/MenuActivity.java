package com.cookandroid.dream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity {

    public static String UPDATEIS = "X";

    Button btnwrt_4,btncal_4, btnrd_4, btnftn_4;
    ImageView btntool4;
    String id, name, phone, today;

    dbHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnwrt_4 = (Button) findViewById(R.id.btnwrt_4);
        btncal_4=(Button)findViewById(R.id.btncal_4);
        btnrd_4 = (Button) findViewById(R.id.btnrd_4);
        btnftn_4 = (Button) findViewById(R.id.btnftn_4);
        btntool4 = (ImageView) findViewById(R.id.btntool4);

        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(date);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        id = intent.getExtras().getString("id");
        phone = intent.getExtras().getString("phone");

        Toast.makeText(this, name + "님 안녕하세요.", Toast.LENGTH_SHORT).show();

        ArrayList<DiaryVO> diaryVOS = new ArrayList<DiaryVO>();
        diaryVOS.add(new DiaryVO());


        // 오늘의 다이어리 작성
        btnwrt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    helper = new dbHelper(MenuActivity.this);
                    db = helper.getReadableDatabase();
                } catch (SQLiteException e) {
                }
                view.getId();

                final String ckDate = today;
                final String ckId = id;

                DiaryVO diary = new DiaryVO();
                diary.setId(id);
                diary.setDdate(today);

                StringBuffer sbCk = new StringBuffer();
                sbCk.append("SELECT * FROM diary WHERE id = #id# AND ddate = #date#");

                String queryCk = sbCk.toString();
                queryCk = queryCk.replace("#id#", "'" + ckId + "'");
                queryCk = queryCk.replace("#date#", "'" + ckDate + "'");

                Cursor cursorCk;
                cursorCk = db.rawQuery(queryCk, null);
                if (cursorCk.moveToNext()) {
                    String no = cursorCk.getString(0);
                    String date = cursorCk.getString(1);
                    String mood = cursorCk.getString(2);
                    String content = cursorCk.getString(3);
                    String id = cursorCk.getString(4);

                    if (no != null) {
                        // Toast.makeText(getApplicationContext(), no + date + mood + content + id, Toast.LENGTH_SHORT).show();
                    }

                    cursorCk.close();
                    db.close();

                    Intent readIntent = new Intent(getApplicationContext(), TodiaryActivity.class);
                    readIntent.putExtra("no", no);
                    readIntent.putExtra("date", date);
                    readIntent.putExtra("mood", mood);
                    readIntent.putExtra("content", content);
                    readIntent.putExtra("id", id);
                    startActivity(readIntent);

                } else {
                    Intent intent = new Intent(MenuActivity.this, TodiaryActivity.class);
                    intent.putExtra("date", today);
                    intent.putExtra("Id", id);
                    startActivity(intent);
                }
            }
        });

        // 달력 보기
        btncal_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    helper = new dbHelper(MenuActivity.this);
                    db = helper.getReadableDatabase();
                } catch (SQLiteException e) {
                }
                view.getId();

                final String ckDate = today;
                final String ckId = id;

                DiaryVO diary = new DiaryVO();
                diary.setId(id);
                diary.setDdate(today);

                StringBuffer sbCk = new StringBuffer();
                sbCk.append("SELECT * FROM diary WHERE id = #id# AND ddate = #date#");

                String queryCk = sbCk.toString();
                queryCk = queryCk.replace("#id#", "'" + ckId + "'");
                queryCk = queryCk.replace("#date#", "'" + ckDate + "'");

                Cursor cursorCk;
                cursorCk = db.rawQuery(queryCk, null);
                if (cursorCk.moveToNext()) {
                    String no = cursorCk.getString(0);
                    String date = cursorCk.getString(1);
                    String mood = cursorCk.getString(2);
                    String content = cursorCk.getString(3);
                    String id = cursorCk.getString(4);

                    if (no != null) {
                        // Toast.makeText(getApplicationContext(), no + date + mood + content + id, Toast.LENGTH_SHORT).show();
                    }

                    cursorCk.close();
                    db.close();

                    Intent readIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                    readIntent.putExtra("no", no);
                    readIntent.putExtra("date", date);
                    readIntent.putExtra("mood", mood);
                    readIntent.putExtra("content", content);
                    readIntent.putExtra("id", id);
                    startActivity(readIntent);

                } else {
                    Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);
                    intent.putExtra("date", today);
                    intent.putExtra("Id", id);
                    startActivity(intent);
                }
            }
        });


        // 다이어리 목록
        btnrd_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    helper = new dbHelper(MenuActivity.this);
                    db = helper.getReadableDatabase();
                } catch (SQLiteException e) {
                }
                view.getId();

                final String ckId2 = id;

                DiaryVO diary = new DiaryVO();
                diary.setId(id);

                StringBuffer sbCk = new StringBuffer();
                sbCk.append("SELECT * FROM diary WHERE id = #id#");

                String queryCk = sbCk.toString();
                queryCk = queryCk.replace("#id#", "'" + ckId2 + "'");

                Cursor cursor;
                cursor = db.rawQuery(queryCk, null);
                if(cursor.getCount()==0){
                    Toast.makeText(getApplicationContext(),"일기가 없습니다",Toast.LENGTH_SHORT).show();
                }
                if (cursor.moveToNext()) {
                    String no = cursor.getString(0);
                    String date = cursor.getString(1);
                    String mood = cursor.getString(2);
                    String content = cursor.getString(3);
                    String id = cursor.getString(4);

                    cursor.close();
                    db.close();

                    Intent readIntent = new Intent(getApplicationContext(), DlistActivity.class);
                    readIntent.putExtra("no", no);
                    readIntent.putExtra("date", date);
                    readIntent.putExtra("mood", mood);
                    readIntent.putExtra("content", content);
                    readIntent.putExtra("id", id);
                    startActivity(readIntent);

                }
            }
        });

        // 해몽 사이트로 연결
        btnftn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.haemong2.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // 설정창
        btntool4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("phone",phone);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

    }
}