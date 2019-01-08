package utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "cache.db";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("create table commonly_cache(commonly_id integer primary key autoincrement,CommonlyName nvarchar(20),CommonlyLiable nvarchar(20),CommonlyTypeClassification nvarchar(20),Rectification nvarchar(20),InvestigationDate nvarchar(20),ClosingDate nvarchar(20),CommonlyLocation nvarchar(20),CommonlyDescribe nvarchar(200),CommonlyScheme nvarchar(200),SaveTime nvarchar(20),ImagesAddress nvarchar(100))");
        db.execSQL("create table hidden_cache(hidden_id integer primary key autoincrement,HiddenName nvarchar(20),HiddenLiable nvarchar(20),HiddenTypeClassification nvarchar(20),HiddenType nvarchar(20),InvestigationDate nvarchar(20),ClosingDate nvarchar(20),HiddenLocation nvarchar(20),HiddenDescribe nvarchar(200),HiddenScheme nvarchar(200),HiddenCasualties nvarchar(100),HiddenLoss nvarchar(50),HiddenTimeAndRange nvarchar(100),HiddenLevel nvarchar(20),SaveTime nvarchar(20),ImagesAddress nvarchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
