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

import adapter.DangerousAdapter;
import bean.DangerousBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Dangerous extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView dangerous_back;
    private EditText dangerous_search;
    private ImageView dangerous_clear;
    private ListView dangerous_listview;
    private List<DangerousBean> mDatas;
    private List<DangerousBean> searchDatas;
    private DangerousAdapter adapter;
    private BGARefreshLayout dangerous_refresh;
    private BGARefreshLayout dangerous_nodatarefresh;

    private String url;
    private String user_token;
    private DangerousBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        dangerous_back = (ImageView) findViewById(R.id.dangerous_back);
        dangerous_search = (EditText) findViewById(R.id.dangerous_search);
        dangerous_clear = (ImageView) findViewById(R.id.dangerous_clear);
        dangerous_listview = (ListView) findViewById(R.id.dangerous_listview);
        dangerous_refresh = (BGARefreshLayout) findViewById(R.id.dangerous_refresh);
        dangerous_nodatarefresh = (BGARefreshLayout) findViewById(R.id.dangerous_nodatarefresh);
        MyRefreshStyle(dangerous_refresh);
        MyRefreshStyle(dangerous_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.GetChemical();
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<DangerousBean>();
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
                        ShowToast.showToastNoWait(Dangerous.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "----");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                dangerous_refresh.setVisibility(View.VISIBLE);
                                dangerous_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new DangerousBean();
                                    bean.setChname(jsonArray.optJSONObject(i).getString("chname"));
                                    bean.setHxpid(jsonArray.optJSONObject(i).getString("hxpid"));
                                    bean.setCcode(jsonArray.optJSONObject(i).getString("ccode"));
                                    bean.setEnname(jsonArray.optJSONObject(i).getString("enname"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new DangerousAdapter(Dangerous.this, mDatas);
                                    dangerous_listview.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                dangerous_refresh.setVisibility(View.GONE);
                                dangerous_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        dangerous_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (DangerousBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(Dangerous.this, DangerousDetail.class);
                intent.putExtra("hxpid", bean.getHxpid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        dangerous_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null){
                    if (dangerous_search.length() > 0) {
                        dangerous_clear.setVisibility(View.VISIBLE);
                        search(dangerous_search.getText().toString().trim());
                    } else {
                        dangerous_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                }else{
                    if (dangerous_search.length() > 0) {
                        dangerous_clear.setVisibility(View.VISIBLE);
                    }else{
                        dangerous_clear.setVisibility(View.GONE);
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
        if (mDatas != null){
            searchDatas = new ArrayList<DangerousBean>();
            for (DangerousBean entity : mDatas) {
                try {
                    if (entity.getChname().contains(str) || entity.getCcode().contains(str) || entity.getEnname().contains(str)) {
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
        dangerous_back.setOnClickListener(this);
        dangerous_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dangerous_back:
                finish();
                break;
            case R.id.dangerous_clear:
                dangerous_search.setText("");
                dangerous_clear.setVisibility(View.GONE);
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
                    dangerous_refresh.endRefreshing();
                    dangerous_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Dangerous.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            dangerous_refresh.endRefreshing();
            dangerous_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
