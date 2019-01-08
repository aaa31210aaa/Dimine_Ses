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
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AqJyPxDetail extends BaseActivity {
    private ImageView aqjypx_detail_back;
    //培训名称
    private TextView aqjypx_detail_trainingname;
    //培训时间
    private TextView aqjypx_detail_trainingdate;
    //培训地点
    private TextView aqjypx_detail_trainingadd;
    //培训内容
    private TextView aqjypx_detail_trainingcontent;
    //培训方式
    private TextView aqjypx_detail_trainingtype;
    //培训授课人
    private TextView aqjypx_detail_trainingman;
    //授课人介绍
    private TextView aqjypx_detail_trainingintro;
    //组织单位
    private TextView aqjypx_detail_trainingunit;
    //完成情况
    private TextView aqjypx_detail_completion;
    //培训效果
    private TextView aqjypx_detail_effect;
    //企业名称
    private TextView aqjypx_detail_qyname;

    //附件名称
//    private TextView aqjypx_detail_filename;

    //备注
    private TextView aqjypx_detail_memo;

    private ListView aqjypx_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String trainingid = "";
    private String url = "";
    private String detailid = "";
    private String mycode = "";
    private String qyid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aq_jy_px_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        aqjypx_detail_back = (ImageView) findViewById(R.id.aqjypx_detail_back);
        aqjypx_detail_trainingname = (TextView) findViewById(R.id.aqjypx_detail_trainingname);
        aqjypx_detail_trainingdate = (TextView) findViewById(R.id.aqjypx_detail_trainingdate);
        aqjypx_detail_trainingadd = (TextView) findViewById(R.id.aqjypx_detail_trainingadd);
        aqjypx_detail_trainingcontent = (TextView) findViewById(R.id.aqjypx_detail_trainingcontent);
        aqjypx_detail_trainingtype = (TextView) findViewById(R.id.aqjypx_detail_trainingtype);
        aqjypx_detail_trainingman = (TextView) findViewById(R.id.aqjypx_detail_trainingman);
        aqjypx_detail_trainingintro = (TextView) findViewById(R.id.aqjypx_detail_trainingintro);
        aqjypx_detail_trainingunit = (TextView) findViewById(R.id.aqjypx_detail_trainingunit);
        aqjypx_detail_completion = (TextView) findViewById(R.id.aqjypx_detail_completion);
        aqjypx_detail_effect = (TextView) findViewById(R.id.aqjypx_detail_effect);
        aqjypx_detail_qyname = (TextView) findViewById(R.id.aqjypx_detail_qyname);
        //加下划线
        aqjypx_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        aqjypx_detail_qyname.getPaint().setAntiAlias(true);
//        aqjypx_detail_filename = (TextView) findViewById(R.id.aqjypx_detail_filename);
        aqjypx_detail_memo = (TextView) findViewById(R.id.aqjypx_detail_memo);
        aqjypx_detail_list = (ListView) findViewById(R.id.aqjypx_detail_list);

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
        trainingid = intent.getStringExtra("trainingid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetSafetytrainingDetailQy();
        } else {
            detailid = "trainingid";
            url = PortIpAddress.GetSafetytrainingDetail();
        }
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", SharedPrefsUtil.getValue(this, "userInfo", "user_token", null))
                .addParams(detailid, trainingid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AqJyPxDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            aqjypx_detail_trainingname.setText(jsonArray.getJSONObject(0).getString("trainingname"));
                            aqjypx_detail_trainingdate.setText(jsonArray.getJSONObject(0).getString("trainingdate"));
                            aqjypx_detail_trainingadd.setText(jsonArray.getJSONObject(0).getString("trainingadd"));
                            aqjypx_detail_trainingcontent.setText(jsonArray.getJSONObject(0).getString("trainingcontent"));
                            aqjypx_detail_trainingtype.setText(jsonArray.getJSONObject(0).getString("trainingtype"));
                            aqjypx_detail_trainingman.setText(jsonArray.getJSONObject(0).getString("trainingman"));
                            aqjypx_detail_trainingintro.setText(jsonArray.getJSONObject(0).getString("trainingintro"));
                            aqjypx_detail_trainingunit.setText(jsonArray.getJSONObject(0).getString("trainingunit"));
                            aqjypx_detail_completion.setText(jsonArray.getJSONObject(0).getString("completion"));
                            aqjypx_detail_effect.setText(jsonArray.getJSONObject(0).getString("effect"));
                            aqjypx_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            if (aqjypx_detail_qyname.getText().toString().trim().equals("")) {
                                aqjypx_detail_qyname.setEnabled(false);
                            }

//                            aqjypx_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            aqjypx_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));
                            qyid = jsonArray.getJSONObject(0).getString("qyid");

                            for (int i = 0; i < files.length(); i++) {
                                if (!(files.getJSONObject(i).getString("filename").equals(""))) {
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
                                adapter = new FileListAdapter(AqJyPxDetail.this, mDatas, filenames);
                                aqjypx_detail_list.setAdapter(adapter);
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
    public void AqJyPxDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(AqJyPxDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(AqJyPxDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(AqJyPxDetail.this, "下载失败");
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
        aqjypx_detail_back.setOnClickListener(this);
        aqjypx_detail_qyname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aqjypx_detail_back:
                finish();
                break;
            case R.id.aqjypx_detail_qyname:
                Intent intent = new Intent(this, EnterpriseMapInformation.class);
                intent.putExtra("clickId", qyid);
                startActivity(intent);
                break;
        }
    }
}
