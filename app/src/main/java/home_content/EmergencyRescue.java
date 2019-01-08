package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import emergency_rescue.AccidentManagement;
import emergency_rescue.EmergencyRescueManagement;
import utils.BaseActivity;

public class EmergencyRescue extends BaseActivity {
    private ImageView emergency_rescue_back;
    private RelativeLayout emergency_rescue_rl1;
    private RelativeLayout emergency_rescue_rl2;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_rescue);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        emergency_rescue_back = (ImageView) findViewById(R.id.emergency_rescue_back);
        emergency_rescue_rl1 = (RelativeLayout) findViewById(R.id.emergency_rescue_rl1);
        emergency_rescue_rl2 = (RelativeLayout) findViewById(R.id.emergency_rescue_rl2);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        emergency_rescue_back.setOnClickListener(this);
        emergency_rescue_rl1.setOnClickListener(this);
        emergency_rescue_rl2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emergency_rescue_back:
                finish();
                break;
            case R.id.emergency_rescue_rl1:
                intent = new Intent(this, AccidentManagement.class);
                startActivity(intent);
                break;
            case R.id.emergency_rescue_rl2:
                intent = new Intent(this, EmergencyRescueManagement.class);
                startActivity(intent);

                break;
        }
    }
}
