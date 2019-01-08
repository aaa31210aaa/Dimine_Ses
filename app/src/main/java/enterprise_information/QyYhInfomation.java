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

import adapter.QyYhInformationAdapter;
import bean.QyYhInformationBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class QyYhInfomation extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView qyyh_information_back;
    private ListView qyyh_information_listview;
    private String user_token;
    private String url;
    private String qyid;
    private Intent intent;
    private List<QyYhInformationBean> mDatas;
    private List<QyYhInformationBean> searchDatas;

    private EditText qyyh_information_search;
    private ImageView qyyh_information_clear;
    private QyYhInformationAdapter adapter;
    private Dialog dialog;
    private BGARefreshLayout qyyh_information_refresh;
    private BGARefreshLayout qyyh_information_nodatarefresh;
    private QyYhInformationBean bean;
    private String mycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_yh_infomation);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        qyyh_information_back = (ImageView) findViewById(R.id.qyyh_information_back);
        qyyh_information_listview = (ListView) findViewById(R.id.qyyh_information_listview);
        qyyh_information_search = (EditText) findViewById(R.id.qyyh_information_search);
        qyyh_information_clear = (ImageView) findViewById(R.id.qyyh_information_clear);
        qyyh_information_refresh = (BGARefreshLayout) findViewById(R.id.qyyh_information_refresh);
        qyyh_information_nodatarefresh = (BGARefreshLayout) findViewById(R.id.qyyh_information_nodatarefresh);
        MyRefreshStyle(qyyh_information_refresh);
        MyRefreshStyle(qyyh_information_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);

        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            url = PortIpAddress.GetRiskinfoQy();
            qyid = intent.getStringExtra("mClickId");
        } else {
            url = PortIpAddress.GetRiskinfo();
            qyid = "";
        }
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<QyYhInformationBean>();
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
                        ShowToast.showShort(QyYhInfomation.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                qyyh_information_refresh.setVisibility(View.VISIBLE);
                                qyyh_information_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new QyYhInformationBean();
                                    bean.setCrname(jsonArray.optJSONObject(i).getString("crname"));
                                    bean.setYhid(jsonArray.optJSONObject(i).getString("yhid"));
                                    bean.setCrtypename(jsonArray.optJSONObject(i).getString("crtypename"));
                                    bean.setPcdate(jsonArray.optJSONObject(i).getString("pcdate"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("deptname"));
                                    }
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new QyYhInformationAdapter(QyYhInfomation.this, mDatas, mycode);
                                    qyyh_information_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                qyyh_information_refresh.setVisibility(View.GONE);
                                qyyh_information_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        qyyh_information_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (QyYhInformationBean) parent.getItemAtPosition(position);
                intent = new Intent(QyYhInfomation.this, QyYhInfomationDetail.class);
                intent.putExtra("yhid", bean.getYhid());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }


    private void MonitorEtv() {
        qyyh_information_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (qyyh_information_search.length() > 0) {
                        qyyh_information_clear.setVisibility(View.VISIBLE);
                        search(qyyh_information_search.getText().toString().trim());
                    } else {
                        qyyh_information_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (qyyh_information_search.length() > 0) {
                        qyyh_information_clear.setVisibility(View.VISIBLE);
                    } else {
                        qyyh_information_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<QyYhInformationBean>();
        for (QyYhInformationBean entity : mDatas) {
            try {
                if (entity.getCrname().contains(str) || entity.getCrtypename().contains(str) || entity.getPcdate().contains(str)) {
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
        qyyh_information_back.setOnClickListener(this);
        qyyh_information_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qyyh_information_back:
                finish();
                break;
            case R.id.qyyh_information_clear:
                qyyh_information_search.setText("");
                qyyh_information_clear.setVisibility(View.GONE);
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
                    qyyh_information_refresh.endRefreshing();
                    qyyh_information_nodatarefresh.endRefreshing();
                    ShowToast.showShort(QyYhInfomation.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            qyyh_information_refresh.endRefreshing();
            qyyh_information_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
