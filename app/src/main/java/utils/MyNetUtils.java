package utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * 判断网络是否可用
 */
public class MyNetUtils {
    public static DownloadManager manager;

    private MyNetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 调用系统下载器创建下载请求
     */
    public static long downLoadFile(Context context, String url, String title, String description, String fileName) {
        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                /*
                 * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
                 *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
                 *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
                 *    VISIBILITY_HIDDEN:                    始终不显示通知
                 */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知的标题和描述
        request.setTitle(title);
        request.setDescription(description);

                /*
                 * 设置允许使用的网络类型, 可选值:
                 *     NETWORK_MOBILE:      移动网络
                 *     NETWORK_WIFI:        WIFI网络
                 *     NETWORK_BLUETOOTH:   蓝牙网络
                 * 默认为所有网络都允许
                 */
        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        // 添加请求头
        // request.addRequestHeader("User-Agent", "Chrome Mozilla/5.0");
        // 设置下载文件的保存位置
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
        request.setDestinationUri(Uri.fromFile(saveFile));
         /*
         * 2. 获取下载管理器服务的实例, 添加下载任务
         */
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // 将下载请求加入下载队列, 返回一个下载ID
        return manager.enqueue(request);
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

}
