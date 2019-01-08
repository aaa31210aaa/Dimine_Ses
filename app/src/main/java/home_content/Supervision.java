package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import supervision.SupervisionManagement;
import utils.BaseActivity;

public class Supervision extends BaseActivity {
    private ImageView supervision_back;
    private RelativeLayout supervision_rl1;
    private RelativeLayout supervision_rl2;
    private RelativeLayout supervision_rl3;
    private RelativeLayout supervision_rl4;
    private RelativeLayout supervision_rl5;
    private RelativeLayout supervision_rl6;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        supervision_back = (ImageView) findViewById(R.id.supervision_back);
        supervision_rl1 = (RelativeLayout) findViewById(R.id.supervision_rl1);
        supervision_rl2 = (RelativeLayout) findViewById(R.id.supervision_rl2);
        supervision_rl3 = (RelativeLayout) findViewById(R.id.supervision_rl3);
        supervision_rl4 = (RelativeLayout) findViewById(R.id.supervision_rl4);
        supervision_rl5 = (RelativeLayout) findViewById(R.id.supervision_rl5);
        supervision_rl6 = (RelativeLayout) findViewById(R.id.supervision_rl6);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        supervision_back.setOnClickListener(this);
        supervision_rl1.setOnClickListener(this);
        supervision_rl2.setOnClickListener(this);
        supervision_rl3.setOnClickListener(this);
        supervision_rl4.setOnClickListener(this);
        supervision_rl5.setOnClickListener(this);
        supervision_rl6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supervision_back:
                finish();
                break;
//            case R.id.employee_participation_rl:
//                Intent intent = new Intent(this, HiddenReport.class);
//                startActivity(intent);
//                break;
            case R.id.supervision_rl1:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);

                break;
            case R.id.supervision_rl2:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);
                break;
            case R.id.supervision_rl3:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);
                break;
            case R.id.supervision_rl4:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);
                break;
            case R.id.supervision_rl5:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);
                break;
            case R.id.supervision_rl6:
                intent = new Intent(this, SupervisionManagement.class);
                startActivity(intent);
                break;
        }
    }
}