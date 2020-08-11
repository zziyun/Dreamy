package com.cookandroid.dream;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DlistActivity extends AppCompatActivity {
    Button btnred_8, btnorg_8, btngrn_8, btnvio_8, btngry_8;
    ListView list_8;
    ImageView btn_toolbar_back8;
    private String no, date, mood, content, id;
    String[] result;
    String cuid;
    dbHelper helper;
    SQLiteDatabase db;
    int redd=0, orangee=0, greenn=0, violett=0, grayy=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlist);

        btnred_8 = (Button) findViewById(R.id.btnred_8);
        btnorg_8 = (Button) findViewById(R.id.btnorg_8);
        btngrn_8 = (Button) findViewById(R.id.btngrn_8);
        btnvio_8 = (Button) findViewById(R.id.btnvio_8);
        btngry_8 = (Button) findViewById(R.id.btngry_8);
        list_8 = (ListView) findViewById(R.id.list_8);

        btn_toolbar_back8 = (ImageView) findViewById(R.id.btn_toolbar_back8);
        btn_toolbar_back8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        no = intent.getExtras().getString("no");
        date = intent.getExtras().getString("date");
        mood = intent.getExtras().getString("mood");
        content = intent.getExtras().getString("content");
        id = intent.getExtras().getString("id");

        if (content == null) {
            Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
        } else { // 일기가 있으면
            try {
                try {
                    helper = new dbHelper(DlistActivity.this);
                    db = helper.getReadableDatabase();
                } catch (SQLiteException e) {
                }

                cuid=id;
                final DiaryVO diary = new DiaryVO();
                diary.setId(id);

                //sql = "SELECT * FROM diary WHERE id = #id#";
                StringBuffer sbCk = new StringBuffer();
                sbCk.append("SELECT * FROM diary WHERE id = #id#");
                String queryCk = sbCk.toString();
                queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                Cursor cursor;
                cursor = db.rawQuery(queryCk, null);
                if (no != null) {
                    int count = cursor.getCount();
                    result = new String[count];
                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성
                        result[i] = str_date + " "+ str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                        if(str_mood.equals("red")) redd++;
                        if(str_mood.equals("orange")) orangee++;
                        if(str_mood.equals("green")) greenn++;
                        if(str_mood.equals("violet")) violett++;
                        if(str_mood.equals("gray")) grayy++;
                        //Log.d("포문 안에서 다이어리 정보..", str_date + " " + str_mood+" red:"+red+" orange:"+orange+" green:"+green+" violet:"+violet+" gray:"+gray);
                        Log.d("포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                    }
                }

                final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                list_8.setAdapter(adapter);



                list_8.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Object vo = (Object)adapterView.getAdapter().getItem(position);  //리스트뷰의 포지션 내용을 가져옴.
                        String[] data = vo.toString().split(" ");
                        Toast.makeText(getApplicationContext(),data[0]+"\n"+data[1],Toast.LENGTH_SHORT).show();

                        DiaryVO diary = new DiaryVO();
                        diary.setId(id);
                        diary.setDdate(data[0]);

                        try {
                            helper = new dbHelper(DlistActivity.this);
                            db = helper.getReadableDatabase();
                        } catch (SQLiteException e) {
                        }

                        StringBuffer sb = new StringBuffer();
                        sb.append("SELECT * FROM DIARY WHERE id = #id# AND ddate = #date#");
                        String query = sb.toString();
                        query = query.replace("#id#", "'" + diary.getId() + "'");
                        query = query.replace("#date#", "'" + diary.getDdate() + "'");

                        Cursor cursor;
                        cursor = db.rawQuery(query, null);

                        if (cursor.moveToNext()) {
                            String no = cursor.getString(0);
                            String date = cursor.getString(1);
                            String mood = cursor.getString(2);
                            String content = cursor.getString(3);
                            String id = cursor.getString(4);

                            cursor.close();
                            db.close();

                            Intent readIntent = new Intent(getApplicationContext(), DeditActivity.class);
                            readIntent.putExtra("no", no);
                            readIntent.putExtra("date", date);
                            readIntent.putExtra("mood", mood);
                            readIntent.putExtra("content", content);
                            readIntent.putExtra("id", id);
                            startActivity(readIntent);

                        } else {
                            Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });

                cursor.close();
                db.close();

            } catch (Exception e) {
                System.out.println("무슨오류? :  " + e);
            }
        }

        btnred_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    try {
                        helper = new dbHelper(DlistActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }
                    cuid=id;
                    DiaryVO diary = new DiaryVO();
                    diary.setId(id);

                    //sql = "SELECT * FROM diary WHERE id = #id#";
                    StringBuffer sbCk = new StringBuffer();
                    sbCk.append("SELECT * FROM diary WHERE id = #id#");
                    String queryCk = sbCk.toString();
                    queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                    Cursor cursor;
                    cursor = db.rawQuery(queryCk, null);
                    int clrcount=0;

                    int count = cursor.getCount();

                    result = new String[redd];  // 여기를 orange일기의 수를 result의 크기로 미리 정하면 오류 안남

                    clrcount=0;

                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_no=cursor.getString(0);
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성

                        if(str_mood.equals("red")){
                            result[ clrcount++] = str_date + " " + str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                            Log.d("레드 포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                        }
                    }

                    if(redd==0) {
                        Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    list_8.setAdapter(adapter);

                    cursor.close();
                    db.close();
                }catch (Exception e){
                    Log.d("레드 리스트 실행 후 ", "레드씨 여기서 오류라니");
                }
            }
        });

        btnorg_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    try {
                        helper = new dbHelper(DlistActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }
                    cuid=id;
                    DiaryVO diary = new DiaryVO();
                    diary.setId(id);

                    //sql = "SELECT * FROM diary WHERE id = #id#";
                    StringBuffer sbCk = new StringBuffer();
                    sbCk.append("SELECT * FROM diary WHERE id = #id#");
                    String queryCk = sbCk.toString();
                    queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                    Cursor cursor;
                    cursor = db.rawQuery(queryCk, null);
                    int clrcount=0;

                    int count = cursor.getCount();

                    result = new String[orangee];  // 여기를 orange일기의 수를 result의 크기로 미리 정하면 오류 안남

                    clrcount=0;

                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_no=cursor.getString(0);
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성

                        if(str_mood.equals("orange")){
                            result[ clrcount++] = str_date + " " + str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                            Log.d("오렌지 포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                        }
                    }

                    if(orangee==0) {
                        Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    list_8.setAdapter(adapter);

                    cursor.close();
                    db.close();
                }catch (Exception e){
                    Log.d("오렌지 리스트 실행 후 ", "오렌지씨 여기서 오류라니");
                }
            }
        });

        btngrn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    try {
                        helper = new dbHelper(DlistActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }
                    cuid=id;
                    DiaryVO diary = new DiaryVO();
                    diary.setId(id);

                    //sql = "SELECT * FROM diary WHERE id = #id#";
                    StringBuffer sbCk = new StringBuffer();
                    sbCk.append("SELECT * FROM diary WHERE id = #id#");
                    String queryCk = sbCk.toString();
                    queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                    Cursor cursor;
                    cursor = db.rawQuery(queryCk, null);
                    int clrcount=0;

                    int count = cursor.getCount();

                    result = new String[greenn];  // 여기를 orange일기의 수를 result의 크기로 미리 정하면 오류 안남

                    clrcount=0;

                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_no=cursor.getString(0);
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성

                        if(str_mood.equals("green")){
                            result[ clrcount++] = str_date + " " + str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                            Log.d("그린 포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                        }
                    }

                    if(greenn==0) {
                        Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    list_8.setAdapter(adapter);

                    cursor.close();
                    db.close();
                }catch (Exception e){
                    Log.d("그린 리스트 실행 후 ", "그린씨 여기서 오류라니");
                }
            }
        });

        btnvio_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    try {
                        helper = new dbHelper(DlistActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }
                    cuid=id;
                    DiaryVO diary = new DiaryVO();
                    diary.setId(id);

                    //sql = "SELECT * FROM diary WHERE id = #id#";
                    StringBuffer sbCk = new StringBuffer();
                    sbCk.append("SELECT * FROM diary WHERE id = #id#");
                    String queryCk = sbCk.toString();
                    queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                    Cursor cursor;
                    cursor = db.rawQuery(queryCk, null);
                    int clrcount=0;

                    int count = cursor.getCount();

                    result = new String[violett];  // 여기를 orange일기의 수를 result의 크기로 미리 정하면 오류 안남

                    clrcount=0;

                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_no=cursor.getString(0);
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성

                        if(str_mood.equals("violet")){
                            result[ clrcount++] = str_date + " " + str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                            Log.d("바이올렛 포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                        }
                    }

                    if(violett==0) {
                        Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    list_8.setAdapter(adapter);

                    cursor.close();
                    db.close();
                }catch (Exception e){
                    Log.d("바이올렛 리스트 실행 후 ", "바이올렛씨 여기서 오류라니");
                }
            }
        });

        btngry_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    try {
                        helper = new dbHelper(DlistActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }
                    cuid=id;
                    DiaryVO diary = new DiaryVO();
                    diary.setId(id);

                    //sql = "SELECT * FROM diary WHERE id = #id#";
                    StringBuffer sbCk = new StringBuffer();
                    sbCk.append("SELECT * FROM diary WHERE id = #id#");
                    String queryCk = sbCk.toString();
                    queryCk = queryCk.replace("#id#", "'" + cuid + "'");

                    Cursor cursor;
                    cursor = db.rawQuery(queryCk, null);
                    int clrcount=0;

                    int count = cursor.getCount();

                    result = new String[grayy];  // 여기를 orange일기의 수를 result의 크기로 미리 정하면 오류 안남

                    clrcount=0;

                    for (int i = 0; i < count; i++) {
                        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_no=cursor.getString(0);
                        String str_date = cursor.getString(1);   // 첫번째 속성
                        String str_mood = cursor.getString(2);   // 두번째 속성

                        if(str_mood.equals("gray")){
                            result[ clrcount++] = str_date + " " + str_mood;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                            Log.d("그레이 포문 안에서 다이어리 정보..", str_date + " " + str_mood);
                        }
                    }

                    if(grayy==0) {
                        Toast.makeText(getApplicationContext(), "일기가 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    list_8.setAdapter(adapter);

                    cursor.close();
                    db.close();
                }catch (Exception e){
                    Log.d("그레이 리스트 실행 후 ", "그레이씨 여기서 오류라니");
                }
            }
        });
    }
}

