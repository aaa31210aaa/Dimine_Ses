package tab;


import android.app.DownloadManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.example.administrator.dimine_sis.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import adapter.HomeRecyclerAdapter;
import bean.HomeBean;
import bean.MessageEvent;
import data_report.DataReport;
import enterprise_information.EnterpriseInformation;
import home_content.AccidentPresentation;
import home_content.AqJyPx;
import home_content.MyNotification;
import home_content.QyZz;
import home_content.RiskManagement;
import home_content.RiskMonitoring;
import home_content.RyGl;
import home_content.TzSb;
import home_content.WeihuapinCx;
import home_content.YjYaXx;
import home_content.ZyWsXx;
import utils.BaseFragment;
import utils.GridItemDecoration;
import utils.LocalImageHolderView;
import utils.PortIpAddress;
import wfp_query.Laws;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    //    private Banner home_banner;
//    private List<Integer> images;
//    private GridView gridview;
//    private String[] imageUrls;
//    private ArrayList<String> imgList;

    private ConvenientBanner banner;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();

    private ArrayList<String> transformerList = new ArrayList<String>();

    private Class cls;
    private ABaseTransformer transforemer;
    private String transforemerName;

    private List<String> networkImages;
    private String[] imageUrls;

    private RecyclerView recyclerView;
    private ArrayList<HomeBean> mDates;  //文字集合
    private ArrayList<HomeBean> mImages; //图片集合
    private HomeRecyclerAdapter adapter;
    private int[] imgs = {R.drawable.ic_sitesafe, R.drawable.ic_news, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.accident_letters, R.drawable.ic_risk, R.drawable.weihuapin, R.drawable.home_map};
    private int[] imgs_qy = {R.drawable.ic_news, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.accident_letters, R.drawable.weihuapin, R.drawable.flfg};

    private String[] tvs = {"企业信息", "通知公告", "隐患管理", "安全检查", "事故快报", "数据统计", "危化品查询", "风险监控"};
    private String[] tvs_qy = {"通知公告", "隐患管理", "自检自查", "事故快报", "危化品查询", "法律法规"};

    private Class<?>[] ACTIVITY = {EnterpriseInformation.class, MyNotification.class, RiskManagement.class, EnterpriseInformation.class, AccidentPresentation.class,
            DataReport.class, WeihuapinCx.class, RiskMonitoring.class};
    private Class<?>[] ACTIVITY_QY = {MyNotification.class, RiskManagement.class, EnterpriseInformation.class, AccidentPresentation.class, WeihuapinCx.class, Laws.class};

    private int num = 3;
    private int tvs_length = tvs_qy.length;
    private int imgs_length = imgs_qy.length;
    private String[] mTv = tvs_qy;
    private int[] mImg = imgs_qy;


//    private int[] imgs = {R.drawable.ic_news, R.drawable.hidden_management, R.drawable.law_enforcement, R.drawable.accident_letters, R.drawable.weihuapin, R.drawable.flfg};
//    private String[] tvs = {"通知公告", "隐患管理", "自检自查", "事故快报", "危化品查询", "法律法规"};

    //    private LinearLayout home_wxy;
//    private LinearLayout home_yhxx;
//    private LinearLayout home_scxx;
//    private LinearLayout home_gzzd;
    private LinearLayout home_zywsxx;
    private LinearLayout home_yjjyxx;
    private LinearLayout home_aqjypx;
//    private LinearLayout home_aqsctr;

    private LinearLayout home_rygl;
    private LinearLayout home_tzsb;
    private LinearLayout home_qyzz;

//    private TextView hometest;

    //    public static TextView testdownload;
//    private TextView canceldown;
    private DownloadManager.Request request;
    private DownloadManager manager;
    private long downloadId;


    private Intent intent;
    public String intentIndex;
    private String tag = "home";
    private String mycode = "home";


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_home, null);
            initView();
            setOnclick();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    private void initView() {
        banner = (ConvenientBanner) view.findViewById(R.id.banner);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerview);
        if (PortIpAddress.GetUserType(getActivity())) {
            num = 4;
            tvs_length = tvs.length;
            imgs_length = imgs.length;
            mTv =tvs;
            mImg = imgs;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), num, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridItemDecoration(getActivity()));
        //如果可以确定每个item的高度是固定的...设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

