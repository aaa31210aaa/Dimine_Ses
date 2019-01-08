package security_check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;

public class SafetyOfGoods extends BaseActivity {
    private ImageView safety_of_goods_back;
    private TextView safety_of_goods_dj1;
    private TextView safety_of_goods_dj2;
    private TextView safety_of_goods_dj3;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_of_goods);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        safety_of_goods_back = (ImageView) findViewById(R.id.safety_of_goods_back);
        safety_of_goods_dj1 = (TextView) findViewById(R.id.safety_of_goods_dj1);
        safety_of_goods_dj2 = (TextView) findViewById(R.id.safety_of_goods_dj2);
        safety_of_goods_dj3 = (TextView) findViewById(R.id.safety_of_goods_dj3);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        safety_of_goods_back.setOnClickListener(this);
        safety_of_goods_dj1.setOnClickListener(this);
        safety_of_goods_dj2.setOnClickListener(this);
        safety_of_goods_dj3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safety_of_goods_back:
                finish();
                break;
            case R.id.safety_of_goods_dj1:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);

                break;
            case R.id.safety_of_goods_dj2:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;
            case R.id.safety_of_goods_dj3:
                intent = new Intent(this, HiddenTroubleRegistration.class);
                startActivity(intent);
                break;


        }
    }
}
