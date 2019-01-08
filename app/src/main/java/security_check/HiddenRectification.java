package security_check;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class HiddenRectification extends BaseActivity {
    private ImageView hidden_rectification_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_rectification);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        hidden_rectification_back = (ImageView) findViewById(R.id.hidden_rectification_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        hidden_rectification_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hidden_rectification_back:
                finish();
                break;
        }
    }
}
