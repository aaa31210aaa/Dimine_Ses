package risk_management;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.dimine_sis.MyGridView;
import com.example.administrator.dimine_sis.NetUtils;
import com.example.administrator.dimine_sis.R;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.CommonlyGridViewAdapter;
import okhttp3.Call;
import utils.BaseActivity;
import utils.CameraUtil;
import utils.DateUtils;
import utils.DialogUtil;
import utils.ImageCompressUtil;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;
import utils.WaterImage;

import static com.example.administrator.dimine_sis.MyApplication.sqldb;
import static risk_management.AddCommonly.TEMPORARY_CODE;
import static risk_management.AddCommonly.listImages;
import static risk_management.AddCommonly.listTexts;
import static utils.PermissionUtil.CAMERA_REQUESTCODE;

public class AddHiddenDangerRegistration extends BaseActivity {
    private ImageView hidden_danger_registration_back;
    //隐患名称
    private EditText registration_hidden_name_etv;
    //整改责任人
    private EditText registration_personLiable_etv;
    //隐患类型分类
    private TextView registration_classification;
    private String[] classification_arr;
    //整改类型
    private TextView registration_type;
    private String[] type_arr;
    //排查日期
    private TextView registration_investigation_date_tv;
    //治理截止日期
    private TextView registration_governance_deadline_tv;
    //隐患具体位置
    private EditText registration_etv_position;
    //隐患描述
    private EditText registration_etv_hidden_description;
    //主要治理方案
    private EditText registration_etv_main_treatment_plan;
    //伤亡人数
    private TextView registration_number_of_casualties;
    private String[] swnum_arr;
    //经济损失
    private TextView registration_economic_loss;
    private String[] loss_arr;
    //时间和范围
    private TextView registration_timeandrange;
    private String[] timeandrange_arr;
    //隐患级别
    private TextView registration_hiddenlevel;
    private String[] hiddenlevel_arr;
    private boolean first_investigation = true; //排查日期第一次点击
    private boolean first_deadline = true; //治理截止日期第一次点击

    private String[] temporaryTexts;

    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    private int myYear_investigation;
    private int myMonth_investigation;
    private int myDay_investigation;
    private int myYear_deadline;
    private int myMonth_deadline;
    private int myDay_deadline;
    //图片选择
    private RadioGroup registration_radiogroup;
    private RadioButton registration_radiobutton1;
    private RadioButton registration_radiobutton2;
    private MyGridView registration_gridview;
    //提交
    private Button registration_submit_btn;

    //当前位置的信息
    private String local;
    //默认字符
    private static final String myCode = "000000";
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    //返回码
    private static final int REQUEST_CAMERA_CODE = 10;  //相册
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
    private CommonlyGridViewAdapter gridAdapter;

    private String url;
    private String user_token;
    private String qyid;
    private String intentIndex;
    //重大隐患登记标识符
    private String crtype = "YHLB002";
    private Map<String, String> arrMap = new HashMap<>();

