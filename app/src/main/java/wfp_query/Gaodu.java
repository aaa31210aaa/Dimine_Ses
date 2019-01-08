package wfp_query;

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

import adapter.GaoDuAdapter;
import bean.GaoduBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Gaodu extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView gaodu_back;
    private EditText gaodu_search;
    private List<GaoduBean> mDatas;
    private List<GaoduBean> searchDatas;
    private GaoDuAdapter adapter;

    private ImageView gaodu_clear;
    private ListView gaodu_listview;
    private String url;
    private String user_token;
    private BGARefreshLayout gaodu_refresh;
    private BGARefreshLayout gaodu_nodatarefresh;
    private GaoduBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaodu);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        gaodu_back = (ImageView) findViewById(R.id.gaodu_back);
        gaodu_search = (EditText) findViewById(R.id.gaodu_search);
        gaodu_clear = (ImageView) findViewById(R.id.gaodu_clear);
        gaodu_listview = (ListView) findViewById(R.id.gaodu_listview);
        gaodu_refresh = (BGARefreshLayout) findViewById(R.id.gaodu_refresh);
        gaodu_nodatarefresh = (BGARefreshLayout) findViewById(R.id.gaodu_nodatarefresh);
        MyRefreshStyle(gaodu_refresh);
        MyRefreshStyle(gaodu_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.Htoxicmatter();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(Gaodu.this, R.string.loading);
        mDatas = new ArrayList<GaoduBean>();
        mOkhttp();
        MonitorList();
        MonitorEditext();
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
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(Gaodu.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                gaodu_refresh.setVisibility(View.VISIBLE);
                                gaodu_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new GaoduBean();
                                    bean.setGdwpid(jsonArray.optJSONObject(i).getString("gdwpid"));
                                    bean.setGwwpbm(jsonArray.optJSONObject(i).getString("gwwpbm"));
                                    bean.setZwmz(jsonArray.optJSONObject(i).getString("zwmz"));
                                    bean.setYwmz(jsonArray.optJSONObject(i).getString("ywmz"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new GaoDuAdapter(Gaodu.this, mDatas);
                                    gaodu_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                gaodu_refresh.setVisibility(View.GONE);
                                gaodu_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void MonitorList() {
        gaodu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (GaoduBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(Gaodu.this, GaoduDetail.class);
                intent.putExtra("gdwpid", bean.getGdwpid());
                startActivity(intent);
            }
        });

    }

    private void MonitorEditext() {
        //监听edittext
        gaodu_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (gaodu_search.length() > 0) {
                        gaodu_clear.setVisibility(View.VISIBLE);
                        search(gaodu_search.getText().toString().trim());
                    } else {
                        gaodu_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (gaodu_search.length() > 0) {
                        gaodu_clear.setVisibility(View.VISIBLE);
                    }else{
                        gaodu_clear.setVisibility(View.GONE);
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
        if (mDatas != null) {
            searchDatas = new ArrayList<GaoduBean>();
            for (GaoduBean entity : mDatas) {
                try {
                    if (entity.getGwwpbm().contains(str) || entity.getZwmz().contains(str) || entity.getYwmz().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.DataNotify(searchDatas);
            }
        }
    }


    @Override
    protected void setOnClick() {
        gaodu_back.setOnClickListener(this);
        gaodu_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gaodu_back:
                finish();
                break;
            case R.id.gaodu_clear:
                gaodu_search.setText("");
                gaodu_clear.setVisibility(View.GONE);
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
                    gaodu_refresh.endRefreshing();
                    gaodu_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Gaodu.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            gaodu_refresh.endRefreshing();
            gaodu_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
