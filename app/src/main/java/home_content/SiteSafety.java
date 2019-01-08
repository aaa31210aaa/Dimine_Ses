package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import site_safety.ClassMeetingRecord;
import site_safety.DangerousWorkPermit;
import site_safety.DownholeSafetyQueren;
import utils.BaseActivity;

public class SiteSafety extends BaseActivity {
    private ImageView site_safety_back;
    private RelativeLayout site_safety_rl1;
    private RelativeLayout site_safety_rl2;
    private RelativeLayout site_safety_rl3;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_safety);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        site_safety_back = (ImageView) findViewById(R.id.site_safety_back);
        site_safety_rl1 = (RelativeLayout) findViewById(R.id.site_safety_rl1);
        site_safety_rl2 = (RelativeLayout) findViewById(R.id.site_safety_rl2);
        site_safety_rl3 = (RelativeLayout) findViewById(R.id.site_safety_rl3);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        site_safety_back.setOnClickListener(this);
        site_safety_rl1.setOnClickListener(this);
        site_safety_rl2.setOnClickListener(this);
        site_safety_rl3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.site_safety_back:
                finish();
                break;
            case R.id.site_safety_rl1:
                intent = new Intent(this, ClassMeetingRecord.class);
                startActivity(intent);
                break;
            case R.id.site_safety_rl2:
                intent = new Intent(this, DangerousWorkPermit.class);
                startActivity(intent);
                break;
            case R.id.site_safety_rl3:
                intent = new Intent(this, DownholeSafetyQueren.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
