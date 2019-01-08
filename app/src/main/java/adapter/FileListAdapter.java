package adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.io.File;
import java.util.List;

import bean.FileBean;
import utils.AndroidFileUtil;
import utils.ListViewHolder;
import utils.MyNetUtils;
import utils.SDUtils;
import utils.ShowToast;

public class FileListAdapter extends BaseAdapter {
    private Context context;
    private List<FileBean> mDatas;
    //    private Map<String, String> fileMap;
    private List<String> filenames;
    private DownloadManager.Request request;
    private DownloadManager manager;
    //    private long[] downloadId;
    //保存下载状态
//    private Map<Long, String> downloadType;
    private String downloadUrl = "http://dl.op.wpscdn.cn/dl/wps/mobile/apk/moffice_10.3.1_1033_cn00563_multidex_f728b35.apk";


    public FileListAdapter(Context context, List<FileBean> mDatas, List<String> filenames) {
        this.context = context;
        this.mDatas = mDatas;
//        this.fileMap = fileMap;
        this.filenames = filenames;
//        downloadId = new long[mDatas.size()];
//        downloadType = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.filelist_item, null);
        }
        final FileBean bean = mDatas.get(position);


        TextView filelist_item_filename = ListViewHolder.get(convertView, R.id.filelist_item_filename);
        final TextView filelist_item_downopen = ListViewHolder.get(convertView, R.id.filelist_item_downopen);
//        ImageView filelist_item_cancel = ListViewHolder.get(convertView, R.id.filelist_item_cancel);
        ImageView filelist_item_delete = ListViewHolder.get(convertView, R.id.filelist_item_delete);
        filelist_item_filename.setText(bean.getFilename());
//        filelist_item_downopen.setText(bean.getDownloadType());

        if (0 == bean.getStatus()) {
            filelist_item_downopen.setText("下载");
            filelist_item_delete.setImageResource(R.drawable.unclickdelete);
        } else if (1 == bean.getStatus()) {
            filelist_item_downopen.setText("取消");
            filelist_item_delete.setImageResource(R.drawable.unclickdelete);
        } else if (2 == bean.getStatus()) {
            filelist_item_downopen.setText("打开");
            filelist_item_delete.setImageResource(R.drawable.deletefile);
        } else if (3 == bean.getStatus()) {
            filelist_item_downopen.setText("下载");
            filelist_item_delete.setImageResource(R.drawable.unclickdelete);
        }


        //下载按钮点击事件
        filelist_item_downopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, SDUtils.WriteFilePermission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, SDUtils.CreateDeleteFilePermission) != PackageManager.PERMISSION_GRANTED) {
                    ShowToast.showShort(context, "请设置权限后进行操作");
                } else {
                    if (filelist_item_downopen.getText().toString().equals("下载")) {
                        if (MyNetUtils.isConnected(context)) {
                            if (MyNetUtils.isWifi(context)) {
                                //fileMap.get(filenames.get(position))
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
//                        context.startActivity(browserIntent);

//                                OkHttpUtils
//                                        .get()
//                                        .url(PortIpAddress.DownLoadFiles())
//                                        .addParams("access_token", PortIpAddress.GetToken(context))
//                                        .addParams("attachmentid", bean.getAttachmentid())
//                                        .build()
//                                        .execute(new StringCallback() {
//                                            @Override
//                                            public void onError(Call call, Exception e, int id) {
//                                                e.printStackTrace();
//                                            }
//
//                                            @Override
//                                            public void onResponse(String response, int id) {
//                                                Log.e("ces", response);
//                                            }
//                                        });
                                String url = bean.getFileUrl();
                                bean.setDownloadId(MyNetUtils.downLoadFile(context, bean.getFileUrl(), filenames.get(position), "", filenames.get(position)));
//                                downloadType.put(bean.getDownloadId(), bean.getDownloadType());
                                bean.setStatus(FileBean.STATE_DOWNLOADING);
                                ShowToast.showShort(context, "正在后台下载" + filenames.get(position));
                                notifyDataSetChanged();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("提示");
                                builder.setMessage("当前非WIFI状态下是否继续下载");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bean.setDownloadId(MyNetUtils.downLoadFile(context, bean.getFileUrl(), filenames.get(position), "", filenames.get(position)));
//                                        downloadType.put(bean.getDownloadId(), bean.getDownloadType());
                                        filelist_item_downopen.setText("取消");
                                        ShowToast.showShort(context, "正在后台下载" + filenames.get(position));
                                        notifyDataSetChanged();
                                    }
                                });

                                builder.setNegativeButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                    }
                                });
                                builder.show();
                            }
                        } else {
                            ShowToast.showShort(context, "当前无网络");
                        }
                    } else if (filelist_item_downopen.getText().toString().equals("取消")) {
                        ShowToast.showShort(context, "取消" + filenames.get(position) + "下载");
                        bean.setStatus(FileBean.STATE_NONE);
                        MyNetUtils.manager.remove(bean.getDownloadId());
                        Log.e("ces", bean.getDownloadId() + "");
                        notifyDataSetChanged();
                    }
                    else if (filelist_item_downopen.getText().toString().equals("打开")) {
//                        Log.e("ces", SDUtils.sdPath + "/" + filenames.get(position));
//                        if (AndroidFileUtil.openFile(SDUtils.sdPath + "/" + filenames.get(position)) != null) {
//                            context.startActivity(AndroidFileUtil.openFile(SDUtils.sdPath + "/" + filenames.get(position)));
//                        }
                        if (Build.VERSION.SDK_INT >= 24){
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            File file = new File(SDUtils.sdPath + "/" + filenames.get(position));
                            Uri fileUri = FileProvider.getUriForFile(context, "demo.yqh.wisdomapp.fileprovider", file);
                            intent.setDataAndType(fileUri, "image/*");
                            context.startActivity(intent);
                        }else{
                            context.startActivity(AndroidFileUtil.openFile(SDUtils.sdPath + "/" + filenames.get(position)));
                        }

                    }
                }
            }
        });

        //删除文件按钮点击事件
        filelist_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.remind);
                builder.setMessage("是否删除" + filenames.get(position) + "此文件");
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(SDUtils.sdPath + "/" + filenames.get(position));
                        file.delete();
//                      bean.setDeleteClick(false);
//                      downloadType.put(bean.getDownloadId(), "下载");
//                      filelist_item_downopen.setText(downloadType.get(bean.getDownloadId()));
                        bean.setStatus(FileBean.STATE_NONE);
                        notifyDataSetChanged();
                        ShowToast.showShort(context, "已删除文件");
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        return convertView;
    }

    public void DataNotify(List<FileBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }


}
