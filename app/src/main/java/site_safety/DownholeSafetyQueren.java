package site_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class DownholeSafetyQueren extends BaseActivity {
    private ImageView downhole_safety_queren_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downhole_safety_queren);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        downhole_safety_queren_back = (ImageView) findViewById(R.id.downhole_safety_queren_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        downhole_safety_queren_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.downhole_safety_queren_back:
                finish();
                break;
        }
    }
}
