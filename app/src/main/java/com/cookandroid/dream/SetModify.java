package com.cookandroid.dream;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SetModify extends AppCompatActivity {

    EditText edt_call, edt_name;
    Button btn_user_mody;
    String id, name, phone;
    ImageView btn_toolbar_back10;

    dbHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_modify);

        btn_toolbar_back10 = (ImageView) findViewById(R.id.btn_toolbar_back10);
        btn_toolbar_back10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        id = intent.getExtras().getString("id");
        phone = intent.getExtras().getString("phone");

        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_call = (EditText)findViewById(R.id.edt_call);
        btn_user_mody = (Button)findViewById(R.id.btn_user_mody);

        edt_name.setText(name);
        edt_call.setText(phone);

        btn_user_mody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SetModify.this);

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("수정 후에는 재로그인이 필요합니다")
                        .setCancelable(false)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper = new dbHelper(SetModify.this);
                                db = helper.getWritableDatabase();
                                try {
                                    db.execSQL("update user set name='"+edt_name.getText()+"' where id='"+id+"';");
                                    db.execSQL("update user set phone='"+edt_call.getText()+"' where id='"+id+"';");
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                finally {
                                    db.close();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}
