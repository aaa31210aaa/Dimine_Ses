package home_content;

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

import com.example.administrator.dimine_sis.NetUtils;
import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.AqScTrAdapter;
import bean.AqScTrBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AqScTr extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView aqsctr_back;
    private EditText aqsctr_search;
    private ImageView aqsctr_clear;
    private BGARefreshLayout aqsctr_refresh;
    private BGARefreshLayout aqsctr_nodatarefresh;
    private ListView aqsctr_listview;

    private String qyid;
    private Intent intent;
    private String mycode;

    private List<AqScTrBean> mDatas;
    private List<AqScTrBean> searchDatas;
    private AqScTrBean bean;
    private AqScTrAdapter adapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_sc_tr);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        aqsctr_back = (ImageView) findViewById(R.id.aqsctr_back);
        aqsctr_search = (EditText) findViewById(R.id.aqsctr_search);
        aqsctr_clear = (ImageView) findViewById(R.id.aqsctr_clear);
        aqsctr_refresh = (BGARefreshLayout) findViewById(R.id.aqsctr_refresh);
        aqsctr_nodatarefresh = (BGARefreshLayout) findViewById(R.id.aqsctr_nodatarefresh);
        MyRefreshStyle(aqsctr_refresh);
        MyRefreshStyle(aqsctr_nodatarefresh);
        aqsctr_listview = (ListView) findViewById(R.id.aqsctr_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetSafeinvestmentQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetSafeinvestment();
            qyid = "";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<AqScTrBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", SharedPrefsUtil.getValue(this, "userInfo", "user_token", null))
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AqScTr.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "---");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                aqsctr_refresh.setVisibility(View.VISIBLE);
                                aqsctr_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new AqScTrBean();
                                    bean.setSiid(jsonArray.optJSONObject(i).getString("siid"));
                                    bean.setMonthvalue(jsonArray.optJSONObject(i).getString("monthvalue"));
                                    bean.setInvestypename(jsonArray.optJSONObject(i).getString("investypename"));
                                    bean.setInvestname(jsonArray.optJSONObject(i).getString("investname"));
                                    bean.setUsemoney(jsonArray.optJSONObject(i).getString("usemoney"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new AqScTrAdapter(AqScTr.this, mDatas, mycode);
                                    aqsctr_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                aqsctr_refresh.setVisibility(View.GONE);
                                aqsctr_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        aqsctr_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (AqScTrBean) parent.getItemAtPosition(position);
                intent = new Intent(AqScTr.this, AqScTrDetail.class);
                intent.putExtra("siid", bean.getSiid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        aqsctr_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (aqsctr_search.length() > 0) {
                        aqsctr_clear.setVisibility(View.VISIBLE);
                        search(aqsctr_search.getText().toString().trim());
                    } else {
                        aqsctr_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (aqsctr_search.length() > 0) {
                        aqsctr_clear.setVisibility(View.VISIBLE);
                    } else {
                        aqsctr_clear.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索框
    private void search(String str) {
        searchDatas = new ArrayList<AqScTrBean>();
        for (AqScTrBean entity : mDatas) {
            try {
                if (entity.getMonthvalue().contains(str) || entity.getInvestypename().contains(str) || entity.getInvestname().contains(str) || entity.getUsemoney().contains(str) || entity.getQyname().contains(str)) {
                    searchDatas.add(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter.DataNotify(searchDatas);
        }
    }


    @Override
    protected void setOnClick() {
        aqsctr_back.setOnClickListener(this);
        aqsctr_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aqsctr_back:
                finish();
                break;
            case R.id.aqsctr_clear:
                aqsctr_search.setText("");
                aqsctr_clear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(this)) {
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOkhttp();
                    aqsctr_refresh.endRefreshing();
                    aqsctr_nodatarefresh.endRefreshing();
                    ShowToast.showShort(AqScTr.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            aqsctr_refresh.endRefreshing();
            aqsctr_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
