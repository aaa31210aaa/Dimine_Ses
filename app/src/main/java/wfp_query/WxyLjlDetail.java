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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.GaoduJbkAdapter;
import bean.GaoduBean;
import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class WxyLjlDetail extends BaseActivity {
    private ImageView wxy_ljl_detail_back;
    private ListView wxy_ljl_detail_listview;

    private List<GaoduBean> mDatas;

    private String url;
    private String user_token;
    private Intent intent;

    private String[] key = {"wzmc", "lbcode", "typename", "sccsljl", "ccqljl"};
    private String[] value = {"物质名称：", "危险源类别编号：", "危险源类别名称：", "生产场所临界量：", "储存区临界量："};
    private String wxwzid;
    private GaoduJbkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxy_ljl_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        wxy_ljl_detail_back = (ImageView) findViewById(R.id.wxy_ljl_detail_back);
        wxy_ljl_detail_listview= (ListView) findViewById(R.id.wxy_ljl_detail_listview);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.GetHazardmatterdetail();
        intent = getIntent();
        wxwzid = intent.getStringExtra("wxwzid");

        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("wxwzid", wxwzid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showToastNoWait(WxyLjlDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            mDatas=  new ArrayList<GaoduBean>();
                            for (int i = 0; i < key.length; i++) {
                                GaoduBean bean = new GaoduBean();
                                bean.setZwmz(jsonArray.getJSONObject(0).getString(key[i]));
                                bean.setYwmz(value[i]);
                                mDatas.add(bean);
                            }
                            adapter = new GaoduJbkAdapter(WxyLjlDetail.this, mDatas);
                            wxy_ljl_detail_listview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        wxy_ljl_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxy_ljl_detail_back:
                finish();
                break;
        }
    }
}
