package com.example.administrator.dimine_sis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;


public class Welcome extends BaseActivity {
    private int send_time = 2000;
    private Intent intent;


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    intent = new Intent(Welcome.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                    break;
                case 2:
                    intent = new Intent(Welcome.this, MainActivity.class);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void mOkhttp(String loginname, String pwd) {
        OkHttpUtils
                .post()
                .url(PortIpAddress.LoginAddress())
                .addParams("loginname", loginname)
                .addParams("loginpwd", pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();
    }

    @Override
    protected void initView() {
//        initStatusBarColor(ContextCompat.getColor(this,R.color.my_transparence));
    }

    @Override
    protected void initData() {
//        String a = SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", "");
//        String b = SharedPrefsUtil.getValue(this, "userInfo", "USER_PWD", "");
//        Log.e(TAG, a + "----" + b);
        if (!SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", "").equals("") && !SharedPrefsUtil.getValue(this, "userInfo", "USER_PWD", "").equals("")) {
            handler.sendEmptyMessageDelayed(2,send_time);
        } else {
            handler.sendEmptyMessageDelayed(1,send_time);
        }
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }
}
