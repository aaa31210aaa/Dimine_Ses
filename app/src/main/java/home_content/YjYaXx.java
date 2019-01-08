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

import adapter.YjYaXxAdapter;
import bean.YjYaXxBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class YjYaXx extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView yjyaxx_back;
    private EditText yjyaxx_search;
    private ImageView yjyaxx_clear;
    private BGARefreshLayout yjyaxx_refresh;
    private BGARefreshLayout yjyaxx_nodatarefresh;
    private ListView yjyaxx_listview;

    private String qyid;
    private Intent intent;
    private String mycode;
    private List<YjYaXxBean> mDatas;
    private List<YjYaXxBean> searchDatas;
    private YjYaXxBean bean;
    private YjYaXxAdapter adapter;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yj_ya_xx);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        yjyaxx_back = (ImageView) findViewById(R.id.yjyaxx_back);
        yjyaxx_search = (EditText) findViewById(R.id.yjyaxx_search);
        yjyaxx_clear = (ImageView) findViewById(R.id.yjyaxx_clear);
        yjyaxx_refresh = (BGARefreshLayout) findViewById(R.id.yjyaxx_refresh);
        yjyaxx_nodatarefresh = (BGARefreshLayout) findViewById(R.id.yjyaxx_nodatarefresh);
        MyRefreshStyle(yjyaxx_refresh);
        MyRefreshStyle(yjyaxx_nodatarefresh);
        yjyaxx_listview = (ListView) findViewById(R.id.yjyaxx_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetEmergencyreserveQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetEmergencyreserve();
            qyid = "";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<YjYaXxBean>();
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
                        ShowToast.showShort(YjYaXx.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                yjyaxx_refresh.setVisibility(View.VISIBLE);
                                yjyaxx_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new YjYaXxBean();
                                    bean.setErid(jsonArray.optJSONObject(i).getString("erid"));
                                    bean.setOhaddres(jsonArray.optJSONObject(i).getString("ohaddres"));
                                    bean.setReservname(jsonArray.optJSONObject(i).getString("reservname"));
                                    bean.setReloaddate(jsonArray.optJSONObject(i).getString("reloaddate"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new YjYaXxAdapter(YjYaXx.this, mDatas, mycode);
                                    yjyaxx_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                yjyaxx_refresh.setVisibility(View.GONE);
                                yjyaxx_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    private void MonitorList() {
        yjyaxx_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (YjYaXxBean) parent.getItemAtPosition(position);
                intent = new Intent(YjYaXx.this, YjYaXxDetail.class);
                intent.putExtra("erid", bean.getErid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        yjyaxx_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (yjyaxx_search.length() > 0) {
                        yjyaxx_clear.setVisibility(View.VISIBLE);
                        search(yjyaxx_search.getText().toString().trim());
                    } else {
                        yjyaxx_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (yjyaxx_search.length() > 0) {
                        yjyaxx_clear.setVisibility(View.VISIBLE);
                    } else {
                        yjyaxx_clear.setVisibility(View.GONE);
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
        if (mDatas!=null){
            searchDatas = new ArrayList<YjYaXxBean>();
            for (YjYaXxBean entity : mDatas) {
                try {
                    if (entity.getOhaddres().contains(str) || entity.getReservname().contains(str) || entity.getReloaddate().contains(str) || entity.getQyname().contains(str)) {
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
        yjyaxx_back.setOnClickListener(this);
        yjyaxx_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yjyaxx_back:
                finish();
                break;
            case R.id.yjyaxx_clear:
                yjyaxx_search.setText("");
                yjyaxx_clear.setVisibility(View.GONE);
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
                    yjyaxx_refresh.endRefreshing();
                    yjyaxx_nodatarefresh.endRefreshing();
                    ShowToast.showShort(YjYaXx.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            yjyaxx_refresh.endRefreshing();
            yjyaxx_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
