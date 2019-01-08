package emergency_rescue;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class EmergencyRescueManagement extends BaseActivity {
    private ImageView emergency_rescue_management_back;
    private RelativeLayout emergency_rescue_management_rl1;
    private RelativeLayout emergency_rescue_management_rl2;
    private RelativeLayout emergency_rescue_management_rl3;
    private RelativeLayout emergency_rescue_management_rl4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_rescue_management);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        emergency_rescue_management_back = (ImageView) findViewById(R.id.emergency_rescue_management_back);
        emergency_rescue_management_rl1 = (RelativeLayout) findViewById(R.id.emergency_rescue_management_rl1);
        emergency_rescue_management_rl2 = (RelativeLayout) findViewById(R.id.emergency_rescue_management_rl2);
        emergency_rescue_management_rl3 = (RelativeLayout) findViewById(R.id.emergency_rescue_management_rl3);
        emergency_rescue_management_rl4 = (RelativeLayout) findViewById(R.id.emergency_rescue_management_rl4);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        emergency_rescue_management_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_rescue_management_back:
                finish();
                break;
        }
    }
}
