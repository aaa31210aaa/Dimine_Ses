package site_safety;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class ClassMeetingRecord extends BaseActivity {
    private ImageView class_meeting_record_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_meeting_record);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        class_meeting_record_back = (ImageView) findViewById(R.id.class_meeting_record_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        class_meeting_record_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.class_meeting_record_back:
                finish();
                break;
        }
    }
}
