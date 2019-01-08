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

import adapter.NodataAdapter;
import adapter.ReViewAdapter;
import bean.HiddenReviewBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class HiddenDangerReview extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView hidden_danger_review_back;
    private ListView hidden_danger_review_listview;
    private List<HiddenReviewBean> mDatas;
    private List<HiddenReviewBean> searchDatas;
    private HiddenReviewBean entity;
    private String url;
    private String user_token;
    private ReViewAdapter adapter;
    private Intent intent;
    private EditText danger_review_search;
    private ImageView danger_review_clear;
    private BGARefreshLayout hidden_danger_review_refresh;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_review);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_danger_review_back = (ImageView) findViewById(R.id.hidden_danger_review_back);
        hidden_danger_review_listview = (ListView) findViewById(R.id.hidden_danger_review_listview);
        danger_review_search = (EditText) findViewById(R.id.danger_review_search);
        danger_review_clear = (ImageView) findViewById(R.id.danger_review_clear);
        hidden_danger_review_refresh = (BGARefreshLayout) findViewById(R.id.hidden_danger_review_refresh);
        MyRefreshStyle(hidden_danger_review_refresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.RiskList();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
        MonitorList();
        MonitorEditext();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(HiddenDangerReview.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            mDatas = new ArrayList<HiddenReviewBean>();
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HiddenReviewBean bean = new HiddenReviewBean();
                                    bean.setYhid(jsonArray.optJSONObject(i).getString("yhid"));
                                    bean.setHiddenNumber(jsonArray.optJSONObject(i).getString("crcode"));
                                    bean.setRectificationPersonLiable(jsonArray.optJSONObject(i).getString("zgman"));
                                    bean.setHiddenName(jsonArray.optJSONObject(i).getString("crname"));
                                    mDatas.add(bean);

                                }
                                if (first) {
                                    adapter = new ReViewAdapter(HiddenDangerReview.this, mDatas);
                                    hidden_danger_review_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                NodataAdapter adapter = new NodataAdapter(HiddenDangerReview.this);
                                hidden_danger_review_listview.setAdapter(adapter);
                                hidden_danger_review_listview.setEnabled(false);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        //监听
        hidden_danger_review_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(HiddenDangerReview.this, HiddenDangerReviewDetail.class);
                intent.putExtra("yhid", mDatas.get(position).getYhid());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        danger_review_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (danger_review_search.length() > 0) {
                        danger_review_clear.setVisibility(View.VISIBLE);
                        search(danger_review_search.getText().toString().trim());
                    } else {
                        danger_review_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (danger_review_search.length() > 0) {
                        danger_review_clear.setVisibility(View.VISIBLE);
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
            searchDatas = new ArrayList<HiddenReviewBean>();
            for (HiddenReviewBean entity : mDatas) {
                try {
                    if (entity.getHiddenNumber().contains(str) || entity.getHiddenName().contains(str) || entity.getDescribe().contains(str) || entity.getRectificationPersonLiable().contains(str)) {
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
        hidden_danger_review_back.setOnClickListener(this);
        danger_review_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_danger_review_back:
                finish();
                break;
            case R.id.danger_review_clear:
                danger_review_search.setText("");
                danger_review_clear.setVisibility(View.GONE);
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
                    first = false;
                    mOkhttp();
                    hidden_danger_review_refresh.endRefreshing();
                    ShowToast.showShort(HiddenDangerReview.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);
        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            hidden_danger_review_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