//        home_wxy = (LinearLayout) view.findViewById(R.id.home_wxy);
//        home_yhxx = (LinearLayout) view.findViewById(R.id.home_yhxx);
//        home_scxx = (LinearLayout) view.findViewById(R.id.home_scxx);
//        home_gzzd = (LinearLayout) view.findViewById(R.id.home_gzzd);
        home_zywsxx = (LinearLayout) view.findViewById(R.id.home_zywsxx);
        home_yjjyxx = (LinearLayout) view.findViewById(R.id.home_yjjyxx);
        home_aqjypx = (LinearLayout) view.findViewById(R.id.home_aqjypx);
//        home_aqsctr = (LinearLayout) view.findViewById(R.id.home_aqsctr);
//        testdownload = (TextView) view.findViewById(R.id.testdownload);
//        canceldown = (TextView) view.findViewById(R.id.canceldown);
        home_rygl = (LinearLayout) view.findViewById(R.id.home_rygl);
        home_tzsb = (LinearLayout) view.findViewById(R.id.home_tzsb);
        home_qyzz = (LinearLayout) view.findViewById(R.id.home_qyzz);

//        hometest = (TextView) view.findViewById(R.id.hometest);

        //注册
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HomeEvent(MessageEvent messageEvent) {
//        testdownload.setText(messageEvent.getMessage());
    }


    @Override
    protected void loadData() {
        //本地图片集合
        mDates = new ArrayList<HomeBean>();
        mImages = new ArrayList<HomeBean>();

        for (int i = 0; i < tvs_length; i++) {
            HomeBean homeEntity = new HomeBean();
            homeEntity.setRecycler_tv(mTv[i]);
            mDates.add(homeEntity);
        }

        for (int i = 0; i < imgs_length; i++) {
            HomeBean homeEntity = new HomeBean();
            homeEntity.setRecycler_img(mImg[i]);
            mImages.add(homeEntity);
        }

        adapter = new HomeRecyclerAdapter(getActivity(), mDates, mImages);
        recyclerView.setAdapter(adapter);

        initEvent();

        for (int position = 1; position < 3; position++)
            localImages.add(getResId("spanner" + position, R.drawable.class));

        //自定义Holder
        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        // 设置翻页的效果，不需要翻页效果可用不设

//                .setPageTransformer(Transformer.CubeIn);
//        convenientBanner.setManualPageable(false);//设置不能手动影响


        //各种翻页效果
        transformerList.add(DefaultTransformer.class.getSimpleName());
        transformerList.add(AccordionTransformer.class.getSimpleName());
        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
        transformerList.add(CubeInTransformer.class.getSimpleName());
        transformerList.add(CubeOutTransformer.class.getSimpleName());
        transformerList.add(DepthPageTransformer.class.getSimpleName());
        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
        transformerList.add(RotateDownTransformer.class.getSimpleName());
        transformerList.add(RotateUpTransformer.class.getSimpleName());
        transformerList.add(StackTransformer.class.getSimpleName());
        transformerList.add(ZoomInTransformer.class.getSimpleName());
        transformerList.add(ZoomOutTranformer.class.getSimpleName());

        transforemerName = transformerList.get(13);
        try {
            cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
            transforemer = (ABaseTransformer) cls.newInstance();
            banner.getViewPager().setPageTransformer(true, transforemer);

            //部分3D特效需要调整滑动速度
            if (transforemerName.equals("StackTransformer")) {
                banner.setScrollDuration(3500);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private void initEvent() {
        adapter.setOnItemClickLitener(new HomeRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (PortIpAddress.GetUserType(getActivity())) {
                    switch (position) {
                        case 0:
                            //企业信息
                            intent = new Intent(getActivity(), ACTIVITY[0]);
                            intentIndex = "home";
                            intent.putExtra("intentIndex", intentIndex);
                            intent.putExtra("tag", tag);
                            startActivity(intent);
                            break;
                        case 1:
                            //通知公告
                            intent = new Intent(getActivity(), ACTIVITY[1]);
                            startActivity(intent);
                            break;
                        case 2:
                            //隐患管理
                            intent = new Intent(getActivity(), ACTIVITY[2]);
                            startActivity(intent);
                            break;
                        case 3:
                            //安全检查
                            intent = new Intent(getActivity(), ACTIVITY[3]);
                            intentIndex = "home_zfjc";
                            intent.putExtra("intentIndex", intentIndex);
                            intent.putExtra("tag", tag);
                            startActivity(intent);
                            break;
                        case 4:
                            //事故快报
                            intent = new Intent(getActivity(), ACTIVITY[4]);
                            startActivity(intent);
                            break;
                        case 5:
                            //数据统计
                            intent = new Intent(getActivity(), ACTIVITY[5]);
                            startActivity(intent);
                            break;
                        case 6:
                            //违化品查询
                            intent = new Intent(getActivity(), ACTIVITY[6]);
                            startActivity(intent);
                            break;
                        case 7:
                            //风险监控
                            intent = new Intent(getActivity(), ACTIVITY[7]);
                            startActivity(intent);
                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            //通知公告
                            intent = new Intent(getActivity(), ACTIVITY_QY[0]);
                            startActivity(intent);
                            break;
                        case 1:
                            //隐患管理
                            intent = new Intent(getActivity(), ACTIVITY_QY[1]);
                            startActivity(intent);
                            break;
                        case 2:
                            //自检自查
                            intent  = new Intent(getActivity(),ACTIVITY_QY[2]);
                            intent.putExtra("tag", tag);
                            startActivity(intent);
                            break;
                        case 3:
                            //事故快报
                            intent = new Intent(getActivity(), ACTIVITY_QY[3]);
                            startActivity(intent);
                            break;
                        case 4:
                            //违化品查询
                            intent = new Intent(getActivity(), ACTIVITY_QY[4]);
                            startActivity(intent);
                            break;
                        case 5:
                            //法律法规
                            intent = new Intent(getActivity(), ACTIVITY_QY[5]);
                            startActivity(intent);
                            break;
                    }
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        banner.startTurning(4000);
    }

    // 停止自动翻页
    @Override
    public void onStop() {
        super.onStop();
        //停止翻页
        banner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    private void setOnclick() {
//        home_wxy.setOnClickListener(this);
//        home_yhxx.setOnClickListener(this);
//        home_scxx.setOnClickListener(this);
//        home_gzzd.setOnClickListener(this);
        home_zywsxx.setOnClickListener(this);
        home_yjjyxx.setOnClickListener(this);
        home_aqjypx.setOnClickListener(this);
//        home_aqsctr.setOnClickListener(this);

//      testdownload.setOnClickListener(this);
//      canceldown.setOnClickListener(this);

        home_rygl.setOnClickListener(this);
        home_tzsb.setOnClickListener(this);
        home_qyzz.setOnClickListener(this);
//      hometest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.home_wxy:
//                intent = new Intent(getActivity(), WeiXianYuan.class);
//                intent.putExtra("mycode", mycode);
//                startActivity(intent);
//                break;
//            case R.id.home_yhxx:
//                intent = new Intent(getActivity(), QyYhInfomation.class);
//                intent.putExtra("mycode", mycode);
//                startActivity(intent);
//                break;
//            case R.id.home_scxx:
//                intent = new Intent(getActivity(), ScInfomation.class);
//                intent.putExtra("mycode", mycode);
//                startActivity(intent);
//                break;
//            case R.id.home_gzzd:
//                intent = new Intent(getActivity(), Rules.class);
//                intent.putExtra("mycode", mycode);
//                startActivity(intent);
//                break;
            case R.id.home_zywsxx:
                intent = new Intent(getActivity(), ZyWsXx.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;
            case R.id.home_yjjyxx:
                intent = new Intent(getActivity(), YjYaXx.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;
            case R.id.home_aqjypx:
                intent = new Intent(getActivity(), AqJyPx.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;
//            case R.id.home_aqsctr:
//                intent = new Intent(getActivity(), AqScTr.class);
//                intent.putExtra("mycode", mycode);
//                startActivity(intent);
//                break;
            case R.id.home_rygl:
                intent = new Intent(getActivity(), RyGl.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;
            case R.id.home_tzsb:
                intent = new Intent(getActivity(), TzSb.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;
            case R.id.home_qyzz:
                intent = new Intent(getActivity(), QyZz.class);
                intent.putExtra("mycode", mycode);
                startActivity(intent);
                break;

//            case R.id.hometest:
//                OkHttpUtils
//                        .get()
//                        .url(PortIpAddress.DownLoadFiles())
//                        .addParams("access_token", PortIpAddress.GetToken(getActivity()))
//                        .addParams("attachmentid", "beb8e9c82b79413d9ee777394bd059e8")
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                Log.e(TAG, response);
//                            }
//                        });
//                break;


        }
    }
}
