package tab;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.example.administrator.dimine_sis.LoginActivity;
import com.example.administrator.dimine_sis.R;

import java.io.File;

import utils.AppUtils;
import utils.BaseFragment;
import utils.CheckVersion;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;


public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView user_photo;
    private String imagePath;
    //返回码
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private RelativeLayout mine_version_information_rl;
    private RelativeLayout mine_modify_password_rl;
    private RelativeLayout mine_cancellation_rl;
    private Intent intent;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private PullToZoomScrollViewEx mine_scrollview;
    private Dialog mCameraDialog;

    private static final int PHOTO_REQUEST_CAMERA = 1;// 相机
    private static final int PHOTO_REQUEST_GALLERY = 2;// 相册
    private static final int PHOTO_REQUEST_CUT = 3;// 裁剪

    private Bitmap bitmap;
    private static final String PHOTO_FILE_NAME = "mine_photo.jpg";
    private File tempFile;
    private TextView mine_username;

    private RelativeLayout mine_custom_camera;

    private TextView mine_dqbb;
    private String apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void loadData() {
        mine_scrollview = (PullToZoomScrollViewEx) view.findViewById(R.id.mine_scrollview);
        loadViewForPullToZoomScrollView(mine_scrollview);
        mine_username = (TextView) view.findViewById(R.id.mine_username);
        mine_version_information_rl = (RelativeLayout) mine_scrollview.getPullRootView().findViewById(R.id.mine_version_information_rl);
//        mine_modify_password_rl = (RelativeLayout) mine_scrollview.getPullRootView().findViewById(R.id.mine_modify_password_rl);
        mine_cancellation_rl = (RelativeLayout) mine_scrollview.getPullRootView().findViewById(R.id.mine_cancellation_rl);
//        mine_custom_camera = (RelativeLayout) view.findViewById(R.id.mine_custom_camera);
//        setPullToZoomViewLayoutParams(mine_scrollview);
        mine_scrollview.setParallax(true);
        mine_username.setText(SharedPrefsUtil.getValue(getActivity(), "userInfo", "username", null));
        user_photo = (ImageView) view.findViewById(R.id.user_photo);
        user_photo.setImageResource(R.drawable.default_headimg);
        mine_dqbb = (TextView) view.findViewById(R.id.mine_dqbb);
        mine_dqbb.setText(AppUtils.getVersionName(getActivity()));

        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        setOnclick();
    }

    // 设置头部的View的宽高。
    private void setPullToZoomViewLayoutParams(PullToZoomScrollViewEx scrollView) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    //加载其中视图
    private void loadViewForPullToZoomScrollView(PullToZoomScrollViewEx scrollView) {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.mine_headview, null);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.mine_head_zoomview, null);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.mine_contentview, null);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    //设置选择头像的dialog
    private void setUserPhoto() {
        mCameraDialog = new Dialog(getActivity(), R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.mine_camera_control, null);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        mCameraDialog.setCanceledOnTouchOutside(true);//点击dialog外部消失
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    /**
     * 相册选择
     */
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 相机
     */
    private void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断有无SD卡
        if (SDUtils.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 裁剪
     *
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 768);
        intent.putExtra("outputY", 1280);
        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    private void setOnclick() {
//        mine_todo_rl.setOnClickListener(this);
//        mine_done_rl.setOnClickListener(this);
        mine_version_information_rl.setOnClickListener(this);
//        mine_modify_password_rl.setOnClickListener(this);
        mine_cancellation_rl.setOnClickListener(this);
        user_photo.setOnClickListener(this);
//        mine_custom_camera.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_version_information_rl:
                CheckVersion checkVersion = new CheckVersion();
                checkVersion.CheckVersions(getActivity(), TAG);
                break;
//            case R.id.mine_modify_password_rl:
//                intent = new Intent(getActivity(), ModifyPassword.class);
//                startActivity(intent);
//                break;
            case R.id.mine_cancellation_rl:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.mine_cancellation_dialog_title);
                builder.setMessage(R.string.mine_cancellation_dialog_content);
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                        editor.remove("ISCHECK");
                        editor.remove("USER_NAME");
                        editor.remove("USER_PWD");
                        editor.remove("AUTOLOGIN");
//                      editor.remove("first");
                        editor.commit();
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.user_photo:
                setUserPhoto();
                break;
            //相机
            case R.id.btn_open_camera:
                camera();
                break;
            //相册
            case R.id.btn_choose_img:
                gallery();
                break;
            case R.id.btn_cancel:
                mCameraDialog.dismiss();
                break;
//            case R.id.mine_custom_camera:
////                Intent intent = new Intent(getActivity(), CustomCamera.class);
////                startActivity(intent);
//                Intent intent = new Intent(getActivity(), WaterTest.class);
//                startActivity(intent);
//                break;
            default:
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (SDUtils.hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                ShowToast.showShort(getActivity(), "没有找到SD卡");
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                user_photo.setImageBitmap(bitmap);
                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
