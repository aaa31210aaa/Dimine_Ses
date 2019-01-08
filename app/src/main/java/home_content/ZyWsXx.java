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

import adapter.ZyWsXxAdapter;
import bean.ZyWsXxBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class ZyWsXx extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView zywsxx_back;
    private EditText zywsxx_search;
    private ImageView zywsxx_clear;
    private BGARefreshLayout zywsxx_refresh;
    private BGARefreshLayout zywsxx_nodatarefresh;
    private ListView zywsxx_listview;
    private String qyid;
    private Intent intent;
    private String mycode;
    private List<ZyWsXxBean> mDatas;
    private List<ZyWsXxBean> searchDatas;
    private ZyWsXxBean bean;
    private ZyWsXxAdapter adapter;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zy_ws_xx);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        zywsxx_back = (ImageView) findViewById(R.id.zywsxx_back);
        zywsxx_search = (EditText) findViewById(R.id.zywsxx_search);
        zywsxx_clear = (ImageView) findViewById(R.id.zywsxx_clear);
        zywsxx_listview = (ListView) findViewById(R.id.zywsxx_listview);
        zywsxx_refresh = (BGARefreshLayout) findViewById(R.id.zywsxx_refresh);
        zywsxx_nodatarefresh = (BGARefreshLayout) findViewById(R.id.zywsxx_nodatarefresh);
        MyRefreshStyle(zywsxx_refresh);
        MyRefreshStyle(zywsxx_nodatarefresh);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetOccuhealthQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetOccuhealth();
            qyid = "";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<ZyWsXxBean>();
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
                        ShowToast.showShort(ZyWsXx.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                zywsxx_refresh.setVisibility(View.VISIBLE);
                                zywsxx_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new ZyWsXxBean();
                                    bean.setOhid(jsonArray.optJSONObject(i).getString("ohid"));
                                    bean.setOhaddres(jsonArray.optJSONObject(i).getString("ohaddres"));
                                    bean.setSites(jsonArray.optJSONObject(i).getString("sites"));
                                    bean.setOhname(jsonArray.optJSONObject(i).getString("ohname"));
                                    bean.setCreatedate(jsonArray.optJSONObject(i).getString("createdate"));
                                    bean.setDqdate(jsonArray.optJSONObject(i).getString("dqdate"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));


                                    if (mycode.equals("home")) {
                                        bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new ZyWsXxAdapter(ZyWsXx.this, mDatas, mycode);
                                    zywsxx_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                zywsxx_refresh.setVisibility(View.GONE);
                                zywsxx_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        zywsxx_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (ZyWsXxBean) parent.getItemAtPosition(position);
                intent = new Intent(ZyWsXx.this, ZyWsXxDetail.class);
                intent.putExtra("ohid", bean.getOhid());
                intent.putExtra("qyid", bean.getQyid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        zywsxx_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (zywsxx_search.length() > 0) {
                        zywsxx_clear.setVisibility(View.VISIBLE);
                        search(zywsxx_search.getText().toString().trim());
                    } else {
                        zywsxx_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (zywsxx_search.length() > 0) {
                        zywsxx_clear.setVisibility(View.VISIBLE);
                    } else {
                        zywsxx_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<ZyWsXxBean>();
        for (ZyWsXxBean entity : mDatas) {
            try {
                if (entity.getOhaddres().contains(str) || entity.getSites().contains(str) || entity.getOhname().contains(str) || entity.getCreatedate().contains(str) || entity.getQyname().contains(str)) {
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
        zywsxx_back.setOnClickListener(this);
        zywsxx_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zywsxx_back:
                finish();
                break;
            case R.id.zywsxx_clear:
                zywsxx_search.setText("");
                zywsxx_clear.setVisibility(View.GONE);
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
                    zywsxx_refresh.endRefreshing();
                    zywsxx_nodatarefresh.endRefreshing();
                    ShowToast.showShort(ZyWsXx.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            zywsxx_refresh.endRefreshing();
            zywsxx_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
