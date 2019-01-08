package enterprise_information;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.NetUtils;
import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.EnterpriseInformationAdapter;
import bean.EnterpriseInformationBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import hidden_danger.HiddenDangerDetail;
import okhttp3.Call;
import risk_management.CommonlyRiskList;
import risk_management.HiddenDangerRegistrationList;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class EnterpriseInformation extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView enterprise_information_back;
    private BGARefreshLayout enterprise_information_refresh;
    private BGARefreshLayout enterprise_informationnodata_refresh;
    //搜索框
    private EditText inspect_search;
    private ImageView inspect_clear;
    private ListView enterprise_information_listview;
    private List<EnterpriseInformationBean> mDatas;
    private List<EnterpriseInformationBean> searchDatas;
    private String url;
    private String user_token;
    private EnterpriseInformationAdapter adapter;
    private Intent intent;
    private String intentIndex;
    private String ymindex = "qylbym";
    private EnterpriseInformationBean bean;
    private String qyid;
    //行业
    private TextView enterprise_information_industry;
    private String industry = "";
    private String[] industry_arr;
    //行业集合
    private Map<String, String> industryMap = new HashMap<>();
    private Dialog myDialog;

    private String tag;
    private String crtype;
    private String addTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_information);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        enterprise_information_back = (ImageView) findViewById(R.id.enterprise_information_back);
        enterprise_information_listview = (ListView) findViewById(R.id.enterprise_information_listview);
        inspect_search = (EditText) findViewById(R.id.inspect_search);
        inspect_clear = (ImageView) findViewById(R.id.inspect_clear);
        enterprise_information_refresh = (BGARefreshLayout) findViewById(R.id.enterprise_information_refresh);
        MyRefreshStyle(enterprise_information_refresh);
        enterprise_informationnodata_refresh = (BGARefreshLayout) findViewById(R.id.enterprise_informationnodata_refresh);
        MyRefreshStyle(enterprise_informationnodata_refresh);
        enterprise_information_industry = (TextView) findViewById(R.id.enterprise_information_industry);
    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        intentIndex = intent.getStringExtra("intentIndex");
        tag = intent.getStringExtra("tag");
        addTag = intent.getStringExtra("addTag");

        myDialog = DialogUtil.createLoadingDialog(EnterpriseInformation.this, R.string.loading);

        //初始化行业
        industry_arr = getResources().getStringArray(R.array.risk_industry);
        enterprise_information_industry.setText(industry_arr[0]);
        if (tag.equals("home")) {
            url = PortIpAddress.Companyinfo();
            crtype = "";
        } else {
            url = PortIpAddress.YhCompanyinfo();
            crtype = intent.getStringExtra("crtype");
        }

        industryMap.put(industry_arr[0], "");
        industryMap.put(industry_arr[1], "HYML021");
        industryMap.put(industry_arr[2], "HYML022");
        industryMap.put(industry_arr[3], "HYML023");
        industryMap.put(industry_arr[4], "HYML024");

        mDatas = new ArrayList<EnterpriseInformationBean>();
        mOkhttp(industryMap.get(industry_arr[0]));

