package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.administrator.dimine_sis.DialogCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import static utils.SDUtils.STORAGE_REQUESTCODE;


/**
 * Created by Teprinciple on 2016/11/15.
 */
public class UpdateAppUtils {

    private final String TAG = "UpdateAppUtils";
    public static final int CHECK_BY_VERSION_NAME = 1001;
    public static final int CHECK_BY_VERSION_CODE = 1002;
    public static final int DOWNLOAD_BY_APP = 1003;
    public static final int DOWNLOAD_BY_BROWSER = 1004;

    private Activity activity;
    private int checkBy = CHECK_BY_VERSION_CODE;
    private int downloadBy = DOWNLOAD_BY_APP;
    private int serverVersionCode = 0;
    private String apkPath = "";
    private String serverVersionName = "";
    private boolean isForce = false; //是否强制更新
    private int localVersionCode = 0;
    private String localVersionName = "";


    public UpdateAppUtils(Activity activity) {
        this.activity = activity;
        getAPPLocalVersion(activity);
    }

    public static UpdateAppUtils from(Activity activity) {
        return new UpdateAppUtils(activity);
    }

    public UpdateAppUtils checkBy(int checkBy) {
        this.checkBy = checkBy;
        return this;
    }

    public UpdateAppUtils apkPath(String apkPath) {
        this.apkPath = apkPath;
        return this;
    }

    public UpdateAppUtils downloadBy(int downloadBy) {
        this.downloadBy = downloadBy;
        return this;
    }

    public UpdateAppUtils serverVersionCode(int serverVersionCode) {
        this.serverVersionCode = serverVersionCode;
        return this;
    }

    public UpdateAppUtils serverVersionName(String serverVersionName) {
        this.serverVersionName = serverVersionName;
        return this;
    }

    public UpdateAppUtils isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    //获取apk的版本号 currentVersionCode
    private void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = info.versionName; // 版本名
            localVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        switch (checkBy) {
            case CHECK_BY_VERSION_CODE:
                if (serverVersionCode > localVersionCode) {
                    toUpdate();
                } else {
                    ShowToast.showShort(activity, "当前为最新版本" +  serverVersionName);
                }
                break;

            case CHECK_BY_VERSION_NAME:
                if (!serverVersionName.equals(localVersionName)) {
                    toUpdate();
                } else {
                    ShowToast.showShort(activity, "当前版本是最新版本" + serverVersionCode + "/" + serverVersionName);
                }
                break;
        }

    }

    private void toUpdate() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            realUpdate();
        } else {//申请权限
            SDCardPermission();
        }

    }

    //动态配置SD卡读写权限
    private void SDCardPermission() {
        AndPermission.with(activity)
                .requestCode(STORAGE_REQUESTCODE)
                .permission(SDUtils.WriteFilePermission)
                .send();
    }

    //权限回调
    public PermissionListener writelistener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == STORAGE_REQUESTCODE) {
                realUpdate();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == STORAGE_REQUESTCODE) {
                Toast.makeText(activity, "拒绝权限可能会导致功能不可用", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void realUpdate() {
        ConfirmDialog dialog = new ConfirmDialog(activity, new DialogCallback() {
            @Override
            public void callback(int position) {
                switch (position) {
                    case 0:  //cancle
                        if (isForce) activity.finish();
                        break;

                    case 1:  //sure
                        if (downloadBy == DOWNLOAD_BY_APP) {
                            DownloadAppUtils.downloadForAutoInstall(activity, apkPath, "Dimine_Ses.apk", serverVersionName);
                        } else if (downloadBy == DOWNLOAD_BY_BROWSER) {
                            DownloadAppUtils.downloadForWebView(activity, apkPath);
                        }
                        break;
                }
            }
        });
        dialog.setTitle("提示");
        dialog.setContent("发现新版本：" + AppUtils.getVersionName(activity) + "\n" + "更新内容：" + "\n" + "1.修改了若干BUG" + "\n" + "2.添加了XX功能");
        dialog.setCancelable(false);
        dialog.show();
    }


}
