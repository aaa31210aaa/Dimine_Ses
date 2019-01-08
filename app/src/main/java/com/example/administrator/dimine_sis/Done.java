package com.example.administrator.dimine_sis;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import utils.BaseActivity;

public class Done extends BaseActivity {
    private ImageView done_back;
    private ListView done_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        done_back = (ImageView) findViewById(R.id.done_back);
        done_listview = (ListView) findViewById(R.id.done_listview);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        done_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_back:
                finish();
                break;
            default:
                break;
        }
    }
}
