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

import adapter.TzSbAdapter;
import bean.TzSbBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class TzSb extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView tzsb_back;
    private EditText tzsb_search;
    private ImageView tzsb_clear;
    private BGARefreshLayout tzsb_refresh;
    private BGARefreshLayout tzsb_nodatarefresh;
    private ListView tzsb_listview;

    private String qyid;
    private Intent intent;

    private List<TzSbBean> mDatas;
    private List<TzSbBean> searchDatas;
    private TzSbBean bean;
    private TzSbAdapter adapter;
    private String mycode;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz_sb);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        tzsb_back = (ImageView) findViewById(R.id.tzsb_back);
        tzsb_search = (EditText) findViewById(R.id.tzsb_search);
        tzsb_clear = (ImageView) findViewById(R.id.tzsb_clear);
        tzsb_refresh = (BGARefreshLayout) findViewById(R.id.tzsb_refresh);
        tzsb_nodatarefresh = (BGARefreshLayout) findViewById(R.id.tzsb_nodatarefresh);
        MyRefreshStyle(tzsb_refresh);
        MyRefreshStyle(tzsb_nodatarefresh);
        tzsb_listview = (ListView) findViewById(R.id.tzsb_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            qyid = intent.getStringExtra("mClickId");
            url = PortIpAddress.GetSafefacilitiesQy();
        } else {
            qyid = "";
            url = PortIpAddress.GetSafefacilities();
        }
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<TzSbBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(TzSb.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                tzsb_refresh.setVisibility(View.VISIBLE);
                                tzsb_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new TzSbBean();
                                    bean.setSfid(jsonArray.optJSONObject(i).getString("sfid"));
                                    bean.setFacilitiesname(jsonArray.optJSONObject(i).getString("facilitiesname"));
                                    bean.setFacilitiesno(jsonArray.optJSONObject(i).getString("facilitiesno"));
                                    bean.setManufacturer(jsonArray.optJSONObject(i).getString("manufacturer"));
                                    bean.setInstallsite(jsonArray.optJSONObject(i).getString("installsite"));
                                    bean.setCheckdate(jsonArray.optJSONObject(i).getString("checkdate"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new TzSbAdapter(TzSb.this, mDatas);
                                    tzsb_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                tzsb_refresh.setVisibility(View.GONE);
                                tzsb_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        tzsb_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (TzSbBean) parent.getItemAtPosition(position);
                intent = new Intent(TzSb.this, TzSbDetail.class);
                intent.putExtra("detailid", bean.getSfid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        tzsb_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (tzsb_search.length() > 0) {
                        tzsb_clear.setVisibility(View.VISIBLE);
                        search(tzsb_search.getText().toString().trim());
                    } else {
                        tzsb_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (tzsb_search.length() > 0) {
                        tzsb_clear.setVisibility(View.VISIBLE);
                    } else {
                        tzsb_clear.setVisibility(View.GONE);
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
        for (TzSbBean entity : mDatas) {
            try {
                if (entity.getFacilitiesname().contains(str) || entity.getFacilitiesno().contains(str) || entity.getManufacturer().contains(str) || entity.getInstallsite().contains(str)) {
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
        tzsb_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tzsb_back:
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
                    tzsb_refresh.endRefreshing();
                    tzsb_nodatarefresh.endRefreshing();
                    ShowToast.showShort(TzSb.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            tzsb_refresh.endRefreshing();
            tzsb_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
