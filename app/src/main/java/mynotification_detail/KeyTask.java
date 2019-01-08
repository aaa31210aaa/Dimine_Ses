package mynotification_detail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class KeyTask extends BaseActivity {
    private ImageView key_task_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_task);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        key_task_back = (ImageView) findViewById(R.id.key_task_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        key_task_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.key_task_back:
                finish();
                break;
        }
    }
}
