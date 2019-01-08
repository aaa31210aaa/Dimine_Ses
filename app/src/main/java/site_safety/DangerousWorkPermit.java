package site_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class DangerousWorkPermit extends BaseActivity {
    private ImageView dangerous_work_permit_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous_work_permit);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        dangerous_work_permit_back = (ImageView) findViewById(R.id.dangerous_work_permit_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        dangerous_work_permit_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dangerous_work_permit_back:
                finish();
                break;
        }
    }
}
