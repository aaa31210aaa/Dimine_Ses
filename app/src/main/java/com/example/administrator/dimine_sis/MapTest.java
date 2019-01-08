package com.example.administrator.dimine_sis;

import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

import utils.BaseActivity;
import utils.SDUtils;

public class MapTest extends BaseActivity implements View.OnClickListener {
    private MapView map_test;
    private String[] gsname = {"湖南瑶岗仙矿业有限责任公司", "湖南瑶岗仙矿业有限责任公司", "湖南瑶岗仙矿业有限责任公司", "湖南宝山有色金属矿业有限责任公司", "湖南有色黄沙坪矿业分公司", "湖南柿竹园有色金属有限公司铅锌矿"};
    private int image[] = {R.drawable.dw_red, R.drawable.dwhs, R.drawable.dwls, R.drawable.dwls, R.drawable.dwls, R.drawable.dwls};
    private double latarr[] = {28.007638, 28.461347, 28.344172, 28.305489, 28.305489, 28.194195};
    private double lngarr[] = {113.09369, 113.027772, 113.174715, 113.156862, 113.156862, 112.94177};

    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 10;
    private ArrayList<BitmapDescriptor> icon_arr;
    private AMap aMap;
    //地图UI设置
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        initView();
        map_test.onCreate(savedInstanceState);
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        map_test = (MapView) findViewById(R.id.map_test);
        //配置权限
        if (ContextCompat.checkSelfPermission(this, SDUtils.LocationPermission)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{SDUtils.LocationPermission},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }

        initAmap();
    }

    @Override
    protected void initData() {


    }

    //初始化地图
    private void initAmap() {
        if (aMap == null) {
            aMap = map_test.getMap();
        }
        mUiSettings = aMap.getUiSettings();
        //开启比例尺
        mUiSettings.setScaleControlsEnabled(true);
        //开启指南针
        mUiSettings.setCompassEnabled(true);
        for (int i = 0; i < gsname.length; i++) {
            AddMarker(latarr[i], lngarr[i], gsname[i], image[i]);
        }

    }


    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }

    //在指定位置添加标记
    private void AddMarker(double dlat, double dlng, String gsname, int drawable) {
        LatLng pos = new LatLng(dlat, dlng);

        //绘制marker
        aMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(gsname)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), drawable)))
                .draggable(true));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map_test.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map_test.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map_test.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map_test.onSaveInstanceState(outState);
    }

}
