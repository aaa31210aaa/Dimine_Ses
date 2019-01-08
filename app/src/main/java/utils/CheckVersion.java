package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.administrator.dimine_sis.CustomDialog;
import com.example.administrator.dimine_sis.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class CheckVersion {
    public ProgressDialog progressDialog;

    /**
     * 检查版本
     */
    public void CheckVersions(final Context context, final String tag) {
        OkGo.<String>get(PortIpAddress.updateApp())
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray(PortIpAddress.JsonArrName);
                            String appmemo = jsonArray.getJSONObject(0).getString("appmemo");
                            int verCode = Integer.parseInt(jsonArray.getJSONObject(0).getString("verCode"));
                            String url = jsonArray.getJSONObject(0).getString("url");
                            if (AppUtils.getLocalVersion(context) < verCode) {
                                OpenDialog(appmemo, url, context);
                            } else {
                                if (!tag.equals("MainActivity")) {
                                    ShowToast.showShort(context, "当前版本为最新版本");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(context, R.string.connect_err);
                    }
                });

    }


    /**
     * 打开更新提示dialog
     */
    public void OpenDialog(String appmemo, final String url, final Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("版本更新提示");
        builder.setMessage(appmemo);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //开始下载
                ProgressDialog(context);
                DownLoadFile(url, context);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


    /**
     * 启动下载文件
     *
     * @param downloadUrl
     */
    public void DownLoadFile(String downloadUrl, final Context context) {
        OkGo.<File>get(downloadUrl)
                .tag("download")
                .execute(new FileCallback("DimineSes.apk") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        OpenApk.OpenFile(context);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
//                        OpenDialog();
                    }

                    @Override
                    public void downloadProgress(final Progress progress) {
                        super.downloadProgress(progress);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Log.e(TAG, (int)((progress.fraction) * 100) + "");
                                while ((int) ((progress.fraction) * 100) < 100) {
                                    progressDialog.setProgress((int) ((progress.fraction) * 100));
                                }
                                progressDialog.dismiss();
                            }
                        }).start();

                    }
                });
    }


    /**
     * 水平进度条
     */
    public void ProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setIcon(R.mipmap.dm_logo);// 设置提示的title的图标，默认是没有的
        progressDialog.setTitle("下载提示");
        progressDialog.setMessage("正在下载中...");
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OkGo.getInstance().cancelTag("download");
                        progressDialog.dismiss();
                    }
                });
        progressDialog.show();
    }
}
