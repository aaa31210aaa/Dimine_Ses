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
import com.zhy.http.okhttp.OkHttpUtils;
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
import java.util.List;
import java.util.Map;

import adapter.CommonlyGridViewAdapter;
import okhttp3.Call;
import okhttp3.MediaType;
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

/**
 * 一般隐患登记
 */
public class AddCommonly extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AddCommonly";
    private ImageView commonly_back;
    private TextView temporary_data;
    //隐患名称
    private EditText commonly_hidden_name_etv;
    //整改责任人
    private EditText commonly_personLiable_etv;
    //隐患类型分类
    private TextView commonly_classification;
    private String[] classification_arr;
    //整改类型
    private TextView commonly_type;
    private String[] type_arr;
    //排查日期
    private TextView commonly_investigation_date_tv;
    //治理截止日期
    private TextView commonly_governance_deadline_tv;
    //隐患具体位置
    private EditText commonly_specific_location_etv;
    //隐患描述
    private EditText commonly_yhms;
    //主要治理方案
    private EditText commonly_zyzlfa;
    private RadioGroup commonly_radiogroup;
    //现场环境限制
    private RadioButton commonly_radiobutton1;
    //图片
    private RadioButton commonly_radiobutton2;
    //图片选择
    private MyGridView commonly_gridview;
    //提交
    private Button commonly_submit_btn;
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;
    //当前日期
    private String nowdate;

    private int sj_month;

    private int myYear_investigation;
    private int myMonth_investigation;
    private int myDay_investigation;
    private int myYear_deadline;
    private int myMonth_deadline;
    private int myDay_deadline;

    private boolean first_investigation = true; //排查日期第一次点击
    private boolean first_deadline = true; //治理截止日期第一次点击

    private String url;
    private String user_token;
    //一般隐患登记标识符a
    private String crtype = "YHLB001";
    private String qyid;
    private String crid;
    private Intent intent;
    private Map<String, String> arrMap = new HashMap<>();

    private static final int REQUEST_CAMERA_CODE = 10;// 相机
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
    //返回码
    private ImageView water_test_image;
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    //拍照的照片集合
    private ArrayList<String> list;
    private CommonlyGridViewAdapter gridAdapter;
    //照相名字地址
    private static String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";
    private Uri cameraFileUri;
    private String savePath;
    private File picture;
    //加水印后的图片地址
    private String waterPath;
    private File waterf;

    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //当前位置的信息
    private String local;
    //默认字符
    private static final String myCode = "000000";
    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
