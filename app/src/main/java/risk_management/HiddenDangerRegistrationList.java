package risk_management;

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

import adapter.RiskListAdapter;
import bean.QyYhInformationBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import enterprise_information.QyYhInfomationDetail;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class HiddenDangerRegistrationList extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView hidden_risk_list_back;
    private ImageView hidden_risk_list_add;
    private ListView hidden_risk_list_listview;
    private String url;
    private String user_token;
    private String clickId;
    private Intent intent;
    private List<QyYhInformationBean> mDatas;
    private List<QyYhInformationBean> searchDatas;
    private RiskListAdapter adapter;
    private EditText hidden_risk_list_search;
    private ImageView hidden_risk_list_clear;
    private BGARefreshLayout hidden_risk_list_refresh;
    private BGARefreshLayout hidden_risk_list_nodatarefresh;
    private QyYhInformationBean bean;
    private String crtype = "";
    private String addTag = "";
    private int ADD_REQUESTCODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_registration_list);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_risk_list_back = (ImageView) findViewById(R.id.hidden_risk_list_back);
        hidden_risk_list_add = (ImageView) findViewById(R.id.hidden_risk_list_add);
        hidden_risk_list_listview = (ListView) findViewById(R.id.hidden_risk_list_listview);
        hidden_risk_list_search = (EditText) findViewById(R.id.hidden_risk_list_search);
        hidden_risk_list_clear = (ImageView) findViewById(R.id.hidden_risk_list_clear);
        hidden_risk_list_refresh = (BGARefreshLayout) findViewById(R.id.hidden_risk_list_refresh);
        hidden_risk_list_nodatarefresh = (BGARefreshLayout) findViewById(R.id.hidden_risk_list_nodatarefresh);
        MyRefreshStyle(hidden_risk_list_refresh);
        MyRefreshStyle(hidden_risk_list_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.GetRiskinfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        intent = getIntent();
        clickId = intent.getStringExtra("clickId");
        crtype = intent.getStringExtra("crtype");
        addTag = intent.getStringExtra("addTag");
        if (addTag.equals("gl")) {
            hidden_risk_list_add.setVisibility(View.VISIBLE);
        } else {
            hidden_risk_list_add.setVisibility(View.GONE);
        }
        mDatas = new ArrayList<QyYhInformationBean>();
        mOkhttp();
        MonitorList();
        MonitorEditext();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("crtype", crtype)
                .addParams("qyid", clickId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(HiddenDangerRegistrationList.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                hidden_risk_list_refresh.setVisibility(View.VISIBLE);
                                hidden_risk_list_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    bean = new QyYhInformationBean();
                                    bean.setCrname(jsonArray.optJSONObject(i).getString("crname"));
                                    bean.setYhid(jsonArray.optJSONObject(i).getString("yhid"));
                                    bean.setCrtypename(jsonArray.optJSONObject(i).getString("crtypename"));
                                    bean.setPcdate(jsonArray.optJSONObject(i).getString("pcdate"));
                                    if (jsonArray.optJSONObject(i).getString("crtypename").equals("重大隐患")) {
                                        mDatas.add(bean);
                                    }
                                }
                                if (adapter == null) {
                                    adapter = new RiskListAdapter(HiddenDangerRegistrationList.this, mDatas);
                                    hidden_risk_list_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                hidden_risk_list_refresh.setVisibility(View.GONE);
                                hidden_risk_list_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        hidden_risk_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (QyYhInformationBean) parent.getItemAtPosition(position);
                intent = new Intent(HiddenDangerRegistrationList.this, QyYhInfomationDetail.class);
                intent.putExtra("yhid", bean.getYhid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        hidden_risk_list_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (hidden_risk_list_search.length() > 0) {
                        hidden_risk_list_clear.setVisibility(View.VISIBLE);
                        search(hidden_risk_list_search.getText().toString().trim());
                    } else {
                        hidden_risk_list_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (hidden_risk_list_search.length() > 0) {
                        hidden_risk_list_clear.setVisibility(View.VISIBLE);
                    } else {
                        hidden_risk_list_clear.setVisibility(View.GONE);
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
        if (mDatas != null) {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_REQUESTCODE){
                mOkhttp();
            }
        }
    }

    @Override
    protected void setOnClick() {
        hidden_risk_list_back.setOnClickListener(this);
        hidden_risk_list_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_risk_list_back:
                finish();
                break;
            case R.id.hidden_risk_list_add:
                Intent intent = new Intent(this, AddHiddenDangerRegistration.class);
                intent.putExtra("clickId", clickId);
                startActivityForResult(intent,ADD_REQUESTCODE);
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
                    hidden_risk_list_refresh.endRefreshing();
                    hidden_risk_list_nodatarefresh.endRefreshing();
                    ShowToast.showShort(HiddenDangerRegistrationList.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            hidden_risk_list_refresh.endRefreshing();
            hidden_risk_list_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

}
