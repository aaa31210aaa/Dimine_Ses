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

public class ZyWsXxDetail extends BaseActivity {
    private ImageView zywsxx_detail_back;
    //作业场所
    private TextView zywsxx_detail_ohaddres;
    //岗位
    private TextView zywsxx_detail_sites;
    //危险因素名称
    private TextView zywsxx_detail_ohname;
    //创建日期
    private TextView zywsxx_detail_createdate;
    //企业名称
    private TextView zywsxx_detail_qyname;
    //附件名称
//    private TextView zywsxx_detail_filename;
    //说明
    private TextView zywsxx_detail_remark;
    //备注
    private TextView zywsxx_detail_memo;

    private ListView zywsxx_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;


    private String url= "";
    private String ohid= "";
    private String mycode= "";
    private String detailid= "";
    private String qyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zy_ws_xx_detail);
        initView();
        initData();
        setOnClick();

    }

    @Override
    protected void initView() {
        zywsxx_detail_back = (ImageView) findViewById(R.id.zywsxx_detail_back);
        zywsxx_detail_ohaddres = (TextView) findViewById(R.id.zywsxx_detail_ohaddres);
        zywsxx_detail_sites = (TextView) findViewById(R.id.zywsxx_detail_sites);
        zywsxx_detail_ohname = (TextView) findViewById(R.id.zywsxx_detail_ohname);
        zywsxx_detail_createdate = (TextView) findViewById(R.id.zywsxx_detail_createdate);
        zywsxx_detail_qyname = (TextView) findViewById(R.id.zywsxx_detail_qyname);
        //加下划线
        zywsxx_detail_qyname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        zywsxx_detail_qyname.getPaint().setAntiAlias(true);
//        zywsxx_detail_filename = (TextView) findViewById(R.id.zywsxx_detail_filename);
        zywsxx_detail_remark = (TextView) findViewById(R.id.zywsxx_detail_remark);
        zywsxx_detail_memo = (TextView) findViewById(R.id.zywsxx_detail_memo);
        zywsxx_detail_list = (ListView) findViewById(R.id.zywsxx_detail_list);

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
        qyid = intent.getStringExtra("qyid");
        ohid = intent.getStringExtra("ohid");
        mycode = intent.getStringExtra("mcode");
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        if (mycode.equals("emap")) {
            detailid = "detailid";
            url = PortIpAddress.GetOccuhealthDetailQy();
        } else {
            detailid = "ohid";
            url = PortIpAddress.GetOccuhealthDetail();
        }
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams(detailid, ohid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(ZyWsXxDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");

                            zywsxx_detail_ohaddres.setText(jsonArray.getJSONObject(0).getString("ohaddres"));
                            zywsxx_detail_sites.setText(jsonArray.getJSONObject(0).getString("sites"));
                            zywsxx_detail_ohname.setText(jsonArray.getJSONObject(0).getString("ohname"));
                            zywsxx_detail_createdate.setText(jsonArray.getJSONObject(0).getString("createdate"));
                            zywsxx_detail_qyname.setText(jsonArray.getJSONObject(0).getString("qyname"));
                            if (zywsxx_detail_qyname.getText().toString().trim().equals("")){
                                zywsxx_detail_qyname.setEnabled(false);
                            }
//                            zywsxx_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            zywsxx_detail_remark.setText(jsonArray.getJSONObject(0).getString("remark"));
                            zywsxx_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
                                adapter = new FileListAdapter(ZyWsXxDetail.this, mDatas, filenames);
                                zywsxx_detail_list.setAdapter(adapter);
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
    public void ZyWsXxDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1) {
            ShowToast.showShort(ZyWsXxDetail.this, "下载中...");
        } else if (messageEvent.getStatus() == 2) {
            ShowToast.showShort(ZyWsXxDetail.this, "下载成功");
        } else if (messageEvent.getStatus() == 3) {
            ShowToast.showShort(ZyWsXxDetail.this, "下载失败");
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
        zywsxx_detail_back.setOnClickListener(this);
        zywsxx_detail_qyname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zywsxx_detail_back:
                finish();
                break;
            case R.id.zywsxx_detail_qyname:
                Intent intent = new Intent(this, EnterpriseMapInformation.class);
                intent.putExtra("clickId", qyid);
                startActivity(intent);
                break;
        }
    }
}
