package com.cookandroid.dream;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static com.cookandroid.dream.MenuActivity.UPDATEIS;

public class DeditActivity extends AppCompatActivity {
    EditText tv_9;
    ImageView  btn_toolbar_back9;
    private String id, date1, no, mood, content, updateFlag = "NON";
    public static final int UPDATESIGN = 3;

    Button dlt_9;
    dbHelper helper;
    SQLiteDatabase db;
    DiaryVO diary = new DiaryVO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedit);



        btn_toolbar_back9 = (ImageView) findViewById(R.id.btn_toolbar_back9);
        btn_toolbar_back9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_9 = (EditText) findViewById(R.id.tv_9);
        dlt_9 = (Button) findViewById(R.id.dlt_9);

        Intent intent = getIntent();
        no = intent.getExtras().getString("no");
        id = intent.getExtras().getString("Id");
        date1 = intent.getExtras().getString("date");
        content = intent.getExtras().getString("content");
        mood = intent.getExtras().getString("mood");

        if(mood.equals("red"))
            tv_9.setBackgroundColor(Color.parseColor("#FFFFD8D8"));
        else if(mood.equals("orange"))
            tv_9.setBackgroundColor(Color.parseColor("#FFFFF1C2"));
        else  if(mood.equals("green"))
            tv_9.setBackgroundColor(Color.parseColor("#FFDBFFB9"));
        else if(mood.equals("violet"))
            tv_9.setBackgroundColor(Color.parseColor("#FFDFD7FF"));
        else if(mood.equals("gray"))
            tv_9.setBackgroundColor(Color.parseColor("#FFDEDEDE"));

        tv_9.setText(content);
        diary.setDdate(date1);
        diary.setId(id);
        diary.setNo(no);

        dlt_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeditActivity.this);

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    helper = new dbHelper(DeditActivity.this);
                                    db = helper.getWritableDatabase();
                                } catch (SQLiteException e) {
                                }
                                StringBuffer sb = new StringBuffer();
                                sb.append("DELETE FROM DIARY WHERE dno = #no#");
                                String query = sb.toString();
                                query = query.replace("#no#", diary.getNo());
                                db.execSQL(query);
                                db.close();
                                finish();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show
                alertDialog.show();
               // startActivity(DeditActivity.super.getIntent());
            }
        });
    }
}
