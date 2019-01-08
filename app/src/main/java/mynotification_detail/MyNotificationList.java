package mynotification_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import home_content.MyNotification;
import utils.BaseActivity;

public class MyNotificationList extends BaseActivity {
    private ImageView mynotificationlist_back;
    private RelativeLayout mynotificationlist_rl1;
    private RelativeLayout mynotificationlist_rl2;
    private RelativeLayout mynotificationlist_rl3;
    private RelativeLayout mynotificationlist_rl4;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_list);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        mynotificationlist_back = (ImageView) findViewById(R.id.mynotificationlist_back);
        mynotificationlist_rl1 = (RelativeLayout) findViewById(R.id.mynotificationlist_rl1);
        mynotificationlist_rl2 = (RelativeLayout) findViewById(R.id.mynotificationlist_rl2);
        mynotificationlist_rl3 = (RelativeLayout) findViewById(R.id.mynotificationlist_rl3);
        mynotificationlist_rl4 = (RelativeLayout) findViewById(R.id.mynotificationlist_rl4);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        mynotificationlist_back.setOnClickListener(this);
        mynotificationlist_rl1.setOnClickListener(this);
        mynotificationlist_rl2.setOnClickListener(this);
        mynotificationlist_rl3.setOnClickListener(this);
        mynotificationlist_rl4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mynotificationlist_back:
                finish();
                break;
            case R.id.mynotificationlist_rl1:
                intent = new Intent(this, MyNotification.class);
                startActivity(intent);
                break;
            case R.id.mynotificationlist_rl2:
                break;
            case R.id.mynotificationlist_rl3:
                break;
            case R.id.mynotificationlist_rl4:
                break;
        }
    }
}
