package security_check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.MonthlyCheck;
import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class SafetyCheckList extends BaseActivity {
    private ImageView safety_check_list_back;
    private RelativeLayout safety_check_rl1;
    private RelativeLayout safety_check_rl2;
    private RelativeLayout safety_check_rl3;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_check_list);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        safety_check_list_back = (ImageView) findViewById(R.id.safety_check_list_back);
        safety_check_rl1 = (RelativeLayout) findViewById(R.id.safety_check_rl1);
        safety_check_rl2 = (RelativeLayout) findViewById(R.id.safety_check_rl2);
        safety_check_rl3 = (RelativeLayout) findViewById(R.id.safety_check_rl3);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        safety_check_list_back.setOnClickListener(this);
        safety_check_rl1.setOnClickListener(this);
        safety_check_rl2.setOnClickListener(this);
        safety_check_rl3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safety_check_list_back:
                finish();
                break;
            case R.id.safety_check_rl1:
                intent = new Intent(this,OfficeCheck.class);
                startActivity(intent);
                break;
            case R.id.safety_check_rl2:
                intent = new Intent(this,SafetyOfGoods.class);
                startActivity(intent);
                break;
            case R.id.safety_check_rl3:
                intent = new Intent(this, MonthlyCheck.class);
                startActivity(intent);
                break;
        }
    }
}
