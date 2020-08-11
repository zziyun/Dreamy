package com.cookandroid.dream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {
    Button btn_set_modify5, btn_set_logout5, btn_set_data5, btn_set_delete5;
    ImageView btn_toolbar_back5;
    String id, name, phone;
    String no[];

    dbHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        id = intent.getExtras().getString("id");
        phone = intent.getExtras().getString("phone");

        btn_set_modify5 = (Button)findViewById(R.id.btn_set_modify5);
        btn_set_modify5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetModify.class);
                intent.putExtra("id", id);
                intent.putExtra("phone",phone);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        btn_set_logout5 = (Button)findViewById(R.id.btn_set_logout5);
        btn_set_logout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_set_data5 = (Button)findViewById(R.id.btn_set_data5);
        btn_set_data5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // diary 전체삭제
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetActivity.this);

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("모든 일기 내용을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    helper = new dbHelper(SetActivity.this);
                                    db = helper.getReadableDatabase();
                                } catch (SQLiteException e) {
                                }

                                final String ckId2 = id;

                                DiaryVO diary = new DiaryVO();
                                diary.setId(id);

                                StringBuffer sbCk = new StringBuffer();
                                sbCk.append("SELECT * FROM diary WHERE id = #id#");

                                String queryCk = sbCk.toString();
                                queryCk = queryCk.replace("#id#", "'" + ckId2 + "'");

                                Cursor cursor;
                                cursor = db.rawQuery(queryCk, null);
                                no = new String[cursor.getCount()];
                                while (cursor.moveToNext()){ //moveToNext는 다음값이 없으면 null로 while문 빠져나와
                                    for(int i=0; i<cursor.getCount();i++){
                                        no[i] = cursor.getString(0);
                                        cursor.moveToNext();
                                        if (no != null) {
                                            StringBuffer sb = new StringBuffer();
                                            sb.append("DELETE FROM DIARY WHERE dno = #no#");
                                            String query = sb.toString();
                                            query = query.replace("#no#", no[i]);
                                            db.execSQL(query);
                                        }
                                    }
                                }
                                cursor.close();
                                db.close();
                                Toast.makeText(SetActivity.this, "일기 초기화 완료", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btn_set_delete5 = (Button)findViewById(R.id.btn_set_delete5);
        btn_set_delete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원탈퇴
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetActivity.this);

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("탈퇴하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    helper = new dbHelper(SetActivity.this);
                                    db = helper.getReadableDatabase();
                                } catch (SQLiteException e) {
                                }

                                final String ckId2 = id;

                                DiaryVO diary = new DiaryVO();
                                diary.setId(id);

                                StringBuffer sbCk = new StringBuffer();
                                sbCk.append("SELECT * FROM diary WHERE id = #id#");

                                String queryCk = sbCk.toString();
                                queryCk = queryCk.replace("#id#", "'" + ckId2 + "'");
                                Cursor cursor;
                                cursor = db.rawQuery(queryCk, null);
                                no = new String[cursor.getCount()];
                                while (cursor.moveToNext()){ //moveToNext는 다음값이 없으면 null로 while문 빠져나와
                                    for(int i=0; i<cursor.getCount();i++){
                                        no[i] = cursor.getString(0);
                                        cursor.moveToNext();
                                        if (no != null) {
                                            StringBuffer sb = new StringBuffer();
                                            sb.append("DELETE FROM DIARY WHERE dno = #no#");
                                            String query = sb.toString();
                                            query = query.replace("#no#", no[i]);
                                            db.execSQL(query);
                                        }
                                    }
                                }
                                cursor.close();

                                StringBuffer sb = new StringBuffer();
                                sb.append("DELETE FROM USER WHERE id = #id#");
                                String query = sb.toString();
                                query = query.replace("#id#", "'" + ckId2 + "'");
                                db.execSQL(query);

                                db.close();
                                Toast.makeText(SetActivity.this, "탈퇴 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btn_toolbar_back5 = (ImageView) findViewById(R.id.btn_toolbar_back5);
        btn_toolbar_back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
