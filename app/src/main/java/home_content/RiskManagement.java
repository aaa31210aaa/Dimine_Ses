package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import enterprise_information.EnterpriseInformation;
import utils.BaseActivity;

public class RiskManagement extends BaseActivity {
    private ImageView risk_management_back;
    //    private RelativeLayout risk_management_rl1;
//    private RelativeLayout risk_management_rl2;
    //一般隐患管理
    private RelativeLayout risk_management_rl3;
    //重大隐患管理
    private RelativeLayout risk_management_rl4;
    //    private RelativeLayout risk_management_rl5;
    //一般隐患查看
    private RelativeLayout risk_management_rl6;
    //重大隐患查看
    private RelativeLayout risk_management_rl7;

    private Intent intent;
    private String intentIndex_yb;  //一般隐患标识
    private String intnetIndex_zd; //重大隐患标识
    //区分从首页进入还是隐患进入
    private String tag = "riskmanagement";
    //一般重大入参
    private String crtype = "";
    //区分添加按钮显示隐藏
    private String addTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_management);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        risk_management_back = (ImageView) findViewById(R.id.risk_management_back);
//        risk_management_rl1 = (RelativeLayout) findViewById(R.id.risk_management_rl1);
//        risk_management_rl2 = (RelativeLayout) findViewById(R.id.risk_management_rl2);
        risk_management_rl3 = (RelativeLayout) findViewById(R.id.risk_management_rl3);
        risk_management_rl4 = (RelativeLayout) findViewById(R.id.risk_management_rl4);
//        risk_management_rl5 = (RelativeLayout) findViewById(R.id.risk_management_rl5);
        risk_management_rl6 = (RelativeLayout) findViewById(R.id.risk_management_rl6);
        risk_management_rl7 = (RelativeLayout) findViewById(R.id.risk_management_rl7);
    }

    @Override
    protected void initData() {
        intentIndex_yb = "risk_yb";
        intnetIndex_zd = "risk_zd";
    }

    @Override
    protected void setOnClick() {
        risk_management_back.setOnClickListener(this);
//        risk_management_rl1.setOnClickListener(this);
//        risk_management_rl2.setOnClickListener(this);
        risk_management_rl3.setOnClickListener(this);
        risk_management_rl4.setOnClickListener(this);
//        risk_management_rl5.setOnClickListener(this);
        risk_management_rl6.setOnClickListener(this);
        risk_management_rl7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.risk_management_back:
                finish();
                break;
//            case R.id.risk_management_rl1:
//                intent = new Intent(this, AddDangerous.class);
//                startActivity(intent);
//                break;
//            case R.id.risk_management_rl2:
//                intent = new Intent(this, KeyTask.class);
//                startActivity(intent);
//                break;
            case R.id.risk_management_rl3:
//              //一般隐患登记跳到企业信息
                intent = new Intent(this, EnterpriseInformation.class);
                intent.putExtra("intentIndex", intentIndex_yb);
                intent.putExtra("crtype", crtype);
                addTag = "gl";
                intent.putExtra("addTag",addTag);
                //从隐患点进去
                intent.putExtra("tag", tag);
                startActivity(intent);
//                intent = new Intent(this, CommonlyRiskList.class);
//                intent.putExtra("clickId", SharedPrefsUtil.QYID);
//                startActivity(intent);
                break;
            case R.id.risk_management_rl4:
//                //重大隐患登记跳到企业信息
                intent = new Intent(this, EnterpriseInformation.class);
                intent.putExtra("intentIndex", intnetIndex_zd);
                intent.putExtra("crtype", crtype);
                addTag = "gl";
                intent.putExtra("addTag",addTag);
                //从隐患点进去
                intent.putExtra("tag", tag);
                startActivity(intent);
//                intent = new Intent(this, HiddenDangerRegistrationList.class);
//                intent.putExtra("clickId",SharedPrefsUtil.QYID);
//                startActivity(intent);

                break;
//            case R.id.risk_management_rl5:
//                //重大隐患复查
//                intent = new Intent(this, HiddenDangerReview.class);
//                startActivity(intent);
//                break;

            case R.id.risk_management_rl6:
                //一般隐患查看
                intent = new Intent(this, EnterpriseInformation.class);
                crtype = "YHLB001";
                intent.putExtra("intentIndex", intentIndex_yb);
                //从隐患点进去
                intent.putExtra("tag", tag);
                intent.putExtra("crtype", crtype);
                addTag = "ck";
                intent.putExtra("addTag",addTag);
                startActivity(intent);
                break;
            case R.id.risk_management_rl7:
                //重大隐患查看
                intent = new Intent(this, EnterpriseInformation.class);
                crtype = "YHLB002";
                intent.putExtra("intentIndex", intnetIndex_zd);
                //从隐患点进去
                intent.putExtra("tag", tag);
                intent.putExtra("crtype", crtype);
                addTag = "ck";
                intent.putExtra("addTag",addTag);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}