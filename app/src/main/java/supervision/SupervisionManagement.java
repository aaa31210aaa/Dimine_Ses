package supervision;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class SupervisionManagement extends BaseActivity {
    private ImageView supervision_management_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision_management);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        supervision_management_back = (ImageView) findViewById(R.id.supervision_management_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        supervision_management_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supervision_management_back:
                finish();
                break;

        }
    }
}
