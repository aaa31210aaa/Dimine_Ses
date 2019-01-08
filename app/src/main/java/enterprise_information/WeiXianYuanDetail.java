package enterprise_information;

import android.app.Dialog;
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
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class WeiXianYuanDetail extends BaseActivity {
    private ImageView wxy_detail_back;
    //主要风险点
    private TextView wxy_detail_mainrisk;
    //主要危险
    private TextView wxy_detail_maindanger;
    //风险类型
    private TextView wxy_detail_risktypename;
    //是否重大危险源
    private TextView wxy_detail_iszdwxy;
    //附件名称
//    private TextView wxy_detail_filename;
    //创建时间
    private TextView wxy_detail_createdate;


    //备注
    private TextView wxy_detail_memo;

    private Intent intent;
    private String url;
    private String user_token;
    private String fxdid;
    private Dialog dialog;
    private String detailid;

    private ListView wxy_detai_filelist;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


//    private ScrollView scrollView;

//    private String[] keys = {"mainrisk", "maindanger", "risktypename", "iszdwxy", "filename", "memo"};
//    private String[] values = {"主要风险点", "主要危险", "风险类型", "是否重大危险源", "附件名称", "备注"};
//    private LinearLayout outLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xian_yuan_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        wxy_detail_back = (ImageView) findViewById(R.id.wxy_detail_back);
        wxy_detail_mainrisk = (TextView) findViewById(R.id.wxy_detail_mainrisk);
        wxy_detail_maindanger = (TextView) findViewById(R.id.wxy_detail_maindanger);
        wxy_detail_risktypename = (TextView) findViewById(R.id.wxy_detail_risktypename);
        wxy_detail_iszdwxy = (TextView) findViewById(R.id.wxy_detail_iszdwxy);
        wxy_detail_createdate = (TextView) findViewById(R.id.wxy_detail_createdate);
//        wxy_detail_filename = (TextView) findViewById(R.id.wxy_detail_filename);
        wxy_detail_memo = (TextView) findViewById(R.id.wxy_detail_memo);
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//        outLl = (LinearLayout) findViewById(R.id.outLl);
        wxy_detai_filelist = (ListView) findViewById(R.id.wxy_detai_filelist);

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
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        fxdid = intent.getStringExtra("fxdid");
        url = PortIpAddress.GetCompanyRiskdetailQy();
        detailid = "detailid";
//        else {
//            url = PortIpAddress.GetCompanyRiskdetail();
//            detailid = "fxdid";
//        }
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams(detailid, fxdid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(WeiXianYuanDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            wxy_detail_mainrisk.setText(jsonArray.getJSONObject(0).getString("mainrisk"));
                            wxy_detail_maindanger.setText(jsonArray.getJSONObject(0).getString("maindanger"));
                            wxy_detail_risktypename.setText(jsonArray.getJSONObject(0).getString("risktypename"));
                            wxy_detail_iszdwxy.setText(jsonArray.getJSONObject(0).getString("iszdwxy"));
                            wxy_detail_createdate.setText(jsonArray.getJSONObject(0).getString("createdate"));
//                          wxy_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            wxy_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
//                                    bean.setDownopen(R.drawable.openfile);
                                        Log.e(TAG, files.getJSONObject(i).getString("filename"));
                                        bean.setDownloadType("打开");
                                        bean.setDeleteClick(true);
                                        bean.setStatus(FileBean.STATE_DOWNLOADED);
                                    } else {
//                                    bean.setDownopen(R.drawable.downloadfile);
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
                                adapter = new FileListAdapter(WeiXianYuanDetail.this, mDatas, filenames);
                                wxy_detai_filelist.setAdapter(adapter);
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
    public void WxyDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(WeiXianYuanDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(WeiXianYuanDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(WeiXianYuanDetail.this, "下载失败");
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
        wxy_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wxy_detail_back:
                finish();
                break;
        }
    }
}
