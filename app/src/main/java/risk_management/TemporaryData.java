package risk_management;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.ArrayList;
import java.util.List;

import adapter.TemporaryDataAdapter;
import bean.TemporaryBean;
import utils.BaseActivity;

import static com.example.administrator.dimine_sis.MyApplication.sqldb;

public class TemporaryData extends BaseActivity {
    private ImageView temporary_data_back;
    private ListView temporary_data_listview;
    private List<TemporaryBean> mDatas;
    private TemporaryDataAdapter adapter;
    private List<String> mlist = new ArrayList<>();
    private RelativeLayout nodata_layout;
    private TemporaryBean bean;
    private TextView temporary_data_batchdeletecancel;
    private LinearLayout temporary_data_container;
    private TextView temporary_data_delete; //删除
    private TextView temporary_data_allcheck; //全选
    private List<TemporaryBean> isCheckedlist = new ArrayList<>(); //用来保存选中状态的数据集合
    private String crtype;
    private Intent intent;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_data);
        initView();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        temporary_data_back = (ImageView) findViewById(R.id.temporary_data_back);
        temporary_data_batchdeletecancel = (TextView) findViewById(R.id.temporary_data_batchdeletecancel);
        temporary_data_container = (LinearLayout) findViewById(R.id.temporary_data_container);
        temporary_data_listview = (ListView) findViewById(R.id.temporary_data_listview);
        nodata_layout = (RelativeLayout) findViewById(R.id.nodata_layout);
        temporary_data_delete = (TextView) findViewById(R.id.temporary_data_delete);
        temporary_data_allcheck = (TextView) findViewById(R.id.temporary_data_allcheck);
    }


    @Override
    protected void initData() {
        intent = getIntent();
        crtype = intent.getStringExtra("crtype");

//        initListView();
        //给listView赋值
        mDatas = new ArrayList<>();
        if (crtype.equals("YHLB001")){
            cursor = sqldb.rawQuery("select * from commonly_cache", null);
        }else{
            cursor = sqldb.rawQuery("select * from hidden_cache", null);
        }

        while (cursor.moveToNext()) {
            bean = new TemporaryBean();
            if (crtype.equals("YHLB001")){
                bean.setCommonlyName(cursor.getString(cursor.getColumnIndex("CommonlyName")));
                bean.setSaveTime(cursor.getString(cursor.getColumnIndex("SaveTime")));
                bean.setSaveId(cursor.getString(cursor.getColumnIndex("commonly_id")));
            }else{
                bean.setCommonlyName(cursor.getString(cursor.getColumnIndex("HiddenName")));
                bean.setSaveTime(cursor.getString(cursor.getColumnIndex("SaveTime")));
                bean.setSaveId(cursor.getString(cursor.getColumnIndex("hidden_id")));
            }

            bean.setShow(false);
            bean.setCheck(false);
            mDatas.add(bean);
            String a = bean.getSaveId();
            String b = bean.getCommonlyName();
            Log.e(TAG, a + "---" + b);
        }

        adapter = new TemporaryDataAdapter(this, mDatas);
        temporary_data_listview.setAdapter(adapter);
        temporary_data_listview.setEmptyView(nodata_layout);

        temporary_data_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (temporary_data_batchdeletecancel.getText().toString().equals("批量删除")) {
                    //                ShowToast.showShort(TemporaryData.this, "点击了" + ++position);
                    Intent intent = new Intent();
//                    String a = mDatas.get(position).getSaveId();
                    intent.putExtra("clicknum", mDatas.get(position).getSaveId());
//                    Log.e(TAG, a);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (mDatas.get(position).isCheck()) {
                        mDatas.get(position).setCheck(false);
                        isCheckedlist.remove(mDatas.get(position));
                    } else {
                        mDatas.get(position).setCheck(true);
                        isCheckedlist.add(mDatas.get(position));
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    /**
     * 初始化ListView滑动菜单
     */
    private void initListView() {
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
////                // 创建选择按钮
////                SwipeMenuItem detailsItem = new SwipeMenuItem(
////                        getApplicationContext());
////                // 设置查看详情背景颜色
////                detailsItem.setBackground(new ColorDrawable(Color.rgb(28, 134,
////                        238)));
////                detailsItem.setWidth(dp2px(90));
////                detailsItem.setIcon(R.drawable.see_details);
////                // 添加到横向滑动菜单中
////                menu.addMenuItem(detailsItem);
//
//                // 创建删除按钮
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
//                        0, 0)));
//                deleteItem.setWidth(dp2px(90));
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // 添加到横向滑动菜单中
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        temporary_data_listview.setMenuCreator(creator);
//
//        //菜单点击事件
//        temporary_data_listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        AlertDialog.Builder builder = new AlertDialog.Builder(TemporaryData.this);
//                        builder.setTitle(R.string.Prompt);
//                        builder.setMessage(R.string.DeleteTemporaryData);
//                        builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                sqldb.execSQL("delete from commonly_cache where commonly_id = ?", new Object[]{mDatas.get(position).getSaveId()});
//                                mDatas.remove(position);
//                                ShowToast.showToastNowait(TemporaryData.this, R.string.DeleteSuccess);
//                                adapter.notifyDataSetChanged();
//                                temporary_data_listview.setAdapter(adapter);
//                            }
//                        });
//                        builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        builder.show();
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void setOnClick() {
        temporary_data_back.setOnClickListener(this);
        temporary_data_batchdeletecancel.setOnClickListener(this);
        temporary_data_delete.setOnClickListener(this);
        temporary_data_allcheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.temporary_data_back:
                finish();
                break;
            case R.id.temporary_data_batchdeletecancel: //打开多选
                isCheckedlist.clear();
                if (temporary_data_batchdeletecancel.getText().toString().equals("批量删除")) {
                    temporary_data_batchdeletecancel.setText(R.string.mine_cancellation_dialog_btn1);
                    temporary_data_container.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setShow(true);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    temporary_data_batchdeletecancel.setText("批量删除");
                    temporary_data_container.setVisibility(View.GONE);
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setShow(false);
                        mDatas.get(i).setCheck(false);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.temporary_data_delete: //删除
                AlertDialog.Builder builder = new AlertDialog.Builder(TemporaryData.this);
                builder.setTitle(R.string.Prompt);
                builder.setMessage(R.string.DeleteTemporaryData);
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatas.removeAll(isCheckedlist);
                        adapter.notifyDataSetChanged();

                        //同时删除数据库中的数据
                        for (int i = 0; i < isCheckedlist.size(); i++) {
                            if (crtype.equals("YHLB001")){
                                sqldb.execSQL("delete from commonly_cache where commonly_id = ?", new Object[]{isCheckedlist.get(i).getSaveId()});
                            }else{
                                sqldb.execSQL("delete from hidden_cache where hidden_id = ?", new Object[]{isCheckedlist.get(i).getSaveId()});
                            }
                        }
                    }
                });
                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.temporary_data_allcheck: //全选
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).setCheck(true);
                    isCheckedlist.add(mDatas.get(i));
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
