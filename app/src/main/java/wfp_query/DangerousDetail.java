package wfp_query;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SimpleFragmentPagerAdapter;
import utils.BaseActivity;

public class DangerousDetail extends BaseActivity {
    private ImageView dangerous_detail_back;
    private TabLayout dangerous_detail_viewpagertab;
    private ViewPager dangerous_detail_viewpager;
    //定义要装fragment的列
    private List<Fragment> list_fragment;
    //tab名称列表
    private List<String> list_title;
    //危险性概述
    private WhxGs wxxGs;
    //化学反应
    private HxFy hxFy;
    //理化特性
    private LhTx lhTx;
    //接触控制
    private JcKz jcKz;
    //运输/废弃
    private YsFq ysFq;
    //应急处理
    private YjCl yjCl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerous_detail);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        dangerous_detail_back = (ImageView) findViewById(R.id.dangerous_detail_back);
        dangerous_detail_viewpagertab = (TabLayout) findViewById(R.id.dangerous_detail_viewpagertab);
        dangerous_detail_viewpager = (ViewPager) findViewById(R.id.dangerous_detail_viewpager);

    }

    @Override
    protected void initData() {
        SetTablayout();
    }

    private void SetTablayout() {
        wxxGs = new WhxGs();
        hxFy = new HxFy();
        lhTx = new LhTx();
        jcKz = new JcKz();
        ysFq = new YsFq();
        yjCl = new YjCl();
        //将名称加载tab名字列表
        list_title = new ArrayList<>();
        list_title.add("危险性概述");
        list_title.add("化学反应");
        list_title.add("理化特性");
        list_title.add("接触控制");
        list_title.add("运输/废弃");
        list_title.add("应急处理");

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(wxxGs);
        list_fragment.add(hxFy);
        list_fragment.add(lhTx);
        list_fragment.add(jcKz);
        list_fragment.add(ysFq);
        list_fragment.add(yjCl);

        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(0)));
        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(1)));
        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(2)));
        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(3)));
        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(4)));
        dangerous_detail_viewpagertab.addTab(dangerous_detail_viewpagertab.newTab().setText(list_title.get(5)));

        dangerous_detail_viewpager.setOffscreenPageLimit(list_fragment.size() - 1);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), list_fragment, list_title);
        dangerous_detail_viewpager.setAdapter(adapter);
        dangerous_detail_viewpagertab.setupWithViewPager(dangerous_detail_viewpager);
    }


    @Override
    protected void setOnClick() {
        dangerous_detail_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dangerous_detail_back:
                finish();
                break;
        }
    }
}
