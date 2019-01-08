package home_content;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.administrator.dimine_sis.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.RiskJkBean;
import enterprise_information.EnterpriseMapInformation;
import okhttp3.Call;
import utils.AMapUtil;
import utils.CheckPermissionsActivity;
import utils.DialogUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;


public class RiskMonitoring extends CheckPermissionsActivity implements View.OnClickListener
        , LocationSource
        , AMapLocationListener
        , OnMarkerClickListener
        , OnInfoWindowClickListener
        , OnMapClickListener
        , GeocodeSearch.OnGeocodeSearchListener
        , InfoWindowAdapter {
    public static final String STRING = "28.21539, 112.884349";
    private ImageView risk_monitoring_back;
    //隐患街道集合
    private String[] rc_arr;
    private TextView risk_street_tv;

    //隐患级别集合
    private String[] level_arr;
    private TextView risk_level_tv;

    //行业集合
    private String[] industry_arr;
    private TextView risk_industry_tv;

    //街道类型集合
    private Map<String, String> street = new HashMap<>();
    //隐患级别集合
    private Map<String, String> yhlevel = new HashMap();
    //行业集合
    private Map<String, String> industry = new HashMap<>();
    private Dialog myDialog;

    private MapView risk_monitoring_map;
    private AMap aMap;
    private ArrayList<BitmapDescriptor> icon_arr;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //定位标识
    private boolean dw = true;
    private StringBuffer buffer;
    private LatLng latLng;
    private MarkerOptions options;

    private List<LatLng> points;

    private Map<String, Integer> marker_images = new HashMap<>();
    //marker集合
    private ArrayList<Marker> markerList = new ArrayList<>();
    private int log;

    //marker图片集合
    private int[] img_arr = {R.drawable.dw_red, R.drawable.dw_orange, R.drawable.dw_yellow, R.drawable.dw_blue};
    //marker类型集合
    private Map<String, String> marker_tag = new HashMap<>();
    //marker
    private Marker marker;

    //地图UI设置
    private UiSettings mUiSettings;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 10;
    //定位按钮
    private ImageButton risk_monitoring_imagebtn;
    //保存当前marker
    private Marker currMarker;

    private String url;
    private String user_token;
    private Map<Marker, String> qyid;
    private Map<Marker, Double> qyLat;
    private Map<Marker, Double> qyLng;

    //记录企业名字
    private Map<Marker, String> qyname;
    private String ymindex = "qyyhym";

    //编码后的经纬度
    private LatLonPoint myLatLng;
    private GeocodeSearch geocoderSearch;

    //图片ID
    private int imageid;
    //危险级别
    private String wxjb;

    //定位名字
    private String locationName;
    //获取屏幕宽度
    private DisplayMetrics metrics = new DisplayMetrics();

    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_monitoring);
        initView();
        risk_monitoring_map.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, SDUtils.LocationPermission) != PackageManager.PERMISSION_GRANTED) {
            MapPermission();
        } else {
            initData();
            setOnClick();
        }
    }

    //询问权限
    private void MapPermission() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(SDUtils.LocationPermission)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 100) {
                initData();
                setOnClick();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 100) {
                ShowToast.showShort(RiskMonitoring.this, "定位失败,需开启定位权限");
//                MapPermission();
//                finish();
            }
        }
    };


    protected void initView() {
        risk_monitoring_back = (ImageView) findViewById(R.id.risk_monitoring_back);
        risk_street_tv = (TextView) findViewById(R.id.risk_street_tv);
        risk_level_tv = (TextView) findViewById(R.id.risk_level_tv);
        risk_industry_tv = (TextView) findViewById(R.id.risk_industry_tv);
        risk_monitoring_map = (MapView) findViewById(R.id.risk_monitoring_map);
        risk_monitoring_imagebtn = (ImageButton) findViewById(R.id.risk_monitoring_imagebtn);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    protected void initData() {
        url = PortIpAddress.Companyreg();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);

        //初始化数据集
        //隐患街道pop
        rc_arr = getResources().getStringArray(R.array.risk_street);
//        RiskMonitoringSetPopWindow(risk_street_tv, rc_arr);
        risk_street_tv.setText(rc_arr[0]);

        //隐患级别pop
        level_arr = getResources().getStringArray(R.array.risk_level);
//        RiskMonitoringLevel(risk_level_tv, level_arr);
        risk_level_tv.setText(level_arr[0]);

        //行业pop
        industry_arr = getResources().getStringArray(R.array.risk_industry);
//        RiskMonitoringIndustry(risk_industry_tv, industry_arr);
        risk_industry_tv.setText(industry_arr[0]);

        //添加定位图片集合
        icon_arr = new ArrayList<BitmapDescriptor>();
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        icon_arr.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));

        marker_images.put("重大", R.drawable.dw_red);
        marker_images.put("严重", R.drawable.dw_orange);
        marker_images.put("一般", R.drawable.dw_yellow);
        marker_images.put("轻微", R.drawable.dw_blue);

        marker_tag.put("", level_arr[0]);
        marker_tag.put("WXJB001", level_arr[1]);
        marker_tag.put("WXJB002", level_arr[2]);
        marker_tag.put("WXJB003", level_arr[3]);
        marker_tag.put("WXJB004", level_arr[4]);


        street.put(rc_arr[0], "");
        street.put(rc_arr[1], "DQ01010101");
        street.put(rc_arr[2], "DQ01010102");
        street.put(rc_arr[3], "DQ01010103");
        street.put(rc_arr[4], "DQ01010104");
        street.put(rc_arr[5], "DQ01010105");
        street.put(rc_arr[6], "DQ01010106");
        street.put(rc_arr[7], "DQ01010107");
        street.put(rc_arr[8], "DQ01010108");
        street.put(rc_arr[9], "DQ01010109");
        street.put(rc_arr[10], "DQ01010110");
        street.put(rc_arr[11], "DQ01010111");
        street.put(rc_arr[12], "DQ01010112");
        street.put(rc_arr[13], "DQ01010113");
        street.put(rc_arr[14], "DQ01010114");
        street.put(rc_arr[15], "DQ01010115");
        street.put(rc_arr[16], "DQ01010116");

        yhlevel.put(level_arr[0], "");
        yhlevel.put(level_arr[1], "WXJB001");
        yhlevel.put(level_arr[2], "WXJB002");
        yhlevel.put(level_arr[3], "WXJB003");
        yhlevel.put(level_arr[4], "WXJB004");

        industry.put(industry_arr[0], "");
        industry.put(industry_arr[1], "HYML021");
        industry.put(industry_arr[2], "HYML022");
        industry.put(industry_arr[3], "HYML023");
        industry.put(industry_arr[4], "HYML024");

        initAmap();
