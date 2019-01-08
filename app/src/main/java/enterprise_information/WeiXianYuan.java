package enterprise_information;

import android.app.Dialog;
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

import adapter.WxyAdapter;
import bean.WxyBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class WeiXianYuan extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView wxy_back;
    private ListView wxy_listview;
    private String user_token;
    private String url;
    private String qyid;
    private Intent intent;
    private List<WxyBean> mDatas;
    private List<WxyBean> searchDatas;
    private WxyAdapter adapter;
    private EditText wxy_search;
    private ImageView wxy_clear;
    private Dialog dialog;
    private WxyBean bean;
    private BGARefreshLayout wxy_refresh;
    private BGARefreshLayout wxy_nodatarefresh;
    private String mycode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xian_yuan);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        wxy_back = (ImageView) findViewById(R.id.wxy_back);
        wxy_listview = (ListView) findViewById(R.id.wxy_listview);
        wxy_search = (EditText) findViewById(R.id.wxy_search);
        wxy_clear = (ImageView) findViewById(R.id.wxy_clear);
        wxy_refresh = (BGARefreshLayout) findViewById(R.id.wxy_refresh);
        wxy_nodatarefresh = (BGARefreshLayout) findViewById(R.id.wxy_nodatarefresh);
        MyRefreshStyle(wxy_refresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            url = PortIpAddress.GetCompanyRiskQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetCompanyRisk();
            qyid = "";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<WxyBean>();
        mOkhttp();
        MonitorList();
        MonitorEtv();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("qyid", qyid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(WeiXianYuan.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "---");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                wxy_refresh.setVisibility(View.VISIBLE);
                                wxy_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new WxyBean();
                                    bean.setMainrisk(jsonArray.optJSONObject(i).getString("mainrisk"));
                                    bean.setFxdid(jsonArray.optJSONObject(i).getString("fxdid"));
                                    bean.setMaindanger(jsonArray.optJSONObject(i).getString("maindanger"));
                                    bean.setRisktypename(jsonArray.optJSONObject(i).getString("risktypename"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new WxyAdapter(WeiXianYuan.this, mDatas, mycode);
                                    wxy_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                wxy_refresh.setVisibility(View.GONE);
                                wxy_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        wxy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (WxyBean) parent.getItemAtPosition(position);
                intent = new Intent(WeiXianYuan.this, WeiXianYuanDetail.class);
                intent.putExtra("fxdid", bean.getFxdid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        wxy_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (wxy_search.length() > 0) {
                        wxy_clear.setVisibility(View.VISIBLE);
                        search(wxy_search.getText().toString().trim());
                    } else {
                        wxy_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (wxy_search.length() > 0) {
                        wxy_clear.setVisibility(View.VISIBLE);
                    } else {
                        wxy_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<WxyBean>();
        for (WxyBean entity : mDatas) {
            try {
                if (entity.getMainrisk().contains(str) || entity.getMaindanger().contains(str) || entity.getRisktypename().contains(str)) {
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
        wxy_back.setOnClickListener(this);
        wxy_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxy_back:
                finish();
                break;
            case R.id.wxy_clear:
                wxy_search.setText("");
                wxy_clear.setVisibility(View.GONE);
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
                    wxy_refresh.endRefreshing();
                    wxy_nodatarefresh.endRefreshing();
                    ShowToast.showShort(WeiXianYuan.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            wxy_refresh.endRefreshing();
            wxy_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
