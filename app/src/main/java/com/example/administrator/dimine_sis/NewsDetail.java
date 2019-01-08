package com.example.administrator.dimine_sis;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class NewsDetail extends BaseActivity {
    private ImageView news_detail_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        news_detail_back = (ImageView) findViewById(R.id.news_detail_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        news_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.news_detail_back:
                finish();
                break;
        }
    }
}