//        getMarker();
        mOkhttp(yhlevel.get(level_arr[0]), street.get(rc_arr[0]), industry.get(industry_arr[0]));
        Log.e("CAT", yhlevel.get(level_arr[0]) + "======" + street.get(rc_arr[0]));
    }

    private void mOkhttp(final String township, final String risktype, final String industry) {
        myDialog = DialogUtil.createLoadingDialog(RiskMonitoring.this, R.string.loading);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(url)
                        .addParams("access_token", user_token)
                        .addParams("bean.risktype", risktype)
                        .addParams("bean.township", township)
                        .addParams("ctcode", industry)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                myDialog.dismiss();
                                ShowToast.showToastNoWait(RiskMonitoring.this, R.string.network_error);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    Log.e("ces", response + "");
                                    JSONArray jsonArray = new JSONArray(response);
                                    qyid = new HashMap<Marker, String>();
                                    qyLat = new HashMap<Marker, Double>();
                                    qyLng = new HashMap<Marker, Double>();
                                    qyname = new HashMap<Marker, String>();
                                    points = new ArrayList<LatLng>();
                                    CoordinateConverter converter = new CoordinateConverter(RiskMonitoring.this);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        RiskJkBean bean = new RiskJkBean();
                                        bean.setQyid(jsonArray.optJSONObject(i).getString("qyid"));
                                        bean.setName(jsonArray.optJSONObject(i).getString("name"));
                                        bean.setAddress(jsonArray.optJSONObject(i).getString("address"));
                                        bean.setTel(jsonArray.optJSONObject(i).getString("tel"));
                                        bean.setLng(jsonArray.optJSONObject(i).getString("lng"));
                                        bean.setLat(jsonArray.optJSONObject(i).getString("lat"));
                                        bean.setWxjb(jsonArray.optJSONObject(i).getString("wxjb"));

                                        if (bean.getWxjb() != null && !bean.getWxjb().toString().equals("")) {
                                            if (!bean.getLat().equals("") && !bean.getLng().equals("")) {
                                                wxjb = marker_tag.get(bean.getWxjb());
                                                imageid = marker_images.get(wxjb);
                                                //标题
                                                String title = bean.getName();
                                                //副标题
                                                String fbt = bean.getTel();
                                                if (!bean.getLat().equals("") && !bean.getLng().equals("")) {
                                                    lat = Double.parseDouble(bean.getLat());
                                                    lng = Double.parseDouble(bean.getLng());
                                                } else {
                                                    lat = 0;
                                                    lng = 0;
                                                }

                                                //设置marker
                                                LatLng latLng = new LatLng(lat, lng);
                                                LatLng realLatLng = AMapUtil.convert(RiskMonitoring.this, latLng, "BAIDU");

                                                //经纬度集合
                                                points.add(realLatLng);
                                                options = AddMarker(realLatLng, imageid, title, fbt);
                                                //设置标记
                                                marker = aMap.addMarker(options);
                                                AMapUtil.startGrowAnimation(marker);

                                                marker.setObject(wxjb);
                                                markerList.add(marker);
                                                qyid.put(marker, bean.getQyid());
                                                qyname.put(marker, title);
                                                qyLat.put(marker, lat);
                                                qyLng.put(marker, lng);
                                            }
                                        }
                                    }
                                    myDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).start();
    }

    //初始化地图
    private void initAmap() {
        aMap = risk_monitoring_map.getMap();
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
        aMap.showBuildings(true);
    }


    protected void setOnClick() {
        risk_monitoring_back.setOnClickListener(this);
        risk_monitoring_imagebtn.setOnClickListener(this);
        risk_street_tv.setOnClickListener(this);
        risk_level_tv.setOnClickListener(this);
        risk_industry_tv.setOnClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);
        ;// 添加显示infowindow监听事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.risk_monitoring_back:
                finish();
                break;
            case R.id.risk_monitoring_imagebtn:
                ShowToast.showToastNowait(RiskMonitoring.this, "定位");
                mLocationClient.startLocation();
                break;
            case R.id.risk_street_tv:
                showStreetDialog();
                break;
            case R.id.risk_level_tv:
                showLevelDialog();
                break;
            case R.id.risk_industry_tv:
                showIndustryDialog();
                break;
        }
    }

    private void showStreetDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rc_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择街道").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        ShowToast.showShort(EnterpriseInformation.this, industry_arr[which]);
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                        risk_street_tv.setText(rc_arr[which]);
                        Log.e(TAG, street.get(rc_arr[which]));
                        mOkhttp(street.get(rc_arr[which]), yhlevel.get(risk_level_tv.getText().toString()), industry.get(risk_industry_tv.getText().toString()));
                        risk_monitoring_map.invalidate();
                    }
                }).create();
        dialog.show();
    }

    private void showLevelDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, level_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择级别").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ShowToast.showShort(EnterpriseInformation.this, industry_arr[which]);
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                        risk_level_tv.setText(level_arr[which]);
                        Log.e(TAG, yhlevel.get(level_arr[which]));
                        mOkhttp(street.get(risk_street_tv.getText().toString()), yhlevel.get(level_arr[which]), industry.get(risk_industry_tv.getText().toString()));
                        risk_monitoring_map.invalidate();
                    }
                }).create();
        dialog.show();
    }

    private void showIndustryDialog() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industry_arr);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择行业").setIcon(R.drawable.industry)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ShowToast.showShort(EnterpriseInformation.this, industry_arr[which]);
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                        risk_industry_tv.setText(industry_arr[which]);
//                        Log.e(TAG, street.get(industry_arr[which]));
                        mOkhttp(street.get(risk_street_tv.getText().toString()), yhlevel.get(risk_level_tv.getText().toString()), industry.get(industry_arr[which]));
                        risk_monitoring_map.invalidate();
                    }
                }).create();
        dialog.show();
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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        risk_monitoring_map.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        risk_monitoring_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        risk_monitoring_map.onPause();
    }

    //定位信息回调
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
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
//                aMapLocation.getCityCode();//城市编码
//                aMapLocation.getAdCode();//地区编码
                locationName = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
                if (dw) {
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    for (LatLng point : points) {
                        boundsBuilder.include(point);
                    }
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));
//                    aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
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
     * 添加marker  设置infowindow值
     */
    private MarkerOptions AddMarker(LatLng latLng, int imageID, String title, String snippet) {
        options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), imageID)));
        options.title(title);
        options.snippet(snippet);
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        options.setFlat(true);//设置marker平贴地图效果
        return options;
    }


    /**
     * 自定义一个当前位置图钉，并且设置图标，当我们点击图钉时，显示设置的信息
     *
     * @param amapLocation
     * @return
     */
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation, double lat, double dlng) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //位置
        options.position(new LatLng(lat, dlng));
        //设置图标
        options
                .anchor(0.5f, 0.5f)
                .icons(icon_arr);

        buffer = new StringBuffer();
        //设置infowindow信息内容
        buffer.append(amapLocation.getCity()
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        currMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currMarker != null) {
            if (currMarker.isInfoWindowShown()) {
                currMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
            }
        }
    }


    /**
     * infowindow点击事件
     * 跳转到企业详情信息页面
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //判断是否是定位是定位则不跳转企业信息详情页面
        if (!marker.getSnippet().toString().equals(locationName)) {
            if (marker.isInfoWindowShown()) {
                Intent intent = new Intent(this, EnterpriseMapInformation.class);
                intent.putExtra("clickId", qyid.get(marker));
                intent.putExtra("qyLat", qyLat.get(marker));
                intent.putExtra("qyLng", qyLng.get(marker));
                intent.putExtra("ymindex", ymindex);
                intent.putExtra("qyname", qyname.get(marker));
                startActivity(intent);
            }
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name, String city) {
        GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));


                //获得编码后的经纬度
                myLatLng = address.getLatLonPoint();

                Log.e("qqqqq", rCode + "=============");
            } else {
                ShowToast.showShort(RiskMonitoring.this, "没有查询到结果");
                Log.e("qqqqq", rCode + "=============");
            }
        } else {
            Log.e("qqqqq", rCode + "=============");
            ShowToast.showShort(RiskMonitoring.this, R.string.network_error);
        }
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

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.daohang,
                null);
        TextView title = (TextView) view.findViewById(R.id.daohang_title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.daohang_snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