    private Uri cameraFileUri;
    private String savePath;
    private File picture;
    //加水印后的图片地址
    private String waterPath;
    private File waterf;
    private ArrayList<String> list;
    //临时数据
    private TextView temporary_data;

    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String imgs;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_danger_registration);
        initView();
        initLocal();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        CameraUtil.init(this);
        hidden_danger_registration_back = (ImageView) findViewById(R.id.hidden_danger_registration_back);
        registration_hidden_name_etv = (EditText) findViewById(R.id.registration_hidden_name_etv);
        registration_personLiable_etv = (EditText) findViewById(R.id.registration_personLiable_etv);
        registration_classification = (TextView) findViewById(R.id.registration_classification);
        registration_type = (TextView) findViewById(R.id.registration_type);
        registration_investigation_date_tv = (TextView) findViewById(R.id.registration_investigation_date_tv);
        registration_governance_deadline_tv = (TextView) findViewById(R.id.registration_governance_deadline_tv);
        registration_etv_position = (EditText) findViewById(R.id.registration_etv_position);
        registration_etv_hidden_description = (EditText) findViewById(R.id.registration_etv_hidden_description);
        registration_etv_main_treatment_plan = (EditText) findViewById(R.id.registration_etv_main_treatment_plan);
        registration_number_of_casualties = (TextView) findViewById(R.id.registration_number_of_casualties);
        registration_economic_loss = (TextView) findViewById(R.id.registration_economic_loss);
        registration_timeandrange = (TextView) findViewById(R.id.registration_timeandrange);
        registration_hiddenlevel = (TextView) findViewById(R.id.registration_hiddenlevel);
        registration_radiogroup = (RadioGroup) findViewById(R.id.registration_radiogroup);
        registration_radiobutton1 = (RadioButton) findViewById(R.id.registration_radiobutton1);
        registration_radiobutton2 = (RadioButton) findViewById(R.id.registration_radiobutton2);
        registration_gridview = (MyGridView) findViewById(R.id.registration_gridview);
        registration_submit_btn = (Button) findViewById(R.id.registration_submit_btn);
        temporary_data = (TextView) findViewById(R.id.temporary_data);
    }

    @Override
    protected void initData() {
        imagePaths = new ArrayList<String>();
        list = new ArrayList<>();
        url = PortIpAddress.Hidden();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        Intent intent = getIntent();
        qyid = intent.getStringExtra("clickId");

        //初始化数据集
        classification_arr = getResources().getStringArray(R.array.array_classification);
        type_arr = getResources().getStringArray(R.array.array_type);
        swnum_arr = getResources().getStringArray(R.array.array_swnum);
        loss_arr = getResources().getStringArray(R.array.array_jjss);
        timeandrange_arr = getResources().getStringArray(R.array.time_fw);
        hiddenlevel_arr = getResources().getStringArray(R.array.hidden_level);

        registration_classification.setText(classification_arr[0]);
        registration_type.setText(type_arr[0]);
        registration_number_of_casualties.setText(swnum_arr[0]);
        registration_economic_loss.setText(loss_arr[0]);
        registration_timeandrange.setText(timeandrange_arr[0]);
        registration_hiddenlevel.setText(hiddenlevel_arr[0]);

        //将数据添加至map
        addArrData();


        //设置popwindow
        setPopWindow(registration_classification, classification_arr);
        setPopWindow(registration_type, type_arr);
        setPopWindow(registration_number_of_casualties, swnum_arr);
        setPopWindow(registration_economic_loss, loss_arr);
        setPopWindow(registration_timeandrange, timeandrange_arr);
        setPopWindow(registration_hiddenlevel, hiddenlevel_arr);

        //设置初始日期

        String nowdate = DateUtils.getStringDate();
        registration_investigation_date_tv.setText(nowdate);
        registration_governance_deadline_tv.setText(nowdate);


        registration_radiobutton2.setChecked(true);

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        registration_gridview.setNumColumns(cols);
        AddImage();
        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        registration_gridview.setAdapter(gridAdapter);


        registration_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == registration_radiobutton1.getId()) {
                    registration_gridview.setVisibility(View.GONE);
                } else {
                    registration_gridview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void AddImage() {
        //添加多张图片
        registration_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddHiddenDangerRegistration.this)
                        .requestCode(200)
                        .permission(
                                // 申请多个权限组方式：
                                PermissionUtil.CameraPermission,
                                PermissionUtil.WriteFilePermission,
                                PermissionUtil.LocationPermission
                        ).send();

            }
        });
    }

    private void ClickAdd(int position) {
        if (SDUtils.hasSdcard()) {
            if (myCode.equals(imgs)) {
//                    PhotoPickerIntent intent = new PhotoPickerIntent(AddCommonly.this);
//                    imagePaths.remove(imagePaths.size() - 1);
//                    intent.setSelectModel(SelectModel.MULTI);
//                    intent.setShowCarema(true); // 是否显示拍照
//                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
//                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
//                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                if (registration_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddHiddenDangerRegistration.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddHiddenDangerRegistration.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddHiddenDangerRegistration.this, "没有SD卡");
        }
    }

    /**
     * 打开相机
     */
    private void OpenCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            createFile();
            picture = new File(savePath, System.currentTimeMillis() + ".jpg");
            cameraFileUri = FileProvider.getUriForFile(AddHiddenDangerRegistration.this, "com.example.administrator.dimine_sis.fileprovider", picture);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
        } else {//判断是否有相机应用
            ShowToast.showShort(AddHiddenDangerRegistration.this, "无相机应用");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                ClickAdd(clickPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == CAMERA_REQUESTCODE) {
                ShowToast.showShort(AddHiddenDangerRegistration.this, "拍照失败，需要开启相机权限");
            }
        }
    };


    private void addArrData() {
        arrMap.put(classification_arr[0], "YHLX001");
        arrMap.put(classification_arr[1], "YHLX002");
        arrMap.put(classification_arr[2], "YHLX003");
        arrMap.put(type_arr[0], "ZGLX001");
        arrMap.put(type_arr[1], "ZGLX002");
        arrMap.put(type_arr[2], "ZGLX003");
        arrMap.put(swnum_arr[0], "SWRS001");
        arrMap.put(swnum_arr[1], "SWRS002");
        arrMap.put(swnum_arr[2], "SWRS003");
        arrMap.put(swnum_arr[3], "SWRS004");
        arrMap.put(loss_arr[0], "JJSS001");
        arrMap.put(loss_arr[1], "JJSS002");
        arrMap.put(loss_arr[2], "JJSS003");
        arrMap.put(loss_arr[3], "JJSS004");
        arrMap.put(timeandrange_arr[0], "SJFW001");
        arrMap.put(timeandrange_arr[1], "SJFW002");
        arrMap.put(timeandrange_arr[2], "SJFW003");
        arrMap.put(timeandrange_arr[3], "SJFW004");
        arrMap.put(hiddenlevel_arr[0], "YHJB001");
        arrMap.put(hiddenlevel_arr[1], "YHJB002");
        arrMap.put(hiddenlevel_arr[2], "YHJB003");
        arrMap.put(hiddenlevel_arr[3], "YHJB004");
    }

    private void initLocal() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    private void mOkhttp() {
        long begintime = DateUtils.getStringToDate(registration_investigation_date_tv.getText().toString());
        long endtime = DateUtils.getStringToDate(registration_governance_deadline_tv.getText().toString());
        Log.e(TAG, begintime + "--------" + endtime);
        if (registration_hidden_name_etv.getText().toString().trim().equals("")) {
            ShowToast.showToastNowait(this, "请填写隐患名称");
        } else if (registration_personLiable_etv.getText().toString().trim().equals("")) {
            ShowToast.showToastNowait(this, "请填写整改责任人");
        } else if (registration_etv_position.getText().toString().trim().equals("")) {
            ShowToast.showToastNowait(this, "请填写隐患具体位置");
        } else if (endtime - begintime < 0) {
            ShowToast.showShort(this, "日期选择有误");
        } else {
            if (NetUtils.isConnected(this)) {
                dialog = DialogUtil.createLoadingDialog(this, R.string.submitting);
                addFile();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.SaveData);
                builder.setMessage(R.string.NoNet);

                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temporaryTexts = new String[14];
                        temporaryTexts[0] = registration_hidden_name_etv.getText().toString();
                        temporaryTexts[1] = registration_personLiable_etv.getText().toString();
                        temporaryTexts[2] = registration_classification.getText().toString();
                        temporaryTexts[3] = registration_type.getText().toString();
                        temporaryTexts[4] = registration_investigation_date_tv.getText().toString();
                        temporaryTexts[5] = registration_governance_deadline_tv.getText().toString();
                        temporaryTexts[6] = registration_etv_position.getText().toString();
                        temporaryTexts[7] = registration_etv_hidden_description.getText().toString();
                        temporaryTexts[8] = registration_etv_main_treatment_plan.getText().toString();
                        temporaryTexts[9] = registration_number_of_casualties.getText().toString();
                        temporaryTexts[10] = registration_economic_loss.getText().toString();
                        temporaryTexts[11] = registration_timeandrange.getText().toString();
                        temporaryTexts[12] = registration_hiddenlevel.getText().toString();
                        temporaryTexts[13] = DateUtils.getStringToday();

                        listTexts.add(temporaryTexts);
                        if (registration_radiobutton2.isChecked()) {
//                            list.remove(myCode);
                            listImages.add(list);
                        }

                        //把图片集合转换成字符串以逗号隔开
                        String str = StringUtils.join(imagePaths, ",");
                        Log.e(TAG, str);

                        sqldb.execSQL("insert into hidden_cache(HiddenName,HiddenLiable,HiddenTypeClassification,HiddenType,InvestigationDate,ClosingDate,HiddenLocation,HiddenDescribe,HiddenScheme,HiddenCasualties,HiddenLoss,HiddenTimeAndRange,HiddenLevel,SaveTime,ImagesAddress) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                                new Object[]{
                                        registration_hidden_name_etv.getText().toString(),
                                        registration_personLiable_etv.getText().toString(),
                                        registration_classification.getText().toString(),
                                        registration_type.getText().toString(),
                                        registration_investigation_date_tv.getText().toString(),
                                        registration_governance_deadline_tv.getText().toString(),
                                        registration_etv_position.getText().toString(),
                                        registration_etv_hidden_description.getText().toString(),
                                        registration_etv_main_treatment_plan.getText().toString(),
                                        registration_number_of_casualties.getText().toString(),
                                        registration_economic_loss.getText().toString(),
                                        registration_timeandrange.getText().toString(),
                                        registration_hiddenlevel.getText().toString(),
                                        DateUtils.getStringToday(),
                                        str});
                        clear();
                        imagePaths.clear();
                        loadAdpater(imagePaths);
                        ShowToast.showShort(AddHiddenDangerRegistration.this, "保存成功");
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        }
    }

    private void clear() {
        registration_hidden_name_etv.setText("");
        registration_personLiable_etv.setText("");
        registration_classification.setText(classification_arr[0]);
        registration_type.setText(type_arr[0]);
        registration_investigation_date_tv.setText("");
        registration_governance_deadline_tv.setText("");
        registration_etv_position.setText("");
        registration_etv_hidden_description.setText("");
        registration_etv_main_treatment_plan.setText("");
        registration_number_of_casualties.setText(swnum_arr[0]);
        registration_economic_loss.setText(loss_arr[0]);
        registration_timeandrange.setText(timeandrange_arr[0]);
        registration_hiddenlevel.setText(hiddenlevel_arr[0]);
    }


    private void addFile() {
        PostFormBuilder builder = new PostFormBuilder();
        //添加图片绝对路径到builder，并约定key“files”作为后台接受多张图片的key
        if (registration_radiobutton2.isChecked()) {
            for (int i = 0; i < imagePaths.size() - 1; i++) {
                File file = new File(imagePaths.get(i));
                builder.addFile("files", file.getName(), file);
            }
        }
        builder
                .url(url)
                .addParams("access_token", user_token)
                .addParams("bean.crtype", crtype)
                .addParams("bean.qyid", qyid)
                .addParams("bean.crname", registration_hidden_name_etv.getText().toString().trim())
                .addParams("bean.zgman", registration_personLiable_etv.getText().toString().trim())
                .addParams("bean.crlxfl", arrMap.get(registration_classification.getText().toString().trim()))
                .addParams("bean.zgtype", arrMap.get(registration_type.getText().toString().trim()))
                .addParams("bean.pcdate", registration_investigation_date_tv.getText().toString().trim())
                .addParams("bean.zljzdate", registration_governance_deadline_tv.getText().toString().trim())
                .addParams("bean.craddr", registration_etv_position.getText().toString().trim())
                .addParams("bean.crdesc", registration_etv_hidden_description.getText().toString().trim())
                .addParams("bean.zyzlfa", registration_etv_main_treatment_plan.getText().toString().trim())
                .addParams("bean.swrsxx", arrMap.get(registration_number_of_casualties.getText().toString().trim()))
                .addParams("bean.jjssxx", arrMap.get(registration_economic_loss.getText().toString().trim()))
                .addParams("bean.sjjefwxx", arrMap.get(registration_timeandrange.getText().toString().trim()))
                .addParams("bean.yhgrade", arrMap.get(registration_hiddenlevel.getText().toString().trim()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AddHiddenDangerRegistration.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "------");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                ShowToast.showShort(AddHiddenDangerRegistration.this, "添加成功");
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ShowToast.showShort(AddHiddenDangerRegistration.this, "添加失败，请重新添加");
                                dialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private boolean CompareDate(String date1, String date2) {
        boolean a = false;
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginTime = CurrentTime.parse(date1);
            Date endTime = CurrentTime.parse(date2);
            if (endTime.getTime() - beginTime.getTime() < 0) {
                a = false;
            } else {
                a = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
//                    final String imagePath = cameraFileUri.getPath();
//                    Log.e("ces", imagePath + "---------------");
                    Bitmap bitmap = BitmapFactory.decodeFile(picture.getAbsolutePath());
                    //获得旋转角度
                    int degree = WaterImage.getBitmapDegree(picture.getAbsolutePath());
                    //矫正照片被旋转
                    bitmap = WaterImage.rotateBitmapByDegree(bitmap, degree);
                    //添加水印
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String water_time = "" + sdf.format(new Date());

                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄时间：" + water_time, 50, Color.WHITE, CameraUtil.screenHeight / 20, CameraUtil.screenWidth - 100);
                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄地点：" + local, 50, Color.WHITE, CameraUtil.screenHeight / 20, CameraUtil.screenWidth - 160);

                    //删除不带水印的图片
                    picture.delete();
                    String path = saveBitmap(bitmap);
                    //保存带水印的图片并添加到集合中展示
                    list.add(path);
                    SDUtils.refreshAlbum(AddHiddenDangerRegistration.this, picture);
                    if (list.contains(myCode)) {
                        list.remove(myCode);
                    }
                    list.add(myCode);
                    imagePaths = list;
                    gridAdapter.DataNotify(imagePaths);

                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
//                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
//                    Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
//                    loadAdpater(ListExtra);
                    if (data != null) {
                        ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                        Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
                        if (!ListExtra.contains(myCode)) {
                            ListExtra.add(myCode);
                        }
                        imagePaths = ListExtra;
                        gridAdapter.DataNotify(imagePaths);
                    }
                    break;
                case TEMPORARY_CODE:
                    if (data != null) {
                        String clicknum = data.getExtras().getString("clicknum");
                        Log.e(TAG, clicknum);
                        Cursor cursor = sqldb.rawQuery("select * from hidden_cache where hidden_id = ?", new String[]{clicknum});
                        while (cursor.moveToNext()) {
                            registration_hidden_name_etv.setText(cursor.getString(cursor.getColumnIndex("HiddenName")));
                            registration_personLiable_etv.setText(cursor.getString(cursor.getColumnIndex("HiddenLiable")));
                            registration_classification.setText(cursor.getString(cursor.getColumnIndex("HiddenTypeClassification")));
                            registration_type.setText(cursor.getString(cursor.getColumnIndex("HiddenType")));
                            registration_investigation_date_tv.setText(cursor.getString(cursor.getColumnIndex("InvestigationDate")));
                            registration_governance_deadline_tv.setText(cursor.getString(cursor.getColumnIndex("ClosingDate")));
                            registration_etv_position.setText(cursor.getString(cursor.getColumnIndex("HiddenLocation")));
                            registration_etv_hidden_description.setText(cursor.getString(cursor.getColumnIndex("HiddenDescribe")));
                            registration_etv_main_treatment_plan.setText(cursor.getString(cursor.getColumnIndex("HiddenScheme")));
                            registration_number_of_casualties.setText(cursor.getString(cursor.getColumnIndex("HiddenCasualties")));
                            registration_economic_loss.setText(cursor.getString(cursor.getColumnIndex("HiddenLoss")));
                            registration_timeandrange.setText(cursor.getString(cursor.getColumnIndex("HiddenTimeAndRange")));
                            registration_hiddenlevel.setText(cursor.getString(cursor.getColumnIndex("HiddenLevel")));

                            ArrayList<String> temlist = new ArrayList<>();
                            temlist.addAll(Arrays.asList(cursor.getString(cursor.getColumnIndex("ImagesAddress")).split(",")));
                            imagePaths = temlist;
                            //赋值图片
                            gridAdapter.DataNotify(imagePaths);
                        }


                    }
                    break;
            }
        } else {
            if (data == null) {
                if (!imagePaths.contains(myCode)) {
                    imagePaths.add(myCode);
                }
                gridAdapter.notifyDataSetChanged();
            } else {
                ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                if (!ListExtra.contains(myCode)) {
                    ListExtra.add(myCode);
                }
                imagePaths = ListExtra;
                gridAdapter.DataNotify(imagePaths);
            }
        }
    }


    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths.contains(myCode)) {
            imagePaths.remove(myCode);
        }
        imagePaths.addAll(paths);
        imagePaths.add(myCode);
        gridAdapter.DataNotify(imagePaths);
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void setOnClick() {
        hidden_danger_registration_back.setOnClickListener(this);
        registration_investigation_date_tv.setOnClickListener(this);
        registration_governance_deadline_tv.setOnClickListener(this);
        registration_submit_btn.setOnClickListener(this);
        temporary_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hidden_danger_registration_back:
                finish();
                break;
            case R.id.temporary_data:
                Intent intent = new Intent(this, TemporaryData.class);
                intent.putExtra("crtype", crtype);
                startActivityForResult(intent, TEMPORARY_CODE);
                break;

            case R.id.registration_investigation_date_tv:
                if (first_investigation) {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                registration_investigation_date_tv.setText(select_date);
                                myYear_investigation = year;
                                myMonth_investigation = monthOfYear;
                                myDay_investigation = dayOfMonth;
                                first_investigation = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog_date.show();
                } else {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                registration_investigation_date_tv.setText(select_date);
                                myYear_investigation = year;
                                myMonth_investigation = monthOfYear;
                                myDay_investigation = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myYear_investigation, myMonth_investigation, myDay_investigation);
                    dialog_date.show();
                }
                break;
            case R.id.registration_governance_deadline_tv:
                if (first_deadline) {
                    DatePickerDialog dialog_deadline = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                registration_governance_deadline_tv.setText(select_date);
                                myYear_deadline = year;
                                myMonth_deadline = monthOfYear;
                                myDay_deadline = dayOfMonth;
                                first_deadline = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog_deadline.show();
                } else {
                    DatePickerDialog dialog_deadline = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                registration_governance_deadline_tv.setText(select_date);
                                myYear_deadline = year;
                                myMonth_deadline = monthOfYear;
                                myDay_deadline = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, myYear_deadline, myMonth_deadline, myDay_deadline);
                    dialog_deadline.show();
                }
                break;
            case R.id.registration_submit_btn:
                mOkhttp();
                break;
        }
    }


    /**
     * 保存照片
     *
     * @param mBitmap
     * @return
     */
    public String saveBitmap(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            ShowToast.showShort(this, "内存卡异常，请检查内存卡插入是否正确");
            return "";
        }
        //用时间戳生成照片名称
        waterPath = System.currentTimeMillis() + ".jpg";
        waterf = new File(SDUtils.sdCard + "/WCYH/", waterPath);
        createWaterFile();

        ImageCompressUtil.compressBitmapToFile(mBitmap, waterf);
        SDUtils.refreshAlbum(this, waterf);
        return waterf.getAbsolutePath();
    }

    /**
     * 创建加水印后的图片文件夹
     */
    private void createWaterFile() {
        savePath = SDUtils.sdCard + "/WCYH/";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        savePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }
}
