package utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import bean.FileBean;
import bean.MessageEvent;

public class DownloadManagerReceiver extends BroadcastReceiver {
    private long id;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            Log.e("ces", "用户点击了通知");

            // 点击下载进度通知时, 对应的下载ID以数组的方式传递
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            Log.e("ces", "ids: " + Arrays.toString(ids));

        } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Log.e("ces", "下载完成");
            /*
             * 获取下载完成对应的下载ID, 这里下载完成指的不是下载成功, 下载失败也算是下载完成,
             * 所以接收到下载完成广播后, 还需要根据 id 手动查询对应下载请求的成功与失败.
             */
            id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            Log.e("ces", "id: " + id);

            // 根据获取到的ID，使用上面第3步的方法查询是否下载成功

            // 获取下载管理器服务的实例
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            // 创建一个查询对象
            DownloadManager.Query query = new DownloadManager.Query();

            // 根据 下载ID 过滤结果
            query.setFilterById(id);

            // 还可以根据状态过滤结果
            // query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);

            // 执行查询, 返回一个 Cursor (相当于查询数据库)
            Cursor cursor = manager.query(query);

            if (!cursor.moveToFirst()) {
                cursor.close();
                return;
            }

            // 下载请求的状态
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            // 下载文件在本地保存的路径（Android 7.0 以后 COLUMN_LOCAL_FILENAME 字段被弃用, 需要用 COLUMN_LOCAL_URI 字段来获取本地文件路径的 Uri）
            Log.e("ces", status + "----");
            String localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            /*
             * 判断是否下载成功，其中状态 status 的值有 5 种:
             *     DownloadManager.STATUS_SUCCESSFUL:   下载成功
             *     DownloadManager.STATUS_FAILED:       下载失败
             *     DownloadManager.STATUS_PENDING:      等待下载
             *     DownloadManager.STATUS_RUNNING:      正在下载
             *     DownloadManager.STATUS_PAUSED:       下载暂停
             *
             * 特别注意: 查询获取到的 localFilename 才是下载文件真正的保存路径，在创建
             * 请求时设置的保存路径不一定是最终的保存路径，因为当设置的路径已是存在的文件时，
             * 下载器会自动重命名保存路径，例如: .../demo-1.apk, .../demo-2.apk
             *
             */
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Log.e("ces", "下载成功, 打开文件, 文件路径: " + localFilename);

                EventBus.getDefault().post(new MessageEvent(id, FileBean.STATE_DOWNLOADED));

            } else if (status == DownloadManager.STATUS_PENDING) {
                EventBus.getDefault().post(new MessageEvent(id, FileBean.STATE_DOWNLOADING));
                Log.e("ces","下载中");
            } else if (status == DownloadManager.STATUS_FAILED) {
                EventBus.getDefault().post(new MessageEvent(id, FileBean.STATE_ERROR));
                Log.e("ces","下载失败");
            }
        }
    }
}
