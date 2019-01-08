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

import adapter.YjYlAdapter;
import bean.YjYlBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class YjYl extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private ImageView yjyl_back;
    private EditText yjyl_search;
    private ImageView yjyl_clear;
    private BGARefreshLayout yjyl_refresh;
    private BGARefreshLayout yjyl_nodatarefresh;
    private ListView yjyl_listview;

    private String qyid;
    private Intent intent;

    private List<YjYlBean> mDatas;
    private List<YjYlBean> searchDatas;
    private YjYlBean bean;
    private YjYlAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yj_yl);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        yjyl_back = (ImageView) findViewById(R.id.yjyl_back);
        yjyl_search = (EditText) findViewById(R.id.yjyl_search);
        yjyl_clear = (ImageView) findViewById(R.id.yjyl_clear);
        yjyl_refresh = (BGARefreshLayout) findViewById(R.id.yjyl_refresh);
        yjyl_nodatarefresh = (BGARefreshLayout) findViewById(R.id.yjyl_nodatarefresh);
        MyRefreshStyle(yjyl_refresh);
        MyRefreshStyle(yjyl_nodatarefresh);
        yjyl_listview = (ListView) findViewById(R.id.yjyl_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        qyid = intent.getStringExtra("mClickId");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<YjYlBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetEmergencydrill())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(YjYl.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "---");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                yjyl_refresh.setVisibility(View.VISIBLE);
                                yjyl_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new YjYlBean();
                                    bean.setEdid(jsonArray.optJSONObject(i).getString("edid"));
                                    bean.setDrilldate(jsonArray.optJSONObject(i).getString("drilldate"));
                                    bean.setDrillcontent(jsonArray.optJSONObject(i).getString("drillcontent"));
                                    bean.setDrillman(jsonArray.optJSONObject(i).getString("drillman"));
                                    bean.setReservname(jsonArray.optJSONObject(i).getString("reservname"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new YjYlAdapter(YjYl.this, mDatas);
                                    yjyl_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                yjyl_refresh.setVisibility(View.GONE);
                                yjyl_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        yjyl_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (YjYlBean) parent.getItemAtPosition(position);
                intent = new Intent(YjYl.this, YjYlDetail.class);
                intent.putExtra("detailid", bean.getEdid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        yjyl_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (yjyl_search.length() > 0) {
                        yjyl_clear.setVisibility(View.VISIBLE);
                        search(yjyl_search.getText().toString().trim());
                    } else {
                        yjyl_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (yjyl_search.length() > 0) {
                        yjyl_clear.setVisibility(View.VISIBLE);
                    } else {
                        yjyl_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<>();
        for (YjYlBean entity : mDatas) {
            try {
                if (entity.getDrilldate().contains(str) || entity.getDrillcontent().contains(str) || entity.getDrillman().contains(str) || entity.getReservname().contains(str)) {
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
        yjyl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yjyl_back:
                finish();
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
                    yjyl_refresh.endRefreshing();
                    yjyl_nodatarefresh.endRefreshing();
                    ShowToast.showShort(YjYl.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            yjyl_refresh.endRefreshing();
            yjyl_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
