package home_content;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.administrator.dimine_sis.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.AMapUtil;
import utils.CheckPermissionsActivity;
import utils.DialogUtil;
import utils.SDUtils;
import utils.ShowToast;

public class HiddenMap extends CheckPermissionsActivity implements View.OnClickListener
        , LocationSource
        , AMapLocationListener
        , AMap.OnMarkerClickListener
        , TextWatcher
        , Inputtips.InputtipsListener
        , AMap.InfoWindowAdapter
        , PoiSearch.OnPoiSearchListener {
    private MapView hidden_map;
    private AMap aMap;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 10;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //定位标识
    private boolean dw = true;
    //输入搜索关键字
    private AutoCompleteTextView hidden_map_keyword;
    //要输入的poi搜索关键字
    private String keyWord = "";
    //搜索按钮
    private TextView hidden_map_btn_search;
    // 搜索时进度条
    private ProgressDialog progDialog = null;
    // poi返回的结果
    private PoiResult poiResult;
    //当前页面，从0开始计数
    private int currentPage = 0;
    //Poi查询条件类
    private PoiSearch.Query query;
    //Poi搜索
    private PoiSearch poiSearch;

    private MarkerOptions options;
    private StringBuffer buffer;

    private ImageButton hidden_map_imagebtn;
    private ArrayList<BitmapDescriptor> icon_arr;
    //地图UI设置
    private UiSettings mUiSettings;
    //获取当前位置的城市名
    private String mCity;

    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_map);
        initView();
        hidden_map.onCreate(savedInstanceState);
        initData();
        setOnClick();
    }

    protected void initView() {
        initStatusBarColor(ContextCompat.getColor(this, R.color.blue_deep));
        hidden_map_keyword = (AutoCompleteTextView) findViewById(R.id.hidden_map_keyword);
        hidden_map_btn_search = (TextView) findViewById(R.id.hidden_map_btn_search);
        hidden_map = (MapView) findViewById(R.id.hidden_map);
        hidden_map_imagebtn = (ImageButton) findViewById(R.id.hidden_map_imagebtn);
        SettingQx();

    }

    protected void initData() {
        //添加定位图片集合
        icon_arr = new ArrayList<BitmapDescriptor>();
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        initAmap();
    }

    //初始化地图
    private void initAmap() {
        aMap = hidden_map.getMap();
        mUiSettings = aMap.getUiSettings();
        //开启比例尺
        mUiSettings.setScaleControlsEnabled(true);
        //开启指南针
        mUiSettings.setCompassEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        //设置显示定位按钮 并且可以点击
//        UiSettings settings = aMap.getUiSettings();
//        // 是否显示定位按钮
//        settings.setMyLocationButtonEnabled(true);
    }


    protected void setOnClick() {
        hidden_map_imagebtn.setOnClickListener(this);
        aMap.setOnMarkerClickListener(this);
        hidden_map_keyword.addTextChangedListener(this);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_map_imagebtn:
                dw = true;
                mLocationClient.startLocation();
                break;
        }
    }

    /**
     * 激活定位
     *
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //获取一次定位结果
            mLocationOption.setOnceLocation(true);

            //启动定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                //显示系统小蓝点
//                mListener.onLocationChanged(aMapLocation);
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                mCity = aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
//                aMapLocation.getStreet();//街道信息
//                aMapLocation.getStreetNum();//街道门牌号信息
//                aMapLocation.getCityCode();//城市编码
//                aMapLocation.getAdCode();//地区编码

                if (dw) {
                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    mLocationClient.stopLocation();
                }
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    aMap.addMarker(getMarkerOptions(aMapLocation, aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    //设置定位监听
//                  只定位一次
                    isFirstLoc = false;
                    mLocationClient.stopLocation();
                }
            } else {
                String errText = "定位失败，" + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr:", errText);
                ShowToast.showToastNowait(this, errText);
            }
        }
    }

    /**
     * 自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
     *
     * @param amapLocation
     * @return
     */
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation, double lat, double dlng) {
        //设置图钉选项
        options = new MarkerOptions();
        //位置
        options.position(new LatLng(lat, dlng));
        //设置图标
        options
                .anchor(0.5f, 0.5f)
                .icons(icon_arr);

        buffer = new StringBuffer();
        //设置infowindow信息内容
        buffer.append(amapLocation.getCountry()
                + amapLocation.getProvince()
                + amapLocation.getCity()
                + amapLocation.getDistrict()
                + amapLocation.getStreet()
                + amapLocation.getStreetNum());
        //标题
        options.title("详细地址：");
        //子标题
        options.snippet(buffer.toString());
        //设置多少帧刷新一次图片资源
        options.period(5);
//      Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
        return options;
    }


    //当输入框改变内容时提示
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (newText != null || !(newText.toString().trim().length() == 0)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, mCity);
            Inputtips inputTips = new Inputtips(HiddenMap.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            hidden_map_keyword.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
//            ShowToast.showerror(this, rCode);
        }
    }


    private void SettingQx() {
        //配置权限
        if (ContextCompat.checkSelfPermission(this, SDUtils.LocationPermission)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{SDUtils.LocationPermission},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }


    /**
     * 设置状态栏颜色
     */
    protected void initStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        hidden_map.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        hidden_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        hidden_map.onPause();
    }


    /**
     * 标记点击监听
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            marker.showInfoWindow();
        }
        return false;
    }


    /**
     * 悬浮气泡信息回调
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_infowindow, null);
        //设置悬浮气泡的标题
        TextView title = (TextView) view.findViewById(R.id.poi_infowindow_title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.poi_infowindow_snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view.findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startAMapNavi(marker);
            }
        });

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }

    }



    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        //判断输入框是否为空
        keyWord = AMapUtil.checkEditText(hidden_map_keyword);
        if ("".equals(keyWord)) {
            ShowToast.showShort(HiddenMap.this, "请输入搜索关键字");
            return;
        } else {
            doSearchQuery();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        dialog = DialogUtil.createLoadingDialog(this, "正在搜索：\n" + keyWord);
//        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", mCity);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查第一页
        query.setPageNum(currentPage);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * Poi搜索回调
     *
     * @param poiResult
     * @param i
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
