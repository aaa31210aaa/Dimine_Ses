package accident;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import utils.BaseActivity;
import utils.DateUtils;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class AddAccidentPresentation extends BaseActivity {
    private ImageView add_accident_presentation_back;
    //事故单位
    private EditText add_accident_presentation_deptname;
    //事故名称
    private EditText add_accident_presentation_aititle;
    //事故日期
    private TextView add_accident_presentation_aldate;
    //事故分类
    private TextView add_accident_presentation_aitypename;
    private String[] aitypename_arr;
    //事故等级
    private TextView add_accident_presentation_ailevelname;
    private String[] ailevelname_arr;

    //死亡人数
    private EditText add_accident_presentation_swnum;
    //重伤人数
    private EditText add_accident_presentation_zsnum;
    //轻伤人数
    private EditText add_accident_presentation_qsnum;
    //失踪人口
    private EditText add_accident_presentation_sznum;
    //事故地区
//    private EditText add_accident_presentation_areaname;
    //上报人
    private EditText add_accident_presentation_sbr;
    //提交
    private ImageView add_accident_presentation_submit;
    //排查日期第一次点击
    private boolean first = true;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int myYear_accidentpresentation;
    private int myMonth_accidentpresentation;
    private int myDay_accidentpresentation;

    private Map<String, String> arrMap = new HashMap<>();

    private String url;
    private String user_token;
    private String userid;
    private String username;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident_presentation);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        add_accident_presentation_back = (ImageView) findViewById(R.id.add_accident_presentation_back);
        add_accident_presentation_deptname = (EditText) findViewById(R.id.add_accident_presentation_deptname);
        add_accident_presentation_aititle = (EditText) findViewById(R.id.add_accident_presentation_aititle);
        add_accident_presentation_aldate = (TextView) findViewById(R.id.add_accident_presentation_aldate);
        add_accident_presentation_aitypename = (TextView) findViewById(R.id.add_accident_presentation_aitypename);
        add_accident_presentation_ailevelname = (TextView) findViewById(R.id.add_accident_presentation_ailevelname);
        add_accident_presentation_swnum = (EditText) findViewById(R.id.add_accident_presentation_swnum);
        add_accident_presentation_zsnum = (EditText) findViewById(R.id.add_accident_presentation_zsnum);
        add_accident_presentation_qsnum = (EditText) findViewById(R.id.add_accident_presentation_qsnum);
        add_accident_presentation_sznum = (EditText) findViewById(R.id.add_accident_presentation_sznum);
//        add_accident_presentation_areaname = (EditText) findViewById(R.id.add_accident_presentation_areaname);
        add_accident_presentation_sbr = (EditText) findViewById(R.id.add_accident_presentation_sbr);
        add_accident_presentation_submit = (ImageView) findViewById(R.id.add_accident_presentation_submit);

        add_accident_presentation_swnum.setText("0");
        add_accident_presentation_zsnum.setText("0");
        add_accident_presentation_qsnum.setText("0");
        add_accident_presentation_sznum.setText("0");
    }

    @Override
    protected void initData() {
        url = PortIpAddress.AddAccidentdetailinfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        userid = SharedPrefsUtil.getValue(this, "userInfo", "userid", null);
        username = SharedPrefsUtil.getValue(this, "userInfo", "username", null);

        //初始化下拉内容数据
        aitypename_arr = getResources().getStringArray(R.array.array_aitypename);
        ailevelname_arr = getResources().getStringArray(R.array.array_ailevelname);
        add_accident_presentation_aitypename.setText(aitypename_arr[0]);
        add_accident_presentation_ailevelname.setText(ailevelname_arr[0]);
        //将数据添加至map
        addArrData();
        //设置popwindow
        setPopWindow(add_accident_presentation_aitypename, aitypename_arr);
        setPopWindow(add_accident_presentation_ailevelname, ailevelname_arr);


        //获取上报人
        add_accident_presentation_sbr.setText(username);
        //设置初始日期
        String nowdate = DateUtils.getStringDate();
        add_accident_presentation_aldate.setText(nowdate);
    }

    //将数据添加至map集合
    private void addArrData() {
        arrMap.put(aitypename_arr[0], "SGFL001");
        arrMap.put(aitypename_arr[1], "SGFL002");
        arrMap.put(ailevelname_arr[0], "SGDJ001");
        arrMap.put(ailevelname_arr[1], "SGDJ002");
    }


    private void mOkhttp() {
        if (!add_accident_presentation_deptname.getText().toString().equals("")
                && !add_accident_presentation_aititle.getText().toString().equals("")
                && !add_accident_presentation_aldate.getText().toString().equals("")
                && !add_accident_presentation_swnum.getText().toString().equals("")
                && !add_accident_presentation_zsnum.getText().toString().equals("")
                && !add_accident_presentation_qsnum.getText().toString().equals("")
                && !add_accident_presentation_sznum.getText().toString().equals("")
//                && !add_accident_presentation_areaname.getText().toString().equals("")
                ) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("access_token", user_token)
                    .addParams("aidept", add_accident_presentation_deptname.getText().toString().trim())
                    .addParams("aititle", add_accident_presentation_aititle.getText().toString().trim())
                    .addParams("aldate", add_accident_presentation_aldate.getText().toString().trim())
                    .addParams("aitypename", arrMap.get(add_accident_presentation_aitypename.getText().toString().trim()))
                    .addParams("ailevelname", arrMap.get(add_accident_presentation_ailevelname.getText().toString().trim()))
                    .addParams("swnum", add_accident_presentation_swnum.getText().toString().trim())
                    .addParams("zsnum", add_accident_presentation_zsnum.getText().toString().trim())
                    .addParams("qsnum", add_accident_presentation_qsnum.getText().toString().trim())
                    .addParams("sznum", add_accident_presentation_sznum.getText().toString().trim())
//                    .addParams("areaname", add_accident_presentation_areaname.getText().toString().trim())
                    .addParams("userid", userid)
                    .addParams("sgqksm", add_accident_presentation_sbr.getText().toString().trim())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            ShowToast.showToastNoWait(AddAccidentPresentation.this, R.string.network_error);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.e(TAG, jsonObject + "");
                                if (jsonObject.getString("success").equals("true")) {
                                    ShowToast.showToastNoWait(AddAccidentPresentation.this, R.string.write_success);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
                                    ShowToast.showToastNoWait(AddAccidentPresentation.this, R.string.write_fail);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ShowToast.showToastNowait(this, "有必填选项未填写");
        }

    }


    @Override
    protected void setOnClick() {
        add_accident_presentation_submit.setOnClickListener(this);
        add_accident_presentation_back.setOnClickListener(this);
        add_accident_presentation_aldate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_accident_presentation_back:
                finish();
                break;
            case R.id.add_accident_presentation_submit:
                mOkhttp();
                break;
            case R.id.add_accident_presentation_aldate:
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
                                add_accident_presentation_aldate.setText(select_date);
                                myYear_accidentpresentation = year;
                                myMonth_accidentpresentation = monthOfYear;
                                myDay_accidentpresentation = dayOfMonth;
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
                                add_accident_presentation_aldate.setText(select_date);
                                myYear_accidentpresentation = year;
                                myMonth_accidentpresentation = monthOfYear;
                                myDay_accidentpresentation = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myYear_accidentpresentation, myMonth_accidentpresentation, myDay_accidentpresentation);
                    dialog_date.show();
                }

                break;
        }
    }
}
