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

import adapter.BmGlAdapter;
import bean.BmGlBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class BmGl extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private ImageView bmgl_back;
    private EditText bmgl_search;
    private ImageView bmgl_clear;
    private BGARefreshLayout bmgl_refresh;
    private BGARefreshLayout bmgl_nodatarefresh;
    private ListView bmgl_listview;

    private String qyid;
    private Intent intent;

    private List<BmGlBean> mDatas;
    private List<BmGlBean> searchDatas;
    private BmGlBean bean;
    private BmGlAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bm_gl);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        bmgl_back = (ImageView) findViewById(R.id.bmgl_back);
        bmgl_search = (EditText) findViewById(R.id.bmgl_search);
        bmgl_clear = (ImageView) findViewById(R.id.bmgl_clear);
        bmgl_refresh = (BGARefreshLayout) findViewById(R.id.bmgl_refresh);
        bmgl_nodatarefresh = (BGARefreshLayout) findViewById(R.id.bmgl_nodatarefresh);
        MyRefreshStyle(bmgl_refresh);
        MyRefreshStyle(bmgl_nodatarefresh);
        bmgl_listview = (ListView) findViewById(R.id.bmgl_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        qyid = intent.getStringExtra("mClickId");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<BmGlBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetCompanydept())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(BmGl.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "---");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                bmgl_refresh.setVisibility(View.VISIBLE);
                                bmgl_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new BmGlBean();
                                    bean.setDeptid(jsonArray.optJSONObject(i).getString("deptid"));
                                    bean.setParentdeptname(jsonArray.optJSONObject(i).getString("parentdeptname"));
                                    bean.setDeptname(jsonArray.optJSONObject(i).getString("deptname"));
                                    bean.setDeptcode(jsonArray.optJSONObject(i).getString("deptcode"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new BmGlAdapter(BmGl.this, mDatas);
                                    bmgl_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                bmgl_refresh.setVisibility(View.GONE);
                                bmgl_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        bmgl_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (BmGlBean) parent.getItemAtPosition(position);
                intent = new Intent(BmGl.this, BmGlDetail.class);
                intent.putExtra("detailid", bean.getDeptid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        bmgl_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (bmgl_search.length() > 0) {
                        bmgl_clear.setVisibility(View.VISIBLE);
                        search(bmgl_search.getText().toString().trim());
                    } else {
                        bmgl_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (bmgl_search.length() > 0) {
                        bmgl_clear.setVisibility(View.VISIBLE);
                    } else {
                        bmgl_clear.setVisibility(View.GONE);
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
        for (BmGlBean entity : mDatas) {
            try {
                if (entity.getParentdeptname().contains(str) || entity.getDeptname().contains(str) || entity.getDeptcode().contains(str)) {
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
        bmgl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bmgl_back:
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
                    bmgl_refresh.endRefreshing();
                    bmgl_nodatarefresh.endRefreshing();
                    ShowToast.showShort(BmGl.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            bmgl_refresh.endRefreshing();
            bmgl_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
