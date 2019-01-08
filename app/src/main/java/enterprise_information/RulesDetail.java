package enterprise_information;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
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

public class RulesDetail extends BaseActivity {
    private ImageView rules_detail_back;
    private TextView rules_detail_cbname;
    private TextView rules_detail_efftime;
    private TextView rules_detail_createname;
    private TextView rules_detail_cbtypename;
    private TextView rules_detail_createdate;
    //    private TextView rules_detail_filename;
    private TextView wxy_detail_memo;
    private String gzzdid;
    private String user_token;
    private String url;
    private Intent intent;
    private Dialog dialog;
    private String mycode;
    private String detailid;
    private ListView rules_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        rules_detail_back = (ImageView) findViewById(R.id.rules_detail_back);
        rules_detail_cbname = (TextView) findViewById(R.id.rules_detail_cbname);
        rules_detail_efftime = (TextView) findViewById(R.id.rules_detail_efftime);
        rules_detail_createname = (TextView) findViewById(R.id.rules_detail_createname);
        rules_detail_cbtypename = (TextView) findViewById(R.id.rules_detail_cbtypename);
        rules_detail_createdate = (TextView) findViewById(R.id.rules_detail_createdate);
//        rules_detail_filename = (TextView) findViewById(R.id.rules_detail_filename);
        wxy_detail_memo = (TextView) findViewById(R.id.wxy_detail_memo);
        rules_detail_list = (ListView) findViewById(R.id.rules_detail_list);

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
        gzzdid = intent.getStringExtra("gzzdid");
        mycode = intent.getStringExtra("mcode");

        if (mycode.equals("emap")) {
            url = PortIpAddress.GetCompanybylawdetailQy();
            detailid = "detailid";
        } else {
            url = PortIpAddress.GetCompanybylawdetail();
            detailid = "gzzdid";
        }

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams(detailid, gzzdid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(RulesDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            rules_detail_cbname.setText(jsonArray.getJSONObject(0).getString("cbname"));
                            rules_detail_efftime.setText(jsonArray.getJSONObject(0).getString("efftime"));
                            rules_detail_createname.setText(jsonArray.getJSONObject(0).getString("createname"));
                            rules_detail_cbtypename.setText(jsonArray.getJSONObject(0).getString("cbtypename"));
                            rules_detail_createdate.setText(jsonArray.getJSONObject(0).getString("createdate"));
//                            rules_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
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
                                adapter = new FileListAdapter(RulesDetail.this, mDatas, filenames);
                                rules_detail_list.setAdapter(adapter);
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
    public void RulesDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1){
            ShowToast.showShort(RulesDetail.this, "下载中...");
        }else if (messageEvent.getStatus() == 2){
            ShowToast.showShort(RulesDetail.this, "下载成功");
        }else if (messageEvent.getStatus() == 3){
            ShowToast.showShort(RulesDetail.this, "下载失败");
        }
    }


    @Override
    protected void setOnClick() {
        rules_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rules_detail_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 104) {
            } else if (requestCode == 105) {
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 104) {
                ShowToast.showShort(RulesDetail.this, "拒绝权限后会导致某些功能不可用");
            } else if (requestCode == 105) {
                ShowToast.showShort(RulesDetail.this, "拒绝权限后会导致某些功能不可用");
            }
        }
    };


}
