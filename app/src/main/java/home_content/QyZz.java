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

import adapter.QyZzAdapter;
import bean.QyZzBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.ShowToast;

public class QyZz extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private ImageView qyzz_back;
    private EditText qyzz_search;
    private ImageView qyzz_clear;
    private BGARefreshLayout qyzz_refresh;
    private BGARefreshLayout qyzz_nodatarefresh;
    private ListView qyzz_listview;

    private String qyid;
    private Intent intent;

    private List<QyZzBean> mDatas;
    private List<QyZzBean> searchDatas;
    private QyZzBean bean;
    private QyZzAdapter adapter;
    private String mycode;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_zz);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        qyzz_back = (ImageView) findViewById(R.id.qyzz_back);
        qyzz_search = (EditText) findViewById(R.id.qyzz_search);
        qyzz_clear = (ImageView) findViewById(R.id.qyzz_clear);
        qyzz_refresh = (BGARefreshLayout) findViewById(R.id.qyzz_refresh);
        qyzz_nodatarefresh = (BGARefreshLayout) findViewById(R.id.qyzz_nodatarefresh);
        MyRefreshStyle(qyzz_refresh);
        MyRefreshStyle(qyzz_nodatarefresh);
        qyzz_listview = (ListView) findViewById(R.id.qyzz_listview);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            qyid = intent.getStringExtra("mClickId");
            url = PortIpAddress.GetCertificatelistQy();
        } else {
            qyid = "";
            url = PortIpAddress.GetCertificatelist();
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<QyZzBean>();
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
                        ShowToast.showShort(QyZz.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                qyzz_refresh.setVisibility(View.VISIBLE);
                                qyzz_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new QyZzBean();
                                    bean.setCfid(jsonArray.optJSONObject(i).getString("cfid"));
                                    bean.setCftypename(jsonArray.optJSONObject(i).getString("cftypename"));
                                    bean.setCertificate(jsonArray.optJSONObject(i).getString("certificate"));
                                    bean.setCardname(jsonArray.optJSONObject(i).getString("cardname"));
                                    bean.setAdministrator(jsonArray.optJSONObject(i).getString("administrator"));
                                    bean.setCarddate(jsonArray.optJSONObject(i).getString("carddate"));
                                    bean.setIsexpire(jsonArray.optJSONObject(i).getString("isexpire"));
                                    mDatas.add(bean);
                                }

                                if (adapter == null) {
                                    adapter = new QyZzAdapter(QyZz.this, mDatas);
                                    qyzz_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                qyzz_refresh.setVisibility(View.GONE);
                                qyzz_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        qyzz_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (QyZzBean) parent.getItemAtPosition(position);
                intent = new Intent(QyZz.this, QyZzDetail.class);
                intent.putExtra("detailid", bean.getCfid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEtv() {
        //监听edittext
        qyzz_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (qyzz_search.length() > 0) {
                        qyzz_clear.setVisibility(View.VISIBLE);
                        search(qyzz_search.getText().toString().trim());
                    } else {
                        qyzz_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (qyzz_search.length() > 0) {
                        qyzz_clear.setVisibility(View.VISIBLE);
                    } else {
                        qyzz_clear.setVisibility(View.GONE);
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
        for (QyZzBean entity : mDatas) {
            try {
                if (entity.getCftypename().contains(str) || entity.getCertificate().contains(str) || entity.getCardname().contains(str) || entity.getAdministrator().contains(str) || entity.getCarddate().contains(str)) {
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
        qyzz_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qyzz_back:
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
                    qyzz_refresh.endRefreshing();
                    qyzz_nodatarefresh.endRefreshing();
                    ShowToast.showShort(QyZz.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            qyzz_refresh.endRefreshing();
            qyzz_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
