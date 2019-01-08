package hidden_danger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.NetUtils;
import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.HiddenDangerDetailAdapter;
import bean.HiddenDangerDetailBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.GridItemDecoration;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class HiddenDangerDetail extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView hidden_danger_detail_back;
    private TextView hidden_danger_detail_allstandard;
    private RecyclerView hidden_danger_detail_recyclerview;
    private List<HiddenDangerDetailBean> mDatas;
    private boolean[] flag;
    private HiddenDangerDetailAdapter adapter;
    private int[] flag_btn;
    private String url;
    private String user_token;
    private Intent intent;
    private String qyid;
    private String mNamestr;
    private BGARefreshLayout hidden_danger_detail_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_danger_detail_back = (ImageView) findViewById(R.id.hidden_danger_detail_back);
        hidden_danger_detail_allstandard = (TextView) findViewById(R.id.hidden_danger_detail_allstandard);
        hidden_danger_detail_recyclerview = (RecyclerView) findViewById(R.id.hidden_danger_detail_recyclerview);

        //设置布局类型
        hidden_danger_detail_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hidden_danger_detail_recyclerview.setHasFixedSize(true);
        hidden_danger_detail_recyclerview.addItemDecoration(new GridItemDecoration(this));
        hidden_danger_detail_refresh = (BGARefreshLayout) findViewById(R.id.hidden_danger_detail_refresh);
        MyRefreshStyle(hidden_danger_detail_refresh);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<HiddenDangerDetailBean>();
        url = PortIpAddress.Riskzcbzdetail();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        qyid = intent.getStringExtra("clickId");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
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
                        ShowToast.showToastNoWait(HiddenDangerDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            if (jsonArray.length() > 0) {
                                flag = new boolean[jsonArray.length()];
                                flag_btn = new int[jsonArray.length()];
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HiddenDangerDetailBean bean = new HiddenDangerDetailBean();
                                    bean.setIndex(String.valueOf(i + 1));
                                    if (jsonArray.optJSONObject(i).getString("amount").equals("0")) {
                                        bean.setmVisible(false);
                                        flag[i] = true;
                                    } else {
                                        bean.setmVisible(true);
                                        flag[i] = false;
                                    }

                                    bean.setZcbzdid(jsonArray.optJSONObject(i).getString("zcbzdid"));
                                    bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                    bean.setPczq(jsonArray.optJSONObject(i).getString("pczq"));
                                    bean.setPcdeptname(jsonArray.optJSONObject(i).getString("pcdeptname"));
                                    bean.setAmount(jsonArray.optJSONObject(i).getString("amount"));
                                    mNamestr = jsonArray.optJSONObject(i).getString("namestr").replace("\\", "");
                                    bean.setNamestr(mNamestr);
                                    bean.setRscdesc(jsonArray.optJSONObject(i).getString("rscdesc"));
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new HiddenDangerDetailAdapter(HiddenDangerDetail.this, mDatas, flag);
                                    hidden_danger_detail_recyclerview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas, flag);
                                }
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mOkhttp();
        }
    }

    @Override
    protected void setOnClick() {
        hidden_danger_detail_back.setOnClickListener(this);
        hidden_danger_detail_allstandard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_danger_detail_back:
                finish();
                break;
            case R.id.hidden_danger_detail_allstandard:
                //!!!调用接口!!!


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
                    hidden_danger_detail_refresh.endRefreshing();
                    ShowToast.showShort(HiddenDangerDetail.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);

        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            hidden_danger_detail_refresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
