package enterprise_information;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.QyYhInfoDetailAdapter;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class QyYhInfomationDetail extends BaseActivity {
    private ImageView qy_yh_information_detail_back;
    //隐患名称
    private TextView qy_yh_information_detail_crname;
    //隐患类别
    private TextView qy_yh_information_detail_crtypename;
    //隐患编号
    private TextView qy_yh_information_detail_crcode;
    //隐患分类
    private TextView qy_yh_information_detail_crlxflname;
    //排查日期
    private TextView qy_yh_information_detail_pcdate;
    //整改类型
    private TextView qy_yh_information_detail_zgtypename;
    //隐患地点
    private TextView qy_yh_information_detail_craddr;
    //隐患描述
    private TextView qy_yh_information_detail_crdesc;
    //报告摘要
    private TextView qy_yh_information_detail_crbgzy;
    //隐患状态
    private TextView qy_yh_information_detail_crstatename;
    //治理方案
    private TextView qy_yh_information_detail_zyzlfa;
    //隐患来源
    private TextView qy_yh_information_detail_source;
    //整改责任人
    private TextView qy_yh_information_detail_zgman;
    //治理截止日
    private TextView qy_yh_information_detail_zljzdate;
    //企业名称
    private TextView qy_yh_information_detail_deptname;
    //附件名称
//    private TextView qy_yh_information_detail_filename;
    //图片
    private RecyclerView qy_yh_information_detail_recycler;

    private Dialog dialog;

    private Intent intent;
    private String url;
    private String user_token;
    private String yhid;
    private QyYhInfoDetailAdapter adapter;
    private List<String> imagePaths;
    private String mycode;
    private String detailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qy_yh_infomation_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        qy_yh_information_detail_back = (ImageView) findViewById(R.id.qy_yh_information_detail_back);
        qy_yh_information_detail_crname = (TextView) findViewById(R.id.qy_yh_information_detail_crname);
        qy_yh_information_detail_crtypename = (TextView) findViewById(R.id.qy_yh_information_detail_crtypename);
        qy_yh_information_detail_crcode = (TextView) findViewById(R.id.qy_yh_information_detail_crcode);
        qy_yh_information_detail_crlxflname = (TextView) findViewById(R.id.qy_yh_information_detail_crlxflname);
        qy_yh_information_detail_pcdate = (TextView) findViewById(R.id.qy_yh_information_detail_pcdate);
        qy_yh_information_detail_zgtypename = (TextView) findViewById(R.id.qy_yh_information_detail_zgtypename);
        qy_yh_information_detail_craddr = (TextView) findViewById(R.id.qy_yh_information_detail_craddr);
        qy_yh_information_detail_crdesc = (TextView) findViewById(R.id.qy_yh_information_detail_crdesc);
        qy_yh_information_detail_crbgzy = (TextView) findViewById(R.id.qy_yh_information_detail_crbgzy);
        qy_yh_information_detail_crstatename = (TextView) findViewById(R.id.qy_yh_information_detail_crstatename);
        qy_yh_information_detail_zyzlfa = (TextView) findViewById(R.id.qy_yh_information_detail_zyzlfa);
        qy_yh_information_detail_source = (TextView) findViewById(R.id.qy_yh_information_detail_source);
        qy_yh_information_detail_zgman = (TextView) findViewById(R.id.qy_yh_information_detail_zgman);
        qy_yh_information_detail_zljzdate = (TextView) findViewById(R.id.qy_yh_information_detail_zljzdate);
        qy_yh_information_detail_deptname = (TextView) findViewById(R.id.qy_yh_information_detail_deptname);
        qy_yh_information_detail_recycler = (RecyclerView) findViewById(R.id.qy_yh_information_detail_recycler);
//        qy_yh_information_detail_filename = (TextView) findViewById(R.id.qy_yh_information_detail_filename);

    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);

        intent = getIntent();
        yhid = intent.getStringExtra("yhid");
        mycode = intent.getStringExtra("mcode");

//        if (mycode.equals("emap")) {
//            url = PortIpAddress.GetRiskinfodetailQy();
//            detailid = "detailid";
//        } else {
        url = PortIpAddress.GetRiskinfodetail();
        detailid = "yhid";
//        }
        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams(detailid, yhid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(QyYhInfomationDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            qy_yh_information_detail_crname.setText(jsonArray.getJSONObject(0).getString("crname"));
                            qy_yh_information_detail_crtypename.setText(jsonArray.getJSONObject(0).getString("crtypename"));
                            qy_yh_information_detail_pcdate.setText(jsonArray.getJSONObject(0).getString("pcdate"));
                            qy_yh_information_detail_crcode.setText(jsonArray.getJSONObject(0).getString("crcode"));
                            qy_yh_information_detail_crlxflname.setText(jsonArray.getJSONObject(0).getString("crlxflname"));
                            qy_yh_information_detail_zgtypename.setText(jsonArray.getJSONObject(0).getString("zgtypename"));
                            qy_yh_information_detail_craddr.setText(jsonArray.getJSONObject(0).getString("craddr"));
                            qy_yh_information_detail_crdesc.setText(jsonArray.getJSONObject(0).getString("crdesc"));
                            qy_yh_information_detail_crbgzy.setText(jsonArray.getJSONObject(0).getString("crbgzy"));
                            qy_yh_information_detail_crstatename.setText(jsonArray.getJSONObject(0).getString("crstatename"));
                            qy_yh_information_detail_zyzlfa.setText(jsonArray.getJSONObject(0).getString("zyzlfa"));
                            qy_yh_information_detail_source.setText(jsonArray.getJSONObject(0).getString("source"));
                            qy_yh_information_detail_zgman.setText(jsonArray.getJSONObject(0).getString("zgman"));
                            qy_yh_information_detail_zljzdate.setText(jsonArray.getJSONObject(0).getString("zljzdate"));
                            qy_yh_information_detail_deptname.setText(jsonArray.getJSONObject(0).getString("deptname"));
//                            qy_yh_information_detail_filename.setText(jsonArray.getJSONObject(0).getString("filename"));
                            JSONArray arrayFiles = jsonArray.getJSONObject(0).getJSONArray("files");
                            imagePaths = new ArrayList<String>();
                            for (int i = 0; i < arrayFiles.length(); i++) {
                                String fileurl = arrayFiles.optJSONObject(i).getString("fileurl");
                                if (!fileurl.equals("")) {
                                    String path = PortIpAddress.myUrl + fileurl;
                                    path = path.replaceAll("\\\\", "/");
                                    imagePaths.add(path);
                                    Log.e(TAG, path);
                                }
                            }
                            qy_yh_information_detail_recycler.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
                            adapter = new QyYhInfoDetailAdapter(QyYhInfomationDetail.this, imagePaths);
                            qy_yh_information_detail_recycler.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        qy_yh_information_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qy_yh_information_detail_back:
                finish();
                break;
        }
    }
}
