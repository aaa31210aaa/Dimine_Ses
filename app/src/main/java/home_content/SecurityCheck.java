package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import security_check.HiddenRectification;
import security_check.HiddenTroubleRegistration;
import security_check.ReviewAndAcceptance;
import security_check.SafetyCheckList;
import utils.BaseActivity;

public class SecurityCheck extends BaseActivity {
    private ImageView security_check_back;
    private RelativeLayout safety_check_list_rl;
    private RelativeLayout hidden_trouble_registration_rl;
    private RelativeLayout hidden_rectification_rl;
    private RelativeLayout review_and_acceptance_rl;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        security_check_back = (ImageView) findViewById(R.id.security_check_back);
        safety_check_list_rl = (RelativeLayout) findViewById(R.id.safety_check_list_rl);
        hidden_trouble_registration_rl = (RelativeLayout) findViewById(R.id.hidden_trouble_registration_rl);
        hidden_rectification_rl = (RelativeLayout) findViewById(R.id.hidden_rectification_rl);
        review_and_acceptance_rl = (RelativeLayout) findViewById(R.id.review_and_acceptance_rl);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        security_check_back.setOnClickListener(this);
        safety_check_list_rl.setOnClickListener(this);
        hidden_trouble_registration_rl.setOnClickListener(this);
        hidden_rectification_rl.setOnClickListener(this);
        review_and_acceptance_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_check_back:
                finish();
                break;
            case R.id.safety_check_list_rl:
                intent = new Intent(this, SafetyCheckList.class);
                startActivity(intent);
                break;
            case R.id.hidden_trouble_registration_rl:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
            case R.id.hidden_rectification_rl:
                intent = new Intent(this, HiddenRectification.class);
                startActivity(intent);
                break;
            case R.id.review_and_acceptance_rl:
                intent = new Intent(this, ReviewAndAcceptance.class);
                startActivity(intent);
                break;
            default:
                finish();
                break;
        }
    }
}
