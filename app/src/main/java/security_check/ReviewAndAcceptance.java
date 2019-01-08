package security_check;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class ReviewAndAcceptance extends BaseActivity {
    private ImageView review_and_acceptance_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_acceptance);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        review_and_acceptance_back = (ImageView) findViewById(R.id.review_and_acceptance_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        review_and_acceptance_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.review_and_acceptance_back:
                finish();
                break;
        }
    }
}
