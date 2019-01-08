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

import accident.AccidentPresentationDetail;
import accident.AddAccidentPresentation;
import adapter.AccidentPresentationAdapter;
import bean.AccidentBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AccidentPresentation extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView accident_presentation_back;
    private ImageView accident_presentation_add;
    private ListView accident_presentation_listview;
    private List<AccidentBean> mDatas;
    private List<AccidentBean> searchDatas;
    private AccidentPresentationAdapter adapter;
    private EditText accident_presentation_edittext;
    private ImageView accident_presentation_clear;
    private String url;
    private String user_token;
    private String userid;
    private Intent intent;
    private static final int A = 10;
    private BGARefreshLayout accident_presentation_refresh;
    private BGARefreshLayout accident_presentation_nodatarefresh;
    private AccidentBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_presentation);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        accident_presentation_back = (ImageView) findViewById(R.id.accident_presentation_back);
        accident_presentation_add = (ImageView) findViewById(R.id.accident_presentation_add);
        accident_presentation_listview = (ListView) findViewById(R.id.accident_presentation_listview);
        accident_presentation_edittext = (EditText) findViewById(R.id.accident_presentation_edittext);
        accident_presentation_clear = (ImageView) findViewById(R.id.accident_presentation_clear);
        accident_presentation_refresh = (BGARefreshLayout) findViewById(R.id.accident_presentation_refresh);
        accident_presentation_nodatarefresh = (BGARefreshLayout) findViewById(R.id.accident_presentation_nodatarefresh);
        MyRefreshStyle(accident_presentation_refresh);
        MyRefreshStyle(accident_presentation_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.Accidentinfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<AccidentBean>();
        mOkhttp();
        MonitorList();
        EditextMonitor();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("userid", userid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(AccidentPresentation.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsb = new JSONObject(response);
                            Log.e(TAG, jsb + "");
                            JSONArray jsar = jsb.getJSONArray("cells");
                            mDatas.clear();
                            if (jsar.length() > 0) {
                                accident_presentation_refresh.setVisibility(View.VISIBLE);
                                accident_presentation_nodatarefresh.setVisibility(View.GONE);
                                for (int i = 0; i < jsar.length(); i++) {
                                    bean = new AccidentBean();
                                    String aititle = jsar.optJSONObject(i).getString("aititle");
                                    String sbmanname = jsar.optJSONObject(i).getString("sbmanname");
                                    String createdate = jsar.optJSONObject(i).getString("createdate");
                                    String procid = jsar.optJSONObject(i).getString("procid");
                                    bean.setAccidentName(aititle);
                                    bean.setPublisher(sbmanname);
                                    bean.setReleaseTime(createdate);
                                    bean.setProcid(procid);
                                    mDatas.add(bean);
                                }
                                if (adapter == null) {
                                    adapter = new AccidentPresentationAdapter(AccidentPresentation.this, mDatas);
                                    accident_presentation_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }
                            } else {
                                accident_presentation_refresh.setVisibility(View.GONE);
                                accident_presentation_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void MonitorList() {
        //listview子项点击事件
        accident_presentation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (AccidentBean) parent.getItemAtPosition(position);
                intent = new Intent(AccidentPresentation.this, AccidentPresentationDetail.class);
                intent.putExtra("procid", bean.getProcid());
                startActivity(intent);
            }
        });
    }


    //监听edittext
    private void EditextMonitor() {
        accident_presentation_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (accident_presentation_edittext.length() > 0) {
                        accident_presentation_clear.setVisibility(View.VISIBLE);
                        search(accident_presentation_edittext.getText().toString().trim());
                    } else {
                        accident_presentation_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (accident_presentation_edittext.length() > 0) {
                        accident_presentation_clear.setVisibility(View.VISIBLE);
                    }else{
                        accident_presentation_clear.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //搜索内容
    private void search(String str) {
        if (mDatas != null) {
            searchDatas = new ArrayList<AccidentBean>();
            for (AccidentBean entity : mDatas) {
                try {
                    if (entity.getAccidentName().contains(str) || entity.getPublisher().contains(str) || entity.getReleaseTime().contains(str)) {
                        searchDatas.add(entity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            adapter.DataNotify(searchDatas);
        }
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
        accident_presentation_back.setOnClickListener(this);
        accident_presentation_add.setOnClickListener(this);
        accident_presentation_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accident_presentation_back:
                finish();
                break;
            case R.id.accident_presentation_add:
                Intent intent = new Intent(this, AddAccidentPresentation.class);
                startActivityForResult(intent, A);
                break;
            case R.id.accident_presentation_clear:
                accident_presentation_edittext.setText("");
                accident_presentation_clear.setVisibility(View.GONE);
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
                    accident_presentation_refresh.endRefreshing();
                    accident_presentation_nodatarefresh.endRefreshing();
                    ShowToast.showShort(AccidentPresentation.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);
        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            accident_presentation_refresh.endRefreshing();
            accident_presentation_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
