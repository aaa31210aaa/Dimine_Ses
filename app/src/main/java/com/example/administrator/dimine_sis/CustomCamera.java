package com.example.administrator.dimine_sis;

import android.app.Activity;
import android.os.Bundle;

public class CustomCamera extends Activity {
    //    private Camera mCamera;
//    private int cameraPosition = 0;//0代表前置摄像头,1代表后置摄像头,默认打开前置摄像头
//    private SurfaceHolder holder;
//
//    private SurfaceView mSurfaceView;
//    private ImageView custom_camera_back;
//    //闪光灯设置
//    private ImageButton custom_camera_openLight;
//    //前置后置
//    private ImageButton custom_camera_cameraSwitch;
//
//    private View focusIndex;
//    private View bootomRly;
//
//    private float pointX, pointY;
//    static final int FOCUS = 1;            // 聚焦
//    static final int ZOOM = 2;            // 缩放
//    private int mode;                      //0是聚焦 1是放大
//    //放大缩小
//    int curZoomValue = 0;
//    private float dist;
//    private Camera.Parameters parameters;
//    private Handler handler = new Handler();
//    boolean safeToTakePicture = true;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_custom_camera);
//        CameraUtil.init(this);
//        initView();
//        initData();
//        setOnClick();
    }
//
//    @Override
//    protected void initView() {
//        mSurfaceView = (SurfaceView) findViewById(R.id.custom_camera_surface);
//        custom_camera_back = (ImageView) findViewById(R.id.custom_camera_back);
//        custom_camera_openLight = (ImageButton) findViewById(R.id.custom_camera_openLight);
//        custom_camera_cameraSwitch = (ImageButton) findViewById(R.id.custom_camera_cameraSwitch);
//    }
//
//
//    @Override
//    protected void initData() {
//        holder = mSurfaceView.getHolder();
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        holder.addCallback(this); // 回调接口
//    }
//
//    /**
//     * 获取Camera实例
//     */
//    private Camera getCamera(int id) {
//        Camera camera = null;
//        try {
//            camera = Camera.open(id);
//        } catch (Exception e) {
//
//        }
//        return camera;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mCamera == null) {
//            mCamera = getCamera(cameraPosition);
//            if (holder != null) {
//                startPreview(mCamera, holder);
//            }
//        }
//    }
//
//
//    /* 图像数据处理完成后的回调函数 */
//    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {
//        @Override
//        public void onPictureTaken(final byte[] data, Camera camera) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        //将照片改为竖直方向
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                        Matrix matrix = new Matrix();
//                        switch (cameraPosition) {
//                            case 0://前
//                                matrix.preRotate(90);
//                                break;
//                            case 1:
//                                matrix.preRotate(90);
//                                break;
//                        }
////                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
////                        saveImageToGallery(getBaseContext(), bitmap);
//
//                        mCamera.stopPreview();
//                        mCamera.startPreview();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    safeToTakePicture = true;
//                }
//            }).start();
//        }
//    };
//
//    /**
//     * 预览相机
//     */
//    private void startPreview(Camera camera, SurfaceHolder holder) {
//        try {
//            setupCamera(camera);
//            camera.setPreviewDisplay(holder);
//            // 基本覆盖所有手机 将预览矫正
//            CameraUtil.getInstance().setCameraDisplayOrientation(this,cameraPosition, camera);
//            camera.startPreview();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //实现自动对焦
//    private void autoFocus() {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (mCamera == null) {
//                    return;
//                }
//                mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        if (success) {
//                            setupCamera(camera);//实现相机的参数初始化
//                        }
//                    }
//                });
//            }
//        };
//    }
//
//    /**
//     * 设置
//     */
//    private void setupCamera(Camera camera) {
//        Camera.Parameters parameters = camera.getParameters();
//
//        List<String> focusModes = parameters.getSupportedFocusModes();
//        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            //  自动对焦
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        }
//
//        Camera.Size previewSize = CameraUtil.findBestPreviewResolution(camera);
//        parameters.setPreviewSize(previewSize.width, previewSize.height);
//
//        Camera.Size pictrueSize = CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 1000);
//        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
//
//        camera.setParameters(parameters);
//
//        int picHeight = CameraUtil.screenWidth * previewSize.width / previewSize.height;
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(CameraUtil.screenWidth, picHeight);
//        mSurfaceView.setLayoutParams(params);
//    }
//
//    /**
//     * 闪光灯开关   开->关->自动
//     *
//     * @param mCamera
//     */
//    private void turnLight(Camera mCamera) {
//        if (mCamera == null || mCamera.getParameters() == null
//                || mCamera.getParameters().getSupportedFlashModes() == null) {
//            return;
//        }
//        Camera.Parameters parameters = mCamera.getParameters();
//        String flashMode = mCamera.getParameters().getFlashMode();
//        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
//        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
//                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
//            mCamera.setParameters(parameters);
//            custom_camera_openLight.setImageResource(R.drawable.camera_flash_on);
//        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//开启状态
//            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//                custom_camera_openLight.setImageResource(R.drawable.camera_flash_auto);
//                mCamera.setParameters(parameters);
//            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                custom_camera_openLight.setImageResource(R.drawable.camera_flash_off);
//                mCamera.setParameters(parameters);
//            }
//        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
//                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
//            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            mCamera.setParameters(parameters);
//            custom_camera_openLight.setImageResource(R.drawable.camera_flash_off);
//        }
//    }
//
//
//    /**
//     * 释放相机资源
//     */
//    private void releaseCamera() {
//        if (mCamera != null) {
//            mCamera.setPreviewCallback(null);
//            mCamera.stopPreview();
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//
//    @Override
//    protected void setOnClick() {
//        custom_camera_back.setOnClickListener(this);
//        custom_camera_openLight.setOnClickListener(this);
//        custom_camera_cameraSwitch.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.custom_camera_back:
//                finish();
//                break;
//            case R.id.custom_camera_openLight:
//                turnLight(mCamera);
//                break;
//            case R.id.custom_camera_cameraSwitch:
//                releaseCamera();
//                cameraPosition = (cameraPosition + 1) % mCamera.getNumberOfCameras();
//                mCamera = getCamera(cameraPosition);
//                if (holder != null) {
//                    startPreview(mCamera, holder);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        startPreview(mCamera, holder);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        mCamera.stopPreview();
//        startPreview(mCamera, holder);
//        autoFocus();
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        releaseCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        releaseCamera();
//    }
}
