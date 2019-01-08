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

public class GaoduDetail extends BaseActivity {
    private ImageView gaodu_detai_back;
    private TabLayout gaodu_detail_viewpagertab;
    private ViewPager gaodu_detail_viewpager;

    //定义要装fragment的列
    private List<Fragment> list_fragment;
    //tab名称列表
    private List<String> list_title;
    //高毒基本卡fragment
    private GaoduJbk gaoduJbk;
    //救援卡fragment
    private GaoduJyk gaoduJyk;
    //告知卡
    private GaoduGzk gaoduGzk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaodu_detail);
        initView();
        initData();
        setOnClick();

    }

    @Override
    protected void initView() {
        gaodu_detai_back = (ImageView) findViewById(R.id.gaodu_detai_back);
        gaodu_detail_viewpagertab = (TabLayout) findViewById(R.id.gaodu_detail_viewpagertab);
        gaodu_detail_viewpager = (ViewPager) findViewById(R.id.gaodu_detail_viewpager);

        SetTablayout();
    }

    @Override
    protected void initData() {

    }


    private void SetTablayout() {
        gaoduJbk = new GaoduJbk();
        gaoduJyk = new GaoduJyk();
        gaoduGzk = new GaoduGzk();


        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(gaoduJbk);
        list_fragment.add(gaoduJyk);
        list_fragment.add(gaoduGzk);
        //将名称加载tab名字列表
        list_title = new ArrayList<>();
        list_title.add("基本卡");
        list_title.add("救援卡");
        list_title.add("告知卡");
        //设置TabLayout的模式
        gaodu_detail_viewpagertab.setTabMode(TabLayout.MODE_FIXED);
        gaodu_detail_viewpagertab.addTab(gaodu_detail_viewpagertab.newTab().setText(list_title.get(0)));
        gaodu_detail_viewpagertab.addTab(gaodu_detail_viewpagertab.newTab().setText(list_title.get(1)));
        gaodu_detail_viewpagertab.addTab(gaodu_detail_viewpagertab.newTab().setText(list_title.get(2)));
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), list_fragment, list_title);
        gaodu_detail_viewpager.setAdapter(adapter);
        gaodu_detail_viewpagertab.setupWithViewPager(gaodu_detail_viewpager);
    }


    @Override
    protected void setOnClick() {
        gaodu_detai_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gaodu_detai_back:
                finish();
                break;
        }
    }
}
