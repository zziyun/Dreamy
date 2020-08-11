package com.cookandroid.dream;



import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class TodiaryActivity extends AppCompatActivity {
    TextView today_7, tvNo;
    Button btnred_7, btnorg_7, btngrn_7, btnvio_7, btngry_7, save_7;
    EditText et_7;
    ImageView btn_toolbar_back7;
    String moody=null;
    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH)+1;
    int date = now.get(Calendar.DATE);
    int day_of_week = now.get(Calendar.DAY_OF_WEEK);
    int number=0;//////////////
    String[] dayOfWeek={"일", "월","화","수","목","금","토"};

    private String id, date1, no, mood, content, updateFlag = "NON";

    dbHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todiary);

        today_7=(TextView)findViewById(R.id.today_7);
        btnred_7=(Button)findViewById(R.id.btnred_7);
        btnorg_7=(Button)findViewById(R.id.btnorg_7);
        btngrn_7=(Button)findViewById(R.id.btngrn_7);
        btnvio_7=(Button)findViewById(R.id.btnvio_7);
        btngry_7=(Button)findViewById(R.id.btngry_7);
        et_7=(EditText)findViewById(R.id.et_7);
        save_7=(Button)findViewById(R.id.save_7);
        tvNo = (TextView) findViewById(R.id.tvNo);

        btn_toolbar_back7 = (ImageView) findViewById(R.id.btn_toolbar_back7);
        btn_toolbar_back7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        today_7.setText(year+"."+month + "." + date + " ("+dayOfWeek[day_of_week-1]+")");

       if (MenuActivity.UPDATEIS == "O") {
            Intent updatIntent = getIntent();
            updateFlag = updatIntent.getExtras().getString("FLAG");
            no = updatIntent.getExtras().getString("no");
            date1 = updatIntent.getExtras().getString("date");
            mood = updatIntent.getExtras().getString("mood");
            content = updatIntent.getExtras().getString("content");
            id = updatIntent.getExtras().getString("id");

            tvNo.setText(no);
            today_7.setText(year+"."+month + "." + date + " ("+dayOfWeek[day_of_week-1]+")");
            // 이미지나 내용이 없을경우 발생하면 null 오류 발생하여
            // 한번 더 체크하게 만듬
            if (mood != null) {
                if(mood.equals("red"))
                    et_7.setBackgroundColor(Color.parseColor("#FFFFD8D8"));
                else if(mood.equals("orange"))
                    et_7.setBackgroundColor(Color.parseColor("#FFFFF1C2"));
                else  if(mood.equals("green"))
                    et_7.setBackgroundColor(Color.parseColor("#FFDBFFB9"));
                else if(mood.equals("violet"))
                    et_7.setBackgroundColor(Color.parseColor("#FFDFD7FF"));
                else if(mood.equals("gray"))
                    et_7.setBackgroundColor(Color.parseColor("#FFDEDEDE"));
            }
            if (content != null) {
                et_7.setText(content);
            }
            MenuActivity.UPDATEIS = "X";
        } else {
           Intent intent = getIntent();
           id = intent.getExtras().getString("Id");
           date1 = intent.getExtras().getString("date");
           content = intent.getExtras().getString("content");
           mood = intent.getExtras().getString("mood");
           today_7.setText(year + "." + month + "." + date + " (" + dayOfWeek[day_of_week - 1] + ")");
       }
        if (content != null) {
            et_7.setText(content);
        }
        if (mood != null) {
            if(mood.equals("red"))
                et_7.setBackgroundColor(Color.parseColor("#FFFFD8D8"));
            else if(mood.equals("orange"))
                et_7.setBackgroundColor(Color.parseColor("#FFFFF1C2"));
            else  if(mood.equals("green"))
                et_7.setBackgroundColor(Color.parseColor("#FFDBFFB9"));
            else if(mood.equals("violet"))
                et_7.setBackgroundColor(Color.parseColor("#FFDFD7FF"));
            else if(mood.equals("gray"))
                et_7.setBackgroundColor(Color.parseColor("#FFDEDEDE"));
        }

        btnred_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_7.setBackgroundColor(Color.parseColor("#FFFFD8D8"));
                moody="red";
            }
        });

        btnorg_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_7.setBackgroundColor(Color.parseColor("#FFFFF1C2"));
                moody="orange";
            }
        });

        btngrn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_7.setBackgroundColor(Color.parseColor("#FFDBFFB9"));
                moody="green";
            }
        });

        btnvio_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_7.setBackgroundColor(Color.parseColor("#FFDFD7FF"));
                moody="violet";
            }
        });

        btngry_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_7.setBackgroundColor(Color.parseColor("#FFDEDEDE"));
                moody="gray";
            }
        });

        //EditText

        save_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getId();
                // db준비
                Log.d("저장할때엔", moody + " "+et_7.getText().toString());

                try {
                    helper = new dbHelper(TodiaryActivity.this);
                    db = helper.getWritableDatabase();
                } catch (SQLiteException e) {
                    db = helper.getReadableDatabase();
                }
                // 로그인 정보
                MApp appDel = (MApp) getApplication();

                DiaryVO diary = new DiaryVO();
                diary.setId(id);
                diary.setDdate(date1);
                diary.setDmood(moody);
                diary.setDcontent(et_7.getText().toString());

                String db_str=diary.getDcontent();
                String et_str=et_7.getText().toString();

                if(content==null){ // 일기를 처음 작성할 때
                    db.execSQL("INSERT INTO diary VALUES (null, '"+diary.getDdate()+"', '" +diary.getDmood()+"', '" +diary.getDcontent()+"', '" +diary.getId()+"');");
                    Log.d("어디..저장됐냐>", diary.getNo()+" "+diary.getDmood()+ " "+diary.getDcontent());
                    Toast.makeText(TodiaryActivity.this, "오늘의 일기를 작성해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(TodiaryActivity.this, "[일기쓰기]에서는 첫 일기만 작성이 가능합니다.\n\n오늘 작성한 다이어리의 수정은\n[일기보기] 삭제 후 재작성 가능합니다. ", Toast.LENGTH_LONG).show();
                }

                db.close();
                finish();
                MenuActivity.UPDATEIS = "X";
            }
        });
    }
}