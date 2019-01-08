package wfp_query;

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

public class LawsDetail extends BaseActivity {
    private ImageView laws_detail_item_back;
    //法律法规标题
    private TextView laws_detail_item_lrtitle;
    //所属行业
    private TextView laws_detail_item_industryname;
    //所属具体类别
    private TextView laws_detail_item_lrtypename;
    //文号
    private TextView laws_detail_item_docnumber;
    //颁布时间
    private TextView laws_detail_item_bbdate;
    //生效时间
    private TextView laws_detail_item_effdate;
    //法律法规信息
    private TextView laws_detail_item_lrdesc;
    private String url;
    private String user_token;
    //法律法规ID
    private String flfgid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        laws_detail_item_back = (ImageView) findViewById(R.id.laws_detail_item_back);
        laws_detail_item_lrtitle = (TextView) findViewById(R.id.laws_detail_item_lrtitle);
        laws_detail_item_industryname = (TextView) findViewById(R.id.laws_detail_item_industryname);
        laws_detail_item_lrtypename = (TextView) findViewById(R.id.laws_detail_item_lrtypename);
        laws_detail_item_docnumber = (TextView) findViewById(R.id.laws_detail_item_docnumber);
        laws_detail_item_bbdate = (TextView) findViewById(R.id.laws_detail_item_bbdate);
        laws_detail_item_effdate = (TextView) findViewById(R.id.laws_detail_item_effdate);
        laws_detail_item_lrdesc = (TextView) findViewById(R.id.laws_detail_item_lrdesc);

    }

    @Override
    protected void initData() {
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        url = PortIpAddress.GetLawregulationsdetail();
        intent = getIntent();
        flfgid = intent.getStringExtra("flfgid");
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("flfgid", flfgid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showToastNoWait(LawsDetail.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            JSONArray jsonArray = jsonObject.getJSONArray("cells");
                            laws_detail_item_lrtitle.setText(jsonArray.getJSONObject(0).getString("lrtitle"));
                            laws_detail_item_industryname.setText(jsonArray.getJSONObject(0).getString("industryname"));
                            laws_detail_item_lrtypename.setText(jsonArray.getJSONObject(0).getString("lrtypename"));
                            laws_detail_item_docnumber.setText(jsonArray.getJSONObject(0).getString("docnumber"));
                            laws_detail_item_bbdate.setText(jsonArray.getJSONObject(0).getString("bbdate"));
                            laws_detail_item_effdate.setText(jsonArray.getJSONObject(0).getString("effdate"));
                            laws_detail_item_lrdesc.setText(jsonArray.getJSONObject(0).getString("lrdesc"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void setOnClick() {
        laws_detail_item_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laws_detail_item_back:
                finish();
                break;
        }
    }
}
