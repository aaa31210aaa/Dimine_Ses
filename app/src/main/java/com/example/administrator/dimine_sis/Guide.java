package com.example.administrator.dimine_sis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.GuidePageAdapter;
import utils.BaseActivity;
import utils.SharedPrefsUtil;
import utils.StatusBarUtils;

public class Guide extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager guide_viewpager;
    private GuidePageAdapter adapter;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;

    //最后一页的按钮
    private ImageView guide_ib_start;
    private String isFirst;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        isFirst = SharedPrefsUtil.getValue(this, "userInfo", "first", null);
        //判断程序与第几次运行，如果不是第一次运行则跳转到登陆页面
        if (isFirst != null) {
            Intent intent = new Intent(this, Welcome.class);
            startActivity(intent);
            finish();
        } else {
            //加载ViewPager
            initViewPager();
            //加载底部圆点
            initPoint();
            guide_ib_start = (ImageView) findViewById(R.id.guide_ib_start);
            guide_ib_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Guide.this, Welcome.class);
                    startActivity(intent);
                    SharedPrefsUtil.putValue(Guide.this, "userInfo", "first", "noFirst");
                    finish();
                }
            });
        }
    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = (ViewGroup) findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            iv_point = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(10, 10, 10, 10);
            iv_point.setLayoutParams(layoutParams);
            iv_point.setPadding(30, 0, 30, 0);

            ivPointArray[i] = iv_point;
            //第一个页面设置为选中状态
            if (i == 0) {
                iv_point.setBackgroundResource(R.drawable.ic_page_indicator_focused);
            } else {
                iv_point.setBackgroundResource(R.drawable.ic_page_indicator);
            }
            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        StatusBarUtils.transparencyBar(this);
        StatusBarUtils.StatusBarLightMode(this);
        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        //实例化图片资源
        imageIdArray = new int[]{R.drawable.yd2, R.drawable.yd1, R.drawable.yd2};
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        for (int i = 0; i < imageIdArray.length; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);

            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        guide_viewpager.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        guide_viewpager.addOnPageChangeListener(this);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void setOnClick() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = imageIdArray.length;
        for (int i = 0; i < length; i++) {
            ivPointArray[position].setBackgroundResource(R.drawable.ic_page_indicator_focused);
            if (position != i) {
                ivPointArray[i].setBackgroundResource(R.drawable.ic_page_indicator);
            }
        }

        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1) {
            animation = AnimationUtils.loadAnimation(this, R.anim.fade);
            guide_ib_start.startAnimation(animation);
            guide_ib_start.setVisibility(View.VISIBLE);
        } else {
            guide_ib_start.clearAnimation();
            guide_ib_start.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
