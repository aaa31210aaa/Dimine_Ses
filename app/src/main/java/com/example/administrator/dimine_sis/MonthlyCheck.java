package com.example.administrator.dimine_sis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import security_check.HiddenTroubleRegistration;
import utils.BaseActivity;

public class MonthlyCheck extends BaseActivity {
    private ImageView monthly_check_back;
    private TextView monthly_check_dj1;
    private TextView monthly_check_dj2;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_check);
        initView();

        setOnClick();initData();
    }

    @Override
    protected void initView() {
        monthly_check_back = (ImageView) findViewById(R.id.monthly_check_back);
        monthly_check_dj1 = (TextView) findViewById(R.id.monthly_check_dj1);
        monthly_check_dj2 = (TextView) findViewById(R.id.monthly_check_dj2);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        monthly_check_back.setOnClickListener(this);
        monthly_check_dj1.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monthly_check_back:
                finish();
                break;
            case R.id.monthly_check_dj1:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
            case R.id.monthly_check_dj2:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
        }
    }
}
