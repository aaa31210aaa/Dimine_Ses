package bean;

import android.app.Activity;
import android.content.Context;

import com.yanzhenjie.permission.AndPermission;

import utils.SDUtils;

public class FileBean {
    private String filename;
    private String fileUrl;
    private String downloadType;
    private int downopen;
    private boolean deleteClick;
    private String attachmentid;
    //下载状态
    private int status;
    //下载id
    private long downloadId;

    /**
     * 未下载
     **/
    public static final int STATE_NONE = 0;
    /**
     * 下载中
     */
    public static final int STATE_DOWNLOADING = 1;
    /**
     * 下载完毕
     */
    public static final int STATE_DOWNLOADED = 2;
    /**
     * 下载失败
     */
    public static final int STATE_ERROR = 3;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public int getDownopen() {
        return downopen;
    }

    public void setDownopen(int downopen) {
        this.downopen = downopen;
    }

    public boolean isDeleteClick() {
        return deleteClick;
    }

    public void setDeleteClick(boolean deleteClick) {
        this.deleteClick = deleteClick;
    }

    public String getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(String attachmentid) {
        this.attachmentid = attachmentid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }


    public static void FileWritePermission(Context context) {
        AndPermission.with((Activity) context)
                .requestCode(104)
                .permission(SDUtils.WriteFilePermission)
                .send();
    }

    public static void FileCdFilePermission(Context context) {
        AndPermission.with((Activity) context)
                .requestCode(105)
                .permission(SDUtils.CreateDeleteFilePermission)
                .send();
    }
}
