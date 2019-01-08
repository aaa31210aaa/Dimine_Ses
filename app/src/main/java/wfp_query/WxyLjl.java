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

import adapter.WxyLjlAdapter;
import bean.WxyLjlBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class WxyLjl extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView wxy_ljl_back;
    private EditText wxy_ljl_search;
    private ImageView wxy_ljl_clear;
    private ListView wxy_ljl_listview;
    private List<WxyLjlBean> mDatas;
    private List<WxyLjlBean> searchDatas;
    private WxyLjlAdapter adapter;
    private String url;
    private String user_token;
    private BGARefreshLayout wxy_ljl_refresh;
    private BGARefreshLayout wxy_ljl_nodatarefresh;
    private WxyLjlBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxy_ljl);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        wxy_ljl_back = (ImageView) findViewById(R.id.wxy_ljl_back);
        wxy_ljl_search = (EditText) findViewById(R.id.wxy_ljl_search);
        wxy_ljl_clear = (ImageView) findViewById(R.id.wxy_ljl_clear);
        wxy_ljl_listview = (ListView) findViewById(R.id.wxy_ljl_listview);
        wxy_ljl_refresh = (BGARefreshLayout) findViewById(R.id.wxy_ljl_refresh);
        wxy_ljl_nodatarefresh = (BGARefreshLayout) findViewById(R.id.wxy_ljl_nodatarefresh);
        MyRefreshStyle(wxy_ljl_refresh);
        MyRefreshStyle(wxy_ljl_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.GetHazardmatter();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(WxyLjl.this, R.string.loading);
        mDatas = new ArrayList<WxyLjlBean>();
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
                        ShowToast.showToastNoWait(WxyLjl.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                wxy_ljl_refresh.setVisibility(View.VISIBLE);
                                wxy_ljl_nodatarefresh.setVisibility(View.GONE);
                              mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new WxyLjlBean();
                                    bean.setWzmc(jsonArray.optJSONObject(i).getString("wzmc"));
                                    bean.setWxwzid(jsonArray.optJSONObject(i).getString("wxwzid"));
                                    bean.setLbcode(jsonArray.optJSONObject(i).getString("lbcode"));
                                    bean.setTypename(jsonArray.optJSONObject(i).getString("typename"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new WxyLjlAdapter(WxyLjl.this, mDatas);
                                    wxy_ljl_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                wxy_ljl_refresh.setVisibility(View.GONE);
                                wxy_ljl_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        wxy_ljl_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (WxyLjlBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(WxyLjl.this, WxyLjlDetail.class);
                intent.putExtra("wxwzid", bean.getWxwzid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        wxy_ljl_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (wxy_ljl_search.length() > 0) {
                        wxy_ljl_clear.setVisibility(View.VISIBLE);
                        search(wxy_ljl_search.getText().toString().trim());
                    } else {
                        wxy_ljl_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (wxy_ljl_search.length() > 0) {
                        wxy_ljl_clear.setVisibility(View.VISIBLE);
                    } else {
                        wxy_ljl_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<WxyLjlBean>();
            for (WxyLjlBean entity : mDatas) {
                try {
                    if (entity.getWzmc().contains(str) || entity.getLbcode().contains(str) || entity.getTypename().contains(str)) {
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
        wxy_ljl_back.setOnClickListener(this);
        wxy_ljl_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxy_ljl_back:
                finish();
                break;
            case R.id.wxy_ljl_clear:
                wxy_ljl_search.setText("");
                wxy_ljl_clear.setVisibility(View.GONE);
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
                    wxy_ljl_refresh.endRefreshing();
                    wxy_ljl_nodatarefresh.endRefreshing();
                    ShowToast.showShort(WxyLjl.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            wxy_ljl_refresh.endRefreshing();
            wxy_ljl_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
