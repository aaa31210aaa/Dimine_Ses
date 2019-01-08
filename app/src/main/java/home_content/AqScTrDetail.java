package home_content;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.FileListAdapter;
import bean.FileBean;
import bean.MessageEvent;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;

public class AqScTrDetail extends BaseActivity {
    private ImageView aqsctr_detail_back;
    //生产月份
    private TextView aqsctr_detail_monthvalue;
    //费用类型
    private TextView aqsctr_detail_investypename;
    //项目名称
    private TextView aqsctr_detail_investname;
    //费用金额
    private TextView aqsctr_detail_usemoney;
    //企业名称
    private TextView aqsctr_detail_qyname;
    //使用部位
    private TextView aqsctr_detail_usesite;
    //数量
    private TextView aqsctr_detail_usecount;
    //单位
    private TextView aqsctr_detail_useuint;
    //总额
    private TextView aqsctr_detail_summoney;
    //附件名称
//    private TextView aqsctr_detail_filename;
    //备注
    private TextView aqsctr_detail_memo;

    private ListView aqsctr_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    private String siid;
    private String url;
    private String detailid;
    private String mycode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_sc_tr_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        aqsctr_detail_back = (ImageView) findViewById(R.id.aqsctr_detail_back);
        aqsctr_detail_monthvalue = (TextView) findViewById(R.id.aqsctr_detail_monthvalue);
        aqsctr_detail_investypename = (TextView) findViewById(R.id.aqsctr_detail_investypename);
        aqsctr_detail_investname = (TextView) findViewById(R.id.aqsctr_detail_investname);
        aqsctr_detail_usemoney = (TextView) findViewById(R.id.aqsctr_detail_usemoney);
        aqsctr_detail_qyname = (TextView) findViewById(R.id.aqsctr_detail_qyname);
        aqsctr_detail_usesite = (TextView) findViewById(R.id.aqsctr_detail_usesite);
        aqsctr_detail_usecount = (TextView) findViewById(R.id.aqsctr_detail_usecount);
        aqsctr_detail_useuint = (TextView) findViewById(R.id.aqsctr_detail_useuint);
        aqsctr_detail_summoney = (TextView) findViewById(R.id.aqsctr_detail_summoney);
//        aqsctr_detail_filename = (TextView) findViewById(R.id.aqsctr_detail_filename);
        aqsctr_detail_memo = (TextView) findViewById(R.id.aqsctr_detail_memo);
        aqsctr_detail_list = (ListView) findViewById(R.id.aqsctr_detail_list);

        //检查权限
        if (ContextCompat.checkSelfPermission(context, SDUtils.WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, SDUtils.CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
            FileBean.FileWritePermission(this);
            FileBean.FileCdFilePermission(this);
        }
        //注册
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<FileBean>();
        filenames = new ArrayList<String>();
        Intent intent = getIntent();
        siid = intent.getStringExtra("siid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetSafeinvestmentDetailQy();
        } else {
            detailid = "siid";
            url = PortIpAddress.GetSafeinvestmentDetail();
        }
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams(detailid, siid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AqScTrDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            aqsctr_detail_monthvalue.setText(jsonArray.getJSONObject(0).getString("monthvalue"));
                            aqsctr_detail_investypename.setText(jsonArray.getJSONObject(0).getString("investypename"));
                            aqsctr_detail_investname.setText(jsonArray.getJSONObject(0).getString("investname"));
                            aqsctr_detail_usemoney.setText(jsonArray.getJSONObject(0).getString("usemoney"));
                            aqsctr_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            aqsctr_detail_usesite.setText(jsonArray.getJSONObject(0).getString("usesite"));
                            aqsctr_detail_usecount.setText(jsonArray.getJSONObject(0).getString("usecount"));
                            aqsctr_detail_useuint.setText(jsonArray.getJSONObject(0).getString("useuint"));
//                            aqsctr_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            aqsctr_detail_summoney.setText(jsonArray.getJSONObject(0).getString("summoney"));
                            aqsctr_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

                            for (int i = 0; i < files.length(); i++) {
                                if (!files.getJSONObject(i).getString("filename").equals("")) {
                                    FileBean bean = new FileBean();
                                    bean.setFilename(files.getJSONObject(i).getString("filename"));
                                    String path = PortIpAddress.myUrl + files.getJSONObject(i).getString("fileurl");
                                    path = path.replaceAll("\\\\", "/");
                                    bean.setFileUrl(path);
                                    bean.setAttachmentid(files.getJSONObject(i).getString("attachmentid"));

                                    /**判断文件是否存在**/
                                    if (SDUtils.isExistence(SDUtils.sdPath + "/" + files.getJSONObject(i).getString("filename"))) {
                                        Log.e(TAG, files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("打开");
                                        bean.setDeleteClick(true);
                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
                                    } else {
                                        Log.e(TAG, SDUtils.sdPath + files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("下载");
                                        bean.setDeleteClick(false);
                                        bean.setStatus(FileBean.STATE_NONE);
                                    }
                                    filenames.add(files.getJSONObject(i).getString("filename"));
                                    mDatas.add(bean);
                                }
                            }
                            if (adapter == null) {
                                adapter = new FileListAdapter(AqScTrDetail.this, mDatas, filenames);
                                aqsctr_detail_list.setAdapter(adapter);
                            } else {
                                adapter.DataNotify(mDatas);
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Subscribe()
    public void AqScTrDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(AqScTrDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(AqScTrDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(AqScTrDetail.this, "下载失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void setOnClick() {
        aqsctr_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aqsctr_detail_back:
                finish();
                break;
        }
    }
}
