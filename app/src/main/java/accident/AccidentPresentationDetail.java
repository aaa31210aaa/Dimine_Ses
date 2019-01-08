package accident;

import android.content.Intent;
import android.os.Bundle;
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

import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AccidentPresentationDetail extends BaseActivity {
    private ImageView accident_presentation_detail_back;
    //事故名称
    private TextView accident_detail_aititle;
    //事故ID
    private TextView accident_detail_procid;
    //事故单位
    private TextView accident_detail_deptname;
    //事故日期
    private TextView accident_detail_aldate;
    //事故分类
    private TextView accident_detail_aitypename;
    //事故等级
    private TextView accident_detail_ailevelname;
    //死亡人数
    private TextView accident_detail_swnum;
    //重伤人数
    private TextView accident_detail_zsnum;
    //轻伤人数
    private TextView accident_detail_qsnum;
    //上报人
    private TextView accident_detail_sbr;
    // 失踪人口
    private TextView accident_detail_sznum;
    //事故地区
//    private TextView accident_detail_areaname;

    //事故内容
    private TextView accident_content_detail;
    private String url;
    private String user_token;
    private String procid;
    private String username;
    private Intent intent;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_presentation_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        accident_presentation_detail_back = (ImageView) findViewById(R.id.accident_presentation_detail_back);
        accident_detail_aititle = (TextView) findViewById(R.id.accident_detail_aititle);
        accident_detail_deptname = (TextView) findViewById(R.id.accident_detail_deptname);
        accident_detail_aldate = (TextView) findViewById(R.id.accident_detail_aldate);
        accident_detail_aitypename = (TextView) findViewById(R.id.accident_detail_aitypename);
        accident_detail_ailevelname = (TextView) findViewById(R.id.accident_detail_ailevelname);
        accident_detail_swnum = (TextView) findViewById(R.id.accident_detail_swnum);
        accident_detail_zsnum = (TextView) findViewById(R.id.accident_detail_zsnum);
        accident_detail_qsnum = (TextView) findViewById(R.id.accident_detail_qsnum);
        accident_detail_sbr = (TextView) findViewById(R.id.accident_detail_sbr);
        accident_detail_sznum = (TextView) findViewById(R.id.accident_detail_sznum);
//      accident_detail_areaname = (TextView) findViewById(R.id.accident_detail_areaname);
        accident_content_detail = (TextView) findViewById(R.id.accident_content_detail);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.Accidentdetailinfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        username = SharedPrefsUtil.getValue(this, "userInfo", "username", null);
        accident_detail_sbr.setText(username);
        intent = getIntent();
        procid = intent.getStringExtra("procid");
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("procid", procid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showToastNoWait(AccidentPresentationDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsb = new JSONObject(response);
                            JSONArray jsar = jsb.getJSONArray("cells");
                            accident_detail_aititle.setText(jsar.getJSONObject(index).getString("aititle"));
                            accident_detail_deptname.setText(jsar.getJSONObject(index).getString("aidept"));
                            accident_detail_aldate.setText(jsar.getJSONObject(index).getString("aldate"));
                            accident_detail_aitypename.setText(jsar.getJSONObject(index).getString("aitypename"));
                            accident_detail_ailevelname.setText(jsar.getJSONObject(index).getString("ailevelname"));
                            accident_detail_swnum.setText(jsar.getJSONObject(index).getString("swnum"));
                            accident_detail_zsnum.setText(jsar.getJSONObject(index).getString("zsnum"));
                            accident_detail_qsnum.setText(jsar.getJSONObject(index).getString("qsnum"));
                            accident_detail_sznum.setText(jsar.getJSONObject(index).getString("sznum"));
//                          accident_detail_areaname.setText(jsar.getJSONObject(index).getString("areaname"));
                            accident_content_detail.setText(jsar.getJSONObject(index).getString("sgqksm"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        accident_presentation_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accident_presentation_detail_back:
                finish();
                break;
        }
    }
}
