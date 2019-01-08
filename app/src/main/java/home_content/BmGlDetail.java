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

public class BmGlDetail extends BaseActivity {
    private ImageView aqjypx_detail_back;
    //单位名称
    private TextView bmgl_detail_parentdeptname;
    //部门名称
    private TextView bmgl_detail_deptname;
    //部门编号
    private TextView bmgl_detail_deptcode;
    //联系电话
    private TextView bmgl_detail_tel;
    //传真
    private TextView bmgl_detail_fax;
    //地址
    private TextView bmgl_detail_address;
    //附件名称
//    private TextView bmgl_detail_filename;
    //备注
    private TextView bmgl_detail_memo;

    private ListView bmgl_detail_list;
    private List<String> filenames;
    private List<FileBean> mDatas;
    private FileListAdapter adapter;

    private String detailid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bm_gl_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        aqjypx_detail_back = (ImageView) findViewById(R.id.aqjypx_detail_back);
        bmgl_detail_parentdeptname = (TextView) findViewById(R.id.bmgl_detail_parentdeptname);
        bmgl_detail_deptname = (TextView) findViewById(R.id.bmgl_detail_deptname);
        bmgl_detail_deptcode = (TextView) findViewById(R.id.bmgl_detail_deptcode);
        bmgl_detail_tel = (TextView) findViewById(R.id.bmgl_detail_tel);
        bmgl_detail_fax = (TextView) findViewById(R.id.bmgl_detail_fax);
        bmgl_detail_address = (TextView) findViewById(R.id.bmgl_detail_address);
//        bmgl_detail_filename = (TextView) findViewById(R.id.bmgl_detail_filename);
        bmgl_detail_memo = (TextView) findViewById(R.id.bmgl_detail_memo);
        bmgl_detail_list = (ListView) findViewById(R.id.bmgl_detail_list);

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
                .url(PortIpAddress.GetCompanydeptdetail())
                .addParams("access_token", PortIpAddress.GetToken(this))
                .addParams("detailid", detailid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(BmGlDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            JSONArray files = jsonArray.getJSONObject(0).getJSONArray("files");
                            bmgl_detail_parentdeptname.setText(jsonArray.getJSONObject(0).getString("parentdeptname"));
                            bmgl_detail_deptname.setText(jsonArray.getJSONObject(0).getString("deptname"));
                            bmgl_detail_deptcode.setText(jsonArray.getJSONObject(0).getString("deptcode"));
                            bmgl_detail_tel.setText(jsonArray.getJSONObject(0).getString("tel"));
                            bmgl_detail_fax.setText(jsonArray.getJSONObject(0).getString("fax"));
                            bmgl_detail_address.setText(jsonArray.getJSONObject(0).getString("address"));
//                            bmgl_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            bmgl_detail_memo.setText(jsonArray.getJSONObject(0).getString("memo"));

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
                                adapter = new FileListAdapter(BmGlDetail.this, mDatas, filenames);
                                bmgl_detail_list.setAdapter(adapter);
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
    public void BmGlDetailEvent(MessageEvent messageEvent) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getDownloadId() == messageEvent.getId()) {
                mDatas.get(i).setStatus(messageEvent.getStatus());
            }
        }
        adapter.notifyDataSetChanged();
        if (messageEvent.getStatus() == 1){
            ShowToast.showShort(BmGlDetail.this, "下载中...");
        }else if (messageEvent.getStatus() == 2){
            ShowToast.showShort(BmGlDetail.this, "下载成功");
        }else if (messageEvent.getStatus() == 3){
            ShowToast.showShort(BmGlDetail.this, "下载失败");
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aqjypx_detail_back:
                finish();
                break;
        }
    }
}
