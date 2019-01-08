package home_content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.dimine_sis.R;

import utils.BaseActivity;
import wfp_query.Dangerous;
import wfp_query.Gaodu;
import wfp_query.Laws;
import wfp_query.WxyLjl;
import wfp_query.Zywh;

public class WeihuapinCx extends BaseActivity {
    private ImageView weihuapin_back;
    private RelativeLayout weihuapin_flfg;
    private RelativeLayout weihuapin_whp;
    private RelativeLayout weihuapin_ljl;
    private RelativeLayout weihuapin_gdwp;
    private RelativeLayout weihuapin_zywh;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weihuapin_cx);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        weihuapin_back = (ImageView) findViewById(R.id.weihuapin_back);
        weihuapin_flfg = (RelativeLayout) findViewById(R.id.weihuapin_flfg);
        weihuapin_whp = (RelativeLayout) findViewById(R.id.weihuapin_whp);
        weihuapin_ljl = (RelativeLayout) findViewById(R.id.weihuapin_ljl);
        weihuapin_gdwp = (RelativeLayout) findViewById(R.id.weihuapin_gdwp);
        weihuapin_zywh = (RelativeLayout) findViewById(R.id.weihuapin_zywh);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
        weihuapin_back.setOnClickListener(this);
        weihuapin_flfg.setOnClickListener(this);
        weihuapin_whp.setOnClickListener(this);
        weihuapin_ljl.setOnClickListener(this);
        weihuapin_gdwp.setOnClickListener(this);
        weihuapin_zywh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weihuapin_back:
                finish();
                break;
            case R.id.weihuapin_flfg:
                intent = new Intent(WeihuapinCx.this, Laws.class);
                startActivity(intent);
                break;
            case R.id.weihuapin_whp:
                intent = new Intent(WeihuapinCx.this, Dangerous.class);
                startActivity(intent);
                break;
            case R.id.weihuapin_ljl:
                intent = new Intent(WeihuapinCx.this, WxyLjl.class);
                startActivity(intent);
                break;
            case R.id.weihuapin_gdwp:
                intent = new Intent(WeihuapinCx.this, Gaodu.class);
                startActivity(intent);
                break;
            case R.id.weihuapin_zywh:
                intent = new Intent(WeihuapinCx.this, Zywh.class);
                startActivity(intent);
                break;
        }
    }
}