//    private final OkHttpClient client = new OkHttpClient();

    //临时保存的文字和图片
    public static final int TEMPORARY_CODE = 30;

    public static List<String[]> listTexts = new ArrayList<>();
    public static List<List<String>> listImages = new ArrayList<>();
    private String[] temporaryTexts;
    private String imgs;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonly);
        initView();
        initLocal();
        initData();
        setOnClick();
    }

    //    @Override
    protected void initView() {
        CameraUtil.init(this);
        commonly_back = (ImageView) findViewById(R.id.commonly_back);
        temporary_data = (TextView) findViewById(R.id.temporary_data);
        commonly_hidden_name_etv = (EditText) findViewById(R.id.commonly_hidden_name_etv);
        commonly_personLiable_etv = (EditText) findViewById(R.id.commonly_personLiable_etv);
        commonly_classification = (TextView) findViewById(R.id.commonly_classification);
        commonly_type = (TextView) findViewById(R.id.commonly_type);
        commonly_investigation_date_tv = (TextView) findViewById(R.id.commonly_investigation_date_tv);
        commonly_governance_deadline_tv = (TextView) findViewById(R.id.commonly_governance_deadline_tv);
        commonly_specific_location_etv = (EditText) findViewById(R.id.commonly_specific_location_etv);
        commonly_yhms = (EditText) findViewById(R.id.commonly_yhms);
        commonly_zyzlfa = (EditText) findViewById(R.id.commonly_zyzlfa);
        commonly_radiogroup = (RadioGroup) findViewById(R.id.commonly_radiogroup);
        commonly_radiobutton1 = (RadioButton) findViewById(R.id.commonly_radiobutton1);
        commonly_radiobutton2 = (RadioButton) findViewById(R.id.commonly_radiobutton2);
        commonly_gridview = (MyGridView) findViewById(R.id.commonly_gridview);
        commonly_submit_btn = (Button) findViewById(R.id.commonly_submit_btn);
    }

    //    @Override
    protected void initData() {
        imagePaths = new ArrayList<>();
        list = new ArrayList<>();
        url = PortIpAddress.Hidden();
        user_token = SharedPrefsUtil.getValue(this, "userInfo", "user_token", null);
        intent = getIntent();
        qyid = intent.getStringExtra("clickId");
        crid = intent.getStringExtra("crid");

        if (crid == null) {
            crid = "";
        }

        Log.e(TAG, crid + "------------" + qyid);
        //设置初始日期
        nowdate = DateUtils.getStringDate();
        commonly_investigation_date_tv.setText(nowdate);
        commonly_governance_deadline_tv.setText(nowdate);

        //初始化数据集
        classification_arr = getResources().getStringArray(R.array.array_classification);
        type_arr = getResources().getStringArray(R.array.array_type);
        commonly_classification.setText(classification_arr[0]);
        commonly_type.setText(type_arr[0]);

        //将数据添加至map
        addArrData();

        //设置popwindow
        setPopWindow(commonly_classification, classification_arr);
        setPopWindow(commonly_type, type_arr);

        commonly_radiobutton2.setChecked(true);


        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        commonly_gridview.setNumColumns(cols);
        AddImage();
        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        commonly_gridview.setAdapter(gridAdapter);

        commonly_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == commonly_radiobutton1.getId()) {
//                    ShowToast.showShort(AddCommonly.this, "现场环境限制");
                    commonly_gridview.setVisibility(View.GONE);
//                    list.clear();
//                    loadAdpater(list);
                } else {
//                    ShowToast.showShort(AddCommonly.this, "图片");
                    commonly_gridview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void AddImage() {
        //添加多张图片
        commonly_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddCommonly.this)
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
                if (commonly_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddCommonly.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddCommonly.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddCommonly.this, "没有SD卡");
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
            cameraFileUri = FileProvider.getUriForFile(AddCommonly.this, "com.example.administrator.dimine_sis.fileprovider", picture);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
        } else {//判断是否有相机应用
            ShowToast.showShort(AddCommonly.this, "无相机应用");
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
            if (requestCode == 200) {
                ShowToast.showShort(AddCommonly.this, "拍照失败，需要开启相机权限");
            }
        }
    };


    //将数据添加至map集合
    private void addArrData() {
        arrMap.put(classification_arr[0], "YHLX001");
        arrMap.put(classification_arr[1], "YHLX002");
        arrMap.put(classification_arr[2], "YHLX003");
        arrMap.put(type_arr[0], "ZGLX001");
        arrMap.put(type_arr[1], "ZGLX002");
        arrMap.put(type_arr[2], "ZGLX003");
        Log.e(TAG, arrMap.get(commonly_classification.getText().toString().trim()));
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
                    SDUtils.refreshAlbum(AddCommonly.this, picture);
                    if (list.contains(myCode)) {
                        list.remove(myCode);
                    }
                    list.add(myCode);
                    imagePaths = list;
                    gridAdapter.DataNotify(imagePaths);

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    System.gc();
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    if (data != null) {
                        ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                        Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
//                        loadAdpater(ListExtra);
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
                        //查询模板中的数据
                        Cursor cursor = sqldb.rawQuery("select * from commonly_cache where commonly_id = ?", new String[]{clicknum});
                        while (cursor.moveToNext()) {
                            commonly_hidden_name_etv.setText(cursor.getString(cursor.getColumnIndex("CommonlyName")));
                            commonly_personLiable_etv.setText(cursor.getString(cursor.getColumnIndex("CommonlyLiable")));
                            commonly_classification.setText(cursor.getString(cursor.getColumnIndex("CommonlyTypeClassification")));
                            commonly_type.setText(cursor.getString(cursor.getColumnIndex("Rectification")));
                            commonly_investigation_date_tv.setText(cursor.getString(cursor.getColumnIndex("InvestigationDate")));
                            commonly_governance_deadline_tv.setText(cursor.getString(cursor.getColumnIndex("ClosingDate")));
                            commonly_specific_location_etv.setText(cursor.getString(cursor.getColumnIndex("CommonlyLocation")));
                            commonly_yhms.setText(cursor.getString(cursor.getColumnIndex("CommonlyDescribe")));
                            commonly_zyzlfa.setText(cursor.getString(cursor.getColumnIndex("CommonlyScheme")));

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


    //加载图片
    private void loadAdpater(List<String> paths) {
//        if (imagePaths != null && imagePaths.size() > 0) {
//            imagePaths.clear();
//        }
        if (imagePaths.contains(myCode)) {
            imagePaths.remove(myCode);
        }
        imagePaths.addAll(paths);
        imagePaths.add(myCode);
        gridAdapter.DataNotify(imagePaths);
//        gridAdapter.notifyDataSetChanged();
//      upLoadImage();
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
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

    private void mOkhttp() {
        long begintime = DateUtils.getStringToDate(commonly_investigation_date_tv.getText().toString());
        long endtime = DateUtils.getStringToDate(commonly_governance_deadline_tv.getText().toString());
        Log.e("eeeeee", begintime + "---------------" + endtime);
        if (commonly_hidden_name_etv.getText().toString().trim().equals("")) {
            ShowToast.showToastNowait(this, "请填写隐患名称");
        } else if (commonly_personLiable_etv.getText().toString().trim().equals("")) {
            ShowToast.showToastNowait(this, "请填写整改责任人");
        } else if (commonly_specific_location_etv.getText().toString().trim().equals("")) {
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
                        temporaryTexts = new String[10];
                        temporaryTexts[0] = commonly_hidden_name_etv.getText().toString();
                        temporaryTexts[1] = commonly_personLiable_etv.getText().toString();
                        temporaryTexts[2] = commonly_classification.getText().toString();
                        temporaryTexts[3] = commonly_type.getText().toString();
                        temporaryTexts[4] = commonly_investigation_date_tv.getText().toString();
                        temporaryTexts[5] = commonly_governance_deadline_tv.getText().toString();
                        temporaryTexts[6] = commonly_specific_location_etv.getText().toString();
                        temporaryTexts[7] = commonly_yhms.getText().toString();
                        temporaryTexts[8] = commonly_zyzlfa.getText().toString();
                        temporaryTexts[9] = DateUtils.getStringToday();

                        listTexts.add(temporaryTexts);
                        if (commonly_radiobutton2.isChecked()) {
//                            list.remove(myCode);
                            listImages.add(list);
                        }

//                        TemporaryBean bean = new TemporaryBean();
//                        bean.setCommonlyName(commonly_hidden_name_etv.getText().toString());
//                        bean.setCommonlyLiable(commonly_personLiable_etv.getText().toString());
//                        bean.setCommonlyTypeClassification(commonly_classification.getText().toString());
//                        bean.setRectification(commonly_type.getText().toString());
//                        bean.setInvestigationDate(commonly_investigation_date_tv.getText().toString());
//                        bean.setClosingDate(commonly_governance_deadline_tv.getText().toString());
//                        bean.setCommonlyLocation(commonly_specific_location_etv.getText().toString());
//                        bean.setCommonlyDescribe(commonly_yhms.getText().toString());
//                        bean.setCommonlyScheme(commonly_zyzlfa.getText().toString());
//                        bean.setSaveTime(DateUtils.getStringToday());


//                            if (SharedPrefsUtil.getValue(AddCommonly.this, "commonlyInfo", "commonlyData", null) != null) {
//                                SharedPrefsUtil.putValue(AddCommonly.this, "commonlyInfo", "commonlyData", SharedPrefsUtil.getValue(AddCommonly.this, "commonlyInfo", "commonlyData", null) + json + ",");
//                            } else {
//                                SharedPrefsUtil.putValue(AddCommonly.this, "commonlyInfo", "commonlyData", json + ",");
//                            }


                        //把图片集合转换成字符串以逗号隔开
                        String str = StringUtils.join(imagePaths, ",");
                        Log.e(TAG, str);

                        sqldb.execSQL("insert into commonly_cache (CommonlyName,CommonlyLiable,CommonlyTypeClassification,Rectification,InvestigationDate,ClosingDate,CommonlyLocation,CommonlyDescribe,CommonlyScheme,SaveTime,ImagesAddress) values(?,?,?,?,?,?,?,?,?,?,?)",
                                new Object[]{
                                        commonly_hidden_name_etv.getText().toString(),
                                        commonly_personLiable_etv.getText().toString(),
                                        commonly_classification.getText().toString(),
                                        commonly_type.getText().toString(),
                                        commonly_investigation_date_tv.getText().toString(),
                                        commonly_governance_deadline_tv.getText().toString(),
                                        commonly_specific_location_etv.getText().toString(),
                                        commonly_yhms.getText().toString(),
                                        commonly_zyzlfa.getText().toString(),
                                        DateUtils.getStringToday(),
                                        str});
                        clear();
                        imagePaths.clear();
                        loadAdpater(imagePaths);
                        ShowToast.showShort(AddCommonly.this, "保存成功");
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

    private void addFile() {
        PostFormBuilder builder = null;
        builder = OkHttpUtils
                .post()
                .url(url)
                .addParams("access_token", user_token)
                .addParams("bean.crtype", crtype)
                .addParams("bean.qyid", qyid)
                .addParams("bean.crid", crid)
                .addParams("bean.crname", commonly_hidden_name_etv.getText().toString())
                .addParams("bean.zgman", commonly_personLiable_etv.getText().toString())
                .addParams("bean.crlxfl", arrMap.get(commonly_classification.getText().toString()))
                .addParams("bean.zgtype", arrMap.get(commonly_type.getText().toString()))
                .addParams("bean.pcdate", commonly_investigation_date_tv.getText().toString())
                .addParams("bean.zljzdate", commonly_governance_deadline_tv.getText().toString())
                .addParams("bean.craddr", commonly_specific_location_etv.getText().toString())
                .addParams("bean.crdesc", commonly_yhms.getText().toString())
                .addParams("bean.zyzlfa", commonly_zyzlfa.getText().toString());

        if (commonly_radiobutton2.isChecked()) {
            //添加图片绝对路径到builder，并约定key“files”作为后台接受多张图片的key
            for (int i = 0; i < imagePaths.size() - 1; i++) {
                File file = new File(imagePaths.get(i));
                builder.addFile("files", file.getName(), file);
            }
        }
        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        dialog.dismiss();
                        ShowToast.showShort(AddCommonly.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "------");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                ShowToast.showShort(AddCommonly.this, "添加成功");
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                ShowToast.showShort(AddCommonly.this, "添加失败，请重新添加");
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

//    private void Test2() {
//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .cookieJar(LoginActivity.cookieJar)
//                .build();
//        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//        Map<String, String> params = new HashMap<>();
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//
//        params.put("access_token", user_token);
//        params.put("bean.crtype", crtype);
//        params.put("bean.qyid", qyid);
//        params.put("bean.crname", "dsadasd");
//        params.put("bean.zgman", "fsadasdasd");
//        params.put("bean.crlxfl", "YHLX001");
//        params.put("bean.zgtype", "ZGLX001");
//        params.put("bean.pcdate", "2017-04-18");
//        params.put("bean.zljzdate", "2017-04-18");
//        params.put("bean.craddr", "adaqqsd");
//        params.put("bean.crdesc", "sdaqqweq");
//        params.put("bean.zyzlfa", "qweqezx");
//
//        //遍历map中所有参数到builder
//        if (params != null) {
//            for (String key : params.keySet()) {
//                builder.addFormDataPart(key, params.get(key));
//            }
//        }
//
//        for (int i = 0; i < imagePaths.size() - 1; i++) {
//            File file = new File(imagePaths.get(i));
//            builder.addFormDataPart("files", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
//        }
//
//        RequestBody requestBody = builder.build();
//
//        Request.Builder RequestBuilder = new Request.Builder();
//        RequestBuilder.url(url);// 添加URL地址
//        RequestBuilder.post(requestBody);
//        Request request = RequestBuilder.build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String str = response.body().string();
//                Log.e(TAG, str);
//            }
//        });
//
//    }


    @Override
    protected void setOnClick() {
        commonly_back.setOnClickListener(this);
        temporary_data.setOnClickListener(this);
        commonly_submit_btn.setOnClickListener(this);
        commonly_investigation_date_tv.setOnClickListener(this);
        commonly_governance_deadline_tv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commonly_back:
                finish();
                break;
            case R.id.temporary_data:
                intent = new Intent(this, TemporaryData.class);
                intent.putExtra("crtype", crtype);
                startActivityForResult(intent, TEMPORARY_CODE);
                break;

            case R.id.commonly_investigation_date_tv:
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
                                commonly_investigation_date_tv.setText(select_date);
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
                                commonly_investigation_date_tv.setText(select_date);
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
            case R.id.commonly_governance_deadline_tv:
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
                                commonly_governance_deadline_tv.setText(select_date);
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
                                commonly_governance_deadline_tv.setText(select_date);
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
            //提交
            case R.id.commonly_submit_btn:
                mOkhttp();
//                File file = new File(imagePaths.get(0));
//                upload(url);
                break;
        }
    }

    private void clear() {
        commonly_hidden_name_etv.setText("");
        commonly_personLiable_etv.setText("");
        commonly_classification.setText(classification_arr[0]);
        commonly_type.setText(type_arr[0]);
        commonly_investigation_date_tv.setText(nowdate);
        commonly_governance_deadline_tv.setText(nowdate);
        commonly_specific_location_etv.setText("");
        commonly_yhms.setText("");
        commonly_zyzlfa.setText("");
    }


    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }
}
