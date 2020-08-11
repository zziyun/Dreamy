package com.cookandroid.dream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {
EditText edtLoginID2, edtLoginPW2;
Button btnLoginSubmit2, btn_kakao_login;
TextView txtLoginSignup2, txtPassChange2;
CheckBox ckSaveId2;
ImageView img_naver2,img_twt2,img_fb2;
SessionCallback callback;

dbHelper helper;
SQLiteDatabase db;

String saveId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //*카카오톡 로그아웃 요청**//*
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출하므로
        // 매번 로그아웃을 요청함
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        edtLoginID2 = (EditText)findViewById(R.id.edtLoginID2);
        edtLoginPW2 = (EditText)findViewById(R.id.edtLoginPW2);
        btnLoginSubmit2 = (Button)findViewById(R.id.btnLoginSubmit2);
        txtLoginSignup2 = (TextView)findViewById(R.id.txtLoginSignup2);
        txtPassChange2 = (TextView)findViewById(R.id.txtPassChange2);
        ckSaveId2 = (CheckBox) findViewById(R.id.ckSaveId2);

        img_naver2 = (ImageView)findViewById(R.id.img_naver2);
        img_naver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"서비스 준비중 입니다~ :)", Toast.LENGTH_SHORT).show();
            }
        });
        img_twt2 = (ImageView)findViewById(R.id.img_twt2);
        img_twt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"서비스 준비중 입니다~ :)", Toast.LENGTH_SHORT).show();
            }
        });
        img_fb2 = (ImageView)findViewById(R.id.img_fb2);
        img_fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"서비스 준비중 입니다~ :)", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        saveId = auto.getString("inputId", null);

        if (saveId != null) {
            edtLoginID2.setText(saveId);
        }

        edtLoginID2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtLoginID2.hasFocus())
                    edtLoginID2.setBackgroundResource(R.drawable.primary_border);
                else
                    edtLoginID2.setBackgroundResource(R.drawable.purple_border);
            }
        });
        edtLoginPW2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtLoginPW2.hasFocus())
                    edtLoginPW2.setBackgroundResource(R.drawable.primary_border);
                else
                    edtLoginPW2.setBackgroundResource(R.drawable.purple_border);
            }
        });

        btnLoginSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtLoginID2.getText().toString().isEmpty())
                    edtLoginID2.requestFocus();
                else if(edtLoginPW2.getText().toString().isEmpty())
                    edtLoginPW2.requestFocus();
                else {
                    try {
                        helper = new dbHelper(LoginActivity.this);
                        db = helper.getReadableDatabase();
                    } catch (SQLiteException e) {
                    }

                    String id = edtLoginID2.getText().toString();
                    String pw = edtLoginPW2.getText().toString();

                    UserVO userInfo = new UserVO();
                    userInfo.setId(id);
                    userInfo.setPasswd(pw);

                    StringBuffer sb = new StringBuffer();
                    sb.append("SELECT * FROM user WHERE id = #id# AND passwd = #passwd#");

                    String query = sb.toString();
                    query = query.replace("#id#", "'" + userInfo.getId() + "'");
                    query = query.replace("#passwd#", "'" + userInfo.getPasswd() + "'");

                    //
                    Cursor cursor;
                    cursor = db.rawQuery(query, null);

                    if (cursor.moveToNext()) {

                        String strId = cursor.getString(0);
                        String strName = cursor.getString(1);
                        String strPhone = cursor.getString(3);

                        MApp appDel = (MApp) getApplication();
                        appDel.setUserId(strId);
                        appDel.setUserName(strName);
                        appDel.setUserPhone(strPhone);

                        if (strId != null) {
                            if (ckSaveId2.isChecked() == true) {
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", strId);
                                autoLogin.commit();
                                startMain(strId, strName, strPhone);
                            } else {
                                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor autoLogin = auto.edit();
                                autoLogin.putString("inputId", null);
                                autoLogin.commit();
                                startMain(strId, strName, strPhone);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "입력정보를 확인하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cursor.close();
                    db.close();
                }
            }
        });

        txtLoginSignup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    private void startMain(String id, String name, String phone) {

        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        startActivity(intent);

    }

    class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
                        // finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    Log.e("UserProfile", userProfile.toString());
                    Log.e("UserProfile", userProfile.getId() + "");
                    Log.e("UserProfile", userProfile.getProfileImagePath() + "");
                    Log.e("UserProfile", String.valueOf(userProfile.getId()) + "");
                    Log.e("UserProfile", userProfile.getNickname() + "");

                    Intent intent=new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("id",  String.valueOf(userProfile.getId()));
                    intent.putExtra("name",   userProfile.getNickname());
                    intent.putExtra("phone", " ");
                    startActivity(intent);

                    finish();
                }
            });
        }
        // 세션 실패시
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        }
    }
}
