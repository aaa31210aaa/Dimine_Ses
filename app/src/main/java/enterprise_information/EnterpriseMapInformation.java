package enterprise_information;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import home_content.AqJyPx;
import home_content.AqScTr;
import home_content.BmGl;
import home_content.QyZz;
import home_content.RyGl;
import home_content.TzSb;
import home_content.YjWz;
import home_content.YjYaXx;
import home_content.YjYl;
import home_content.ZyWsXx;
import okhttp3.Call;
import utils.BaseActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

public class EnterpriseMapInformation extends BaseActivity {
    private ImageView enterprise_information_back;
    //查看地图
    private TextView map_information_see;
    //企业名称
    private TextView map_information_companyname;
    //成立时间
    private TextView map_information_establishmenttime;
    //法定代表人
    private TextView map_information_legalrepresentative;
    //证件代码
    private TextView map_information_documentcode;
    //经济类型
    private TextView map_information_economictype;
    //主要行业领域
    private TextView map_information_majorindustryareas;
    //主管部门
    private TextView map_information_competentdepartment;
    //员工人数
    private TextView map_information_numberofemployees;
    //注册资金
    private TextView map_information_registeredcapital;
    //企业地址
    private TextView map_information_enterpriseaddress;
    //风险级别
    private TextView map_information_risklevel;
    //企业评分
    private TextView map_information_score;

    //危险源
    private BGABadgeTextView map_information_wxy;
    //隐患信息
    private BGABadgeTextView map_information_yhxx;
    //安全生产信息
    private BGABadgeTextView map_information_scxx;
    //规章制度
    private BGABadgeTextView map_information_gzzd;
    //职业卫生信息
    private BGABadgeTextView map_information_zywsxx;
    //应急救援信息
    private BGABadgeTextView map_information_yjjyxx;
    //安全教育培训
    private BGABadgeTextView map_information_aqjypx;
    //安全生产投入
    private BGABadgeTextView map_information_aqsctr;

    //安全机构部门管理
    private BGABadgeTextView map_information_bmgl;
    //安全人员管理
    private BGABadgeTextView map_information_rygl;

    //特种设备
    private BGABadgeTextView map_information_tzsb;
    //应急物资
    private BGABadgeTextView map_information_yjwz;
    //应急演练
    private BGABadgeTextView map_information_yjyl;
    //企业证照
    private BGABadgeTextView map_information_qyzz;

    private String url;
    private String user_token;
    private int index = 0;

    private Intent intent;
    //点击企业的id
    private String clickId;
    private String mqyid;
    private double qyLat;
    private double qyLng;

    //接收哪个页面传过来的值
//    private String ymindex;
    private String mycode = "emap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_map_information);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        initStatusBarColor(ContextCompat.getColor(this, R.color.blue_deep));
        enterprise_information_back = (ImageView) findViewById(R.id.enterprise_information_back);
        map_information_see = (TextView) findViewById(R.id.map_information_see);
        map_information_companyname = (TextView) findViewById(R.id.map_information_companyname);
        map_information_establishmenttime = (TextView) findViewById(R.id.map_information_establishmenttime);
        map_information_legalrepresentative = (TextView) findViewById(R.id.map_information_legalrepresentative);
        map_information_documentcode = (TextView) findViewById(R.id.map_information_documentcode);
        map_information_economictype = (TextView) findViewById(R.id.map_information_economictype);
        map_information_majorindustryareas = (TextView) findViewById(R.id.map_information_majorindustryareas);
        map_information_competentdepartment = (TextView) findViewById(R.id.map_information_competentdepartment);
        map_information_numberofemployees = (TextView) findViewById(R.id.map_information_numberofemployees);
        map_information_registeredcapital = (TextView) findViewById(R.id.map_information_registeredcapital);
        map_information_enterpriseaddress = (TextView) findViewById(R.id.map_information_enterpriseaddress);
        map_information_risklevel = (TextView) findViewById(R.id.map_information_risklevel);
        map_information_score = (TextView) findViewById(R.id.map_information_score);

