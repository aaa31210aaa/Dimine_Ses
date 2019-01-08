package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.dimine_sis.R;

import java.util.List;

import bean.GaoduBean;
import utils.ListViewHolder;

/**
 * Created by Administrator on 2017-03-25.
 */
public class GaoDuAdapter extends BaseAdapter {
    private Context context;
    private List<GaoduBean> mDatas;

    public GaoDuAdapter(Context context, List<GaoduBean> mDatas) {
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
            convertView = View.inflate(context, R.layout.gaodu_item, null);
        }
        TextView gaodu_item_zwmz = ListViewHolder.get(convertView, R.id.gaodu_item_zwmz);
        TextView gaodu_item_gwwpbm = ListViewHolder.get(convertView, R.id.gaodu_item_gwwpbm);
        TextView gaodu_item_ywmz = ListViewHolder.get(convertView, R.id.gaodu_item_ywmz);

        GaoduBean bean = mDatas.get(position);
        gaodu_item_zwmz.setText("中文名称："+bean.getZwmz());
        gaodu_item_gwwpbm.setText("高毒物品编号：" + bean.getGwwpbm());
        gaodu_item_ywmz.setText("英文名称："+bean.getYwmz());

        return convertView;
    }

    public void DataNotify(List<GaoduBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }
}
