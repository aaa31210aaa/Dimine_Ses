package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.MyNotificationBean;
import utils.ListViewHolder;

public class MyNotificationListViewAdapter extends BaseAdapter {
    private Context context;
    private List<MyNotificationBean> mDatas;


    public MyNotificationListViewAdapter(Context context, List<MyNotificationBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.mynotification_listview_item, null);
        }

        MyNotificationBean myNotificationEntity = mDatas.get(position);
        TextView mynotification_listview_item_title = ListViewHolder.get(convertView, R.id.mynotification_listview_item_title);
        TextView mynotification_listview_item_time_img = ListViewHolder.get(convertView, R.id.mynotification_listview_item_time_img);
        TextView mynotification_listview_item_time_tv = ListViewHolder.get(convertView, R.id.mynotification_listview_item_time_tv);

        mynotification_listview_item_title.setText(myNotificationEntity.getTitle());
        mynotification_listview_item_time_img.setText("发布时间");
        mynotification_listview_item_time_tv.setText(myNotificationEntity.getTime());

        return convertView;
    }

    public void DataNotify(List<MyNotificationBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
}
