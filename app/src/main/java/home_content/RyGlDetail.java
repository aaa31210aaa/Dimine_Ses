package home_content;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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
import enterprise_information.EnterpriseMapInformation;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;

public class RyGlDetail extends BaseActivity {
    private ImageView rygl_detail_back;
    //人员类型
    private TextView rygl_detail_sptypename;
    //姓名
    private TextView rygl_detail_mainhead;
    //所在部门
    private TextView rygl_detail_deptname;
    //移动电话
    private TextView rygl_detail_mhmobilephone;
    //电子邮箱
    private TextView rygl_detail_mhemail;
    //身份证编号
    private TextView rygl_detail_peopleno;
    //职称
    private TextView rygl_detail_titles;
    //职位
    private TextView rygl_detail_postion;
    //证书编号
    private TextView rygl_detail_cardno;
    //有效日期
    private TextView rygl_detail_yxstardate;
    //截止日期
    private TextView rygl_detail_yxenddate;
    //系统用户
    private TextView rygl_detail_isusername;
    //企业名称
    private TextView rygl_detail_qyname;
    //附件名称
//    private TextView rygl_detail_filename;
    private ListView rygl_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String detailid = "";
    private Intent intent;
    private String qyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ry_gl_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        rygl_detail_back = (ImageView) findViewById(R.id.rygl_detail_back);
        rygl_detail_sptypename = (TextView) findViewById(R.id.rygl_detail_sptypename);
        rygl_detail_mainhead = (TextView) findViewById(R.id.rygl_detail_mainhead);
        rygl_detail_deptname = (TextView) findViewById(R.id.rygl_detail_deptname);
        rygl_detail_mhmobilephone = (TextView) findViewById(R.id.rygl_detail_mhmobilephone);
        rygl_detail_mhemail = (TextView) findViewById(R.id.rygl_detail_mhemail);
        rygl_detail_peopleno = (TextView) findViewById(R.id.rygl_detail_peopleno);
        rygl_detail_titles = (TextView) findViewById(R.id.rygl_detail_titles);
        rygl_detail_postion = (TextView) findViewById(R.id.rygl_detail_postion);
        rygl_detail_cardno = (TextView) findViewById(R.id.rygl_detail_cardno);
        rygl_detail_yxstardate = (TextView) findViewById(R.id.rygl_detail_yxstardate);
        rygl_detail_yxenddate = (TextView) findViewById(R.id.rygl_detail_yxenddate);
        rygl_detail_isusername = (TextView) findViewById(R.id.rygl_detail_isusername);
        rygl_detail_qyname = (TextView) findViewById(R.id.rygl_detail_qyname);
        //加下划线
        rygl_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        rygl_detail_qyname.getPaint().setAntiAlias(true);
//        rygl_detail_filename = (TextView) findViewById(R.id.rygl_detail_filename);
        rygl_detail_list = (ListView) findViewById(R.id.rygl_detail_list);

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
        intent = getIntent();
        detailid = intent.getStringExtra("detailid");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(PortIpAddress.GetSafepeopledetail())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("detailid", detailid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(RyGlDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            rygl_detail_sptypename.setText(jsonArray.getJSONObject(0).getString("sptypename"));
                            rygl_detail_mainhead.setText(jsonArray.getJSONObject(0).getString("mainhead"));
                            rygl_detail_deptname.setText(jsonArray.getJSONObject(0).getString("deptname"));
                            rygl_detail_mhmobilephone.setText(jsonArray.getJSONObject(0).getString("mhmobilephone"));
                            rygl_detail_mhemail.setText(jsonArray.getJSONObject(0).getString("mhemail"));
                            rygl_detail_peopleno.setText(jsonArray.getJSONObject(0).getString("peopleno"));
                            rygl_detail_titles.setText(jsonArray.getJSONObject(0).getString("titles"));
                            rygl_detail_postion.setText(jsonArray.getJSONObject(0).getString("postion"));
                            rygl_detail_cardno.setText(jsonArray.getJSONObject(0).getString("cardno"));
                            rygl_detail_yxstardate.setText(jsonArray.getJSONObject(0).getString("yxstardate"));
                            rygl_detail_yxenddate.setText(jsonArray.getJSONObject(0).getString("yxenddate"));
                            rygl_detail_isusername.setText(jsonArray.getJSONObject(0).getString("isusername"));
//                            rygl_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            rygl_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            if (rygl_detail_qyname.getText().toString().trim().equals("")){
                                rygl_detail_qyname.setEnabled(false);
                            }
                            qyid = jsonArray.getJSONObject(0).getString("qyid");

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
                                adapter = new FileListAdapter(RyGlDetail.this, mDatas, filenames);
                                rygl_detail_list.setAdapter(adapter);
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
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(RyGlDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(RyGlDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(RyGlDetail.this, "下载失败");
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
        rygl_detail_back.setOnClickListener(this);
        rygl_detail_qyname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rygl_detail_back:
                finish();
                break;
            case R.id.rygl_detail_qyname:
                Intent intent = new Intent(this, EnterpriseMapInformation.class);
                intent.putExtra("clickId", qyid);
                startActivity(intent);
                break;
        }
    }
}
