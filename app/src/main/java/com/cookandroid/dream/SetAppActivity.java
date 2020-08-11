package com.cookandroid.dream;

import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SetAppActivity extends AppCompatActivity {
    ImageView btn_toolbar_back6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_app);

        btn_toolbar_back6 = (ImageView) findViewById(R.id.btn_toolbar_back6);
        btn_toolbar_back6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
