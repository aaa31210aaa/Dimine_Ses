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

import adapter.MyNotificationListViewAdapter;
import bean.MyNotificationBean;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import mynotification_detail.MyNotificationDetail;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class MyNotification extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private ImageView mynotification_back;
    private ListView mynotification_listview;
    private EditText mynotification_search_edittext;
    private ImageView mynotification_search_clear;
    private List<MyNotificationBean> mDatas;
    private List<MyNotificationBean> searchDatas;
    private String url;
    private String user_token;
    private BGARefreshLayout notification_refresh;
    private BGARefreshLayout notification_nodatarefresh;
    private MyNotificationListViewAdapter adapter;
    private MyNotificationBean myNotificationEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        mynotification_listview = (ListView) findViewById(R.id.mynotification_listview);
        mynotification_back = (ImageView) findViewById(R.id.mynotification_back);
        mynotification_search_edittext = (EditText) findViewById(R.id.mynotification_search_edittext);
        mynotification_search_clear = (ImageView) findViewById(R.id.mynotification_search_clear);
        notification_refresh = (BGARefreshLayout) findViewById(R.id.notification_refresh);
        notification_nodatarefresh = (BGARefreshLayout) findViewById(R.id.notification_nodatarefresh);
        MyRefreshStyle(notification_refresh);
        MyRefreshStyle(notification_nodatarefresh);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.MessageList();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mDatas = new ArrayList<MyNotificationBean>();
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
                        ShowToast.showToastNoWait(MyNotification.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsb = new JSONObject(response);
                            JSONArray jsonArray = jsb.getJSONArray("cells");
                            Log.e(TAG, jsb + "");
                            if (jsonArray.length() > 0) {
                                notification_refresh.setVisibility(View.VISIBLE);
                                notification_nodatarefresh.setVisibility(View.GONE);
                                mDatas.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    myNotificationEntity = new MyNotificationBean();
                                    try {
                                        myNotificationEntity.setTitle(jsonArray.optJSONObject(i).getString("messagetitle"));
                                        myNotificationEntity.setMessageId(jsonArray.optJSONObject(i).getString("messageid"));
                                        myNotificationEntity.setTime(jsonArray.optJSONObject(i).getString("mestime"));
                                        mDatas.add(myNotificationEntity);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (adapter == null) {
                                    adapter = new MyNotificationListViewAdapter(MyNotification.this, mDatas);
                                    mynotification_listview.setAdapter(adapter);
                                } else {
                                    adapter.DataNotify(mDatas);
                                }

                            } else {
                                notification_refresh.setVisibility(View.GONE);
                                notification_nodatarefresh.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void MonitorList() {
        mynotification_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myNotificationEntity = (MyNotificationBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyNotification.this, MyNotificationDetail.class);
                intent.putExtra("messageid", myNotificationEntity.getMessageId());
                startActivity(intent);
            }
        });
    }

    private void MonitorEditext() {
        //监听edittext
        mynotification_search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDatas != null) {
                    if (mynotification_search_edittext.length() > 0) {
                        mynotification_search_clear.setVisibility(View.VISIBLE);
                        search(mynotification_search_edittext.getText().toString().trim());
                    } else {
                        mynotification_search_clear.setVisibility(View.GONE);
                        adapter.DataNotify(mDatas);
                    }
                } else {
                    if (mynotification_search_edittext.length() > 0) {
                        mynotification_search_clear.setVisibility(View.VISIBLE);
                    }else{
                        mynotification_search_clear.setVisibility(View.GONE);
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
            searchDatas = new ArrayList<MyNotificationBean>();
            for (MyNotificationBean entity : mDatas) {
                try {
                    if (entity.getTitle().contains(str) || entity.getTime().contains(str) || entity.getContent().contains(str)) {
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
        mynotification_back.setOnClickListener(this);
        mynotification_search_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mynotification_back:
                finish();
                break;
            case R.id.mynotification_search_clear:
                mynotification_search_edittext.setText("");
                mynotification_search_clear.setVisibility(View.GONE);
                break;
            default:
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
                    notification_refresh.endRefreshing();
                    notification_nodatarefresh.endRefreshing();
                    ShowToast.showShort(MyNotification.this, R.string.refreshed);
                }
            }, LOADING_REFRESH);
        } else {
            ShowToast.showShort(this, R.string.netcantuse);
            notification_refresh.endRefreshing();
            notification_nodatarefresh.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
