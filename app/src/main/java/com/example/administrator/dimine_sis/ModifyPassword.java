package com.example.administrator.dimine_sis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import utils.BaseActivity;

public class ModifyPassword extends BaseActivity {
    private ImageView modify_password_back;
    private EditText modify_password_old_etv;
    private EditText modify_password_new_etv;
    private EditText modify_password_again_etv;
    private Button modify_password_submit_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        modify_password_back = (ImageView) findViewById(R.id.modify_password_back);
        modify_password_old_etv = (EditText) findViewById(R.id.modify_password_old_etv);
        modify_password_new_etv = (EditText) findViewById(R.id.modify_password_new_etv);
        modify_password_again_etv = (EditText) findViewById(R.id.modify_password_again_etv);
        modify_password_submit_btn = (Button) findViewById(R.id.modify_password_submit_btn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        modify_password_back.setOnClickListener(this);
        modify_password_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.modify_password_back:
                finish();
                break;
            case R.id.modify_password_submit_btn:
                break;
            default:
                break;
        }
    }
}
