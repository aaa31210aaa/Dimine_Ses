package home_content;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class AccidentLetters extends BaseActivity {
    private ImageView accident_letters_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_letters);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        accident_letters_back = (ImageView) findViewById(R.id.accident_letters_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        accident_letters_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accident_letters_back:
                finish();
                break;
        }
    }
}
