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

import adapter.RyGlAdapter;
import bean.RyGlBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class RyGl extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView aqrygl_back;
    private EditText aqrygl_search;
    private ImageView aqrygl_clear;
    private BGARefreshLayout aqrygl_refresh;
    private BGARefreshLayout aqrygl_nodatarefresh;
    private ListView aqrygl_listview;

    private String qyid;
    private Intent intent;

    private List<RyGlBean> mDatas;
    private List<RyGlBean> searchDatas;
    private RyGlBean bean;
    private RyGlAdapter adapter;
    private String mycode;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ry_gl);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        aqrygl_back = (ImageView) findViewById(R.id.aqrygl_back);
        aqrygl_search = (EditText) findViewById(R.id.aqrygl_search);
        aqrygl_clear = (ImageView) findViewById(R.id.aqrygl_clear);
        aqrygl_refresh = (BGARefreshLayout) findViewById(R.id.aqrygl_refresh);
        aqrygl_nodatarefresh = (BGARefreshLayout) findViewById(R.id.aqrygl_nodatarefresh);
        MyRefreshStyle(aqrygl_refresh);
        MyRefreshStyle(aqrygl_nodatarefresh);
        aqrygl_listview = (ListView) findViewById(R.id.aqrygl_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            qyid = intent.getStringExtra("mClickId");
            url = PortIpAddress.GetSafepeopleQy();
        } else {
            qyid = "";
            url = PortIpAddress.GetSafepeople();
        }


        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<RyGlBean>();
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
                        ShowToast.showShort(RyGl.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                aqrygl_refresh.setVisibility(View.VISIBLE);
                                aqrygl_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new RyGlBean();
                                    bean.setSpid(jsonArray.optJSONObject(i).getString("spid"));
                                    bean.setSptypename(jsonArray.optJSONObject(i).getString("sptypename"));
                                    bean.setMainhead(jsonArray.optJSONObject(i).getString("mainhead"));
                                    bean.setDeptname(jsonArray.optJSONObject(i).getString("deptname"));
                                    bean.setYxstardate(jsonArray.optJSONObject(i).getString("yxstardate"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new RyGlAdapter(RyGl.this, mDatas);
                                    aqrygl_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                aqrygl_refresh.setVisibility(View.GONE);
                                aqrygl_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        aqrygl_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (RyGlBean) parent.getItemAtPosition(position);
                intent = new Intent(RyGl.this, RyGlDetail.class);
                intent.putExtra("detailid", bean.getSpid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        aqrygl_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (aqrygl_search.length() > 0) {
                        aqrygl_clear.setVisibility(View.VISIBLE);
                        search(aqrygl_search.getText().toString().trim());
                    } else {
                        aqrygl_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (aqrygl_search.length() > 0) {
                        aqrygl_clear.setVisibility(View.VISIBLE);
                    } else {
                        aqrygl_clear.setVisibility(View.GONE);
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
        for (RyGlBean entity : mDatas) {
            try {
                if (entity.getSptypename().contains(str) || entity.getMainhead().contains(str) || entity.getDeptname().contains(str)) {
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
        aqrygl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aqrygl_back:
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
                    aqrygl_refresh.endRefreshing();
                    aqrygl_nodatarefresh.endRefreshing();
                    ShowToast.showShort(RyGl.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            aqrygl_refresh.endRefreshing();
            aqrygl_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
