package com.example.administrator.dimine_sis;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import adapter.ViewPagerAdapter;
import tab.HomeFragment;
import tab.MineFragment;
import utils.BaseActivity;
import utils.CheckVersion;
import utils.PermissionUtil;
import utils.ShowToast;

public class MainActivity extends BaseActivity {
    private NoScrollViewPager main_viewpager;
    private TabLayout main_tablayout;

//    /**
//     * 主界面的viewpager
//     **/
//    private ViewPagerCompat mPager;
//    /**
//     * 所有fragment的集合
//     **/
//    private List<Fragment> fragments = new ArrayList<Fragment>();
//    /**
//     * 适配器
//     **/
//    private MyPagerAdapter adapter;

//    /**
//     * 当前所选中的栏目 默认首页(0-首页  1-我)
//     **/
//    private int current = 0;
    /**
     * 是否退出的的标识
     */
    private static boolean isExit = false;

    //    /**
//     * 判断是否需要检测，防止不停的弹框
//     */
//    private boolean isNeedCheck = true;
//    private static final int PERMISSON_REQUESTCODE = 0;
//
//    private LinearLayout ll_home;
//    //    private LinearLayout ll_news;
//    private LinearLayout ll_mine;
//    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 10;


    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 延迟发送退出
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            ShowToast.showShort(this, R.string.click_agin);
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
//        setPager();
        setOnClick();
    }

    @Override
    protected void initView() {
//        ll_home = (LinearLayout) findViewById(R.id.ll_home);
////        ll_news = (LinearLayout) findViewById(R.id.ll_news);
//        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
//        mPager = (ViewPagerCompat) findViewById(R.id.viewpager);
//        //禁止viewpager滑动
//        mPager.setViewTouchMode(true);
//        mPager.setOffscreenPageLimit(2);
//        ll_home.setSelected(true);
        main_viewpager = (NoScrollViewPager) findViewById(R.id.main_viewpager);
        main_tablayout = (TabLayout) findViewById(R.id.main_tablayout);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        //设置tab
        SetTab();

        AndPermission.with(this)
                .requestCode(200)
                .permission(PermissionUtil.WriteFilePermission)
                .send();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
//        UpdateAppUtils updateAppUtils = new UpdateAppUtils(this);
//        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, paramArrayOfInt, updateAppUtils.writelistener);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                //检查版本
                CheckVersion checkVersion = new CheckVersion();
                checkVersion.CheckVersions(MainActivity.this, TAG);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(MainActivity.this, "拒绝权限会导致某些功能不可用。");
            }
        }
    };



    /**
     * 设置Tab
     */
    private void SetTab() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MineFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"首页", "我的"});

        main_viewpager.setOffscreenPageLimit(1);

        main_viewpager.setAdapter(adapter);
        //关联图文
        main_tablayout.setupWithViewPager(main_viewpager);
        main_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                main_viewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < main_tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = main_tablayout.getTabAt(i);
            Drawable d = null;
            switch (i) {
                case 0:
                    d = ContextCompat.getDrawable(this, R.drawable.home_tab);
                    break;
                case 1:
                    d = ContextCompat.getDrawable(this, R.drawable.mine_tab);
                    break;
            }
            tab.setIcon(d);
        }
    }


    @Override
    protected void setOnClick() {
//        ll_home.setOnClickListener(this);
//        ll_news.setOnClickListener(this);
//        ll_mine.setOnClickListener(this);
    }

//    /**
//     * 跳转某个fragment
//     *
//     * @param which
//     */
//    private void setCurrent(int which) {
//        current = which;
//        mPager.setCurrentItem(current, false);
//    }


//    /**
//     * 设置viewpager的选项
//     */
//    private void setPager() {
//        fragments.add(new HomeFragment());
////        fragments.add(new NewsFragment());
//        fragments.add(new MineFragment());
//        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
//        mPager.setAdapter(adapter);
//        mPager.setCurrentItem(0);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.ll_home:
//                if (current == 0) break;
//                setCurrent(0);
//                ll_home.setSelected(true);
////                ll_news.setSelected(false);
//                ll_mine.setSelected(false);
//                break;
////            case R.id.ll_news:
////                if (current == 1) break;
////                setCurrent(1);
////                ll_home.setSelected(false);
////                ll_news.setSelected(true);
////                ll_mine.setSelected(false);
////                break;
//            case R.id.ll_mine:
//                if (current == 1) break;
//                setCurrent(1);
//                ll_home.setSelected(false);
////                ll_news.setSelected(false);
//                ll_mine.setSelected(true);
//                break;
        }
    }

}
