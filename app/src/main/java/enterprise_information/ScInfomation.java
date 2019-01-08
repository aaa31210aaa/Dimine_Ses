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

import adapter.ScInfomationAdapter;
import bean.ScInfomationBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class ScInfomation extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView sc_infomation_back;
    private EditText sc_infomation_search;
    private ImageView sc_infomation_clear;
    private ListView sc_infomation_list;
    private String url;
    private String user_token;
    private String qyid;
    private Intent intent;
    private List<ScInfomationBean> mDatas;
    private List<ScInfomationBean> searchDatas;
    private ScInfomationAdapter adapter;
    private Dialog dialog;
    private ScInfomationBean bean;
    private BGARefreshLayout sc_infomation_refresh;
    private BGARefreshLayout sc_infomation_nodatarefresh;
    private String mycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc_infomation);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        sc_infomation_back = (ImageView) findViewById(R.id.sc_infomation_back);
        sc_infomation_search = (EditText) findViewById(R.id.sc_infomation_search);
        sc_infomation_clear = (ImageView) findViewById(R.id.sc_infomation_clear);
        sc_infomation_list = (ListView) findViewById(R.id.sc_infomation_list);
        sc_infomation_refresh = (BGARefreshLayout) findViewById(R.id.sc_infomation_refresh);
        sc_infomation_nodatarefresh = (BGARefreshLayout) findViewById(R.id.sc_infomation_nodatarefresh);
        MyRefreshStyle(sc_infomation_refresh);
        MyRefreshStyle(sc_infomation_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.GetSafechecklist();
        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        if (mycode.equals("emap")) {
            qyid = intent.getStringExtra("mClickId");
        } else {
            qyid = "";
        }
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<ScInfomationBean>();
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
                        ShowToast.showToastNoWait(ScInfomation.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                sc_infomation_refresh.setVisibility(View.VISIBLE);
                                sc_infomation_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new ScInfomationBean();
                                    bean.setCheckname(jsonArray.optJSONObject(i).getString("checkname"));
                                    bean.setProblems(jsonArray.optJSONObject(i).getString("problems"));
                                    bean.setIscommit(jsonArray.optJSONObject(i).getString("iscommit"));
//                                    bean.setScdate(jsonArray.optJSONObject(i).getString("scdate"));
                                    bean.setScid(jsonArray.optJSONObject(i).getString("scid"));
                                    if (mycode.equals("home")) {
                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
                                    }
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new ScInfomationAdapter(ScInfomation.this, mDatas, mycode);
                                    sc_infomation_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                sc_infomation_refresh.setVisibility(View.GONE);
                                sc_infomation_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        sc_infomation_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (ScInfomationBean) parent.getItemAtPosition(position);
                intent = new Intent(ScInfomation.this, ScInfomationDetail.class);
                intent.putExtra("scid", bean.getScid());
                startActivity(intent);
            }
        });
    }


    private void MonitorEtv() {
        sc_infomation_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (sc_infomation_search.length() > 0) {
                        sc_infomation_clear.setVisibility(View.VISIBLE);
                        search(sc_infomation_search.getText().toString().trim());
                    } else {
                        sc_infomation_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (sc_infomation_search.length() > 0) {
                        sc_infomation_clear.setVisibility(View.VISIBLE);
                    } else {
                        sc_infomation_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<ScInfomationBean>();
        for (ScInfomationBean entity : mDatas) {
            try {
                if (entity.getCheckname().contains(str) || entity.getProblems().contains(str) || entity.getIscommit().contains(str)) {
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
        sc_infomation_back.setOnClickListener(this);
        sc_infomation_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sc_infomation_back:
                finish();
                break;
            case R.id.sc_infomation_clear:
                sc_infomation_search.setText("");
                sc_infomation_clear.setVisibility(View.GONE);
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
                    sc_infomation_refresh.endRefreshing();
                    sc_infomation_nodatarefresh.endRefreshing();
                    ShowToast.showShort(ScInfomation.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            sc_infomation_refresh.endRefreshing();
            sc_infomation_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
