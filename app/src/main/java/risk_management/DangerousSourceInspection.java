package risk_management;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class DangerousSourceInspection extends BaseActivity {
    private ImageView dangerous_source_inspection_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous_source_inspection);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        dangerous_source_inspection_back = (ImageView) findViewById(R.id.dangerous_source_inspection_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        dangerous_source_inspection_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dangerous_source_inspection_back:
                finish();
                break;
            default:
                break;
        }
    }
}
