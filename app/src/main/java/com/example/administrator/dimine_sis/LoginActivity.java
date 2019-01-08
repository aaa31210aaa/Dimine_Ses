package com.example.administrator.dimine_sis;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class LoginActivity extends BaseActivity {
    private EditText account_etv;
    private EditText password_etv;
    private CheckBox remember_account_checkbox;
    private CheckBox auto_login_checkbox;
    private Button login_submit;
    public String url;
    private JSONObject jsonObject;
    private String success; //登陆成功的标识
    private String error; // 登陆失败的标识
    private String token;
    private String userid; //用户id
    private String username; //用户名字
    private String userType;//用户类型
    private Dialog dialog;
    public static ClearableCookieJar cookieJar;
    public boolean firstLogin = true;

    private boolean a = false;

    /**
     * 是否退出的标识
     */
    public static boolean isExit = false;
    public static int send_time = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        initStatusBarColor(ContextCompat.getColor(this, R.color.my_transparence));
        account_etv = (EditText) findViewById(R.id.account_etv);
        password_etv = (EditText) findViewById(R.id.password_etv);
        remember_account_checkbox = (CheckBox) findViewById(R.id.remember_account_checkbox);
        auto_login_checkbox = (CheckBox) findViewById(R.id.auto_login_checkbox);
        login_submit = (Button) findViewById(R.id.login_submit);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.LoginAddress();
        //储存用户信息的对象
//        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
//        editor = sp.edit();


        //判断记住密码多选框的状态
        if (SharedPrefsUtil.getValue(this, "userInfo", "ISCHECK", false)) {
            //设置默认是记住账号选中状态
            remember_account_checkbox.setChecked(true);
            account_etv.setText(SharedPrefsUtil.getValue(this, "userInfo", "USER_NAME", ""));

            if (account_etv.getText().toString().trim().equals("")) {
                account_etv.requestFocus();
            } else {
                password_etv.requestFocus();
            }
        }


        //监听保存账号的选择框
        remember_account_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (remember_account_checkbox.isChecked()) {
                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "ISCHECK", true);
                } else {
                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "ISCHECK", false);
                }
            }
        });


        //判断自动登陆选框的状态
        if (SharedPrefsUtil.getValue(this, "userInfo", "AUTOLOGIN", false)) {
            //设置默认是自动登陆选中状态
            auto_login_checkbox.setChecked(true);
            if (remember_account_checkbox.isChecked()) {
                password_etv.setText(SharedPrefsUtil.getValue(this, "userInfo", "USER_PWD", ""));
            }
        }

        //监听自动登陆的选择框
        auto_login_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login_checkbox.isChecked()) {
                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "AUTOLOGIN", true);
                } else {
                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "AUTOLOGIN", false);
                }
            }
        });
    }

    @Override
    protected void setOnClick() {
        login_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit:
                if (account_etv.getText().toString().trim().equals("") || password_etv.getText().toString().trim().equals("")) {
                    ShowToast.showToastNoWait(this, R.string.NotEmpty);
                } else {
                    dialog = DialogUtil.createLoadingDialog(this, R.string.loading_write);
                    mOkhttp(account_etv.getText().toString(), password_etv.getText().toString());
//                    Test();
                }
                break;
            default:
                break;
        }
    }


    //获取接口数据
    public void mOkhttp(String loginname, String pwd) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("loginname", loginname)
                .addParams("loginpwd", pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(LoginActivity.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            success = jsonObject.getString("success");
                            error = jsonObject.getString("errormessage");
                            if (success.equals("true")) {
                                token = jsonObject.getString("access_token");
                                userid = jsonObject.getString("userid");
                                username = jsonObject.getString("username");
                                userType = jsonObject.getString("usertype");
                                SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "user_token", token);
                                SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "username", username);
                                SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "userid", userid);
                                SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "usertype", userType);
                                if (remember_account_checkbox.isChecked()) {
                                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "USER_NAME", account_etv.getText().toString().trim());
                                }

                                if (auto_login_checkbox.isChecked()) {
                                    SharedPrefsUtil.putValue(LoginActivity.this, "userInfo", "USER_PWD", password_etv.getText().toString().trim());
                                }

                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                ShowToast.showShort(LoginActivity.this, error);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, send_time);
        } else {
            finish();
            System.exit(0);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                    if (firstLogin) {
                        JPushInterface.setAlias(LoginActivity.this, 1000, account_etv.getText().toString());
                        new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                //回调接口,i=0表示成功,其它设置失败
                                Log.d("alias", "set alias result is" + i);
                            }
                        };

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        startActivity(intent);
//                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        finish();
                        ShowToast.showShort(LoginActivity.this, R.string.login_success);
                        firstLogin = false;
                    }

                    break;
                default:
                    break;
            }
        }
    };


}