        map_information_wxy = (BGABadgeTextView) findViewById(R.id.map_information_wxy);
        map_information_yhxx = (BGABadgeTextView) findViewById(R.id.map_information_yhxx);
        map_information_scxx = (BGABadgeTextView) findViewById(R.id.map_information_scxx);
        map_information_gzzd = (BGABadgeTextView) findViewById(R.id.map_information_gzzd);

        map_information_zywsxx = (BGABadgeTextView) findViewById(R.id.map_information_zywsxx);
        map_information_yjjyxx = (BGABadgeTextView) findViewById(R.id.map_information_yjjyxx);
        map_information_aqjypx = (BGABadgeTextView) findViewById(R.id.map_information_aqjypx);
        map_information_aqsctr = (BGABadgeTextView) findViewById(R.id.map_information_aqsctr);

        map_information_bmgl = (BGABadgeTextView) findViewById(R.id.map_information_bmgl);
        map_information_rygl = (BGABadgeTextView) findViewById(R.id.map_information_rygl);
        map_information_tzsb = (BGABadgeTextView) findViewById(R.id.map_information_tzsb);
        map_information_yjwz = (BGABadgeTextView) findViewById(R.id.map_information_yjwz);
        map_information_yjyl = (BGABadgeTextView) findViewById(R.id.map_information_yjyl);
        map_information_qyzz = (BGABadgeTextView) findViewById(R.id.map_information_qyzz);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 设置状态栏颜色
     */
    protected void initStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void initData() {
        url = PortIpAddress.CompanyDetailInfo();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);

        //获得传过来的企业id
        intent = getIntent();
        clickId = intent.getStringExtra("clickId");
//        qyLat = intent.getDoubleExtra("qyLat", 0);
//        qyLng = intent.getDoubleExtra("qyLng", 0);
//        ymindex = intent.getStringExtra("ymindex");

