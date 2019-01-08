package employee_participation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class HiddenReport extends BaseActivity {
    private ImageView hidden_report_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_report);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_report_back = (ImageView) findViewById(R.id.hidden_report_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        hidden_report_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hidden_report_back:
                finish();
                break;
        }
    }
}