//      initIndustry();
        MonitorList();
        MonitorEditext();
    }

    private void mOkhttp(String industry) {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("industry", industry)
                .addParams("crtype", crtype)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        myDialog.dismiss();
                        ShowToast.showToastNoWait(EnterpriseInformation.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsb = new JSONObject(response);
                            JSONArray jsar = jsb.getJSONArray("cells");
                            if (jsar.length() > 0) {
                                enterprise_information_refresh.setVisibility(View.VISIBLE);
                                enterprise_informationnodata_refresh.setVisibility(View.GONE);
                                mDatas.clear();
                                Log.e(TAG, jsar.length() + "");
                                for (int i = 0; i < jsar.length(); i++) {
                                    bean = new EnterpriseInformationBean();
                                    //企业名称
                                    bean.setCompanyName(jsar.optJSONObject(i).getString("comname"));
                                    //监管部门
//                                    String chargedeptname = jsar.optJSONObject(i).getString("chargedeptname");
                                    //行业
                                    bean.setIndustry(jsar.optJSONObject(i).getString("industryname"));
                                    //企业地址
                                    bean.setEnterpriseAddress(jsar.optJSONObject(i).getString("zcaddress"));
                                    //企业ID
                                    bean.setCompanyId(jsar.optJSONObject(i).getString("qyid"));
                                    //是否有更新
                                    bean.setAddStatus(jsar.optJSONObject(i).getString("addStatus"));
                                    //更新时间
                                    bean.setTablename(jsar.optJSONObject(i).getString("tablename"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new EnterpriseInformationAdapter(EnterpriseInformation.this, mDatas);
                                    enterprise_information_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                enterprise_information_refresh.setVisibility(View.GONE);
                                enterprise_informationnodata_refresh.setVisibility(View.VISIBLE);
                            }
                            myDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        //listview子项点击监听
        enterprise_information_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (EnterpriseInformationBean) parent.getItemAtPosition(position);
                if (intentIndex.equals("home")) {
                    intent = new Intent(EnterpriseInformation.this, EnterpriseMapInformation.class);
                    intent.putExtra("clickId", bean.getCompanyId());
                    intent.putExtra("ymindex", ymindex);
                    startActivity(intent);
                } else if (intentIndex.equals("risk_yb")) {
                    intent = new Intent(EnterpriseInformation.this, CommonlyRiskList.class);
                    intent.putExtra("crtype", crtype);
                    intent.putExtra("clickId", bean.getCompanyId());
                    intent.putExtra("addTag", addTag);
                    startActivity(intent);
                } else if (intentIndex.equals("risk_zd")) {
                    intent = new Intent(EnterpriseInformation.this, HiddenDangerRegistrationList.class);
                    intent.putExtra("crtype", crtype);
                    intent.putExtra("clickId", bean.getCompanyId());
                    intent.putExtra("addTag", addTag);
                    startActivity(intent);
                } else {
                    intent = new Intent(EnterpriseInformation.this, HiddenDangerDetail.class);
                    intent.putExtra("clickId", bean.getCompanyId());
                    startActivity(intent);
                }
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        inspect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (inspect_search.length() > 0) {
                        inspect_clear.setVisibility(View.VISIBLE);
                        search(inspect_search.getText().toString().trim());
                    } else {
                        inspect_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (inspect_search.length() > 0) {
                        inspect_clear.setVisibility(View.VISIBLE);
                    } else {
                        inspect_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<EnterpriseInformationBean>();
            for (EnterpriseInformationBean entity : mDatas) {
                try {
                    if (entity.getCompanyName().contains(str) || entity.getIndustry().contains(str) || entity.getEnterpriseAddress().contains(str)) {
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
        enterprise_information_back.setOnClickListener(this);
        inspect_clear.setOnClickListener(this);
        enterprise_information_industry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterprise_information_back:
                finish();
                break;
            case R.id.inspect_clear:
                inspect_search.setText("");
                inspect_clear.setVisibility(View.GONE);
                break;
            case R.id.enterprise_information_industry:
                showListDialog();
                break;
        }
    }


    private void showListDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industry_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择行业").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog = DialogUtil.createLoadingDialog(EnterpriseInformation.this, R.string.loading);
//                        ShowToast.showShort(EnterpriseInformation.this, industry_arr[which]);
                        enterprise_information_industry.setText(industry_arr[which]);
                        Log.e(TAG, industryMap.get(industry_arr[which]));
                        mOkhttp(industryMap.get(industry_arr[which]));
                    }
                }).create();
        dialog.show();
    }

//    private void initIndustry() {
//        OkHttpUtils
//                .get()
//                .url(PortIpAddress.GetIndustry())
//                .addParams("access_token", user_token)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        try {
//                            Log.e(TAG, response + "");
//                            JSONObject jsb = new JSONObject(response);
//                            JSONArray jsar = jsb.getJSONArray("cells");
//                            industry_arr = new String[jsar.length()];
//                            for (int i = 0; i < jsar.length(); i++) {
//                                industry_arr[i] = jsar.getJSONObject(0).getString("industry");
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        //如果网络可用  加载网络数据 不可用结束刷新
        if (NetUtils.isConnected(this)) {
            sHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOkhttp(industryMap.get(enterprise_information_industry.getText().toString()));
                    enterprise_information_refresh.endRefreshing();
                    enterprise_informationnodata_refresh.endRefreshing();
                    ShowToast.showShort(EnterpriseInformation.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            enterprise_information_refresh.endRefreshing();
            enterprise_informationnodata_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
