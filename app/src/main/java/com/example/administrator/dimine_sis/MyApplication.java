package com.example.administrator.dimine_sis;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import utils.DBHelper;

import static utils.DBHelper.DB_NAME;


public class MyApplication extends Application {
    public final static int CONNECT_TIMEOUT = 30;
    public final static int READ_TIMEOUT = 30;
    public final static int WRITE_TIMEOUT = 30;
    public static SQLiteDatabase sqldb;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化sdk
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);

        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(5, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        OkHttpUtils.initClient(okHttpClient);


        DBHelper dbHelper = new DBHelper(this, DB_NAME, null, 1);
        sqldb = dbHelper.getWritableDatabase();

    }


}
