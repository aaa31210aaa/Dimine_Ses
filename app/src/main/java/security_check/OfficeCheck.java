package security_check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class OfficeCheck extends BaseActivity {
    private ImageView office_check_back;
    private TextView office_check_dj1;
    private TextView office_check_dj2;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_check);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        office_check_back = (ImageView) findViewById(R.id.office_check_back);
        office_check_dj1 = (TextView) findViewById(R.id.office_check_dj1);
        office_check_dj2 = (TextView) findViewById(R.id.office_check_dj2);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        office_check_back.setOnClickListener(this);
        office_check_dj1.setOnClickListener(this);
        office_check_dj2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.office_check_back:
                finish();
                break;
            case R.id.office_check_dj1:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
            case R.id.office_check_dj2:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
        }
    }
}
