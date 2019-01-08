package risk_management;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class HiddenDangerReviewDetail extends BaseActivity {
    private ImageView review_detail_back;
    //隐患名称
    private EditText review_detail_hiddenname_etv;
    //整改责任人
    private EditText review_detail_hiddenpersonLiable_etv;
    //隐患描述
    private EditText review_detail_yhms;
    //验收专家及人员
    private EditText review_detail_expert_etv;
    //验收时间
    private TextView review_detail_ysdate_etv;
    //验收地址
    private EditText review_detail_ysaddress_etv;
    //验收情况
    private EditText review_detail_yssituation_etv;
    //提交
    private Button review_detail_submit_btn;

    private String url;
    private String submit_url;
    private String user_token;
    private Intent intent;
    private String yhid;
    private boolean first = true;
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int sj_month;
    private int myYear_ysdate;
    private int myMonth_ysdate;
    private int myDay_ysdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_review_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        review_detail_back = (ImageView) findViewById(R.id.review_detail_back);
        review_detail_hiddenname_etv = (EditText) findViewById(R.id.review_detail_hiddenname_etv);
        review_detail_hiddenpersonLiable_etv = (EditText) findViewById(R.id.review_detail_hiddenpersonLiable_etv);
        review_detail_yhms = (EditText) findViewById(R.id.review_detail_yhms);
        review_detail_expert_etv = (EditText) findViewById(R.id.review_detail_expert_etv);
        review_detail_ysdate_etv = (TextView) findViewById(R.id.review_detail_ysdate_etv);
        review_detail_ysaddress_etv = (EditText) findViewById(R.id.review_detail_ysaddress_etv);
        review_detail_yssituation_etv = (EditText) findViewById(R.id.review_detail_yssituation_etv);
        review_detail_submit_btn = (Button) findViewById(R.id.review_detail_submit_btn);
    }

    @Override
    protected void initData() {
        url = PortIpAddress.RiskDetail();
        submit_url = PortIpAddress.RiskSaveInfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        yhid = intent.getStringExtra("yhid");
        Log.e(TAG, yhid + "-------" + user_token);
        //设置初始日期
        sj_month = calendar.get(Calendar.MONTH) + 1;
        review_detail_ysdate_etv.setText(calendar.get(Calendar.YEAR) + "-" + sj_month + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("yhid", yhid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showToastNoWait(HiddenDangerReviewDetail.this, R.string.network_error);

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e(TAG, jsonObject + "");
                            review_detail_hiddenname_etv.setText(jsonObject.getString("crname"));
                            review_detail_hiddenpersonLiable_etv.setText(jsonObject.getString("zgman"));
                            review_detail_yhms.setText(jsonObject.getString("crdesc"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        review_detail_back.setOnClickListener(this);
        review_detail_ysdate_etv.setOnClickListener(this);
        review_detail_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_detail_back:
                finish();
                break;
            case R.id.review_detail_ysdate_etv:
                if (first) {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                review_detail_ysdate_etv.setText(select_date);
                                myYear_ysdate = year;
                                myMonth_ysdate = monthOfYear;
                                myDay_ysdate = dayOfMonth;
                                first = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog_date.show();
                } else {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                review_detail_ysdate_etv.setText(select_date);
                                myYear_ysdate = year;
                                myMonth_ysdate = monthOfYear;
                                myDay_ysdate = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myYear_ysdate, myMonth_ysdate, myDay_ysdate);
                    dialog_date.show();
                }

                break;
            case R.id.review_detail_submit_btn:
                OkHttpUtils
                        .get()
                        .url(submit_url)
                        .addParams("access_token", user_token)
                        .addParams("bean.yhid", yhid)
                        .addParams("bean.crname", review_detail_hiddenname_etv.getText().toString().trim())
                        .addParams("bean.zgman", review_detail_hiddenpersonLiable_etv.getText().toString().trim())
                        .addParams("bean.crdesc", review_detail_yhms.getText().toString().trim())
                        .addParams("bean.yszjry", review_detail_expert_etv.getText().toString().trim())
                        .addParams("bean.xcyssj", review_detail_ysdate_etv.getText().toString().trim())
                        .addParams("bean.ysdz", review_detail_ysaddress_etv.getText().toString().trim())
                        .addParams("bean.ysqk", review_detail_yssituation_etv.getText().toString().trim())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                ShowToast.showToastNoWait(HiddenDangerReviewDetail.this, R.string.network_error);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.e(TAG, jsonObject + "");
                                    if (jsonObject.getString("success").equals("true")) {
                                        ShowToast.showToastNoWait(HiddenDangerReviewDetail.this, R.string.write_success);
                                        finish();
                                    } else {
                                        ShowToast.showToastNoWait(HiddenDangerReviewDetail.this, R.string.write_fail);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
        }
    }
}
