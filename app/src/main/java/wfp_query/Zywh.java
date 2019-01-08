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

import adapter.ZywhAdapter;
import bean.ZyWhBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Zywh extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView zywh_back;
    private EditText zywh_search;
    private ImageView zywh_clear;
    private ListView zywh_listview;
    private List<ZyWhBean> mDatas;
    private List<ZyWhBean> searchDatas;
    private ZywhAdapter adapter;
    private BGARefreshLayout zywh_refresh;
    private BGARefreshLayout zywh_nodatarefresh;

    private String url;
    private String user_token;
    private ZyWhBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zywh);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        zywh_back = (ImageView) findViewById(R.id.zywh_back);
        zywh_search = (EditText) findViewById(R.id.zywh_search);
        zywh_clear = (ImageView) findViewById(R.id.zywh_clear);
        zywh_listview = (ListView) findViewById(R.id.zywh_listview);
        zywh_refresh = (BGARefreshLayout) findViewById(R.id.zywh_refresh);
        zywh_nodatarefresh = (BGARefreshLayout) findViewById(R.id.zywh_nodatarefresh);
        MyRefreshStyle(zywh_refresh);
        MyRefreshStyle(zywh_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.Contactlimit();
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<ZyWhBean>();
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
                        ShowToast.showToastNoWait(Zywh.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");

                            if (jsonArray.length() > 0) {
                                zywh_refresh.setVisibility(View.VISIBLE);
                                zywh_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new ZyWhBean();
                                    bean.setClid(jsonArray.optJSONObject(i).getString("clid"));
                                    bean.setDangerno(jsonArray.optJSONObject(i).getString("dangerno"));
                                    bean.setClname(jsonArray.optJSONObject(i).getString("clname"));
                                    bean.setEnname(jsonArray.optJSONObject(i).getString("enname"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new ZywhAdapter(Zywh.this, mDatas);
                                    zywh_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                zywh_refresh.setVisibility(View.GONE);
                                zywh_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void MonitorList() {
        zywh_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (ZyWhBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(Zywh.this, ZywhDetail.class);
                intent.putExtra("clid", bean.getClid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        zywh_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (zywh_search.length() > 0) {
                        zywh_clear.setVisibility(View.VISIBLE);
                        search(zywh_search.getText().toString().trim());
                    } else {
                        zywh_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (zywh_search.length() > 0) {
                        zywh_clear.setVisibility(View.VISIBLE);
                    } else {
                        zywh_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<ZyWhBean>();
            for (ZyWhBean entity : mDatas) {
                try {
                    if (entity.getClname().contains(str) || entity.getDangerno().contains(str) || entity.getEnname().contains(str)) {
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
        zywh_back.setOnClickListener(this);
        zywh_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zywh_back:
                finish();
                break;
            case R.id.zywh_clear:
                zywh_search.setText("");
                zywh_clear.setVisibility(View.GONE);
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
                    zywh_refresh.endRefreshing();
                    zywh_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Zywh.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            zywh_refresh.endRefreshing();
            zywh_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
