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

import adapter.AqJyPxAdapter;
import bean.AqJyPxBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AqJyPx extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView aqjypx_back;
    private EditText aqjypx_search;
    private ImageView aqjypx_clear;
    private BGARefreshLayout aqjypx_refresh;
    private BGARefreshLayout aqjypx_nodatarefresh;
    private ListView aqjypx_listview;


    private String qyid;
    private Intent intent;
    private String mycode;

    private List<AqJyPxBean> mDatas;
    private List<AqJyPxBean> searchDatas;
    private AqJyPxBean bean;
    private AqJyPxAdapter adapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_jy_px);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        aqjypx_back = (ImageView) findViewById(R.id.aqjypx_back);
        aqjypx_search = (EditText) findViewById(R.id.aqjypx_search);
        aqjypx_clear = (ImageView) findViewById(R.id.aqjypx_clear);
        aqjypx_refresh = (BGARefreshLayout) findViewById(R.id.aqjypx_refresh);
        aqjypx_nodatarefresh = (BGARefreshLayout) findViewById(R.id.aqjypx_nodatarefresh);
        MyRefreshStyle(aqjypx_refresh);
        MyRefreshStyle(aqjypx_nodatarefresh);
        aqjypx_listview = (ListView) findViewById(R.id.aqjypx_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetSafetytrainingQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetSafetytraining();
            qyid = "";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<AqJyPxBean>();
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
                        ShowToast.showShort(AqJyPx.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                aqjypx_refresh.setVisibility(View.VISIBLE);
                                aqjypx_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new AqJyPxBean();
                                    bean.setTrainingid(jsonArray.optJSONObject(i).getString("trainingid"));
                                    bean.setTrainingname(jsonArray.optJSONObject(i).getString("trainingname"));
                                    bean.setTrainingdate(jsonArray.optJSONObject(i).getString("trainingdate"));
                                    bean.setTrainingadd(jsonArray.optJSONObject(i).getString("trainingadd"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new AqJyPxAdapter(AqJyPx.this, mDatas, mycode);
                                    aqjypx_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                aqjypx_refresh.setVisibility(View.GONE);
                                aqjypx_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        aqjypx_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (AqJyPxBean) parent.getItemAtPosition(position);
                intent = new Intent(AqJyPx.this, AqJyPxDetail.class);
                intent.putExtra("trainingid", bean.getTrainingid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        aqjypx_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (aqjypx_search.length() > 0) {
                        aqjypx_clear.setVisibility(View.VISIBLE);
                        search(aqjypx_search.getText().toString().trim());
                    } else {
                        aqjypx_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (aqjypx_search.length() > 0) {
                        aqjypx_clear.setVisibility(View.VISIBLE);
                    } else {
                        aqjypx_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<AqJyPxBean>();
        for (AqJyPxBean entity : mDatas) {
            try {
                if (entity.getTrainingname().contains(str) || entity.getTrainingdate().contains(str) || entity.getTrainingadd().contains(str) || entity.getQyname().contains(str)) {
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
        aqjypx_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aqjypx_back:
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
                    aqjypx_refresh.endRefreshing();
                    aqjypx_nodatarefresh.endRefreshing();
                    ShowToast.showShort(AqJyPx.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            aqjypx_refresh.endRefreshing();
            aqjypx_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
