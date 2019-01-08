package emergency_rescue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import home_content.AccidentLetters;
import utils.BaseActivity;

public class AccidentManagement extends BaseActivity {
    private ImageView accident_management_back;
    private RelativeLayout accident_management_rl1;
    private RelativeLayout accident_management_rl2;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_management);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        accident_management_back = (ImageView) findViewById(R.id.accident_management_back);
        accident_management_rl1 = (RelativeLayout) findViewById(R.id.accident_management_rl1);
        accident_management_rl2 = (RelativeLayout) findViewById(R.id.accident_management_rl2);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        accident_management_back.setOnClickListener(this);
        accident_management_rl1.setOnClickListener(this);
        accident_management_rl2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accident_management_back:
                finish();
                break;
            case R.id.accident_management_rl1:
                intent = new Intent(this, AccidentLetters.class);
                startActivity(intent);
                break;
            case R.id.accident_management_rl2:
//                intent = new Intent(this, FieldInvestigation.class);
//                startActivity(intent);
                break;
        }
    }
}
