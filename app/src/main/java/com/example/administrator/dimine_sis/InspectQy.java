package com.example.administrator.dimine_sis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.EnterpriseInformationAdapter;
import bean.EnterpriseInformationBean;
import hidden_danger.HiddenDangerDetail;
import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class InspectQy extends BaseActivity {
    private ImageView inspect_qy_back;
    private EditText inspect_qy_search;
    private ImageView inspect_qy_clear;
    private ListView inspect_qy_listview;
    private List<EnterpriseInformationBean> mDatas;
    private List<EnterpriseInformationBean> searchDatas;
    private String url;
    private String user_token;
    private EnterpriseInformationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_qy);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        inspect_qy_back = (ImageView) findViewById(R.id.inspect_qy_back);
        inspect_qy_search = (EditText) findViewById(R.id.inspect_qy_search);
        inspect_qy_clear = (ImageView) findViewById(R.id.inspect_qy_clear);
        inspect_qy_listview = (ListView) findViewById(R.id.inspect_qy_listview);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.Companyinfo();
//        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
//        editor = sp.edit();
//        user_token = sp.getString("user_token", null);
//        editor.commit();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        mDatas = new ArrayList<EnterpriseInformationBean>();
        mOkhttp();
    }

    @Override
    protected void setOnClick() {
        inspect_qy_back.setOnClickListener(this);
        inspect_qy_clear.setOnClickListener(this);
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, user_token + "-----------" + url);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsb = new JSONObject(response);
                            Log.e(TAG, jsb + "");
                            JSONArray jsar = jsb.getJSONArray("cells");

                            for (int i = 0; i < jsar.length(); i++) {
                                EnterpriseInformationBean bean = new EnterpriseInformationBean();
                                //企业名称
                                String comname = jsar.optJSONObject(i).getString("comname");
                                //监管部门
                                String chargedeptname = jsar.optJSONObject(i).getString("chargedeptname");
                                //企业地址
                                String zcaddress = jsar.optJSONObject(i).getString("zcaddress");
                                //企业ID
                                String qyid = jsar.optJSONObject(i).getString("qyid");

                                //赋值
                                bean.setCompanyName(comname);
                                bean.setSupervisionDepartment(chargedeptname);
                                bean.setEnterpriseAddress(zcaddress);
                                bean.setCompanyId(qyid);
                                mDatas.add(bean);
                            }
                            adapter = new EnterpriseInformationAdapter(InspectQy.this, mDatas);
                            inspect_qy_listview.setAdapter(adapter);

                            //listview子项点击监听
                            inspect_qy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ShowToast.showToastNowait(InspectQy.this, "点击了" + mDatas.get(position).getCompanyId());
                                    Intent intent = new Intent(InspectQy.this, HiddenDangerDetail.class);
                                    intent.putExtra("clickId", mDatas.get(position).getCompanyId());
                                    startActivity(intent);
                                }
                            });


                            //监听edittext
                            inspect_qy_search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (inspect_qy_search.length() > 0) {
                                        inspect_qy_clear.setVisibility(View.VISIBLE);
                                        search(inspect_qy_search.getText().toString().trim());
                                    } else {
                                        inspect_qy_clear.setVisibility(View.GONE);
                                        adapter.DataNotify(mDatas);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //搜索框
    private void search(String str) {
        searchDatas = new ArrayList<EnterpriseInformationBean>();
        for (EnterpriseInformationBean entity : mDatas) {
            try {
                if (entity.getCompanyName().contains(str) || entity.getSupervisionDepartment().contains(str) || entity.getEnterpriseAddress().contains(str)) {
                    searchDatas.add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter.DataNotify(searchDatas);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inspect_qy_back:
                finish();
                break;
            case R.id.inspect_qy_clear:

                break;
        }
    }
}
