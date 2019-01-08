package wfp_query;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.GaoduJbkAdapter;
import bean.GaoduBean;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class ZywhDetail extends BaseActivity {
    private ImageView zywh_detail_back;
    private ListView zywh_detail_list;
    private String url;
    private String user_token;
    private String clid;
    private String[] key = {"casno", "clname", "enname", "dangerno", "macvalue", "ptwa", "pcstel"};
    private String[] value = {"CAS号：", "中文名：", "英文名：", "危险编号：", "MAC值：", "PC-TWA(MG/m³)：","PC-STEL(MG/m³)："};
    private List<GaoduBean> mDatas;
    private GaoduJbkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zywh_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        zywh_detail_back = (ImageView) findViewById(R.id.zywh_detail_back);
        zywh_detail_list = (ListView) findViewById(R.id.zywh_detail_list);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.ContactlimitDetail();
        Intent intent = getIntent();
        clid = intent.getStringExtra("clid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("clid", clid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(ZywhDetail.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            mDatas=  new ArrayList<GaoduBean>();
                            for (int i = 0; i < key.length; i++) {
                                GaoduBean bean = new GaoduBean();
                                bean.setZwmz(jsonObject.getString(key[i]));
                                bean.setYwmz(value[i]);
                                mDatas.add(bean);
                            }
                            adapter = new GaoduJbkAdapter(ZywhDetail.this, mDatas);
                            zywh_detail_list.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        zywh_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zywh_detail_back:
                finish();
                break;
        }
    }
}
