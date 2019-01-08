package mynotification_detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class MyNotificationDetail extends BaseActivity {
    private ImageView mynotification_detail_back;
    //消息详情标题
    private TextView notification_detail_tilte;
    //消息详情时间
    private TextView notification_detail_time;
    //消息详情内容
    private TextView notification_detail_content;
    private String url;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String user_token;
    private String messageid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        mynotification_detail_back = (ImageView) findViewById(R.id.mynotification_detail_back);
        notification_detail_tilte = (TextView) findViewById(R.id.notification_detail_tilte);
        notification_detail_time = (TextView) findViewById(R.id.notification_detail_time);
        notification_detail_content = (TextView) findViewById(R.id.notification_detail_content);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.MessageDetail();
//        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
//        editor = sp.edit();
//        user_token = sp.getString("user_token", null);
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        messageid = intent.getStringExtra("messageid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("messageid", messageid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(MyNotificationDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            notification_detail_tilte.setText(jsonArray.getJSONObject(0).getString("messagetitle"));
                            notification_detail_time.setText(jsonArray.getJSONObject(0).getString("mestime"));
                            notification_detail_content.setText(jsonArray.getJSONObject(0).getString("mescontent"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void setOnClick() {
        mynotification_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mynotification_detail_back:
                finish();
                break;
        }
    }
}
