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

import adapter.RulesAdapter;
import bean.RulesBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class Rules extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView rules_back;
    private ListView rules_list;
    private String url;
    private String user_token;
    private String qyid;
    private Intent intent;
    private List<RulesBean> mDatas;
    private List<RulesBean> searchDatas;
    private EditText rules_search;
    private ImageView rules_clear;
    private RulesAdapter adapter;
    private Dialog dialog;
    private BGARefreshLayout rules_refresh;
    private BGARefreshLayout rules_nodatarefresh;
    private RulesBean bean;
    private String mycode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        rules_back = (ImageView) findViewById(R.id.rules_back);
        rules_list = (ListView) findViewById(R.id.rules_list);
        rules_search = (EditText) findViewById(R.id.rules_search);
        rules_clear = (ImageView) findViewById(R.id.rules_clear);
        rules_refresh = (BGARefreshLayout) findViewById(R.id.rules_refresh);
        rules_nodatarefresh = (BGARefreshLayout) findViewById(R.id.rules_nodatarefresh);
        MyRefreshStyle(rules_refresh);
        MyRefreshStyle(rules_nodatarefresh);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);

        intent = getIntent();
        mycode = intent.getStringExtra("mycode");
        url = PortIpAddress.GetCompanybylawQy();
        qyid = intent.getStringExtra("mClickId");
        mDatas = new ArrayList<RulesBean>();
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
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
                        ShowToast.showToastNoWait(Rules.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "----");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                rules_refresh.setVisibility(View.VISIBLE);
                                rules_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new RulesBean();
                                    bean.setRulesName(jsonArray.optJSONObject(i).getString("cbname"));
                                    bean.setRulesEffTime(jsonArray.optJSONObject(i).getString("efftime"));
                                    bean.setRulesID(jsonArray.optJSONObject(i).getString("gzzdid"));
                                    bean.setRulesType(jsonArray.optJSONObject(i).getString("cbtypename"));
//                                    if (mycode.equals("home")) {
//                                        bean.setQyname(jsonArray.optJSONObject(i).getString("qyname"));
//                                    }
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new RulesAdapter(Rules.this, mDatas, mycode);
                                    rules_list.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                rules_refresh.setVisibility(View.GONE);
                                rules_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        rules_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (RulesBean) parent.getItemAtPosition(position);
                intent = new Intent(Rules.this, RulesDetail.class);
                intent.putExtra("gzzdid", bean.getRulesID());
                intent.putExtra("mcode", mycode);
                startActivity(intent);
            }
        });
    }


    private void MonitorEtv() {

        rules_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (rules_search.length() > 0) {
                        rules_clear.setVisibility(View.VISIBLE);
                        search(rules_search.getText().toString().trim());
                    } else {
                        rules_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (rules_search.length() > 0) {
                        rules_clear.setVisibility(View.VISIBLE);
                    } else {
                        rules_clear.setVisibility(View.GONE);
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
        searchDatas = new ArrayList<RulesBean>();
        for (RulesBean entity : mDatas) {
            try {
                if (entity.getRulesName().contains(str) || entity.getRulesEffTime().contains(str) || entity.getRulesType().contains(str) || entity.getQyname().contains(str)) {
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
        rules_back.setOnClickListener(this);
        rules_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rules_back:
                finish();
                break;
            case R.id.rules_clear:
                rules_search.setText("");
                rules_clear.setVisibility(View.GONE);
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
                    rules_refresh.endRefreshing();
                    rules_nodatarefresh.endRefreshing();
                    ShowToast.showShort(Rules.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            rules_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
