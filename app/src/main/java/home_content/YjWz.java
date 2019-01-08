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

import adapter.YjWzAdapter;
import bean.YjWzBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class YjWz extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView yjwz_back;
    private EditText yjwz_search;
    private ImageView yjwz_clear;
    private BGARefreshLayout yjwz_refresh;
    private BGARefreshLayout yjwz_nodatarefresh;
    private ListView yjwz_listview;

    private String qyid;
    private Intent intent;

    private List<YjWzBean> mDatas;
    private List<YjWzBean> searchDatas;
    private YjWzBean bean;
    private YjWzAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yj_wz);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        yjwz_back = (ImageView) findViewById(R.id.yjwz_back);
        yjwz_search = (EditText) findViewById(R.id.yjwz_search);
        yjwz_clear = (ImageView) findViewById(R.id.yjwz_clear);
        yjwz_refresh = (BGARefreshLayout) findViewById(R.id.yjwz_refresh);
        yjwz_nodatarefresh = (BGARefreshLayout) findViewById(R.id.yjwz_nodatarefresh);
        MyRefreshStyle(yjwz_refresh);
        MyRefreshStyle(yjwz_nodatarefresh);
        yjwz_listview = (ListView) findViewById(R.id.yjwz_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        qyid = intent.getStringExtra("mClickId");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<YjWzBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetEmergencymaterials())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(YjWz.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "---");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                yjwz_refresh.setVisibility(View.VISIBLE);
                                yjwz_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new YjWzBean();
                                    bean.setEmid(jsonArray.optJSONObject(i).getString("emid"));
                                    bean.setMaterialsname(jsonArray.optJSONObject(i).getString("materialsname"));
                                    bean.setMaterialssum(jsonArray.optJSONObject(i).getString("materialssum"));
                                    bean.setOhaddres(jsonArray.optJSONObject(i).getString("ohaddres"));
                                    bean.setModifydate(jsonArray.optJSONObject(i).getString("modifydate"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new YjWzAdapter(YjWz.this, mDatas);
                                    yjwz_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                yjwz_refresh.setVisibility(View.GONE);
                                yjwz_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        yjwz_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (YjWzBean) parent.getItemAtPosition(position);
                intent = new Intent(YjWz.this, YjWzDetail.class);
                intent.putExtra("detailid", bean.getEmid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        yjwz_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (yjwz_search.length() > 0) {
                        yjwz_clear.setVisibility(View.VISIBLE);
                        search(yjwz_search.getText().toString().trim());
                    } else {
                        yjwz_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (yjwz_search.length() > 0) {
                        yjwz_clear.setVisibility(View.VISIBLE);
                    } else {
                        yjwz_clear.setVisibility(View.GONE);
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
        for (YjWzBean entity : mDatas) {
            try {
                if (entity.getMaterialsname().contains(str) || entity.getMaterialssum().contains(str) || entity.getOhaddres().contains(str) || entity.getModifydate().contains(str)) {
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
        yjwz_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yjwz_back:
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
                    yjwz_refresh.endRefreshing();
                    yjwz_nodatarefresh.endRefreshing();
                    ShowToast.showShort(YjWz.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            yjwz_refresh.endRefreshing();
            yjwz_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