        dialog = DialogUtil.createLoadingDialog(this, R.string.loading);
        mOkhttp();
    }

    private void mOkhttp() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("qyid", clickId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showToastNoWait(EnterpriseMapInformation.this, R.string.network_error);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.e(TAG, response + "");
                            JSONObject jsb = new JSONObject(response);
                            JSONArray jsar = jsb.getJSONArray("cells");
                            //赋值
                            map_information_companyname.setText(jsar.getJSONObject(index).getString("comname"));
                            map_information_establishmenttime.setText(jsar.getJSONObject(index).getString("setupdate"));
                            map_information_legalrepresentative.setText(jsar.getJSONObject(index).getString("legalperson"));
                            map_information_documentcode.setText(jsar.getJSONObject(index).getString("certificates"));
                            map_information_economictype.setText(jsar.getJSONObject(index).getString("jytypename"));
                            map_information_majorindustryareas.setText(jsar.getJSONObject(index).getString("mainfield"));
                            map_information_competentdepartment.setText(jsar.getJSONObject(index).getString("chargedeptname"));
                            map_information_numberofemployees.setText(jsar.getJSONObject(index).getString("staffnum"));
                            map_information_registeredcapital.setText(jsar.getJSONObject(index).getString("regcapital"));
                            map_information_enterpriseaddress.setText(jsar.getJSONObject(index).getString("zcaddress"));
                            map_information_risklevel.setText(jsar.getJSONObject(index).getString("risktypename"));
                            map_information_score.setText(jsar.getJSONObject(index).getString("riskscore"));
                            if (!jsar.getJSONObject(index).getString("longitudecoord").equals("") && !jsar.getJSONObject(index).getString("latitudecoord").equals("")) {
                                qyLng = Double.parseDouble(jsar.getJSONObject(index).getString("longitudecoord"));
                                qyLat = Double.parseDouble(jsar.getJSONObject(index).getString("latitudecoord"));
                            } else {
                                qyLng = 0;
                                qyLat = 0;
                            }


                            mqyid = jsar.getJSONObject(index).getString("qyid");

                            if (jsar.getJSONObject(index).getString("wxyl").equals("1")) {
                                map_information_wxy.showCirclePointBadge();
                            } else {
                                map_information_wxy.hiddenBadge();
                            }

                            if (jsar.getJSONObject(index).getString("yhzg").equals("1")) {
                                map_information_yhxx.showCirclePointBadge();
                            } else {
                                map_information_yhxx.hiddenBadge();
                            }

                            if (jsar.getJSONObject(index).getString("aqjc").equals("1")) {
                                map_information_scxx.showCirclePointBadge();
                            } else {
                                map_information_scxx.hiddenBadge();
                            }

                            if (jsar.getJSONObject(index).getString("gzzd").equals("1")) {
                                map_information_gzzd.showCirclePointBadge();
                            } else {
                                map_information_gzzd.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("zyws").equals("1")) {
                                map_information_zywsxx.showCirclePointBadge();
                            } else {
                                map_information_zywsxx.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("yjya").equals("1")) {
                                map_information_yjjyxx.showCirclePointBadge();
                            } else {
                                map_information_yjjyxx.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("aqpx").equals("1")) {
                                map_information_aqjypx.showCirclePointBadge();
                            } else {
                                map_information_aqjypx.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("aqtr").equals("1")) {
                                map_information_aqsctr.showCirclePointBadge();
                            } else {
                                map_information_aqsctr.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("aqjg").equals("1")) {
                                map_information_bmgl.showCirclePointBadge();
                            } else {
                                map_information_bmgl.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("rygl").equals("1")) {
                                map_information_rygl.showCirclePointBadge();
                            } else {
                                map_information_rygl.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("sbss").equals("1")) {
                                map_information_tzsb.showCirclePointBadge();
                            } else {
                                map_information_tzsb.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("yjwz").equals("1")) {
                                map_information_yjwz.showCirclePointBadge();
                            } else {
                                map_information_yjwz.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("yjyl").equals("1")) {
                                map_information_yjyl.showCirclePointBadge();
                            } else {
                                map_information_yjyl.hiddenBadge();
                            }
                            if (jsar.getJSONObject(index).getString("zyzz").equals("1")) {
                                map_information_qyzz.showCirclePointBadge();
                            } else {
                                map_information_qyzz.hiddenBadge();
                            }

                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setOnClick() {
        enterprise_information_back.setOnClickListener(this);
        map_information_see.setOnClickListener(this);
        map_information_wxy.setOnClickListener(this);
        map_information_yhxx.setOnClickListener(this);
        map_information_scxx.setOnClickListener(this);
        map_information_gzzd.setOnClickListener(this);
        map_information_zywsxx.setOnClickListener(this);
        map_information_yjjyxx.setOnClickListener(this);
        map_information_aqjypx.setOnClickListener(this);
        map_information_aqsctr.setOnClickListener(this);
        map_information_bmgl.setOnClickListener(this);
        map_information_rygl.setOnClickListener(this);
        map_information_tzsb.setOnClickListener(this);
        map_information_yjwz.setOnClickListener(this);
        map_information_yjyl.setOnClickListener(this);
        map_information_qyzz.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enterprise_information_back:
                finish();
                break;
            case R.id.map_information_see:
                intent = new Intent(this, QyMap.class);
                intent.putExtra("myqyLat", qyLat);
                intent.putExtra("myqyLng", qyLng);
                intent.putExtra("qyname", map_information_companyname.getText().toString().trim());
//                intent.putExtra("myymindex", ymindex);
                startActivity(intent);
                break;
            case R.id.map_information_wxy:
                intent = new Intent(this, WeiXianYuan.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_yhxx:
                intent = new Intent(this, QyYhInfomation.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_scxx:
                intent = new Intent(this, ScInfomation.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_gzzd:
                intent = new Intent(this, Rules.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_zywsxx:
                intent = new Intent(this, ZyWsXx.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_yjjyxx:
                intent = new Intent(this, YjYaXx.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_aqjypx:
                intent = new Intent(this, AqJyPx.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_aqsctr:
                intent = new Intent(this, AqScTr.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_bmgl:
                intent = new Intent(this, BmGl.class);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_rygl:
                intent = new Intent(this, RyGl.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_tzsb:
                intent = new Intent(this, TzSb.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_yjwz:
                intent = new Intent(this, YjWz.class);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_yjyl:
                intent = new Intent(this, YjYl.class);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;
            case R.id.map_information_qyzz:
                intent = new Intent(this, QyZz.class);
                intent.putExtra("mycode", mycode);
                intent.putExtra("mClickId", mqyid);
                startActivity(intent);
                break;

        }
    }
}
